package com.vlad.demo.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    private static final String DATE_TIME_FORMAT =  "yyyy-MM-dd'T'HH:mm:SS.ssZ";
    private static final String PATH_KEY = "path";
    private static final String MESSAGE_KEY = "message";


    @Autowired
    private ErrorAttributes errorAttributes;

    ObjectMapper objectMapper = new ObjectMapper();

    protected ResponseEntity<String> handleException(final Exception exception,
                                                     final HttpStatus status,
                                                     final WebRequest request,
                                                     final String message) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ErrorResponseDto errorResponseDto = buildErrorResponseDto(status, request, message);
        try {
            return new ResponseEntity<>(objectMapper.writeValueAsString(errorResponseDto), httpHeaders, status);
        } catch(JsonProcessingException jpe) {
            log.error("Exception on creating JSON response due to: '{}'.", jpe.getMessage(), jpe);
            return new ResponseEntity<>(exception.getMessage(), httpHeaders, status);
        }
    }

    protected ErrorResponseDto buildErrorResponseDto(final HttpStatus httpStatus,
                                                     final WebRequest webRequest,
                                                     final String messageDetails){
    	
        ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
        HttpServletRequest request = servletRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, Object> errors = errorAttributes.getErrorAttributes(webRequest, false);
        final Throwable webRequestThrowable = errorAttributes.getError(webRequest);
        
        String exception = webRequestThrowable.getCause() != null ? webRequestThrowable.getCause().getClass().getSimpleName() 
        		: webRequestThrowable.getClass().getSimpleName();

        return ErrorResponseDto.builder()
                .status(httpStatus == HttpStatus.INTERNAL_SERVER_ERROR ? HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase() : httpStatus.getReasonPhrase())
                .code(httpStatus == HttpStatus.INTERNAL_SERVER_ERROR ? String.valueOf(HttpStatus.SERVICE_UNAVAILABLE) : String.valueOf(httpStatus.value()))
                .message(messageDetails != null ? messageDetails : errors.get(MESSAGE_KEY).toString())
                .path(errors.get(PATH_KEY) != null ? errors.get(PATH_KEY).toString() : request.getRequestURI())
                .httpMethod(request.getMethod())
                .exception(exception)
                .timestamp(new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date()))
                .build();
    }

    @ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<String> handleNoSuchElementException(final IllegalStateException ex,
                                                                  final WebRequest webRequest) {

        return handleException(ex, HttpStatus.CONFLICT, webRequest, null);

    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException ex,
                                                                    final WebRequest webRequest) {

        return handleException(ex, HttpStatus.BAD_REQUEST, webRequest, null);

    }


    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected ResponseEntity<String> handleHttpMediaTypeNotAcceptable(
            final HttpMediaTypeNotAcceptableException ex, final WebRequest request)
    {

        return handleException(ex,  HttpStatus.NOT_ACCEPTABLE, request,
                String.format("Use acceptable media types %s.", ex.getSupportedMediaTypes().toString()));
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<String> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final WebRequest request)
    {
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        String detail = String.format("Use a supported HTTP methods %s.", supportedMethods);
        return handleException(ex,  HttpStatus.METHOD_NOT_ALLOWED, request, detail);
    }

}
