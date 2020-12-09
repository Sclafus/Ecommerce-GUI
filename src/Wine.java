import java.io.Serializable;

/**
 * Abstraction of a determined Wine. Every wine has a name, a producer, the year
 * of production, some notes that may be useful for the customers, a string
 * containing all the grapes used in each wine, the quantity in stock of each
 * wine.
 */
public class Wine implements Serializable {

	private static final long serialVersionUID = 1727284212719259730L;
	private String name;
	private int product_id;
	private String producer;
	private int year;
	private String notes;
	private int quantity;
	private String grapes;

	/**
	 * {@code Wine} class constructor.
	 */
	public Wine() {
		this.name = "";
		this.producer = "";
		this.product_id = -1;
		this.year = -1;
		this.notes = "";
		this.grapes = "";
		this.quantity = -1;
	}

	/**
	 * {@code Wine} class constructor.
	 * 
	 * @param name       name of the wine. [String]
	 * @param producer   producer of the wine. [String]
	 * @param year       year of production of the wine. [int]
	 * @param notes      notes for the wine. [String]
	 * @param product_id unique id of the wine. [int]
	 * @param quantity   quantity of the wine. [int]
	 * @param grapes     list of the grapes. [String]
	 */
	public Wine(final int id, final String name, final String producer, final int year, final String notes,
			final int quantity, final String grapes) {

		this.name = name;
		this.producer = producer;
		this.year = year;
		this.notes = notes;
		this.product_id = id;
		this.quantity = quantity;
		this.grapes = grapes;
	}

	/**
	 * Gets the name of the {@code Wine}.
	 * 
	 * @return the name of the {@code Wine}. [String]
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the producer of the {@code Wine}.
	 * 
	 * @return the producer of the {@code Wine}. [String]
	 */
	public String getProducer() {
		return this.producer;
	}

	/**
	 * Gets the name of the {@code Wine}.
	 * 
	 * @return the name of the {@code Wine}. [String]
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * Gets the notes of the {@code Wine}.
	 * 
	 * @return the notes of the {@code Wine}. [String]
	 */
	public String getNotes() {
		return this.notes;
	}

	/**
	 * Gets the quantity of the {@code Wine}.
	 * 
	 * @return the quantity of the {@code Wine}. [int]
	 */
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * Gets the grapes of the {@code Wine}.
	 * 
	 * @return the grapes of the {@code Wine}. [String]
	 */
	public String getGrapewines() {
		return this.grapes;
	}

	/**
	 * Gets the product id of the {@code Wine}.
	 * 
	 * @return the product id of the {@code Wine}. [int]
	 */
	public int getProductId() {
		return this.product_id;
	}

	/**
	 * Adds the specified quantity to the selected {@code Wine}.
	 * 
	 * @param quantity the quantity to add. [int]
	 */
	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}

	/**
	 * Substracts the specified quantity to the selected {@code Wine}.
	 * 
	 * @param quantity the quantity to subtract. [int]
	 */
	public void subtractQuantity(int quantity) {
		this.quantity -= quantity;
	}
}