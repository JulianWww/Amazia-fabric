package net.denanu.amazia.utils.exceptions;

public class UnimplementedException extends Exception {
    public UnimplementedException() {
        super();
    }
    
    public UnimplementedException(String message) {
        super(message);
    }

    public UnimplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnimplementedException(Throwable cause) {
        super(cause);
    }

    protected UnimplementedException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @java.io.Serial
    static final long serialVersionUID = -8734762974267862947L;
}
