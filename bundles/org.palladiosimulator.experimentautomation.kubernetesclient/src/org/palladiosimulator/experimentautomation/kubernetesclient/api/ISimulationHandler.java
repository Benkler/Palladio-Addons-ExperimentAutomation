package org.palladiosimulator.experimentautomation.kubernetesclient.api;

import java.net.URI;
import java.util.List;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationVO;

public interface ISimulationHandler {


  /**
   * Process query to send experiment data to client
   * 
   * @param pathToExperimentFile
   * @return
   * @throws ExperimentException
   * @throws ClientNotAvailableException
   */
  SimulationVO sendExperimentData(String pathToExperimentFile, URI clientURI)
      throws ExperimentException, ClientNotAvailableException;

  /**
   * Retrieve all existing Simulations
   * 
   * @param clientHost
   * @return
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  List<SimulationVO> getExistingSimulation(URI clientURI)
      throws ClientNotAvailableException, ExperimentException;

  /**
   * Retrieve log file as String from given simulation
   * 
   * @param simulationName
   * @param clientHost
   * @return
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  String getSimulationLog(String simulationName, URI clientURI)
      throws ClientNotAvailableException, ExperimentException;

  /**
   * Get simulation results as byte array. Results are zipped, so they must be further processed.
   * 
   * @param simulationName
   * @param clientHost
   * @return
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  byte[] getZippedSimulationResultsAsByteArray(String simulationName, URI clientURI)
      throws ClientNotAvailableException, ExperimentException;



}
