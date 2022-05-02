package main.java.cz.upol.jj.brodacky;

import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClientController {
	
	private final String exampleUrl = "https://www.irozhlas.cz/rss/irozhlas";
	
	private Stage primaryStage;
    private RssReader reader = new RssReader(exampleUrl);

	@FXML private Text textFeed;
	@FXML private ListView<String> listView;
	ObservableList<String> listContent = FXCollections.observableArrayList();

	@FXML private VBox infoVBox;
	@FXML private Text textTitle;
	@FXML private Text textDate;
	@FXML private Text textDescription;

	
    @FXML
    public void initialize() {
		listView.setItems(listContent);
		listView.setOnMouseClicked(value ->  {
			int index = listView.getSelectionModel().getSelectedIndex();

			updateInfo(index);
		});
		loadNew();

		textFeed.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		textTitle.setFont(Font.font("Verdana", FontWeight.MEDIUM, 14));
		textDate.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
		textDescription.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
	}

	@FXML public void exitAction() {
        primaryStage.close();
	}

    public void updateInfo(int index) {
        RssItem item = reader.getItems().get(index);


		textTitle.setText("Title: " + item.title());
		textDate.setText("Date: " + item.pubDate());
		textDescription.setText("Description: " + item.description());

		textTitle.setWrappingWidth(500);
		textDescription.setWrappingWidth(500);
	}

	public void updateList() {
		listView.getItems().clear();
		for (RssItem item : reader.getItems()) {
			listContent.add(item.pubDate() + " | " + item.title());
		}
	}

	@FXML public void loadNew() {
		try {
			reader.downloadFeed();
			updateList();
			updateInfo(0);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error while loading new content.");

            alert.showAndWait();  
		}	
	}

	@FXML public void setFeedUrl() {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);

		VBox dialogVbox = modalContent(dialog);

		Scene dialogScene = new Scene(dialogVbox, 200, 100);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	private VBox modalContent(Stage dialog){
		VBox dialogVbox = new VBox(2);

		Label label = new Label("Enter valid URL:");
		TextField urlField = new TextField();
		Button save = new Button("Save");
		Button close = new Button("Close");

		save.setOnAction(value ->  {
			String url = urlField.getText();

			if(isValid(urlField.getText())) {
				reader.setUrl(url);
				dialog.close();
				loadNew();
			}
		});

		close.setOnAction(value ->  {
			dialog.close();
		});

		HBox hb = new HBox();
		hb.getChildren().addAll(save, close);
		dialogVbox.getChildren().addAll(label, urlField, hb);

		return dialogVbox;
	}

	public static boolean isValid(String url){
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void setPrimaryStage(Stage stage) {
		this.primaryStage = stage;
	}
}
