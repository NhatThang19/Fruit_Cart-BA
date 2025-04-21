package com.vn.fruitcart.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.vn.fruitcart.domain.dto.response.RestResponse;

@ControllerAdvice
public class GlobalException {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestResponse<Object>> handleAllException(Exception ex) {
    RestResponse<Object> res = new RestResponse<>();
    res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    res.setMessage(ex.getMessage());
    res.setError("Internal Server Error");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
  }

  @ExceptionHandler(value = {NoResourceFoundException.class,})
  public ResponseEntity<RestResponse<Object>> handleNotFoundException(Exception ex) {
    RestResponse<Object> res = new RestResponse<Object>();
    res.setStatusCode(HttpStatus.NOT_FOUND.value());
    res.setMessage(ex.getMessage());
    res.setError("404 Not Found. URL may not exist...");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestResponse<Object>> handleValidationError(
      MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    final List<FieldError> fieldErrors = result.getFieldErrors();

    RestResponse<Object> res = new RestResponse<Object>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(ex.getBody().getDetail());

    List<String> errors = fieldErrors.stream().map(
        DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
    res.setMessage(errors.size() > 1 ? errors : errors.get(0));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<RestResponse<Object>> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    RestResponse<Object> res = new RestResponse<>();
    res.setStatusCode(HttpStatus.NOT_FOUND.value());
    res.setMessage(ex.getMessage());
    res.setError("Resource not found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<RestResponse<Object>> handleReResourceAlreadyExistsException(
      ResourceAlreadyExistsException ex) {
    RestResponse<Object> res = new RestResponse<>();
    res.setStatusCode(HttpStatus.CONFLICT.value());
    res.setMessage(ex.getMessage());
    res.setError("Resource Already Exists");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
  }
}
