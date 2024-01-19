package iainpalangkarayarepository.web.controller;

import iainpalangkarayarepository.web.model.WebResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        exception.printStackTrace();
        System.out.println("Exception type " + exception.getClass());
        System.out.println("Exception message " + exception.getMessage());
        ProblemDetail problemDetail = null;
        
        if (exception instanceof ResponseStatusException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());
            problemDetail.setProperty("message", ((ResponseStatusException) exception).getReason());
        }
        
        if (exception instanceof ConstraintViolationException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());
            problemDetail.setProperty("message", "Please fill the input properly");
        }
        
        if (exception instanceof UsernameNotFoundException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());
            problemDetail.setProperty("message", "User not found");
        }
        
        if (exception instanceof MalformedJwtException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            problemDetail.setProperty("message", "Your token is invalid, please login again");
        }
        
        if (exception instanceof ExpiredJwtException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            problemDetail.setProperty("message", "Your token is invalid, please login again");
        }
        
        if (exception instanceof SignatureException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            problemDetail.setProperty("message", "Your token is invalid, please login again");
        }
        
        if (exception instanceof AccessDeniedException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            problemDetail.setProperty("message", "You are not allowed to this specific resource");
        }
        
        return problemDetail;
    }
    
}
