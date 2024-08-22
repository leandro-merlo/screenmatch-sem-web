package br.com.manzatech.screenmatch.exceptions;

public class ModuleNotRegisteredException extends RuntimeException {

    public ModuleNotRegisteredException(String message) {
        super(message);
    }

    public ModuleNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleNotRegisteredException(Throwable cause) {
        super(cause);
    }
}
