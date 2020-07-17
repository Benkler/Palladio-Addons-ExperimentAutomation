package org.palladiosimulator.experimentautomation.kubernetesclient.simulation;

public class KubernetesClientProperties {
	
	public static final int CLIENT_PORT = 30164;
	public static final String CLIENT_PROTOCOL ="http";
	public static final String CLIENT_BASE_PATH ="/";
	public static final String CLIENT_CREATE_SIMULATION_PATH ="/simulation/create";
	public static final String CLIENT_GET_ALL_SIMULATIONS_PATH = "/simulationautomation/simulations";
	public static final String CLIENT_IS_AVAILABLE_PATH = "/simulationautomation/client";
	public static final String CLIENT_SIMULATION_LOG_PATH ="/simulation/{}/log";
	
	public static final int HTTP_CONNECTION_TIMEOUT_IN_MILLI_SECONDS = 5000;
	


}
