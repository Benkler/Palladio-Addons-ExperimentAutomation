package org.palladiosimulator.experimentautomation.kubernetesclient.simulation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.palladiosimulator.experimentautomation.kubernetesclient.api.ISimulationClientInterface;
import org.palladiosimulator.experimentautomation.kubernetesclient.api.ISimulationHandler;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.rest.SimulationClientInterface;
import org.palladiosimulator.experimentautomation.kubernetesclient.util.FileUtil;
import org.palladiosimulator.experimentautomation.kubernetesclient.util.ZipUtil;

/**
 * This class is the entry point for the view.
 * 
 * @author Niko Benkler
 *
 */
public class SimulationHandler implements ISimulationHandler {

  ISimulationClientInterface restClient;

  public SimulationHandler() {
    restClient = new SimulationClientInterface();
  }

  private final static String pathToResourceFolder = "resources/";
  private final static String pathToExperimentFolder = "resources/experimentFiles/";
  private final static String pathToZipFile = "resources/experimentData.zip";

  /**
   * Get simulation results as byte array. Results are zipped, so they must be further processed.
   * 
   * @param simulationName
   * @param clientHost
   * @return
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  @Override
  public byte[] getZippedSimulationResultsAsByteArray(String simulationName, java.net.URI clientURI)
      throws ClientNotAvailableException, ExperimentException {

    return restClient.getSimulationResultsFromSimulation(simulationName, clientURI);
  }

  /**
   * Retrieve all existing Simulations
   * 
   * @param clientHost
   * @return
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  @Override
  public List<SimulationVO> getExistingSimulation(java.net.URI clientURI)
      throws ClientNotAvailableException, ExperimentException {
    return restClient.getAvailableSimulations(clientURI);
  }

  /**
   * Retrieve log file as String from given simulation
   * 
   * @param simulationName
   * @param clientHost
   * @return
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  @Override
  public String getSimulationLog(String simulationName, java.net.URI clientURI)
      throws ClientNotAvailableException, ExperimentException {
    return restClient.getSimulationLogFromSimulation(simulationName, clientURI);
  }

  /**
   * Process query to send experiment data to client
   * 
   * @param pathToExperimentFile
   * @throws ExperimentException
   * @throws ClientNotAvailableException
   */
  @Override
  public SimulationVO sendExperimentData(String pathToExperimentFile, java.net.URI clientURI)
      throws ExperimentException, ClientNotAvailableException {

    URI uriToExperimentFile = createURIFromPath(pathToExperimentFile);
    ResourceSet resSet = resolveResourcesFromExperimentFile(uriToExperimentFile);
    changeURIsInResourceSet(resSet);
    saveResources(resSet);
    byte[] simulationData = zipResourcesAsByteStream();
    return restClient.startSimulation(simulationData, clientURI);

  }

  /*
   * Create directory structure that is necessary for sending experiment data
   */
  private void createResourceAndExperimentDirectory() throws ExperimentException {
    if (!FileUtil.createDirectoryRecursively(pathToExperimentFolder)) {
      throw new ExperimentException(
          "Error while saving resources. Could not create outputdirectory");
    }
  }

  /*
   * Create zip file from ressource
   */
  private byte[] zipResourcesAsByteStream() throws ExperimentException {
    ZipUtil zipUtil = new ZipUtil();
    if (zipUtil.createZipFileRecursively(pathToResourceFolder, pathToZipFile) == null) {
      throw new ExperimentException("Error while zipping experiment data.");
    }

    byte[] content = FileUtil.loadFileAsByteStream(pathToZipFile);
    if (content == null) {
      throw new ExperimentException("Error while loading zipped experiment data");
    }

    return content;

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
   * Adapt URIs of all Resources in given set to point to temporary folder. Remove names, as name
   * conflicts may occur.
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
  private URI adaptURI(URI originalURI, String destDirPath, String newFileName)
      throws ExperimentException {
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
  private ResourceSet resolveResourcesFromExperimentFile(URI uriToExperimentFile)
      throws ExperimentException {

    ResourceSet resSet = new ResourceSetImpl();

    try {
      resSet.getResource(uriToExperimentFile, true);
    } catch (WrappedException e) {
      System.out.println(e.getMessage());
      throw new ExperimentException("Could not find resource with URI: " + uriToExperimentFile);
    }
    EcoreUtil.resolveAll(resSet);

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
