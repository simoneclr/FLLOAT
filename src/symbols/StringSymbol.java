package symbols;

/**
 * This class represent a String symbol by implementing the interface Symbol.
 * <p>
 * As defined in the interface Symbol, it overrides equals and hashcode.
 * 
 * @author Riccardo De Masellis
 * @version 1.0
 * @since January 8, 2015
 *
 * @param <StringS>
 */
public class StringSymbol implements Symbol<String> {
	
	protected final String symbol;	
	
	/**
	 * Constructs a symbol with the given String input
	 * @param string the label of the new StringSymbol
	 */
	public StringSymbol(String symbol) {
		this.symbol=symbol;
	}
	
	
//	public static StringSymbol createSymbol(String symbol) {
//		return new StringSymbol(symbol);
//	}
	
	
	/**
	 * 
	 * @return the string associated to the label.
	 */
	public String getSymbol() {
		return this.symbol;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringSymbol other = (StringSymbol) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return this.symbol;
	}
	
	
//	public boolean isTrueSymbol() {
//		return this.getClass().equals(StringSymbolTrue.class);
//	}
	

}