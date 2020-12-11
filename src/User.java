import java.io.Serializable;

/**
 * Abstraction of a determined User. Every user has a name, a surname,
 * an email and a password to login and a permission which states whenever
 * the user is a normal user (permission 1), an employee (permission 2), or an admin (permission 3).
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
		this.permission = 0;
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