package main.java.cz.upol.jj.brodacky;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ConsoleClient.class.getResource("res/Client.fxml"));
		Parent root = loader.load(); 
        ClientController controller = loader.getController();
        controller.setPrimaryStage(stage);
        

		stage.setTitle("RSS Reader");
		stage.setScene(new Scene(root, 700, 600));
		stage.show();
	}
}
