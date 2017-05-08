package application.db.entities;

public class Author {
	private String name;
	private int ISBN;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getISBN() {
		return ISBN;
	}

	public void setISBN(int iSBN) {
		ISBN = iSBN;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Author [name=");
		builder.append(name);
		builder.append(", ISBN=");
		builder.append(ISBN);
		builder.append("]");
		return builder.toString();
	}

}
