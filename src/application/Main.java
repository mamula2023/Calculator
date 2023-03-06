package application;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private String operators = "+-*/";

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));

			Parent root = loader.load();
			Controller controller = loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	
			
			
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			
				@Override
				public void handle(KeyEvent e) {
					// TODO Auto-generated method stub
					String input = e.getText();
					char ch = '0';
					if (input.length() != 0) {
						ch = input.charAt(0);

						if (ch >= '0' && ch <= '9')
							controller.writeFromKey(ch);
						else if (operators.indexOf(ch) != -1)
							controller.writeFromKey(ch);
					}
				}

			});

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private ArrayList<Integer> toArrayList(String num) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < num.length(); i++) {
			char ch = num.charAt(i);
			int digit = ch - '0';
			result.add(digit);
		}
		return result;
	}
}
