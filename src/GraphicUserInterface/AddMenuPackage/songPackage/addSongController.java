package GraphicUserInterface.AddMenuPackage.songPackage;

import GraphicUserInterface.AddMenuPackage.AddMenu;
import GraphicUserInterface.MainMenuPackage.Main;
import MediaComponents.Artist;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

/**
 * Created by Alex on 14.05.2017.
 */
public class addSongController implements Initializable {
    @FXML
    public ImageView imageView;
    @FXML
    public ComboBox artistComboBox;
    @FXML
    public ComboBox albumComboBox;
    @FXML
    public Button chooseSongButton;
    @FXML
    public TextField textField;
    @FXML
    public Button chooseImageButton;
    @FXML
    public Button addNewSongButton;

    public addSongController(){}

    File song;
    File image;
    public void initialize(URL location, ResourceBundle resources){
        artistComboBox.getItems().addAll(Main.concertPlayer.getArtistsNames());
        artistComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            albumComboBox.getItems().clear();
            albumComboBox.getItems().addAll(Main.concertPlayer.getArtist(artistComboBox.getSelectionModel().getSelectedItem().toString()).getAlbumsNames());
            albumComboBox.setDisable(false);
        });
        albumComboBox.getSelectionModel().selectedItemProperty().addListener(e->chooseSongButton.setDisable(false));
        chooseSongButton.setOnAction(e->{
            FileChooser fileChooser=new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MP3","*.mp3"),
                    new FileChooser.ExtensionFilter("M4A","*.m4a")
            );
            song=fileChooser.showOpenDialog(null);
            if(song!=null)
                textField.setText(song.getName().substring(0,song.getName().lastIndexOf('.')));
        });
        chooseImageButton.setOnAction(e->{
            FileChooser fileChooser=new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG","*.jpg")
            );
            image=fileChooser.showOpenDialog(null);
            if(image!=null)
                imageView.setImage(new Image(image.toPath().toUri().toString()));
        });
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(song!=null && textField.getText().compareTo("")!=0)
                addNewSongButton.setDisable(false);
            else
                addNewSongButton.setDisable(true);
        });
        addNewSongButton.setOnAction(e->{
            Artist.Album album=Main.concertPlayer.getArtist(artistComboBox.getSelectionModel().getSelectedItem().toString()).getAlbum(albumComboBox.getSelectionModel().getSelectedItem().toString());
            album.addSongToAlbum(textField.getText(),song.toPath(), (image!=null)? image.toPath(): null);
            AddMenu.parentController.reloadArtistMenu();
            AddMenu.addWindow.close();
        });
    }
}
