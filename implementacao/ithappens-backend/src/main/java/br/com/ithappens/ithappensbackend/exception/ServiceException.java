package br.com.ithappens.ithappensbackend.exception;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 7940625541380004421L;

    public ServiceException(String message) {
        super(message);
    }
}
