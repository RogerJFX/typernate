package de.crazything.sql;

//CHECKSTYLE:OFF
public class PoolBusyException extends Exception {

	/**
	 * SUID.
	 */
	private static final long serialVersionUID = 677197274345321129L;

	public PoolBusyException() {
		System.err.println("POOL BUSY " + this.getMessage());
	}

	@Override
	public String getMessage() {
		return "All connections in use.";
	}
}
// CHECKSTYLE:ON