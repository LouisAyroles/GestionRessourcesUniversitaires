package bdd;

public class BaseDeDonneesException extends Exception {
	private static final long serialVersionUID = 1L;

	public BaseDeDonneesException() {
	}

	public BaseDeDonneesException(String message) {
		super(message);
	}

	public BaseDeDonneesException(Throwable cause) {
		super(cause);
	}

	public BaseDeDonneesException(String message, Throwable cause) {
		super(message, cause);
	}
}
