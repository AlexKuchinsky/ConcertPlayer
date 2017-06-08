package GraphicUserInterface.AddMenuPackage.albumPackage;

import GraphicUserInterface.AddMenuPackage.AddMenu;
import GraphicUserInterface.MainMenuPackage.Main;
import MediaComponents.Artist;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLSyntaxErrorException;
import java.util.*;

public class addAlbumController implements Initializable {
    @FXML
    public ImageView albumImageView;
    @FXML
    public ComboBox<String> comboBox;
    @FXML
    public TextField textField;
    @FXML
    public Button choosePhotoButton;
    @FXML
    public Button addSongsButton;
    @FXML
    public Button addAlbumButton;

    // Вспомогательные ссылки
    protected Path pathOfTheImage;
    protected Artist currentArtist;

    // Каталог выбранных песен
    List<File> songs;
    public addAlbumController(){

    }
    // Инициализация
    public void initialize(URL location, ResourceBundle resources){
        // Инициализация списка артистов
        Iterator<Artist> artistIterator=AddMenu.concertPlayer.getArtists().iterator();
        while (artistIterator.hasNext())
            comboBox.getItems().add(artistIterator.next().getNameOfTheArtist());

        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentArtist=AddMenu.concertPlayer.getArtist(newValue);
        });

        // Контроль за полем ввода названия
        textField.textProperty().addListener(e->{
            if(textField.getText().compareTo("")!=0 && currentArtist!=null)
                addAlbumButton.setDisable(false);
            else
                addAlbumButton.setDisable(true);
        });

        // Обработчик события для кнопки выбора фотографии
        choosePhotoButton.setOnAction(e->{
            FileChooser fileChooser=new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG","*.jpg")
            );
            File file=fileChooser.showOpenDialog(null);
            if(file!=null) {
                pathOfTheImage = file.toPath();
                albumImageView.setImage(new Image(pathOfTheImage.toUri().toString()));
            }
        });

        // Обработчик события для кнопки выбора песен для  альбома
        addSongsButton.setOnAction(e->{
            FileChooser fileChooser=new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MP3","*.mp3"),
                    new FileChooser.ExtensionFilter("M4A","*.m4a")
            );
            songs= fileChooser.showOpenMultipleDialog(null);
        });

        // Обработчкик события для кнопки добавления альбома
        addAlbumButton.setOnAction(e->{
            // Добавляем альбом, перезагружаем меню, закрываем окно
            AddMenu.concertPlayer.getArtist(currentArtist.getNameOfTheArtist()).addAlbum(textField.getText(),pathOfTheImage,songs);
            AddMenu.parentController.reloadAlbumMenu();
            AddMenu.addWindow.close();
        });
    }

}
