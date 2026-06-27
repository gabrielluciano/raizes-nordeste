package com.raizesdonordeste.app.api.error;

import com.raizesdonordeste.app.domain.comum.exception.DominioException;
import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.identidade.exceptions.AcessoNegadoException;
import com.raizesdonordeste.app.domain.identidade.exceptions.NaoAutorizadoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static final String ERRO_VALIDACAO_CODIGO = "ERRO_VALIDACAO";
    private static final String ERRO_VALIDACAO_MENSAGEM = "Um ou mais campos são inválidos.";
    private static final String ACESSO_NEGADO_CODIGO = "ACESSO_NEGADO";
    private static final String ACESSO_NEGADO_MENSAGEM = "Acesso negado.";
    private static final String ERRO_INTERNO_CODIGO = "ERRO_INTERNO";
    private static final String ERRO_INTERNO_MENSAGEM = "Ocorreu um erro inesperado.";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> detalhes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new ErrorResponse.FieldError(e.getField(), e.getDefaultMessage()))
                .toList();

        return ErrorResponse.of(ERRO_VALIDACAO_CODIGO, ERRO_VALIDACAO_MENSAGEM, detalhes, request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> detalhes = ex.getConstraintViolations()
                .stream()
                .map(v -> new ErrorResponse.FieldError(v.getPropertyPath().toString(), v.getMessage()))
                .toList();

        return ErrorResponse.of(ERRO_VALIDACAO_CODIGO, ERRO_VALIDACAO_MENSAGEM, detalhes, request.getRequestURI());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAuthorizationDenied(AuthorizationDeniedException ex, HttpServletRequest request) {
        return ErrorResponse.of(ACESSO_NEGADO_CODIGO, ACESSO_NEGADO_MENSAGEM, request.getRequestURI());
    }

    @ExceptionHandler(NaoAutorizadoException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNaoAutorizado(NaoAutorizadoException ex, HttpServletRequest request) {
        return ErrorResponse.of(ex.getCodigo(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ValidacaoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidacao(ValidacaoException ex, HttpServletRequest request) {
        return ErrorResponse.of(ex.getCodigo(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AcessoNegadoException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAcessoNegado(AcessoNegadoException ex, HttpServletRequest request) {
        return ErrorResponse.of(ex.getCodigo(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DominioException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ErrorResponse handleDominio(DominioException ex, HttpServletRequest request) {
        return ErrorResponse.of(ex.getCodigo(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex, HttpServletRequest request) {
        log.error("Exceção não mapeada em {} {}", request.getMethod(), request.getRequestURI(), ex);
        return ErrorResponse.of(ERRO_INTERNO_CODIGO, ERRO_INTERNO_MENSAGEM, request.getRequestURI());
    }
}
