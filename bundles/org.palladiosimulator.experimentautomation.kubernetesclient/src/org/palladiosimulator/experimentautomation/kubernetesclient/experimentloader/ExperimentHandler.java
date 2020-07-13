package org.palladiosimulator.experimentautomation.kubernetesclient.experimentloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.util.ZipUtil;

/**
 * This class is the entry point for the view.
 * 
 * @author niko
 *
 */
public class ExperimentHandler {

	
	private static String pathToResourceFolder ="resources/";
	private static String pathToExperimentFolder ="resources/experimentFiles/";
	private static String pathToZipFile ="resources/experimentData";

	/**
	 * Process query to send experiment data to client
	 * 
	 * @param pathToExperimentFile
	 * @throws ExperimentException
	 */
	public void sendExperimentData(String pathToExperimentFile) throws ExperimentException {

		URI uriToExperimentFile = createURIFromPath(pathToExperimentFile);
		ResourceSet resSet = resolveResourcesFromExperimentFile(uriToExperimentFile);
		changeURIsInResourceSet(resSet);
		saveResources(resSet);
		zipResources();

	}

	private void zipResources() throws ExperimentException {
		ZipUtil zipUtil = new ZipUtil();
		if(zipUtil.createZipFileRecursively(pathToResourceFolder, pathToZipFile) == null) {
			throw new ExperimentException("Error while zipping experiment data.");
		}
		
		
	}

	private void deleteDirRecursively(Path pathToDirectory) throws IOException {
		if(Files.exists(pathToDirectory)) {
			System.out.println("File exists");
			Files.walk(pathToDirectory).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		}
		
	}

	private void cleanUpResourceDirectory() throws ExperimentException {

		try {
			deleteDirRecursively(Paths.get(pathToResourceFolder));
		} catch (IOException e1) {
			throw new ExperimentException("Error while saving resources. Clean-Up failed!");
		}
	}

	private void createResourceAndExperimentDirectory() throws ExperimentException {
		try {
			//Recursively creates resource and experiment folder (within resource folder)
			Files.createDirectories(Paths.get(pathToExperimentFolder));
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
			throw new ExperimentException("Error while saving resources. Could not create outputdirectory");
		}
	}

	private void saveResource(Resource resource) throws ExperimentException {
		String path = resource.getURI().path();
		File resourceFile = new File(path);

		try {

			resourceFile.createNewFile();
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new ExperimentException("Error while saving resources");
		}
	}

	/*
	 * Save each resource in given resource set to temporary folder.
	 */
	private void saveResources(ResourceSet resSet) throws ExperimentException {

		cleanUpResourceDirectory();
		createResourceAndExperimentDirectory();

		for (Resource res : resSet.getResources()) {
			saveResource(res);

		}
	}

	/*
	 * Adapt URIs of all Resources in given set to point to temporary folder. Remove
	 * names, as name conflicts may occur.
	 */
	private ResourceSet changeURIsInResourceSet(ResourceSet resSet) throws ExperimentException {
		int i = 0;
		for (Resource res : resSet.getResources()) {
			URI newURI = adaptURI(res.getURI(), pathToExperimentFolder, Integer.toString(i));
			res.setURI(newURI);
			i++;
		}
		return resSet;
	}

	/*
	 * Replace file name and path in given URI.
	 */
	private URI adaptURI(URI originalURI, String destDirPath, String newFileName) throws ExperimentException {
		String fileExtension = originalURI.fileExtension();
		if (fileExtension == null) {
			throw new ExperimentException("Error while adapting URI of Ressource");
		}
		String newFilePath = destDirPath + "/" + newFileName + "." + fileExtension;
		return createURIFromPath(newFilePath);
	}

	/*
	 * Load experiment file and resolve all ressources into a resource set
	 */
	private ResourceSet resolveResourcesFromExperimentFile(URI uriToExperimentFile) throws ExperimentException {
		// Register the XMI resource factory for the .website extension
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("experiments", new XMIResourceFactoryImpl());

		ResourceSet resSet = new ResourceSetImpl();

		// TODO Handle Exception
		Resource resource;
		try {
			resource = resSet.getResource(uriToExperimentFile, true);
		} catch (WrappedException e) {
			System.out.println(e.getMessage());
			throw new ExperimentException("Could not find resource with URI: " + uriToExperimentFile);
		}

		EcoreUtil.resolveAll(resource);

		return resSet;
	}

	/*
	 * Create EMF-compliant URI from path
	 */
	private URI createURIFromPath(String pathToExperimentFile) {
		Path path = Paths.get(pathToExperimentFile);

		// Need to create java.nio URI and the convert to EMF URI
		URI uri = URI.createURI(path.toUri().toString());
		return uri;
	}

}
