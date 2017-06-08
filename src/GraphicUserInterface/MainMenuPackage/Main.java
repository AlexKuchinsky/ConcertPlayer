package GraphicUserInterface.MainMenuPackage;


import MediaComponents.ConcertPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.lang.invoke.ConstantCallSite;
import java.nio.file.Files;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

public class Main extends Application {

    // Десериализация
    public void init(){
        concertPlayer=new ConcertPlayer();
        try(ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream(new File(System.getProperty("user.dir")+"/Data/SERIALIZATION.txt")))){
            concertPlayer=(ConcertPlayer)objectInputStream.readObject();
        }catch (Exception e){
            System.out.println(" Exception during deserialization: "+e.toString());
        }
    }
    // Сериализация
    public void stop(){
        try(ObjectOutputStream objectOutputStream=new ObjectOutputStream(new FileOutputStream(new File(System.getProperty("user.dir")+"/Data/SERIALIZATION.txt")))){
            objectOutputStream.writeObject(concertPlayer);
        }catch (Exception e){
            System.out.println(" Exception during serialization: "+e.toString());
        }
    }

    // Основное окно плеера
    public Stage mainWindow;

    // Объект плеера
    public static ConcertPlayer concertPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Основное окно
        mainWindow=primaryStage;
        // Запуск основного окна со сценой
        mainWindow.setTitle("ConcertPlayer");
        mainWindow.setScene(new Scene(FXMLLoader.load(getClass().getResource("mainScene.fxml"))));
        mainWindow.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
