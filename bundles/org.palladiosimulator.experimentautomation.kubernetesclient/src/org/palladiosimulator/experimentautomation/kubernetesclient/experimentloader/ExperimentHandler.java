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
import org.palladiosimulator.experimentautomation.kubernetesclient.api.IExperimentHandler;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.util.FileUtil;
import org.palladiosimulator.experimentautomation.kubernetesclient.util.ZipUtil;

/**
 * This class is the entry point for the view.
 * 
 * @author niko
 *
 */
public class ExperimentHandler implements IExperimentHandler {

	private static String pathToResourceFolder = "resources/";
	private static String pathToExperimentFolder = "resources/experimentFiles/";
	private static String pathToZipFile = "resources/experimentData";

	/**
	 * Process query to send experiment data to client
	 * 
	 * @param pathToExperimentFile
	 * @throws ExperimentException
	 */
	@Override
	public void sendExperimentData(String pathToExperimentFile) throws ExperimentException {

		URI uriToExperimentFile = createURIFromPath(pathToExperimentFile);
		ResourceSet resSet = resolveResourcesFromExperimentFile(uriToExperimentFile);
		changeURIsInResourceSet(resSet);
		saveResources(resSet);
		zipResources();

	}

	private void zipResources() throws ExperimentException {
		ZipUtil zipUtil = new ZipUtil();
		if (zipUtil.createZipFileRecursively(pathToResourceFolder, pathToZipFile) == null) {
			throw new ExperimentException("Error while zipping experiment data.");
		}

	}

	private void createResourceAndExperimentDirectory() throws ExperimentException {
		if (!FileUtil.createDirectoryRecursively(pathToExperimentFolder)) {
			throw new ExperimentException("Error while saving resources. Could not create outputdirectory");
		}
	}

	/*
	 * Save single Resource under its URI
	 */
	private void saveResource(Resource resource) throws ExperimentException {
		String path = resource.getURI().path();
		File resourceFile = new File(path);

		try {

			resourceFile.createNewFile();
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			throw new ExperimentException("Error while saving resources");
		}
	}

	/*
	 * Save each resource in given resource set to temporary folder.
	 * 
	 */
	private void saveResources(ResourceSet resSet) throws ExperimentException {

		FileUtil.deleteDirectory(pathToResourceFolder);
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
	 * Load experiment file and resolve all resources into a resource set
	 */
	private ResourceSet resolveResourcesFromExperimentFile(URI uriToExperimentFile) throws ExperimentException {
		
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("experiments", new XMIResourceFactoryImpl());

		ResourceSet resSet = new ResourceSetImpl();

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
