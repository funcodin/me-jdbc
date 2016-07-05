package com.multi.enterprise.commons.client;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestServiceErrorHandler implements ResponseErrorHandler{

	private static final Logger log = LoggerFactory.getLogger(RestServiceErrorHandler.class);
	
	
	public boolean hasError(final ClientHttpResponse response ) throws IOException {
		return response.getStatusCode().value() >= 400;
	}
	
	public void handleError(ClientHttpResponse clientResponse) throws IOException {
		log.warn("Client Response Error {} {} ", clientResponse.getStatusCode(), clientResponse.getStatusText());
		String response = IOUtils.toString(clientResponse.getBody());
		if(StringUtils.isBlank(response)){
			response = clientResponse.getStatusText();
		}
		throw new HttpResponseException(clientResponse.getRawStatusCode(), response);
	}

	

}
