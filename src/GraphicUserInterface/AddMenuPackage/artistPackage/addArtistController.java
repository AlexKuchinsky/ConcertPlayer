package GraphicUserInterface.AddMenuPackage.artistPackage;

import GraphicUserInterface.AddMenuPackage.AddMenu;
import GraphicUserInterface.MainMenuPackage.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

/**
 * Created by Alex on 10.05.2017.
 */
public class addArtistController implements Initializable{
    @FXML
    public ImageView artistImageView;
    @FXML
    public TextField textField;
    @FXML
    public Button selectPhotoButton;
    @FXML
    public Button addArtistButton;

    // Путь фотографии артиста
    protected Path pathOfTheImage;

    // Настройка сцены перед показом
    public void initialize(URL location, ResourceBundle resources){

        // Установить наблюдение за компонентом Text Field
        textField.textProperty().addListener((object,oldValue, newValue)->{
            if(textField.getText().compareTo("")==0)
                addArtistButton.setDisable(true);
            else
                addArtistButton.setDisable(false);
        });


        // Установить обработчик события для кнопки выбора фотографии
        selectPhotoButton.setOnAction(e->{
            FileChooser fileChooser=new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG","*.jpg")
            );
            File file=fileChooser.showOpenDialog(null);
            if(file!=null) {
                pathOfTheImage = file.toPath();
                artistImageView.setImage(new Image(pathOfTheImage.toUri().toString()));
            }
        });

        // Установить обработчик события для кнопки добавления
        addArtistButton.setOnAction(e->{
            // Сохраняем в плеере, перезагружаем меню артистов в главном окне, закрываем окно
            if(!Main.concertPlayer.addArtist(textField.getText(),pathOfTheImage)){
                new Alert(Alert.AlertType.WARNING," Such artist already exists").show();
                return;
            }
            AddMenu.parentController.reloadArtistMenu();
            AddMenu.addWindow.close();
        });
    }
}

