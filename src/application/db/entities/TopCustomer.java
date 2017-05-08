package application.db.entities;

public class TopCustomer {
	
	private int id;
	private String firstname;
	private String lastname;
	private double totalCash;

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname
	 *            the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname
	 *            the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the totalCash
	 */
	public double getTotalCash() {
		return totalCash;
	}

	/**
	 * @param totalCash
	 *            the totalCash to set
	 */
	public void setTotalCash(double totalCash) {
		this.totalCash = totalCash;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TopCustomer [firstname=");
		builder.append(firstname);
		builder.append(", lastname=");
		builder.append(lastname);
		builder.append(", totalCash=");
		builder.append(totalCash);
		builder.append("]");
		return builder.toString();
	}

}
