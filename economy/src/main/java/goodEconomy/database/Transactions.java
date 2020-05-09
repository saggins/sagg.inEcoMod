package goodEconomy.database;

public enum Transactions {
	UUID("uuid"),
	PLAYER("player"),
	BLOCKS("blocks");

	private final String text;
	
	/**
	 * @param text
	 */
	Transactions(String text) {
	    this.text = text;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
	    return text;
	}
}
