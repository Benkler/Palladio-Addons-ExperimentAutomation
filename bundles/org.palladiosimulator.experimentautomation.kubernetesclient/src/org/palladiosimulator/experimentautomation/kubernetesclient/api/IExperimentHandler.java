package org.palladiosimulator.experimentautomation.kubernetesclient.api;

import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;

public interface IExperimentHandler {

	/**
	 * Process query to send experiment data to client
	 * 
	 * @param pathToExperimentFile
	 * @throws ExperimentException
	 */
	void sendExperimentData(String pathToExperimentFile) throws ExperimentException;

}