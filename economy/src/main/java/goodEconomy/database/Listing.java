package goodEconomy.database;

public enum Listing {
	BLOCK("blocks"),
	PRICE("price"),
	SOLD("sold");
	
	private final String text;

    /**
     * @param text
     */
	Listing(String text) {
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
