import java.util.ArrayList;
import java.util.Collections;

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
	 * @param wines the wines the {@code People} wants to buy. [Wine Array]
	 * @see Wine
	 * @see User
	 */
	public Order(int id, Boolean ship, String cust, final Wine[] wines){
		this.id = id;
		this.shipped = ship;
		this.customer = cust;
		Collections.addAll(items, wines);
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
	 * @return the wines of the {@code Order}. [Wine Array]
	 * @see Wine
	 */
	public Wine[] getWines(){
		Wine[] wines_arr = new Wine[items.size()]; 
		wines_arr = items.toArray(wines_arr);
		return wines_arr;
	}
	
	/**
	 * Changes the status of the selected {@code Order} to {@code true} once the order has been shipped.
	 */
	public void changeStatus(){
		this.shipped = true;
	}
}