package com.multi.enterprise.commons.client;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.multi.enterprise.types.exception.ClientException;
import com.multi.enterprise.types.exception.ServiceException;

public class BaseRestClient {

	private static final Logger log = LoggerFactory.getLogger(BaseRestClient.class);
	protected RestTemplate restTemplate;
	protected URI rootServiceEndpoint;

	public BaseRestClient() {
	}

	public BaseRestClient(final HttpClient httpClient, final URI rootServiceEndpoint) {
		super();
		this.rootServiceEndpoint = rootServiceEndpoint;
		this.setRestTemplate(httpClient);
	}

	public BaseRestClient(final RestTemplate restTemplate, final URI rootServiceEndpoint) {
		super();
		this.rootServiceEndpoint = rootServiceEndpoint;
		this.setRestTemplate(restTemplate);

	}

	public void setRestTemplate(final HttpClient httpClient) {
		final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		this.setRestTemplate(new RestTemplate(requestFactory));
	}

	public void setRestTemplate(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.restTemplate.setErrorHandler(new RestServiceErrorHandler());
	}

	public URI getRootServiceEndpoint() {
		return this.rootServiceEndpoint;
	}

	public RestTemplate getRestTemplate() {
		return this.restTemplate;
	}

	public <T> T getForObject(final URI uri, final Class<T> responseType) throws ClientException, ServiceException {
		try {
			return this.restTemplate.getForObject(uri, responseType);
		} catch (final RestClientException restClientException) {
			this.handleException(HttpMethod.GET, uri, restClientException);
			return null;
		}
	}

	public <T> T postForObject(final URI uri, final Object requestObject, final Class<T> responseType)
			throws ServiceException, ClientException {
		try {
			return this.restTemplate.postForObject(uri, requestObject, responseType);
		} catch (final RestClientException restClientException) {
			this.handleException(HttpMethod.POST, uri, restClientException);
			return null;
		}
	}

	public void delete(final URI uri) throws ClientException, ServiceException {
		try {
			this.restTemplate.delete(uri);
		} catch (final RestClientException restClientException) {
			this.handleException(HttpMethod.DELETE, uri, restClientException);
		}
	}

	public <T> void put(final URI uri, final T requestObject) throws ClientException, ServiceException {
		try {
			this.restTemplate.put(uri, requestObject);
		} catch (final RestClientException restClientException) {
			this.handleException(HttpMethod.PUT, uri, restClientException);
		}
	}

	public <T> T putForObject(final URI uri, final Object requestObject, final Class<T> responseType)
			throws ServiceException, ClientException {
		try {
			final ResponseEntity<T> response = this.restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<Object>(
					requestObject), responseType);
			return response.getBody();
		} catch (final RestClientException restClientException) {
			this.handleException(HttpMethod.PUT, uri, restClientException);
			return null;
		}
	}

	private void handleException(final HttpMethod httpMethod, final URI uri,
			final RestClientException restClientException) throws ServiceException, ClientException {
		if (restClientException.getCause() != null && restClientException.getCause() instanceof HttpResponseException) {
			final HttpResponseException httpException = (HttpResponseException) restClientException.getCause();
			final ServiceException serviceException = ServiceException.valueOf(httpException.getStatusCode(),
					httpException.getMessage());
			if (serviceException != null) {
				throw serviceException;
			}
			throw new ServiceException("Status code " + httpException.getStatusCode() + "Http method " + httpMethod
					+ " URL " + uri + " Exception " + httpException.toString());
		}
		throw new ClientException("Http method " + httpMethod + " URL " + uri + " Exception "
				+ restClientException.toString());
	}
}
