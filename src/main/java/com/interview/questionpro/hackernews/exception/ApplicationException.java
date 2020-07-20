package com.interview.questionpro.hackernews.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {
	private ApiError apiError;

	public ApplicationException(HttpStatus status, String message, String error) {
		super(message);
		this.apiError = new ApiError(status, message, error);
	}
	
	public ApiError getApiError() {
		return apiError;
	}
	
}
