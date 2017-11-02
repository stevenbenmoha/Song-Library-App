//Created by Steven Benmoha and Colin Ackerley
package view;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class LibraryController
{
	// FXML Fields needed
	@FXML
	Button Add, Edit, Delete, Save;
	@FXML
	TextField SongTitle, ArtistName, AlbumName, Year;
	@FXML
	ListView<Song> mainList = new ListView<Song>();
	private ObservableList<Song> songList;
	public void start(Stage primaryStage) // Set up everything for the initial
											// run of the program
	{
		songList = FXCollections.observableArrayList();
		readFile(); // Get any previous songs
		AlbumName.setEditable(false); // Disable everything when we first start
		SongTitle.setEditable(false);
		ArtistName.setEditable(false);
		Year.setEditable(false);
		Edit.setDisable(true);
		Delete.setDisable(true);
		if(!songList.isEmpty()) // Enable editing if songs are in the list
		{
			Edit.setDisable(false);
		}
		if(songList.isEmpty()) // Don't allow editing or deleting if song list
								// is empty
		{
			Delete.setDisable(true);
			Edit.setDisable(true);
		}
		// Add a listener to determine when to update the displayed song info
		mainList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>()
		{
			public void changed(ObservableValue<? extends Song> ov, Song oldSong, Song newSong)
			{
				if(newSong == null) // If new song is empty, make all fields
									// blank
				{
					SongTitle.setText("");
					ArtistName.setText("");
					AlbumName.setText("");
					Year.setText("");
				}
				// Set album and year to empty fields if info doesn't exist.
				// Album and year would show from other song without these lines
				else if(newSong.album == null || newSong.year == null)
				{
					if(newSong.album == null && newSong.year == null)
					{
						SongTitle.setText(newSong.name.trim());
						ArtistName.setText(newSong.artist.trim());
						Year.setText("");
						AlbumName.setText("");
					}
					else if(newSong.album == null)
					{
						SongTitle.setText(newSong.name.trim());
						ArtistName.setText(newSong.artist.trim());
						AlbumName.setText("");
						Year.setText(newSong.year.trim());
					}
					else
					{
						SongTitle.setText(newSong.name.trim());
						ArtistName.setText(newSong.artist.trim());
						AlbumName.setText(newSong.album.trim());
						Year.setText("");
					}
				}
				// Otherwise, if all info exists, update the ListView to display
				// it
				else
				{
					SongTitle.setText(newSong.name.trim());
					ArtistName.setText(newSong.artist.trim());
					AlbumName.setText(newSong.album.trim());
					Year.setText(newSong.year.trim());
				}
				// If all songs are deleted, disable deletion and editing of
				// songs
				if(songList.isEmpty())
				{
					Delete.setDisable(true);
					Edit.setDisable(true);
				}
				// If song list is not empty, enable deletion of songs
				else if(!songList.isEmpty())
				{
					Delete.setDisable(false);
				}
			}
		});
		// Other setup stuff
		FXCollections.sort(songList);
		mainList.setItems(songList);
		mainList.getSelectionModel().selectFirst();
		Add.setOnAction(this::add);
		Edit.setOnAction(this::edit);
		Delete.setOnAction(this::delete);
		SongTitle.setEditable(false);
		ArtistName.setEditable(false);
		AlbumName.setEditable(false);
		Year.setEditable(false);
		Save.setDisable(true);
	}
	@FXML
	private void add(ActionEvent event)
	{
		// Stuff that should happen when add is clicked
		mainList.setDisable(true);
		Save.setDisable(false);
		mainList.getSelectionModel().clearSelection();
		SongTitle.setEditable(true);
		ArtistName.setEditable(true);
		AlbumName.setEditable(true);
		Year.setEditable(true);
		Add.setDisable(true);
		Edit.setDisable(true);
		// Make sure no duplicate song gets added to the list
		for(Song s:songList)
		{
			if(
				ArtistName.getText().trim().equalsIgnoreCase(s.artist)
						&& SongTitle.getText().trim().equalsIgnoreCase(s.name)
				)
				{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Duplicate Information");
				alert.setHeaderText("Duplicate Song");
				alert.setContentText("That song already exists, please enter a different song");
				alert.showAndWait();
				SongTitle.setText("");
				ArtistName.setText("");
				Year.setText("");
				AlbumName.setText("");
				SongTitle.setEditable(true);
				ArtistName.setEditable(true);
				AlbumName.setEditable(true);
				Year.setEditable(true);
				Save.setDisable(false);
				mainList.setDisable(true);
				return;
			}
		}
		// Otherwise, all good, update the song with the given information
		Song newSong = new Song(SongTitle.getText(), ArtistName.getText());
		newSong.album = AlbumName.getText();
		newSong.year = Year.getText();
		songList.add(newSong);
		mainList.getSelectionModel().select(songList.indexOf(newSong));
	}
	@FXML
	private void edit(ActionEvent event)
	{
		// Stuff to do when user wants to edit a selected song
		Add.setDisable(true);
		mainList.setDisable(true);
		Save.setDisable(false);
		SongTitle.setEditable(true);
		ArtistName.setEditable(true);
		AlbumName.setEditable(true);
		Year.setEditable(true);
	}
	@FXML
	private void delete(ActionEvent event)
	{
		// Setup for deletion, as well as a confirmation aking sure the user
		// actually wants to delete the song they selected
		Save.setDisable(true);
		mainList.setDisable(false);
		Song s = mainList.getSelectionModel().getSelectedItem();
		Alert confirmDelete = new Alert(AlertType.CONFIRMATION);
		confirmDelete.setTitle("Delete Confirmation");
		confirmDelete.setHeaderText("Please Confirm Song Deletion");
		confirmDelete.setContentText("Are you sure you want to delete " + s.getName() + " by " + s.getArtist());
		Optional<ButtonType> result = confirmDelete.showAndWait();
		if(result.get() == ButtonType.OK)
		{
			int tmp = songList.indexOf(s);
			songList.remove(s);
			Add.setDisable(false);
			Edit.setDisable(false);
			if(!songList.isEmpty())
			{
				FXCollections.sort(songList);
				mainList.getSelectionModel().select(tmp);
				SongTitle.setEditable(false);
				ArtistName.setEditable(false);
				AlbumName.setEditable(false);
				Year.setEditable(false);
			}
			if(songList.isEmpty())
			{
				Edit.setDisable(true);
			}
			// Update the text file so information is synced between sessions
			try
			{
				writeToTextFile();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		if(result.get() == ButtonType.CANCEL)
		{
			Add.setDisable(true);
			Edit.setDisable(false);
			Save.setDisable(false);
		}
	}
	@FXML
	private void save(ActionEvent event) throws FileNotFoundException
	{
		mainList.setDisable(false);
		if(mainList.getSelectionModel().getSelectedItem() != null)
		{
			mainList.getSelectionModel().getSelectedItem().setName(SongTitle.getText());
			mainList.getSelectionModel().getSelectedItem().setArtist(ArtistName.getText());
			mainList.getSelectionModel().getSelectedItem().setAlbum(AlbumName.getText());
			mainList.getSelectionModel().getSelectedItem().setYear(Year.getText());
		}
		SongTitle.setEditable(false);
		ArtistName.setEditable(false);
		AlbumName.setEditable(false);
		Year.setEditable(false);
		Save.setDisable(true);
		// Makes sure the user is including an artist and song title at the very
		// least
		// If they do not enter the necessary information, abort adding a song
		// and user needs to reselect the add option
		if(SongTitle.getText().isEmpty() || ArtistName.getText().isEmpty())
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Missing Information");
			alert.setHeaderText("Enter more informtion");
			alert.setContentText("Please include a song title and artist");
			alert.showAndWait();
			SongTitle.setEditable(false);
			ArtistName.setEditable(false);
			AlbumName.setEditable(false);
			Year.setEditable(false);
			Save.setDisable(true);
			Add.setDisable(false);
			mainList.getSelectionModel().selectLast();
			songList.remove(mainList.getSelectionModel().getSelectedItem());
			mainList.getSelectionModel().selectFirst();
			AlbumName.setText("");
			ArtistName.setText("");
			Year.setText("");
			SongTitle.setText("");
			Edit.setDisable(false);
			return;
		}
		// Make sure the year field actually contains an int value
		if(!Year.getText().trim().equals(""))
		{
			String year = Year.getText().trim();
			try
			{
				Integer.parseInt(year);
			}
			catch (NumberFormatException e)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Invalid Year");
				alert.setHeaderText("Year Was Actually A String");
				alert.setContentText("Please Enter a Valid Year");
				alert.showAndWait();
				Year.setText("");
				SongTitle.setEditable(true);
				ArtistName.setEditable(true);
				AlbumName.setEditable(true);
				Year.setEditable(true);
				Save.setDisable(false);
				return;
			}
		}
		// Make sure the user cannot add duplicate songs or edit songs to be
		// duplicates
		for(Song s:songList)
		{
			if(
				ArtistName.getText().trim().equalsIgnoreCase(s.artist)
						&& SongTitle.getText().trim().equalsIgnoreCase(s.name)
						&& !(s.equals(mainList.getSelectionModel().getSelectedItem()))
				)
				{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Duplicate Information");
				alert.setHeaderText("Duplicate Song");
				alert.setContentText("That song already exists, please enter a different song");
				alert.showAndWait();
				SongTitle.setText("");
				ArtistName.setText("");
				AlbumName.setText("");
				Year.setText("");
				SongTitle.setEditable(true);
				ArtistName.setEditable(true);
				AlbumName.setEditable(true);
				Year.setEditable(true);
				Save.setDisable(false);
				mainList.setDisable(true);
				return;
			}
		}
		FXCollections.sort(songList);
		Add.setDisable(false);
		Edit.setDisable(false);
		writeToTextFile();
	}
	// Writes all of the information to a text file
	// ifferent fields are separated by a tab
	public void writeToTextFile() throws FileNotFoundException
	{
		try
		{
			FileWriter writer = new FileWriter("SongFile.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			// Overwrite the previous information anytime a new song is added or
			// a song is modified
			for(Song s:songList)
			{
				if(s.getAlbum() == null)
				{
					s.setAlbum(" ");
				}
				if(s.getYear() == null)
				{
					s.setYear(" ");
				}
				bufferedWriter.write(s.getName() + "\t" + s.getArtist() + "\t" + s.getAlbum() + "\t" + s.getYear());
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	// Read a file if it exists to sync data, or create one if there is no file
	// found
	private void readFile()
	{
		Scanner scan = null;
		try
		{
			scan = new Scanner(new File("SongFile.txt"));
		}
		// Make sure a file exists and a lack of file won't crash the program
		catch (FileNotFoundException e)
		{
			File file = new File("SongFile.txt");
			try
			{
				file.createNewFile();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			// Return to the top of the try catch if a file isn't found, crashes
			// without returning if a file doesn't exist
			return;
		}
		// Read each line, and separate all info that is broken up with a tab
		// character
		while (scan.hasNextLine())
		{
			String curLine = scan.nextLine();
			String[] splitted = curLine.split("\t");
			String title = splitted[0].trim();
			String artist = splitted[1].trim();
			Song s = new Song(title, artist);
			// Prevent IndexOutOfBounds if year/title don't exist
			if(splitted.length > 2)
			{
				s.setAlbum(splitted[2].trim());
			}
			if(splitted.length > 3)
			{
				s.setYear(splitted[3].trim());
			}
			songList.add(s);
		}
		scan.close();
	}
}