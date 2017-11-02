//Created by Steven Benmoha and Colin Ackerley
package app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import view.LibraryController;
public class SongLib extends Application
{
	@Override
	public void start(Stage primaryStage) //Basic setup stuff 
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/RootPane.fxml")); //Directory for the root pane
			SplitPane root = (SplitPane) loader.load();
			LibraryController controller = loader.getController();
			controller.start(primaryStage);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Song Library");
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		launch(args);
	}
}