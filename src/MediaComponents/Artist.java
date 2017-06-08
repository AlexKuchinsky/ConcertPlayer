package MediaComponents;

import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
public class Artist implements Serializable {

    protected String nameOfTheArtist;
    protected String pathToImage;
    protected static Path pathToDefaultAlbumImage=new File(System.getProperty("user.dir")+"/src/Service Information/UnknownAlbum.jpg").toPath();

    protected TreeMap<String, Album> albums;

    protected static Path pathToDefaultArtistImage=new File(System.getProperty("user.dir")+"/src/Service Information/UnknownArtist.jpg").toPath();
    protected String pathToAritstDirectory;

    public Artist(String nameOfTheArtist, Path pathOfArtistImage){
        albums=new TreeMap<>();
        this.nameOfTheArtist=nameOfTheArtist;
        // Создание директорий для артиста
        makeArtistDirectory();
        // Перемещение фотографии
        try{
            Files.copy((pathOfArtistImage!=null)?pathOfArtistImage:pathToDefaultArtistImage,
                    new File(pathToAritstDirectory+"/Image/"+nameOfTheArtist+".jpg").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){}
        // Сохранение пути для фотографии артиста
        pathToImage="file:/"+pathToAritstDirectory+"/Image/"+nameOfTheArtist+".jpg";

    }
    public void makeArtistDirectory(){
        try {
            String pathToNewDirectory=System.getProperty("user.dir") + "/Data/Artists/" + nameOfTheArtist;
            Files.createDirectory(new File(pathToNewDirectory).toPath());
            Files.createDirectory(new File(pathToNewDirectory+"/Albums").toPath());
            Files.createDirectory(new File(pathToNewDirectory+"/Image").toPath());
            pathToAritstDirectory=pathToNewDirectory;
        }catch (IOException e){
            System.out.println(" Can't create directory: "+e);
        }
    }

    public String getNameOfTheArtist(){
        return nameOfTheArtist;
    }
    public Image getImageOfTheArtist(){
        return new Image(pathToImage);
    }

    // Работа с альбомами
    public boolean addAlbum(String nameOfTheAlbum, Path pathToImage,List<File> songsList){
        if(albums.containsKey(nameOfTheAlbum))
            return false;
        albums.put(nameOfTheAlbum,new Album(nameOfTheAlbum,nameOfTheArtist,pathToImage,songsList));
        return true;
    }
    public Album getAlbum(String nameOfTheAlbum){
        return albums.get(nameOfTheAlbum);
    }
    public ArrayList<Album> getAlbums(){
        return new ArrayList<>(albums.values());
    }
    public ArrayList<String> getAlbumsNames(){return  new ArrayList<>(albums.keySet()); }
    public boolean delete(){
        try {
            for(Album albumToDelete: albums.values()){
                if(!albumToDelete.delete())
                    return false;
            }

            albums.clear();

            File imageDirectory=new File(pathToAritstDirectory+"/Image");
            String[] files=imageDirectory.list();
            for(String fileToDelete: files)
                Files.delete(new File(imageDirectory.getAbsolutePath()+"/"+fileToDelete).toPath());

            Files.delete(new File(pathToAritstDirectory + "/Albums").toPath());
            Files.delete(new File(pathToAritstDirectory + "/Image").toPath());
            Files.delete(new File(pathToAritstDirectory).toPath());
            return true;
        }catch (IOException e){
            System.out.println(" Cant't delete artist"+e);
            return false;
        }
    }
    public boolean deleteAlbum(String nameOfTheAlbum){
        if(albums.containsKey(nameOfTheAlbum)) {
            boolean result=albums.get(nameOfTheAlbum).delete();
            albums.remove(nameOfTheAlbum);
            return result;
        }
        return false;

    }

    public class Album implements Serializable {

        protected String nameOfTheAlbum;
        protected String nameOfTheArtist;
        protected String pathToImage;

        protected TreeMap<String,Song> songs;
        protected String pathToAlbumDirectory;

        public Album(String nameOfTheAlbum, String nameOfTheArtist,
                     Path pathToImage, List<File> songsList){
            songs=new TreeMap<>();
            this.nameOfTheAlbum=nameOfTheAlbum ;
            this.nameOfTheArtist=nameOfTheArtist;
            makeAlbumDirectory();
            try{
                Files.copy((pathToImage!=null)?pathToImage:pathToDefaultAlbumImage,
                        new File(pathToAlbumDirectory+"/Images/"+nameOfTheAlbum+".jpg").toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e){}
            // Сохранение пути для фотографии артиста
            this.pathToImage="file:/"+pathToAlbumDirectory+"/Images/"+nameOfTheAlbum +".jpg";
            if(songsList!=null)
                addSongs(songsList);
        }

        protected void makeAlbumDirectory(){
            try {
                String pathToNewDirectory=System.getProperty("user.dir") + "/Data/Artists/" + nameOfTheArtist+"/Albums/"+nameOfTheAlbum;
                Files.createDirectory(new File(pathToNewDirectory).toPath());
                Files.createDirectory(new File(pathToNewDirectory+"/Songs").toPath());
                Files.createDirectory(new File(pathToNewDirectory+"/Images").toPath());
                pathToAlbumDirectory=pathToNewDirectory;
            }catch (Exception e){
                System.out.println(" Can't create directory: "+e);
            }
        }
        public boolean addSongToAlbum(String nameOfTheSong, Path pathToSourceSong, Path pathToImage){
            if(!songs.containsKey(nameOfTheSong)){
                String nameOfTheSongWithExtension=nameOfTheSong+pathToSourceSong.getFileName().toString().substring(pathToSourceSong.getFileName().toString().lastIndexOf('.'));
                try{
                    // Копирование песни
                    Files.copy(pathToSourceSong,new File(pathToAlbumDirectory+"/Songs/"+nameOfTheSongWithExtension).toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                    if(pathToImage!=null)
                        Files.copy(pathToImage,new File(pathToAlbumDirectory+"/Images/"+nameOfTheSong+".jpg").toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    songs.put(nameOfTheSong,new Song(nameOfTheSong, nameOfTheArtist, nameOfTheAlbum,nameOfTheSongWithExtension,
                            ((pathToImage!=null)? ("file:/"+pathToAlbumDirectory+"/Images/"+nameOfTheSong+".jpg") : this.pathToImage)));
                }catch (IOException e){
                    System.out.println(" Can't copy file"+e);
                }
                return true;
            }
            else
                return false;
        }
        public ArrayList<Song> getAlbumSongs(){
            return new ArrayList<>(songs.values());
        }
        public Song getSongFromAlbum(String nameOfTheSong){
            System.out.println(" name of the song in getSongFromAlbum: "+nameOfTheSong);
            return songs.get(nameOfTheSong);
        }

        public boolean delete(){
            try {
                for(Song song: songs.values()) {
                    if(!song.delete())
                        return false;
                }
                File imageDirectory=new File(pathToAlbumDirectory+"/Images");
                String[] files=imageDirectory.list();
                for(String fileToDelete: files)
                    Files.delete(new File(imageDirectory.getAbsolutePath()+"/"+fileToDelete).toPath());
                Files.delete(new File(pathToAlbumDirectory+"/Images").toPath());


                Files.delete(new File(pathToAlbumDirectory+"/Songs").toPath());
                Files.delete(new File(pathToAlbumDirectory).toPath());
                songs.clear();
                return true;
            }catch (IOException e){
                System.out.println(" Can't delete album: "+e);
                return false;
            }

        }
        public boolean deleteSong(String nameOfTheSong){
            if(songs.containsKey(nameOfTheSong)) {
                boolean result=songs.get(nameOfTheSong).delete();
                songs.remove(nameOfTheSong);
                return result;
            }
            return false;
        }


        public String getNameOfTheAlbum(){
            return nameOfTheAlbum;
        }
        public String getNameOfTheArtist(){
            return nameOfTheArtist;
        }
        public Image getImageOfTheAlbum(){
            return new Image(pathToImage);
        }

        protected void addSongs(List<File> songsList){
            if(songsList!=null) {
                Iterator<File> fileIterator = songsList.iterator();
                while (fileIterator.hasNext()) {
                    File file = fileIterator.next();
                    String nameOfTheSong = file.getName().substring(0, file.getName().lastIndexOf('.'));
                    if (!songs.containsKey(nameOfTheSong)) {
                        String nameOfTheSongWithCodingFormat = file.getName();
                        songs.put(nameOfTheSong, new Song(nameOfTheSong, nameOfTheArtist, nameOfTheAlbum,
                                nameOfTheSongWithCodingFormat, pathToImage));
                        try {
                            Files.copy(file.toPath(),
                                    new File(pathToAlbumDirectory+"/Songs/"+nameOfTheSongWithCodingFormat).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }catch (IOException e){

                        }

                    }
                }
            }
        }

        public class Song implements Serializable {

            protected String nameOfTheSong;
            protected String nameOfTheSongWithExtension;


            protected String pathToSong;
            protected String pathToImage;

            public Song(String nameOfTheSong, String nameOfTheArtist, String nameOfTheAlbum,
                        String songNameWithExtension, String pathToImage){

                this.nameOfTheSong=nameOfTheSong;
                nameOfTheArtist=nameOfTheArtist;
                nameOfTheAlbum=nameOfTheAlbum;
                nameOfTheSongWithExtension=songNameWithExtension;

                pathToSong=("file:/" + System.getProperty("user.dir").replace('\\', '/')+"/Data/Artists/"+nameOfTheArtist+"/Albums/"+nameOfTheAlbum+"/Songs/"+songNameWithExtension).replace(" ","%20");
                this.pathToImage=pathToImage.replace(" ","%20");
            }

            public String getNameOfTheSong(){
                return nameOfTheSong;
            }
            public String getNameOfTheAlbum(){
                return nameOfTheAlbum;
            }
            public String getNameOfTheArtist(){
                return nameOfTheArtist;
            }
            public String getNameOfTheSongWithExtension(){return nameOfTheSongWithExtension; }

            public boolean delete(){
                try{
                    Files.delete(new File(System.getProperty("user.dir")+"/Data/Artists/"+nameOfTheArtist+"/Albums/"+nameOfTheAlbum+"/Songs/"+nameOfTheSongWithExtension).toPath());
                    return true;
                }catch (IOException e){
                    System.out.println(" Can't delete song"+e);
                    return false;
                }
            }
            public Media getSong(){
                return new Media(pathToSong);
            }
            public Image getImage(){
                return  new Image(pathToImage);
            }

        }
    }

}
