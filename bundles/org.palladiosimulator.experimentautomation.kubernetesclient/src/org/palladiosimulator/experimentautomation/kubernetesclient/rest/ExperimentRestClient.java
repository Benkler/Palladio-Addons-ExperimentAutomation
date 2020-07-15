package org.palladiosimulator.experimentautomation.kubernetesclient.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.KubernetesClientPathFactory;



public class ExperimentRestClient {

	public boolean startSimulation(byte[] content, URI clientBaseURI) {
		try {
			URI createSimulationURI = KubernetesClientPathFactory.getInstance().getCreateSimulationURIFromBaseURI(clientBaseURI);
			
			
			HttpResponse httpResponse = Request
					.Post(createSimulationURI).body(createMulitPartEntity(content))
					.execute().returnResponse();
		
		} catch (IOException | URISyntaxException e ) {
			System.out.println(e);
			return false;
		}

		return true;
	}
	
	private HttpEntity createMulitPartEntity(byte[] content) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody("file", content, ContentType.DEFAULT_BINARY,"experimentFiles");
		
		return builder.build();
	}
	
	
//	
//	public boolean startSimulation(byte[] content, URI clientBaseURI) {
//		HttpResponse httpResponse;
//		try {
//			httpResponse = Request
//					.Get("http://192.168.39.93:30164/simulation/simulation-1a0edaeb584f47a1973f054bcbcc3fcc/log")
//					.execute().returnResponse();
//			System.out.println("StatusCode: " + httpResponse.getStatusLine().getStatusCode());
//			
//
//			System.out.println("Response Contentd: " + EntityUtils.toString(httpResponse.getEntity()));
//
//			for (Header header : httpResponse.getAllHeaders()) {
//				System.out.println("Header name: " + header.getName() + "  Header Value: " + header.getValue());
//			}
//
//		} catch (IOException e) {
//			System.out.println(e);
//			return false;
//		}
//
//		return true;
//	}

//	public void getExistingSimulations() {
//		HttpResponse httpResponse;
//
//		try {
//			httpResponse = Request.Get("http://192.168.39.93:30164/simulationautomation/simulations").execute()
//					.returnResponse();
//			
//			String output = EntityUtils.toString(httpResponse.getEntity());
//			
//			
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	private ObjectMapper getObjectMapper() {
//		ObjectMapper mapper = new ObjectMapper();
//	}

}
