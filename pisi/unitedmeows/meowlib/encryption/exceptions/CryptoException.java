package pisi.unitedmeows.meowlib.encryption.exceptions;

public class CryptoException extends Exception {
	private static final long serialVersionUID = -2821149113104976263L;

	public CryptoException() {}

	public CryptoException(final String message) { super(message); }

	public CryptoException(final String message, final Throwable cause) { super(message, cause); }

	public CryptoException(final Throwable cause) { super(cause); }

	public CryptoException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}
