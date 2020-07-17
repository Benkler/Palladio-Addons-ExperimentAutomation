package org.palladiosimulator.experimentautomation.kubernetesclient.simulation;

/**
 * Property Class.
 *
 * @author Niko Benkler
 *
 */
//TODO Get rid of that... load as .properties File!
public class KubernetesClientProperties {
  
  private KubernetesClientProperties() {
    
  }

  public static final String CLIENT_CREATE_SIMULATION_PATH = "/simulation/create";
  public static final String CLIENT_GET_ALL_SIMULATIONS_PATH = "/simulationautomation/simulations";
  public static final String CLIENT_IS_AVAILABLE_PATH = "/simulationautomation/client";
  public static final String CLIENT_SIMULATION_LOG_PATH = "/simulation/{}/log";
  public static final String CLIENT_SIMULATION_RESULTS_PATH = "/simulation/{}/results";

  public static final int HTTP_CONNECTION_TIMEOUT_IN_MILLI_SECONDS = 5000;



}
