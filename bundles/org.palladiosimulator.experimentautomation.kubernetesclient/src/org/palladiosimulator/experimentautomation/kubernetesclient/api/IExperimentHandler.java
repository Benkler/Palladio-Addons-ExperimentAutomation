package org.palladiosimulator.experimentautomation.kubernetesclient.api;

import java.util.List;

import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationVO;

public interface IExperimentHandler {


	/**
	 * Process query to send experiment data to client
	 * 
	 * @param pathToExperimentFile
	 * @return 
	 * @throws ExperimentException
	 * @throws ClientNotAvailableException 
	 */
	SimulationVO sendExperimentData(String pathToExperimentFile, String clientHost) throws ExperimentException, ClientNotAvailableException;

	/**
	 * Retrieve all existing Simulations
	 * @param clientHost
	 * @return
	 * @throws ClientNotAvailableException
	 * @throws ExperimentException
	 */
	List<SimulationVO> getExistingSimulation(String clientHost) throws ClientNotAvailableException, ExperimentException;

	
	
}