package MediaComponents;

import javafx.scene.media.Media;

import java.io.Serializable;

public class Song implements Serializable {
    protected String nameOfTheSong;
    protected String nameOfTheArtist;
    protected Media song;

    public static String path;
    static {
        path = "file:/" + System.getProperty("user.dir").replace('\\', '/')+"/";
        System.out.println(" Path: "+path);
    }
    public Song(String nameOfTheSong, String nameOfTheArtist){

        song=new Media(path+nameOfTheSong.replaceAll(" ","%20"));
        nameOfTheSong=nameOfTheSong.substring(0,nameOfTheSong.lastIndexOf('.'));
        this.nameOfTheArtist=nameOfTheArtist;

    }

    public Media getSong(){
        return song;
    }
    public String getNameOfTheSong(){
        System.out.println(" name Of The Song: "+nameOfTheSong);
        return nameOfTheSong;
    }
}
