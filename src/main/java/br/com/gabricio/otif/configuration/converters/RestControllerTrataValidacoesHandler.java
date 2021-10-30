package br.com.gabricio.otif.configuration.converters;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.gabricio.otif.exception.OtifException;
import br.com.gabricio.otif.exception.UsuarioSemAcessoException;
import br.com.gabricio.otif.validation.ApiError;


@ControllerAdvice
public class RestControllerTrataValidacoesHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UsuarioSemAcessoException.class)
	public ResponseEntity<Object> handleUsuarioSemAcesso(final UsuarioSemAcessoException ex, WebRequest request) {
		List<String> lista = new ArrayList<>();

		lista.add(ex.getMessage());

		ApiError error = new ApiError(lista);

		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}


	@ExceptionHandler(OtifException.class)
	public ResponseEntity<Object> handleMyException(OtifException ex, WebRequest request) {
		List<String> lista = new ArrayList<>();

		lista.add(ex.getMessage());

		ApiError error = new ApiError(lista);

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getDefaultMessage());
		}
		final ApiError apiError = new ApiError(errors);
		return handleExceptionInternal(ex, apiError, headers, HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Object> handleAll(Throwable ex, WebRequest request) {
		List<String> lista = new ArrayList<>();

		lista.add(ex.getMessage());

		ApiError error = new ApiError(lista);

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

}