package net.denanu.amazia.exceptions;

public class EconomyException extends RuntimeException {
	@java.io.Serial
	private static final long serialVersionUID = 984792372091489709L;

    public EconomyException() {
        super();
    }

    public EconomyException(String message) {
        super(message);
    }

    public EconomyException(String message, Throwable cause) {
        super(message, cause);
    }

    public EconomyException(Throwable cause) {
        super(cause);
    }
    
    protected EconomyException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
