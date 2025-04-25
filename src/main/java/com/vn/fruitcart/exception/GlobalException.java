package com.vn.fruitcart.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

  @ExceptionHandler(value = {NoResourceFoundException.class})
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

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<RestResponse<Object>> handleUsernameNotFoundException(
      UsernameNotFoundException ex) {
    RestResponse<Object> res = new RestResponse<>();
    res.setStatusCode(HttpStatus.NOT_FOUND.value());
    res.setMessage("Invalid username or password");
    res.setError("USER_NOT_FOUND");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<RestResponse<Object>> handleBadCredentialsException(
      BadCredentialsException ex) {
    RestResponse<Object> res = new RestResponse<>();
    res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
    res.setMessage("Invalid username or password"); // Generic message for security
    res.setError("INVALID_CREDENTIALS");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
  }

  @ExceptionHandler(value = {
      PermissionException.class,
  })
  public ResponseEntity<RestResponse<Object>> handlePermissionException(Exception ex) {
    RestResponse<Object> res = new RestResponse<Object>();
    res.setStatusCode(HttpStatus.FORBIDDEN.value());
    res.setError("Forbidden");
    res.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
  }
}
