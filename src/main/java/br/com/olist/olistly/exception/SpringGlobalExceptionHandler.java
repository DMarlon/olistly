package br.com.olist.olistly.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.olist.olistly.dto.ApiErrorDTO;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.security.service.SecurityService;

@ControllerAdvice
public class SpringGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String RESPONSESTATUS_INTERNAL_MESSAGE = "Erro ao executar operação: ";
	private static final String HTTPMESSAGENOTREADABLE_INTERNAL_MESSAGE = "Erro ao processar os dados da requisição: ";
	private static final String HTTPMESSAGENOTREADABLE_PUBLIC_MESSAGE = "Houve um erro ao processar os dados da requisição.";
	private static final String INTERNALSERVERERROR_INTERNAL_MESSAGE = "Houve um erro interno: ";
	private static final String INTERNALSERVERERROR_PUBLIC_MESSAGE = "Houve um erro ao executar sua requisição, contate o suporte!";
	private static final String ACCESSDENIED_PUBLIC_MESSAGE = "Funcionalidade indisponível!";
	private static final String ACCESSDENIED_INTERNAL_MESSAGE = "Tentou acessar uma função que não tem permissão: ";

	private Logger logger = LoggerFactory.getLogger(SpringGlobalExceptionHandler.class);

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
		createLog(HTTPMESSAGENOTREADABLE_INTERNAL_MESSAGE+exception.getMessage() + (exception.getCause() == null ? "" : " - Cause: "+exception.getCause().getMessage()));
        return new ResponseEntity<Object>(createApiErrorDTO(HttpStatus.FORBIDDEN, HTTPMESSAGENOTREADABLE_PUBLIC_MESSAGE, request), new HttpHeaders(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> accessDeniedExceptionHandler(Exception exception, WebRequest request) {
		createLog(ACCESSDENIED_INTERNAL_MESSAGE+exception.getMessage() + (exception.getCause() == null ? "" : " - Cause: "+exception.getCause().getMessage()));
		return new ResponseEntity<Object>(createApiErrorDTO(HttpStatus.FORBIDDEN, ACCESSDENIED_PUBLIC_MESSAGE, request), new HttpHeaders(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(ResponseStatusException.class)
	protected ResponseEntity<Object> responseStatusExceptionHandler(ResponseStatusException exception, WebRequest request) {
		createLog(RESPONSESTATUS_INTERNAL_MESSAGE + exception.getMessage() + (exception.getCause() == null ? "" : " - Cause: "+exception.getCause().getMessage()));
		return new ResponseEntity<Object>(createApiErrorDTO(exception.getStatus(), exception.getReason(), request), new HttpHeaders(), exception.getStatus());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> exceptionDefaultHandler(Exception exception, WebRequest request) {
		createLog(INTERNALSERVERERROR_INTERNAL_MESSAGE + exception.getMessage() + (exception.getCause() == null ? "" : " - Cause: "+exception.getCause().getMessage()));
		return new ResponseEntity<Object>(createApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, INTERNALSERVERERROR_PUBLIC_MESSAGE, request), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void createLog(String message) {
		User user = SecurityService.getLoggedUser();
		logger.error((user == null ? "[Usuário não logado] " : "["+user.getId() + "-" +user.getLogin()+"]") + message);
	}

	private ApiErrorDTO createApiErrorDTO(HttpStatus status, String message, WebRequest request) {
		return ApiErrorDTO.valueOf(status.value(), status.getReasonPhrase(), message, ((ServletWebRequest) request).getRequest().getRequestURI());
	}
}
