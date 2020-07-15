package org.palladiosimulator.experimentautomation.kubernetesclient.simulation;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

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

	public URI getClientURI(String clientHost) throws URISyntaxException {
		String protocol = KubernetesClientProperties.CLIENT_PROTOCOL;
		String basePath = KubernetesClientProperties.CLIENT_BASE_PATH;
		int port = KubernetesClientProperties.CLIENT_PORT;

		URI uri = new URI(protocol, null, clientHost, port, basePath, null, null);
		return uri;
	}

	/**
	 * Method create URI for "createSimulation"-RestEndpoint. </br>
	 * It expects a valid base URI of the Kubernetes Client (protocol, port...)
	 * 
	 * @param baseURI
	 * @return
	 * @throws URISyntaxException
	 */
	public URI getCreateSimulationURIFromBaseURI(URI baseURI) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(baseURI);
		builder.setPath(KubernetesClientProperties.CLIENT_CREATE_SIMULATION_PATH);
		return builder.build();
	}



}
