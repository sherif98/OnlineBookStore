package application.db.entities;

public class TopBook {

	private int ISBN;
	private String title;
	private String publisherName;
	private int soldCopies;

	/**
	 * @return the iSBN
	 */
	public int getISBN() {
		return ISBN;
	}

	/**
	 * @param iSBN
	 *            the iSBN to set
	 */
	public void setISBN(int iSBN) {
		ISBN = iSBN;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the publisherName
	 */
	public String getPublisherName() {
		return publisherName;
	}

	/**
	 * @param publisherName
	 *            the publisherName to set
	 */
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	/**
	 * @return the soldCopies
	 */
	public int getSoldCopies() {
		return soldCopies;
	}

	/**
	 * @param soldCopies
	 *            the soldCopies to set
	 */
	public void setSoldCopies(int soldCopies) {
		this.soldCopies = soldCopies;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TopBook [ISBN=");
		builder.append(ISBN);
		builder.append(", title=");
		builder.append(title);
		builder.append(", publisherName=");
		builder.append(publisherName);
		builder.append(", soldCopies=");
		builder.append(soldCopies);
		builder.append("]");
		return builder.toString();
	}

}
