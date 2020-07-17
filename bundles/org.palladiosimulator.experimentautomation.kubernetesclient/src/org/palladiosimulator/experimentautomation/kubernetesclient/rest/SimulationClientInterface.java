package org.palladiosimulator.experimentautomation.kubernetesclient.rest;

import java.io.IOException;
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
import org.palladiosimulator.experimentautomation.kubernetesclient.api.ISimulationClientInterface;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.KubernetesClientURIFactory;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.KubernetesClientProperties;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationVO;
import org.palladiosimulator.experimentautomation.kubernetesclient.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

public class SimulationClientInterface implements ISimulationClientInterface  {

  /**
   * Retrieve simulation result of simulation with given name.
   * 
   * @param simulationName
   * @param clientURI
   * @return zip compressed byte-Array, containing the simulation results
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  @Override
  public byte[] getSimulationResultsFromSimulation(String simulationName, URI clientURI)
      throws ClientNotAvailableException, ExperimentException {

    checkIfClientisAvailable(clientURI);

    HttpResponse httpResponse;
    try {
      URI getResultUri = KubernetesClientURIFactory.getInstance()
          .getSimulationResultURI(simulationName, clientURI);
      httpResponse = Request.Get(getResultUri).execute().returnResponse();

      checkIfStatusCodeIsOK(httpResponse);

      return EntityUtils.toByteArray(httpResponse.getEntity());
    } catch (URISyntaxException | ParseException | IOException e) {
      // TODO logging
      throw new ExperimentException(e.getMessage());
    }

  }

  /**
   * Retrieve logs of simulation with give name.
   * 
   * @param simulationName
   * @param clientURI
   * @return log files as String
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  @Override
  public String getSimulationLogFromSimulation(String simulationName, URI clientURI)
      throws ClientNotAvailableException, ExperimentException {

    checkIfClientisAvailable(clientURI);

    HttpResponse httpResponse;
    try {
      URI getLogURI =
          KubernetesClientURIFactory.getInstance().getSimulationLogURI(simulationName, clientURI);
      httpResponse = Request.Get(getLogURI).execute().returnResponse();

      checkIfStatusCodeIsOK(httpResponse);

      return EntityUtils.toString(httpResponse.getEntity());
    } catch (URISyntaxException | IOException | ParseException e) {
      // TODO logging
      throw new ExperimentException(e.getMessage());
    }

  }

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
  @Override
  public SimulationVO startSimulation(byte[] content, URI clientURI)
      throws ClientNotAvailableException, ExperimentException {

    checkIfClientisAvailable(clientURI);

    HttpResponse httpResponse;
    try {
      URI createSimulationURI =
          KubernetesClientURIFactory.getInstance().getCreateSimulationURI(clientURI);
      httpResponse = Request.Post(createSimulationURI).body(createMulitPartEntity(content))
          .execute().returnResponse();
      checkIfStatusCodeIsOK(httpResponse);
      String simulationVOJsonString = EntityUtils.toString(httpResponse.getEntity());
      return JSONUtil.getInstance().fromJson(simulationVOJsonString, SimulationVO.class);

    } catch (ParseException | IOException | URISyntaxException e) {
      // TODO logging
      throw new ExperimentException(e.getMessage());
    }

  }

  /**
   * Get List of available simulation from kubernetes client
   * 
   * @param clientURI
   * @return
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  @Override
  public List<SimulationVO> getAvailableSimulations(URI clientURI)
      throws ClientNotAvailableException, ExperimentException {

    checkIfClientisAvailable(clientURI);
    HttpResponse httpResponse;
    try {
      URI getAllSimulationsURI =
          KubernetesClientURIFactory.getInstance().getGetAllSimulationsURI(clientURI);
      httpResponse = Request.Get(getAllSimulationsURI).execute().returnResponse();

      checkIfStatusCodeIsOK(httpResponse);

      String simulationListAsJson = EntityUtils.toString(httpResponse.getEntity());
      return convertSimulationListFromJsonToList(simulationListAsJson);

    } catch (ParseException | URISyntaxException | IOException e) {
      // TODO logging
      throw new ExperimentException(e.getMessage());
    }

  }

  /**
   * Make Rest-Call to check if Kubernetes Client is up and running. Wait
   * {@link KubernetesClientProperties.HTTP_CONNECTION_TIMEOUT_IN_MILLI_SECONDS} for connection time
   * out.
   * 
   * @param clientURI
   * @throws ClientNotAvailableException
   * @throws ExperimentException
   */
  private void checkIfClientisAvailable(URI clientURI)
      throws ClientNotAvailableException, ExperimentException {

    try {
      URI isClientAvailableURI =
          KubernetesClientURIFactory.getInstance().getIsClientAvailableURI(clientURI);
      HttpResponse httpResponse = Request.Get(isClientAvailableURI)
          .connectTimeout(KubernetesClientProperties.HTTP_CONNECTION_TIMEOUT_IN_MILLI_SECONDS)
          .execute().returnResponse();
      if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        throw new ClientNotAvailableException(
            "Client with uri " + clientURI.toString() + " ia not available");
      }

    } catch (IOException | URISyntaxException e) {
      // TODO logging
      throw new ExperimentException(e.getMessage());
    }

  }

  /*
   * Status Code is okay if in 2xx-range!
   */
  private void checkIfStatusCodeIsOK(HttpResponse httpResponse) throws ExperimentException {
    int statusCode = httpResponse.getStatusLine().getStatusCode();
    if (statusCode < 200 || statusCode > 299) {
      throw new ExperimentException(
          "Status Code of response is expected to be 'Sucessfull' (2xx) but was " + statusCode);
    }
  }

  /*
   * Convert given json String in List of Simulations.
   */
  private List<SimulationVO> convertSimulationListFromJsonToList(String simulationsAsJson) {
    TypeToken<List<SimulationVO>> token = new TypeToken<List<SimulationVO>>() {};
    return JSONUtil.getInstance().fromJson(simulationsAsJson, token);

  }

  /*
   * Create Multipart Entity for rest-call. Multipart Entities are recognized by Kubernetes Client
   */
  private HttpEntity createMulitPartEntity(byte[] content) {
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    builder.addBinaryBody("file", content, ContentType.DEFAULT_BINARY, "experimentFiles");

    return builder.build();
  }



}
