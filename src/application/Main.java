package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// rollback
// error handling
public class Main extends Application {
	private static Stage stage;

	@Override
	public void start(Stage primaryStage) {
		Main.stage = primaryStage;
		Main.stage.setResizable(false);
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../application/views/LoginScene.fxml"));
			Scene scene = new Scene(root, 300, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadScene(Parent root, int width, int height) throws Exception {
		Scene scene = new Scene(root, width, height);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
