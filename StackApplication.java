import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.io.File;

public class StackApplication extends Application {

	private Stage primaryStage;
	private Scene scene;
	private TextArea resultsTextArea;
	private String[] sections;
	private int currentSectionIndex = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		// Set up the main user interface with buttons and text area

		Button fileButton = new Button("Select File");
		fileButton.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: black; -fx-background-color: green;");

		fileButton.setOnAction(e -> handleFileSelection());

		resultsTextArea = new TextArea();
		resultsTextArea.setEditable(false);
		resultsTextArea.setWrapText(true);

		Button nextButton = new Button("Next");
		nextButton.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: black; -fx-background-color: green;");

		nextButton.setOnAction(e -> displayNextSection());

		Button prevButton = new Button("Previous");
		prevButton.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: black; -fx-background-color: green;");

		prevButton.setOnAction(e -> handlePrevious());

		VBox layout = new VBox(20);
		layout.getChildren().addAll(fileButton, resultsTextArea, nextButton, prevButton);
		layout.setAlignment(Pos.CENTER);

		scene = new Scene(layout, 400, 400);
		layout.setStyle("-fx-background-color: #A9A9A9;");
		primaryStage.setTitle("Application Stack");
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	// Displays the next section if available; otherwise, shows an alert.

	private void displayNextSection() {
		if (sections != null && currentSectionIndex < sections.length - 1) {
			currentSectionIndex++;
			String nextSection = sections[currentSectionIndex];
			String results = Te.processSection(nextSection);
			displayResults(results);
		} else {
			showAlert("No more sections available.");
		}
	}
	// Shows a pop-up alert with the provided message.

	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	// Handle the "Previous" button click to navigate to the previous section.
	private void handlePrevious() {
	    if (sections != null && sections.length > 1 && currentSectionIndex > 0) {
	        // Move to the previous section.
	        currentSectionIndex--;
	        String previousSection = sections[currentSectionIndex];
	        String results = Te.processSection(previousSection);
	        displayResults(results);
	    } else {
	        showAlert("No previous section available.");
	    }
	}


	// Handles the file selection using a FileChooser dialog.

	private void handleFileSelection() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select DS-Proj2.242 File");

		// Add a filter to only allow files with the desired extension
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("DS-Proj2.242 Files", "*.242");
		fileChooser.getExtensionFilters().add(extensionFilter);

		try {
			// Show the file dialog and get the selected file
			File selectedFile = fileChooser.showOpenDialog(primaryStage);

			if (selectedFile != null) {
				boolean isValid = Te.validateXMLFile(selectedFile.getAbsolutePath());
				showValidationResult(isValid);

				if (isValid) {
					processAndDisplaySections(selectedFile.getAbsolutePath());
				}
			} else {
				showAlert("No file selected");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Shows a validation result alert based on file validity.

	private void showValidationResult(boolean isValid) {
		Alert alert = new Alert(isValid ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
		alert.setTitle("Validation Result");
		alert.setHeaderText(null);

		if (isValid) {
			alert.setContentText("The file is valid.");
		} else {
			alert.setContentText("The file is not valid.");
		}

		alert.showAndWait();
	}
	// Processes and displays sections from the specified file path.

	private void processAndDisplaySections(String filePath) {
		String fileContent = Te.readFile(filePath);
		sections = fileContent.split("\n\n");

		if (sections.length > 0) {
			displayResults(Te.processSection(sections[0]));
		} else {
			showAlert("No sections found in the file.");
		}
	}
	// Displays the provided results in the text area.

	private void displayResults(String results) {
		resultsTextArea.setText(results);
	}
	// Method to start the JavaFX application.

	public void startApp(String[] args) {
		launch(args);
	}
}
