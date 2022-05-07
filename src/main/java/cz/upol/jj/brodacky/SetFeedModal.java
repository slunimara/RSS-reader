package main.java.cz.upol.jj.brodacky;

import java.net.URL;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SetFeedModal {

    private final static int WHITE_SPACE = ClientController.WHITE_SPACE; 

	private Stage primaryStage;
	private RssReader reader;
	private ClientController client;

	public SetFeedModal(Stage primaryStage, RssReader reader, ClientController client) {
		this.primaryStage = primaryStage;
		this.reader = reader;
		this.client = client;
	}

    public void show() {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);

		VBox dialogVbox = modalVBox(dialog);

		Scene dialogScene = new Scene(dialogVbox, 400, 300);
		dialog.setScene(dialogScene);
		dialog.setTitle("Set Feed URL");
		dialog.show();
	}

	private VBox modalVBox(Stage dialog){
		VBox dialogVbox = new VBox(WHITE_SPACE / 2);
		dialogVbox.setPadding(new Insets(WHITE_SPACE));
		dialogVbox.setAlignment(Pos.CENTER_LEFT);

		Label label = new Label("Enter valid URL or chose from list:");
		label.setFont(Font.font("Verdana", FontWeight.MEDIUM, 14));
		
		TextField urlField = new TextField();
		HBox buttonPanel = buttonPanel(dialog, urlField);

		ListView<String> listView = feedList(urlField);

		dialogVbox.getChildren().addAll(label, urlField, buttonPanel, listView);

		return dialogVbox;
	}

	private ListView<String> feedList(TextField urlField) {
		ListView<String> listView = new ListView<String>();
		
        listView.getItems().addAll(ClientController.feeds);

		listView.setOnMouseClicked(value ->  {
			int index = listView.getSelectionModel().getSelectedIndex();
			String url = listView.getItems().get(index);

			urlField.setText(url);
		});
		return listView;
	}

	private HBox buttonPanel(Stage dialog, TextField urlField) {
		Button save = new Button("Save");
		Button close = new Button("Close");

		save.setOnAction(value ->  {
			String url = urlField.getText();

			if(isValid(urlField.getText())) {
				reader.setUrl(url);
				dialog.close();
				client.loadNew();
			}
		});

		close.setOnAction(value ->  {
			dialog.close();
		});

		HBox hb = new HBox(WHITE_SPACE / 2);
		hb.getChildren().addAll(save, close);

		return hb;
	}

	public static boolean isValid(String url){
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
