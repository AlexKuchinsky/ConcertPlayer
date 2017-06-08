package GraphicUserInterface.AddMenuPackage.playlistPackage;


import GraphicUserInterface.AddMenuPackage.AddMenu;
import GraphicUserInterface.MainMenuPackage.Main;

import MediaComponents.Artist;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

public class addPlaylistController implements Initializable{
    @FXML
    public ImageView imageView;
    @FXML
    public TextField textField;
    @FXML
    public Button choosePhotoButton;
    @FXML
    public ListView<String> songListView;
    @FXML
    public Button addPlaylistButton;

    protected Path pathToImage;
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Artist> artists=Main.concertPlayer.getArtists();
        int i=1;
        for(Artist artist: artists){
            ArrayList<Artist.Album> albums=artist.getAlbums();
            for(Artist.Album album: albums){
                ArrayList<Artist.Album.Song> songs=album.getAlbumSongs();
                for(Artist.Album.Song song: songs){
                    songListView.getItems().add(i+++". "+song.getNameOfTheSong()+" ("+song.getNameOfTheArtist()+", "+song.getNameOfTheAlbum()+")");
                }
            }
        }
        songListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        textField.textProperty().addListener(e->{
            if(textField.getText().compareTo("")==0)
                addPlaylistButton.setDisable(true);
            else
                addPlaylistButton.setDisable(false);
        });
        choosePhotoButton.setOnAction(e->{
            FileChooser fileChooser=new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG","*.jpg")
            );
            File file=fileChooser.showOpenDialog(null);
            if(file!=null) {
                pathToImage = file.toPath();
                imageView.setImage(new Image(pathToImage.toUri().toString()));
            }
        });
        addPlaylistButton.setOnAction(e->{
            ArrayList<Artist.Album.Song> songs=new ArrayList<>();
            ObservableList<String> listOfSongs=songListView.getSelectionModel().getSelectedItems();
            for(String str: listOfSongs){
                String nameOfTheSong=str.substring(str.indexOf('.')+2,str.indexOf('(')-1);
                String nameOfTheArtist=str.substring(str.indexOf('(')+1,str.lastIndexOf(','));
                String nameOfTheAlbum=str.substring(str.lastIndexOf(',')+2,str.length()-1);
                songs.add(Main.concertPlayer.getArtist(nameOfTheArtist).getAlbum(nameOfTheAlbum).getSongFromAlbum(nameOfTheSong));
            }
            Main.concertPlayer.addPlaylist(textField.getText(),pathToImage,songs);
            AddMenu.parentController.reloadPlaylistMenu();
            AddMenu.addWindow.close();
        });
    }
}
