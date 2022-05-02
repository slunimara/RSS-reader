package main.java.cz.upol.jj.brodacky;

import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    private final String exampleUrl = "https://www.irozhlas.cz/rss/irozhlas";
    private RssReader reader = new RssReader(exampleUrl);
    private List<RssItem> items;
    private Scanner scanner;

    public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ConsoleClient.class.getResource("res/Client.fxml"));
		Parent root = loader.load(); 
        ClientController controller = loader.getController();
        controller.setPrimaryStage(stage);
        this.scanner = new Scanner(System.in);

		stage.setTitle("RSS Reader,");
		stage.setScene(new Scene(root, 400, 300));
		stage.show();
	}
}
