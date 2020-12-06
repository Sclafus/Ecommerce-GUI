import java.io.Serializable;

//TODO: JAVADOC CLASS User
/**
 * Class {@code User} is a class that should never be instantiated.
 * This class is used as a container for Class {@code Customer} and
 * its subclass {@code Employee}.
 * @see Customer
 * @see Employee
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = -1558450048043538208L;
	private String name;
	private String surname;
	private String email;
	private String password;
	private int permission;

	/**
	 * {@code User} Class constructor.
	 */
	public User() {
		this.name = "";
		this.surname = "";
		this.email = "";
		this.password = "";
		this.permission = 1;
	}

	/**
	 * {@code User} Class constructor.
	 * @param name name of the new User. [String]
	 * @param sur surname(lastname) of the new User. [String]
	 * @param email email of the new User. [String]
	 * @param password password of the new User. [String]
	 * @param perm permission of the User (1 if User, 2 if employee, 3 if admin). [int]
	 */
	public User(final String name, final String sur, final String email, final String password ,final int perm) {
		this.name = name;
		this.surname = sur;
		this.email = email;
		this.password = password;
		this.permission = perm;
	}

	/**
	 * Gets the name of the {@code User}.
	 * @return The name of the user. [String]
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the surname of the {@code User}.
	 * @return The surname of the user. [String]
	 */
	public String getSurname() {
		return this.surname;
	}

	/**
	 * Gets the email of the {@code User}.
	 * @return The email of the user. [String]
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Gets the permission of the {@code User}.
	 * @return The permission of the user. [int]
	 */
	public int getPermission() {
		return this.permission;
	}

	/**
	 * Gets the password of the {@code User}.
	 * @return The password of the user. [String]
	 */
	public String getPassword() {
		return this.password;
	}

}