package com.example.movies.API.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;


@RestControllerAdvice
public class GlobalValidationHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex
  ) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String message   = error.getDefaultMessage();
      errors.put(fieldName, message);
    });
    return errors;
  }

  // ─── New handler for JSON‐parsing or “cannot read message” errors ─────────
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleParsingExceptions(
      HttpMessageNotReadableException ex
  ) {
    // You can parse the exception message to get more detail—but here’s a simple response:
    String rawMessage = ex.getMessage();
    String userFriendly;
    if (rawMessage != null && rawMessage.contains("LocalDate")) {
      userFriendly = "Invalid date format. Please use ISO 8601 (YYYY-MM-DD).";
    } else {
      userFriendly = "Malformed JSON request.";
    }

    Map<String, String> errorBody = new HashMap<>();
    errorBody.put("error", userFriendly);
    return errorBody;
  }

    // 1) Handle a missing required field in JSON (bind errors)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String,String> handleMissingParam(MissingServletRequestParameterException ex) {
    String name = ex.getParameterName();
    Map<String,String> body = new HashMap<>();
    body.put("error", "Required parameter '" + name + "' is missing.");
    return body;
  }

  // 2) Handle numeric parse errors (e.g. sending "xyz" for an integer)
  @ExceptionHandler(NumberFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String,String> handleNumberFormat(NumberFormatException ex) {
    Map<String,String> body = new HashMap<>();
    body.put("error", "Expected a number but got invalid value.");
    return body;
  }

  /**
   * Catch any ResourceNotFoundException thrown by services/controllers 
   * and return a JSON body { "error": "…" } with HTTP 404.
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String,String> handleNotFound(ResourceNotFoundException ex) {
    Map<String,String> body = new HashMap<>();
    body.put("error", ex.getMessage());
    return body;
  }

  @ExceptionHandler(IllegalStateException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public Map<String, String> handleIllegalState(IllegalStateException ex) {
    return Map.of("error", ex.getMessage());
}
}
