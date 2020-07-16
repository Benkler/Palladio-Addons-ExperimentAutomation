package org.palladiosimulator.experimentautomation.kubernetesclient.simulation;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.util.InetAddressUtils;

public class KubernetesClientPathFactory {

	private static KubernetesClientPathFactory INSTANCE;
	
	private KubernetesClientPathFactory() {

	}

	public static synchronized KubernetesClientPathFactory getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new KubernetesClientPathFactory();
		}
		return INSTANCE;
	}

	/**
	 * Method to create clientURI from host. </br>
	 * clientHost should be either a valid ip adress or dns name but without "http://"
	 * 
	 * @param clientHost
	 * @return http://{clientHost}:KubernetesClientProperties.CLIENT_PORT/
	 * @throws URISyntaxException
	 */
	private URI getClientURI(String clientHost) throws URISyntaxException {
		String host = validateClientHost(clientHost);
		String protocol = KubernetesClientProperties.CLIENT_PROTOCOL;
		String basePath = KubernetesClientProperties.CLIENT_BASE_PATH;
		int port = KubernetesClientProperties.CLIENT_PORT;

		URI uri = new URI(protocol, null, host, port, basePath, null, null);
		return uri;
	}
	
	private String validateClientHost(String clientHost) throws URISyntaxException {
		
		if(clientHost == null || clientHost.isBlank()) {
			throw new URISyntaxException(clientHost, "Host must not be null or blank");
		}
		//We accept pure IP addresses
		if(InetAddressUtils.isIPv4Address(clientHost) || InetAddressUtils.isIPv6Address(clientHost)) {
			return clientHost;
		}
		URI uri = new URI(clientHost);
	    String hostname = uri.getHost();
	    // to provide faultproof result, check if not null then return only hostname, without www.
	    if (hostname != null) {
	        return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
	    }
	    return hostname;
	}

	/**
	 * Method create URI for "createSimulation"-RestEndpoint. </br>
	 * It expects a valid base URI of the Kubernetes Client (protocol, port...)
	 * 
	 * @param baseURI
	 * @return
	 * @throws URISyntaxException
	 */
	public URI getCreateSimulationURI(String clientHost) throws URISyntaxException {
		URI baseURI = getClientURI(clientHost);
		URIBuilder builder = new URIBuilder(baseURI);
		builder.setPath(KubernetesClientProperties.CLIENT_CREATE_SIMULATION_PATH);
		return builder.build();
	}
	
	public URI getGetAllSimulationsUri(String clientHost) throws URISyntaxException {
		URI baseURI = getClientURI(clientHost);
		URIBuilder builder = new URIBuilder(baseURI);
		builder.setPath(KubernetesClientProperties.CLIENT_GET_ALL_SIMULATIONS_PATH);
		return builder.build();
	}
	
	public URI getIsClientAvailableURI(String clientHost) throws URISyntaxException {
		URI baseURI = getClientURI(clientHost);
		URIBuilder builder = new URIBuilder(baseURI);
		builder.setPath(KubernetesClientProperties.CLIENT_IS_AVAILABLE_PATH);
		return builder.build();
	}



}
