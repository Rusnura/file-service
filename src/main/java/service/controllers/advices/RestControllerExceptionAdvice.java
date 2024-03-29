package service.controllers.advices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.io.IOException;

@ControllerAdvice
public class RestControllerExceptionAdvice extends ResponseEntityExceptionHandler {
  @Autowired
  private ObjectMapper objectMapper;

  @ExceptionHandler(value = SecurityException.class)
  protected ResponseEntity<?> handleSecurityException(RuntimeException ex, WebRequest request) {
    final HttpStatus status = HttpStatus.FORBIDDEN;
    final ObjectNode errorObjectNode = objectMapper.createObjectNode();
    errorObjectNode.put("status", status.value());
    errorObjectNode.put("error", ex.getMessage());
    return handleExceptionInternal(ex, errorObjectNode, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(value = IOException.class)
  protected ResponseEntity<?> handleIOException(IOException ex, WebRequest request) {
    final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    final ObjectNode errorObjectNode = objectMapper.createObjectNode();
    errorObjectNode.put("status", status.value());
    errorObjectNode.put("error", ex.getMessage());
    return handleExceptionInternal(ex, errorObjectNode, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  protected ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    final HttpStatus status = HttpStatus.BAD_REQUEST;
    final ObjectNode errorObjectNode = objectMapper.createObjectNode();
    errorObjectNode.put("status", status.value());
    errorObjectNode.put("error", ex.getMessage());
    return handleExceptionInternal(ex, errorObjectNode, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(value = FileNotFoundException.class)
  protected ResponseEntity<?> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
    final HttpStatus status = HttpStatus.NOT_FOUND;
    final ObjectNode errorObjectNode = objectMapper.createObjectNode();
    errorObjectNode.put("status", status.value());
    errorObjectNode.put("error", ex.getMessage());
    return handleExceptionInternal(ex, errorObjectNode, new HttpHeaders(), status, request);
  }
}
