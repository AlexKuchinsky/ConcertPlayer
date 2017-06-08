package MediaComponents;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Alex on 11.05.2017.
 */
public class Playlist implements Serializable{
    protected String nameOfThePlaylist;
    protected TreeMap<String,Artist.Album.Song> songs;

    protected String pathToImage;
    protected static String pathToPlaylistDirectory=System.getProperty("user.dir") + "/Data/Playlists/";
    protected static Path pathToDefaultPlaylistImage= new File(System.getProperty("user.dir")+"/src/Service Information/UnknownAlbum.jpg").toPath();

    public Playlist(String nameOfThePlaylist, Path pathToImage, ArrayList<Artist.Album.Song> songs){
        this.nameOfThePlaylist=nameOfThePlaylist;
        this.songs=new TreeMap<>();
        try {
            Files.createDirectory(new File(pathToPlaylistDirectory+nameOfThePlaylist).toPath());
            Files.createDirectory(new File(pathToPlaylistDirectory+nameOfThePlaylist+"/Image").toPath());
            Files.createDirectory(new File(pathToPlaylistDirectory+nameOfThePlaylist+"/Songs").toPath());
            Files.copy((pathToImage!=null)? pathToImage: pathToDefaultPlaylistImage,
                    new File(pathToPlaylistDirectory+nameOfThePlaylist+"/Image/"+nameOfThePlaylist+".jpg").toPath(),StandardCopyOption.REPLACE_EXISTING);
            this.pathToImage=("file:/"+pathToPlaylistDirectory+nameOfThePlaylist+"/Image/"+nameOfThePlaylist+".jpg").replace(" ","%20");
            for(Artist.Album.Song song: songs) {
                this.songs.put(song.getNameOfTheSong(), song);
                Files.copy(new File(System.getProperty("user.dir")+"/Data/Artists/"+song.getNameOfTheArtist()+"/Albums/"+song.getNameOfTheAlbum()+"/Songs/"+song.getNameOfTheSongWithExtension()).toPath(),
                        new File(pathToPlaylistDirectory+nameOfThePlaylist+"/Songs/"+song.getNameOfTheSongWithExtension()).toPath(),StandardCopyOption.REPLACE_EXISTING);

            }
            }catch (IOException e){
            System.out.println(" Can't create playlist: "+e);
        }
    }

    public Image getImage(){
        return new Image(pathToImage);
    }
    public String getNameOfThePlaylist(){
        return nameOfThePlaylist;
    }
    public ArrayList<Artist.Album.Song> getPlaylistSongs(){
        return new ArrayList<>(songs.values());
    }
    public Artist.Album.Song getSong(String nameOfTheSong){
        return songs.get(nameOfTheSong);
    }

    public boolean delete(){
        try{


            File songsDirectory=new File(pathToPlaylistDirectory+nameOfThePlaylist+"/Songs");
            String[] files=songsDirectory.list();
            for(String fileToDelete: files)
                Files.delete(new File(songsDirectory.getAbsolutePath()+"/"+fileToDelete).toPath());
            Files.delete(new File(System.getProperty("user.dir") + "/Data/Playlists/"+nameOfThePlaylist+"/Songs").toPath());
            Files.delete(new File(System.getProperty("user.dir") + "/Data/Playlists/"+nameOfThePlaylist+"/Image/"+nameOfThePlaylist+".jpg").toPath());
            Files.delete(new File(System.getProperty("user.dir") + "/Data/Playlists/"+nameOfThePlaylist+"/Image").toPath());
            Files.delete(new File(System.getProperty("user.dir") + "/Data/Playlists/"+nameOfThePlaylist).toPath());

        }catch (IOException e){
            System.out.println(" can't : "+e);
            return false;
        }
        return true;
    }

}
