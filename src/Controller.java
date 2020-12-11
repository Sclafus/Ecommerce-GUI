/**
 * This interface is used in conjunction with {@code Loader} class.
 * Every controller that needs to receive data from a previous
 * controller, has to implement this interface.
 * @see Loader
 */
public interface Controller {
    /**
	 * Initialize {@code this.current_user} with the passed value.
	 * This method is made to be called from another controller,
	 * using the {@code load} method in {@code Loader} class.
	 * @param user the {@code User} we want to pass. [User]
	 * @see Loader
	 */
    void initData(User user);
}