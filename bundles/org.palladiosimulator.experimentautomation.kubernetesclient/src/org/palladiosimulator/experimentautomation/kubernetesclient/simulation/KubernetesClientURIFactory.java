package org.palladiosimulator.experimentautomation.kubernetesclient.simulation;

import java.net.URI;
import java.net.URISyntaxException;
import org.palladiosimulator.experimentautomation.kubernetesclient.util.URIBuilder;

/**
 * Factory Class to create URIs for desired Rest-Endpoint
 * @author Niko Benkler
 *
 */
public class KubernetesClientURIFactory {

  //Singleton
  private static KubernetesClientURIFactory INSTANCE;

  private KubernetesClientURIFactory() {

  }

  public static synchronized KubernetesClientURIFactory getInstance() {

    if (INSTANCE == null) {
      INSTANCE = new KubernetesClientURIFactory();
    }
    return INSTANCE;
  }

  private URI addSegmentToPath(URI baseURI, String path) throws URISyntaxException {
    URIBuilder builder = new URIBuilder(baseURI);
    builder.addPath(path);
    return builder.build();
  }



  /**
   * Method create URI for "createSimulation"-RestEndpoint. </br>
   * 
   * @param baseURI
   * @return
   * @throws URISyntaxException
   */
  public URI getCreateSimulationURI(URI clientURI) throws URISyntaxException {
    return addSegmentToPath(clientURI, KubernetesClientProperties.CLIENT_CREATE_SIMULATION_PATH);
  }

  /**
   * Method create URI for "GetAllSimulations"-RestEndpoint. </br>
   * 
   * @param baseURI
   * @return
   * @throws URISyntaxException
   */
  public URI getGetAllSimulationsURI(URI clientURI) throws URISyntaxException {

    return addSegmentToPath(clientURI, KubernetesClientProperties.CLIENT_GET_ALL_SIMULATIONS_PATH);
  }

  /**
   * Method create URI for "isClientAvailable"-RestEndpoint. </br>
   * 
   * @param baseURI
   * @return
   * @throws URISyntaxException
   */
  public URI getIsClientAvailableURI(URI clientURI) throws URISyntaxException {
    return addSegmentToPath(clientURI, KubernetesClientProperties.CLIENT_IS_AVAILABLE_PATH);
  }

  /**
   * Method create URI for "getLogOfSimulation"-RestEndpoint. </br>
   * 
   * @param baseURI
   * @return
   * @throws URISyntaxException
   */
  public URI getSimulationLogURI(String simulationName, URI clientURI) throws URISyntaxException {

    String simulationLogPath =
        KubernetesClientProperties.CLIENT_SIMULATION_LOG_PATH.replace("{}", simulationName);
    return addSegmentToPath(clientURI, simulationLogPath);
  }

  /**
   * Method create URI for "getSimulationResults"-RestEndpoint. </br>
   * 
   * @param baseURI
   * @return
   * @throws URISyntaxException
   */
  public URI getSimulationResultURI(String simulationName, URI clientURI)
      throws URISyntaxException {

    String simulationResultPath =
        KubernetesClientProperties.CLIENT_SIMULATION_RESULTS_PATH.replace("{}", simulationName);
    return addSegmentToPath(clientURI, simulationResultPath);
  }



}
