import java.util.ArrayList;
//TODO javadoc (!!FIX THE OLD ONES) I THINK I DID BUT IDK
/**
 * Abstraction of a determined Order. Every order has an id, an email which relates 
 * the order to the {@code User} who placed it, a status (true if the order has been shipped, 
 * false otherwise) and the list of {@code Wine} that the {@code User} has ordered.
 * @see Wine
 * @see
 */
public class Order {

	private ArrayList<Wine> items = new ArrayList<Wine>();
	private int id;
	private String customer;
	private Boolean shipped;

	/**
	 * {@code Order} class constructor.
	 */
	public Order(){
		this.shipped = false;
	}

	/**
	 * {@code Order} class constructor.
	 * @param wines the wines the {@code User} wants to buy. [ArrayList<Wine>]
	 * @param id of the {@code Order}. [Int]
	 * @param customer email of the {@code User} who placed the {@code Order}. [String]
	 * @see Wine
	 * @see User
	 */
	public Order(final int id, final String cust, final ArrayList<Wine> wines){
		this.id = id;
		this.shipped = false;
		this.customer = cust;
		this.items = wines;
	}

	/**
	 * Gets the email of the customer of the selected {@code Order}.
	 * @return customer's email. [String]
	 * @see User
	 */
	public String getCustomer(){
		return this.customer;
	}
	
	/**
	 * Gets the id of the selected {@code Order}.
	 * @return the id of the {@code Order}. [int]
	 */
	public int getId(){
		return this.id;
	}

	/**
	 * Gets the status of the selected {@code Order}. 
	 * @return {@code true} if the order has been shipped, else {@code false}. [Boolean]
	 */
	public Boolean getStatus(){
		return this.shipped;
	}

	/**
	 * Gets the wines from the selected {@code Order}.
	 * @return the wines of the {@code Order}. [ArrayList<Wine>]
	 * @see Wine
	 */
	public ArrayList<Wine> getWines(){
		return this.items;
	}
	
	/**
	 * Changes the status of the selected {@code Order} to {@code true} once the order has been shipped.
	 */
	public void ship(){
		this.shipped = true;
	}
}