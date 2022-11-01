package net.denanu.amazia.exceptions;

public class EconomyMissingModifierEconomyException extends EconomyException {

	private static final long serialVersionUID = 6313348627856430975L;
	
	public EconomyMissingModifierEconomyException() {
        super();
    }

    public EconomyMissingModifierEconomyException(String message) {
        super(message);
    }

    public EconomyMissingModifierEconomyException(String message, Throwable cause) {
        super(message, cause);
    }

    public EconomyMissingModifierEconomyException(Throwable cause) {
        super(cause);
    }
    
    protected EconomyMissingModifierEconomyException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
