package ma.gov.mini.projet.config;

import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            message = "Ressource introuvable.";
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(message);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<String> handleResourceConflict(ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.TEXT_PLAIN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Suppression impossible: des enregistrements lies existent encore.");
    }
}
