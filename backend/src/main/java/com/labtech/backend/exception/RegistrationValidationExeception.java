package  com.labtech.backend.exception;

import java.util.Map;

public class RegistrationValidationExeception extends RuntimeException{

    private final Map<String,String> errors;

    public RegistrationValidationExeception(Map<String,String> errors){
        super("Registrations failed");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
