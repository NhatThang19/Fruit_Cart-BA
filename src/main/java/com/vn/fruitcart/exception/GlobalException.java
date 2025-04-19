package com.vn.fruitcart.exception;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
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
  public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
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

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<RestResponse<Object>> handleDataIntegrityViolation(
      DataIntegrityViolationException ex) {

    String rootCause = ex.getMessage();
    RestResponse<Object> res = new RestResponse<>();

    if (rootCause.contains("Duplicate entry")) {
      String fieldName = extractFieldNameFromErrorMessage(rootCause);
      res.setStatusCode(HttpStatus.CONFLICT.value());
      res.setError("Duplicate entry");
      res.setMessage(fieldName + " đã tồn tại trong hệ thống");
      return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError("DATA_INTEGRITY_VIOLATION");
    res.setMessage("Vi phạm ràng buộc dữ liệu");
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

  private String extractFieldNameFromErrorMessage(String errorMessage) {
    Pattern pattern = Pattern.compile("Duplicate entry '(.+?)' for key");
    Matcher matcher = pattern.matcher(errorMessage);
    if (matcher.find()) {
      String duplicateValue = matcher.group(1);

      if (duplicateValue.contains("@")) {
        return "Email";
      }
    }
    return "Dữ liệu";
  }
}
