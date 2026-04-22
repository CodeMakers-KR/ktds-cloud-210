package com.example.apigatewayservice.filters;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public class CustomFilter {

	private static final Logger logger = LoggerFactory.getLogger(CustomFilter.class);
	
	// pre Filter
	// ctrl + shift + O
	public static Function<ServerRequest, ServerRequest> printPreRequestId() {
		return (request) -> {
			String reqId = request.servletRequest().getRequestId();
			String requestPath = request.path();
			
			logger.info("<Pre filter> Request ID: {}", reqId);
			logger.info("<Pre filter> Request Path: {}", requestPath);
			
			return request;
		};
	}
	
	// post Filter
	public static BiFunction<ServerRequest, ServerResponse, ServerResponse> 
									printPostResponseStatusCode() {
		return (request, response) -> {
			
			String reqId = request.servletRequest().getRequestId();
			String requestPath = request.path();
			
			logger.info("<Post filter> Request ID: {}", reqId);
			logger.info("<Post filter> Request Path: {}", requestPath);
			
			int responseStatus = response.statusCode().value();
			logger.info("<Post filter> Response Status code {}", responseStatus);
			
			return response;
		};
	}
	
	public static void main(String[] args) {
		Function<Integer, String> convertor = (number) -> String.valueOf(number * 2);
		String result = convertor.apply(12);
		System.out.println(result);
	}
	
	
	
}
