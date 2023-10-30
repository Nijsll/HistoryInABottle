package Exceptions;

public class ConstructEventException extends Exception {
    public ConstructEventException() { super(); }
    public ConstructEventException(String message) { super(message); }
    public ConstructEventException(String message, Throwable cause) { super(message, cause); }
    public ConstructEventException(Throwable cause) { super(cause); }
}
