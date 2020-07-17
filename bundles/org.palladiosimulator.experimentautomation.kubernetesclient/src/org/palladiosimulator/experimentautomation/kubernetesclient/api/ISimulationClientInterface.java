package org.palladiosimulator.experimentautomation.kubernetesclient.api;

import java.net.URI;
import java.util.List;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationVO;


public interface ISimulationClientInterface {

  /**
   * Retrieve simulation result of simulation with given name.
   * 
   * @param simulationName
   * @param clientURI
   * @return zip compressed byte-Array, containing the simulation results
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  byte[] getSimulationResultsFromSimulation(String simulationName, URI clientURI)
      throws ClientNotAvailableException, ExperimentException;

  /**
   * Retrieve logs of simulation with give name.
   * 
   * @param simulationName
   * @param clientURI
   * @return log files as String
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  String getSimulationLogFromSimulation(String simulationName, URI clientURI)
      throws ClientNotAvailableException, ExperimentException;

  /**
   * Trigger Kubernetes Client to start a simulation. Entire resources are packed and zipped and are
   * transmitted via REST as byte-array
   * 
   * @param content
   * @param clientURI
   * @return
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  SimulationVO startSimulation(byte[] content, URI clientURI)
      throws ClientNotAvailableException, ExperimentException;

  /**
   * Get List of available simulation from kubernetes client
   * 
   * @param clientURI
   * @return
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  List<SimulationVO> getAvailableSimulations(URI clientURI)
      throws ClientNotAvailableException, ExperimentException;

}
