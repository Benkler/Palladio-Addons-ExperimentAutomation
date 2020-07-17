package org.palladiosimulator.experimentautomation.kubernetesclient.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.KubernetesClientPathFactory;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.KubernetesClientProperties;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationVO;
import org.palladiosimulator.experimentautomation.kubernetesclient.util.JSONUtil;

import com.google.gson.reflect.TypeToken;

public class ExperimentRestClient {

	public byte[] getSimulationResultsFromSimulation(String simulationName, String clientHost)
			throws ClientNotAvailableException, ExperimentException {
		checkIfClientisAvailable(clientHost);

		HttpResponse httpResponse;
		try {
			URI getResultUri = KubernetesClientPathFactory.getInstance().getSimulationResultURI(simulationName,
					clientHost);
			httpResponse = Request.Get(getResultUri).execute().returnResponse();
			checkIfStatusCodeIsOK(httpResponse);
			return EntityUtils.toByteArray(httpResponse.getEntity());
		} catch (URISyntaxException | ParseException | IOException e) {
			// TODO logging
			throw new ExperimentException(e.getMessage());
		}

	}

	public String getSimulationLogFromSimulation(String simulationName, String clientHost)
			throws ClientNotAvailableException, ExperimentException {

		checkIfClientisAvailable(clientHost);
		HttpResponse httpResponse;

		try {
			URI getLogURI = KubernetesClientPathFactory.getInstance().getSimulationLogURI(simulationName, clientHost);
			httpResponse = Request.Get(getLogURI).execute().returnResponse();
			checkIfStatusCodeIsOK(httpResponse);
			return EntityUtils.toString(httpResponse.getEntity());
		} catch (URISyntaxException | IOException | ParseException e) {
			// TODO logging
			throw new ExperimentException(e.getMessage());
		}

	}

	public SimulationVO startSimulation(byte[] content, String clientHost)
			throws ClientNotAvailableException, ExperimentException {

		checkIfClientisAvailable(clientHost);
		HttpResponse httpResponse;
		try {
			URI createSimulationURI = KubernetesClientPathFactory.getInstance().getCreateSimulationURI(clientHost);
			httpResponse = Request.Post(createSimulationURI).body(createMulitPartEntity(content)).execute()
					.returnResponse();
			checkIfStatusCodeIsOK(httpResponse);
			String simulationVOJsonString = EntityUtils.toString(httpResponse.getEntity());
			return JSONUtil.getInstance().fromJson(simulationVOJsonString, SimulationVO.class);

		} catch (ParseException | IOException | URISyntaxException e) {
			// TODO logging
			throw new ExperimentException(e.getMessage());
		}

	}

	public List<SimulationVO> getAvailableSimulations(String clientHost)
			throws ClientNotAvailableException, ExperimentException {

		checkIfClientisAvailable(clientHost);
		HttpResponse httpResponse;
		try {
			URI getAllSimulationsURI = KubernetesClientPathFactory.getInstance().getGetAllSimulationsURI(clientHost);
			httpResponse = Request.Get(getAllSimulationsURI).execute().returnResponse();

			checkIfStatusCodeIsOK(httpResponse);

			String simulationListAsJson = EntityUtils.toString(httpResponse.getEntity());
			return convertSimulationListFromJsonToList(simulationListAsJson);

		} catch (ParseException | URISyntaxException | IOException e) {
			// TODO logging
			throw new ExperimentException(e.getMessage());
		}

	}

	private void checkIfStatusCodeIsOK(HttpResponse httpResponse) throws ExperimentException {
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode < 200 || statusCode > 299) {
			throw new ExperimentException("Status Code of response is expected to be 'Sucessfull' (2xx) but was " + statusCode);
		}
	}

	private List<SimulationVO> convertSimulationListFromJsonToList(String simulationsAsJson) {
		TypeToken<List<SimulationVO>> token = new TypeToken<List<SimulationVO>>() {
		};
		return JSONUtil.getInstance().fromJson(simulationsAsJson, token);

	}

	private HttpEntity createMulitPartEntity(byte[] content) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody("file", content, ContentType.DEFAULT_BINARY, "experimentFiles");

		return builder.build();
	}

	private void checkIfClientisAvailable(String clientHost) throws ClientNotAvailableException, ExperimentException {

		try {
			URI isClientAvailableURI = KubernetesClientPathFactory.getInstance().getIsClientAvailableURI(clientHost);
			HttpResponse httpResponse = Request.Get(isClientAvailableURI)
					.connectTimeout(KubernetesClientProperties.HTTP_CONNECTION_TIMEOUT_IN_MILLI_SECONDS).execute()
					.returnResponse();
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new ClientNotAvailableException("Client with host adress " + clientHost + " not available");
			}

		} catch (IOException | URISyntaxException e) {
			// TODO logging
			throw new ExperimentException(e.getMessage());
		}

	}

}
