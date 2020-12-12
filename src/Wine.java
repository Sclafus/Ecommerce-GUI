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
	private int productId;
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
		this.productId = -1;
		this.year = -1;
		this.notes = "";
		this.grapes = "";
		this.quantity = -1;
	}

	/**
	 * {@code Wine} class constructor.
	 * 
	 * @param id         product id of the {@code Wine}.[int]
	 * @param name       name of the {@code Wine}. [String]
	 * @param producer   producer of the {@code Wine}. [String]
	 * @param year       year of production of the {@code Wine}. [int]
	 * @param notes      notes for the {@code Wine}. [String]
	 * @param quantity   quantity of the {@code Wine}. [int]
	 * @param grapes     list of the grapes of the {@code Wine}. [String]
	 */
	public Wine(final int id, final String name, final String producer, final int year, final String notes,
			final int quantity, final String grapes) {

		this.name = name;
		this.producer = producer;
		this.year = year;
		this.notes = notes;
		this.productId = id;
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
		return this.productId;
	}
}