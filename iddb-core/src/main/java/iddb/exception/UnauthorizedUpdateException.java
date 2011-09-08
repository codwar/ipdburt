package iddb.exception;

public class UnauthorizedUpdateException extends Exception {

	private static final long serialVersionUID = -2002047086265908820L;

	public UnauthorizedUpdateException() {
		super();
	}

	public UnauthorizedUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedUpdateException(String message) {
		super(message);
	}

	public UnauthorizedUpdateException(Throwable cause) {
		super(cause);
	}
}
