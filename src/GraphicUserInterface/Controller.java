package GraphicUserInterface;

import MediaComponents.Song;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private Button playButton;
    @FXML
    private Label informationLabel;
    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.println(" Initialize method");
        playButton.setOnAction(action->{
            mediaPlayer=new MediaPlayer(song.getSong());
            mediaPlayer.play();
            informationLabel.setText(song.getNameOfTheSong());
        });
    }
    private static MediaPlayer mediaPlayer;
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
    }
    public void play(){
        System.out.println(" play ");

    }
}
