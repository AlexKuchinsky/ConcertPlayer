package MediaComponents;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
public class ConcertPlayer implements Serializable {

    protected TreeMap<String,Artist> artists;

    public ConcertPlayer(){
        artists=new TreeMap<>();
        playlists=new TreeMap<>();
    }

    // Добавление артиста
    public boolean addArtist(String nameOfTheArtist, Path pathOfArtistImage){
        if(artists.containsKey(nameOfTheArtist))
            return false;
        artists.put(nameOfTheArtist, new Artist(nameOfTheArtist,pathOfArtistImage));
        return true;
    }
    public boolean removeArtist(String nameOfTheArtist){
        if(!artists.containsKey(nameOfTheArtist))
            return false;
        boolean result=artists.get(nameOfTheArtist).delete();
        if(result)
            artists.remove(nameOfTheArtist);
        return result;

    }

    public Artist getArtist(String nameOfTheArtist){
        return artists.get(nameOfTheArtist);
    }

    public ArrayList<String> getArtistsNames(){return  new ArrayList<>(artists.keySet());}
    public ArrayList<Artist> getArtists(){
        return new ArrayList<>(artists.values());
    }


    // Плейлисты
    protected  TreeMap<String, Playlist> playlists;
    public boolean addPlaylist(String nameOfThePlaylist, Path pathToSourcePlaylistImage, ArrayList<Artist.Album.Song> songs){
        if(playlists.containsKey(nameOfThePlaylist))
        return false;
        else {
            playlists.put(nameOfThePlaylist,new Playlist(nameOfThePlaylist,pathToSourcePlaylistImage,songs));
            return true;
        }
    }
    public ArrayList<Playlist> getPlaylists(){
        return new ArrayList<>(playlists.values());
    }
    public void removePlaylist(String nameOfThePlaylist){
       if(playlists.get(nameOfThePlaylist).delete())
           playlists.remove(nameOfThePlaylist);
    }


}
