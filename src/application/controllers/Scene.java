package application.controllers;

public enum Scene {
	LOGIN_SCENE("../views/LoginScene.fxml", 300, 400),
	SIGN_UP_SCENE("../views/SignUpScene.fxml", 300, 500),
	CART_SCENE("../views/CartScene.fxml", 650, 630),
	ADD_BOOK_SCENE("../views/AddBookScene.fxml", 300, 400),
	ADD_ORDER_SCENE("../views/AddOrderScene.fxml", 300, 300),
	ADD_PUBLISHER_SCENE("../views/AddPublisherScene.fxml", 300, 400),
	EDIT_PROFILE_SCENE("../views/EditProfileScene.fxml", 300, 500),
	MANAGER_SCENE("../views/ManagerScene.fxml", 650, 550),
	REPORT_SCENE("../views/ReportScene.fxml", 650, 550),
	UPDATE_BOOK_SCENE("../views/UpdateBookScene.fxml", 300, 400),
	SEARCH_BOOKS_SCENE("../views/SearchBooksScene.fxml", 650, 550);
	
	private String path;
	private int width;
	private int height;
	
	private Scene(String path, int width, int height) {
		this.path = path;
		this.width = width;
		this.height = height;
	}
	
	public String getPath() {
		return path;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
