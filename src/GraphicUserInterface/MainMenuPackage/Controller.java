package GraphicUserInterface.MainMenuPackage;

import MediaComponents.Song;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class Controller implements Initializable{
    @FXML
    public Menu menu;
    @FXML
    public Button button;
    @FXML
    public TextField textArea;
    @FXML
    public MenuBar menuBar;

    public void addFunction(){

    }
    /*@FXML
    private Button playButton;
    @FXML
    private Label informationLabel;
    @Override*/
    public void initialize(URL location, ResourceBundle resources){
        button.setOnAction(e->{
            Image image=new Image("file:/"+"C:/Users/Alex/IdeaProjects/ConcertPlayer/src/photo.jpg");

            ImageView imageView =new ImageView(image);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            MenuItem menuItem=new MenuItem(textArea.getText(),imageView);
            menuItem.setOnAction(b->{
                System.out.println(textArea.getText() + "menue was choosen");
            });
            menu.getItems().add(menuItem);
        });

       /* System.out.println(" Initialize method");
        playButton.setOnAction(action->{
            mediaPlayer=new MediaPlayer(song.getSong());
            mediaPlayer.play();
            informationLabel.setText(song.getNameOfTheSong());
        });*/
    }
   /* private static MediaPlayer mediaPlayer;
    private static Song song;
    public Controller(){
        System.out.println(" Contorller constructor");
        song=new Song("Louse yourself to dance.mp3","Daft Punk");
        try(ObjectOutputStream objOStrm=new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir")+"/text.txt"))){
            objOStrm.writeObject(song);
        }catch (IOException e){
            System.out.println(" Исключение "+e);
        }
        /*MyClass object2=new MyClass("",0,0);
        try(ObjectInputStream objIStrm=new ObjectInputStream((new FileInputStream(System.getProperty("user.dir")+"/text.txt")))){
            object2=(MyClass)objIStrm.readObject();
            System.out.println(object2);
        }catch(Exception e){

        }
        Media media=new Media("file:/"+System.getProperty("user.dir").replace('\\','/')+"/song.mp3");
        MediaPlayer mediaPlayer=new MediaPlayer(media);
        mediaPlayer.play();*/
        /// You are my boss
  /*  }
    public void play(){
        System.out.println(" play ");

    }*/
}
