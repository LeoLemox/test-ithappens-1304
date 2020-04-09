package br.com.ithappens.ithappensbackend.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String userMessage = messageSource.getMessage("atributo.invalido", null, LocaleContextHolder.getLocale());
        String developerMessage = ex.getCause().toString();
        return handleExceptionInternal(ex, asList(new ExceptionMessage(userMessage, developerMessage)), headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, getExceptionMessages(ex.getBindingResult()), headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {

        String userMessage = ex.getMessage();
        String developerMessage = ExceptionUtils.getRootCauseMessage(ex);
        return handleExceptionInternal(ex, asList(new ExceptionMessage(userMessage, developerMessage)), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private List<ExceptionMessage> getExceptionMessages(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> new ExceptionMessage(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()), fieldError.toString()))
                .collect(Collectors.toList());
    }
}
