package main.java.cz.upol.jj.brodacky;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

public class ClientController {
	
	private final String exampleUrl = "https://www.irozhlas.cz/rss/irozhlas";
	protected final static int WHITE_SPACE = 10; 
	
	private Stage primaryStage;
    private RssReader reader = new RssReader(exampleUrl);
	private String url = "";

	@FXML private Text textFeed;
	@FXML private ListView<String> listView;
	ObservableList<String> listContent = FXCollections.observableArrayList();
	
	@FXML private VBox feedVBox;
	@FXML private VBox infoVBox;
	@FXML private Text textTitle;
	@FXML private Text textDate;
	@FXML private Text textAuthor;
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

		feedVBox.setPadding(new Insets(WHITE_SPACE / 2, 0, 0, 0));
		infoVBox.setPadding(new Insets(WHITE_SPACE));
	}

	@FXML public void exitAction() {
        primaryStage.close();
	}

    public void updateInfo(int index) {
        RssItem item = reader.getItems().get(index);
		url = item.link();

		textTitle.setText(item.title());
		textDate.setText(item.pubDate());
		textDescription.setText(item.description());
		textAuthor.setText(item.author());

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

	@FXML public void showFeedInfo() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Feed informations");
		alert.setHeaderText(null);
		alert.setContentText(reader.getChannel().toString());
		alert.showAndWait();  
	}

	@FXML public void copyUrl() {
		StringSelection stringSelection = new StringSelection(url);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText("URL was copied to clipboard.");

		alert.showAndWait();  
	}

	@FXML public void openWebView() {
		final Stage webStage = new Stage();
		webStage.initModality(Modality.APPLICATION_MODAL);
		webStage.initOwner(primaryStage);

        WebView webView = new WebView();

        webView.getEngine().load(url);
		
		VBox webVbox = new VBox(webView);

		Scene webScene = new Scene(webVbox, 500, 500);
		webStage.setScene(webScene);
		webStage.setTitle("WebView");
		webStage.show();
	}

	@FXML public void setFeedUrl() {
		SetFeedModal modal = new SetFeedModal(primaryStage, reader, this);
		modal.show();
	}

	public void setPrimaryStage(Stage stage) {
		this.primaryStage = stage;
	}
}
