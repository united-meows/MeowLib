package pisi.unitedmeows.meowlib.encryption.exceptions;

public class BadMacTagException extends Exception {
	private static final long serialVersionUID = -3410484030808188783L;

	public BadMacTagException() {}

	public BadMacTagException(final String message) { super(message); }

	public BadMacTagException(final String message, final Throwable cause) { super(message, cause); }

	public BadMacTagException(final Throwable cause) { super(cause); }

	public BadMacTagException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}