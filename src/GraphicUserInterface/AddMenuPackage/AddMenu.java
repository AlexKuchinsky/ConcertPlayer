package GraphicUserInterface.AddMenuPackage;

import GraphicUserInterface.MainMenuPackage.Main;
import GraphicUserInterface.MainMenuPackage.MainSceneController;
import MediaComponents.ConcertPlayer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddMenu {

    // Главное окно меню добавления
    public static Stage addWindow;

    // Ссылки на плеер и основную сцену
    public static ConcertPlayer concertPlayer;
    public static MainSceneController parentController;

    // Конструктор
    public AddMenu(MainSceneController parentController){
        this.parentController=parentController;
        concertPlayer= Main.concertPlayer;
        addWindow=new Stage();
    }

    // Загрузка и запуск соответствующих сценd
    public void invokeAddSong(){
        addWindow.setTitle(" Add Song");
        try{
            addWindow.setScene(new Scene(FXMLLoader.load(getClass().getResource("songPackage/addSongScene.fxml"))));
        }catch (IOException e){
            System.out.println(" Exception: "+e);
        }

        addWindow.show();
    }
    public void invokeAddArtist(){
        addWindow.setTitle(" Add Artist");
        try{
            addWindow.setScene(new Scene(FXMLLoader.load(getClass().getResource("artistPackage/addArtistScene.fxml"))));
        }catch (IOException e){
        }
        addWindow.show();
    }
    public void invokeAddAlbum(){
        try{
            addWindow.setScene(new Scene(FXMLLoader.load(getClass().getResource("albumPackage/addAlbumsScene.fxml"))));
        }catch (IOException e){
        }
        addWindow.show();
    }
    public void invokeAddPlaylist(){
        addWindow.setTitle(" Add Playlist");
        try{
            addWindow.setScene(new Scene(FXMLLoader.load(getClass().getResource("playlistPackage/addPlaylistScene.fxml"))));
        }catch (IOException e){
        }
        addWindow.show();
    }

}
