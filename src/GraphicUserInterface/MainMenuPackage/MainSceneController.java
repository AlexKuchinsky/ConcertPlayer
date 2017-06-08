package GraphicUserInterface.MainMenuPackage;

import GraphicUserInterface.AddMenuPackage.AddMenu;
import MediaComponents.*;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.*;

public class MainSceneController implements Initializable {

    // Объект проигрывателя
    protected ConcertPlayer concertPlayer;
    // Объект меню добавления
    protected AddMenu addMenu;

    // Конструктор
    public MainSceneController() {
        addMenu = new AddMenu(this);
        concertPlayer = Main.concertPlayer;
    }

    // Инициализатор
    public void initialize(URL location, ResourceBundle resources) {

        reloadArtistMenu();
        reloadAlbumMenu();
        reloadPlaylistMenu();

        setUpSceneForPlayingAllSongs();
        addSongMenuItem.setOnAction(e -> {
            addMenu.invokeAddSong();
        });
        openSongsMenuItem.setOnAction(e->{
            setUpSceneForPlayingAllSongs();
        });
        addPlaylistMenuItem.setOnAction(e -> {
            addMenu.invokeAddPlaylist();
        });
        addArtistMenuItem.setOnAction(e -> {
            addMenu.invokeAddArtist();
        });
        addAlbumMenuItem.setOnAction(e -> {
            addMenu.invokeAddAlbum();
        });




        timeSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if(!isChanging.booleanValue()) {
                mediaPlayer.seek(Duration.seconds(this.timeSlider.getValue()));
            }

        });
        this.timeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            //System.out.println("slider.valueProperty");
            if(!this.timeSlider.isValueChanging()) {
                double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                if(Math.abs(currentTime - newValue.doubleValue()) > 0.5D) {
                    mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                }
            }

        });

        timeSlider.valueProperty().addListener((e) -> {
            int currentSeconds=(int)timeSlider.getValue()%60;
            int currentMinutes=(int)timeSlider.getValue()/60;
            int seconds=(int)timeSlider.getMax()%60;
            int minutes=(int)timeSlider.getMax()/60;
            currentDurationLabel.setText(currentMinutes+":"+((currentSeconds<10)? "0"+currentSeconds:currentSeconds));
            totalDurationLabel.setText(minutes+":"+((seconds<10)? "0"+seconds:seconds));
        });

    }

    // Обновление меню
    public void reloadArtistMenu(){
        artistMenu.getItems().removeAll(artistMenu.getItems().subList(1,artistMenu.getItems().size()));
        Iterator<Artist> artistIterator=concertPlayer.getArtists().iterator();
        while (artistIterator.hasNext()){
            Artist tmpArtist=artistIterator.next();
            ImageView artistImage=new ImageView(tmpArtist.getImageOfTheArtist());
            artistImage.setFitWidth(50);
            artistImage.setFitHeight(50);
            MenuItem tmpMenuItem=new MenuItem(tmpArtist.getNameOfTheArtist(),artistImage);
            tmpMenuItem.setOnAction(e->{
                setUpSceneForPlayingArtist(tmpArtist);
            });
            artistMenu.getItems().add(tmpMenuItem);
        }
    }
    public void reloadAlbumMenu(){
        albumMenu.getItems().removeAll(albumMenu.getItems().subList(1,albumMenu.getItems().size()));
        Iterator<Artist> artistIterator=concertPlayer.getArtists().iterator();
        while (artistIterator.hasNext()){
            Artist tmpArtist=artistIterator.next();
            Iterator<Artist.Album> albumIterator=tmpArtist.getAlbums().iterator();
            while (albumIterator.hasNext()){
                Artist.Album tmpAlbum=albumIterator.next();
                ImageView imageView=new ImageView(tmpAlbum.getImageOfTheAlbum());
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                MenuItem albumMenuItem=new MenuItem(tmpAlbum.getNameOfTheAlbum(),imageView);
                albumMenuItem.setOnAction(e->{
                    setUpSceneForPlayingAlbum(tmpAlbum);
                });
                albumMenu.getItems().add(albumMenuItem);
            }
        }
    }
    public void reloadPlaylistMenu(){
        playlistMenu.getItems().removeAll(playlistMenu.getItems().subList(1,playlistMenu.getItems().size()));
        Iterator<Playlist> playlistIterator=concertPlayer.getPlaylists().iterator();
        while (playlistIterator.hasNext()){
            Playlist tmpPlaylist=playlistIterator.next();
            ImageView imageView=new ImageView(tmpPlaylist.getImage());
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            MenuItem playlistMenuItem=new MenuItem(tmpPlaylist.getNameOfThePlaylist(),imageView);
            playlistMenuItem.setOnAction(e->{
                setUpSceneForPlayingPlaylist(tmpPlaylist);
            });
            playlistMenu.getItems().add(playlistMenuItem);
        }


    }

    protected InvalidationListener listener;

    // Подготовка сцен для проигрования того или иного компонента
    public void setUpSceneForPlayingArtist(Artist artist) {
        mainImageView.setImage(artist.getImageOfTheArtist());
        primaryInformationLabel.setText(artist.getNameOfTheArtist());
        secondaryInformationLabel.setText("");
        removeButton.setText(" Delete this artist");
        removeButton.setOnAction(e -> {
            concertPlayer.removeArtist(artist.getNameOfTheArtist());
            reloadArtistMenu();
            reloadAlbumMenu();
        });
        shuffleRadioButton.setDisable(true);

        songsListView.getItems().clear();
        for (Artist.Album album : artist.getAlbums())
            songsListView.getItems().add(album.getNameOfTheAlbum());

        if(listener!=null)
            songsListView.getSelectionModel().selectedItemProperty().removeListener(listener);

        listener=(e)->{
                setUpSceneForPlayingAlbum(artist.getAlbum(songsListView.getSelectionModel().getSelectedItem()));
        };
        songsListView.getSelectionModel().selectedItemProperty().addListener(listener);

    }
    public void setUpSceneForPlayingAlbum(Artist.Album album) {
        mainImageView.setImage(album.getImageOfTheAlbum());
        primaryInformationLabel.setText(album.getNameOfTheAlbum());
        secondaryInformationLabel.setText(album.getNameOfTheArtist());
        removeButton.setText(" Delete this album");
        removeButton.setOnAction(e -> {
            concertPlayer.getArtist(album.getNameOfTheArtist()).deleteAlbum(album.getNameOfTheAlbum());
            reloadAlbumMenu();
        });
        shuffleRadioButton.setDisable(false);
        shuffleRadioButton.setOnAction((e)->{
            songsListView.getItems().clear();
            ArrayList<Artist.Album.Song> tmpSongList=album.getAlbumSongs();
            if(shuffleRadioButton.isSelected())
                Collections.shuffle(tmpSongList);
            for(Artist.Album.Song song: tmpSongList)
                songsListView.getItems().add(song.getNameOfTheSong());
        });
        Iterator<Artist.Album.Song> songIterator = album.getAlbumSongs().iterator();
        songsListView.getItems().clear();

        while (songIterator.hasNext()) {
            songsListView.getItems().addAll(songIterator.next().getNameOfTheSong());
        }
        if(listener!=null)
            songsListView.getSelectionModel().selectedItemProperty().removeListener(listener);

        songsListView.refresh();
        listener=(e)->{
            String item = songsListView.getSelectionModel().getSelectedItem();
            if(item==null)
                return;
            startPlaySong(album.getSongFromAlbum(item));
        };
        songsListView.getSelectionModel().selectedItemProperty().addListener(listener);
    }
    public void setUpSceneForPlayingPlaylist(Playlist playlist) {
        mainImageView.setImage(playlist.getImage());
        primaryInformationLabel.setText(playlist.getNameOfThePlaylist());
        secondaryInformationLabel.setText("");
        removeButton.setText(" Delete this playlist");
        removeButton.setOnAction(e -> {
            concertPlayer.removePlaylist(playlist.getNameOfThePlaylist());
            reloadPlaylistMenu();
        });
        shuffleRadioButton.setDisable(false);
        shuffleRadioButton.setOnAction((e)->{
            songsListView.getItems().clear();
            ArrayList<Artist.Album.Song> tmpSongList=playlist.getPlaylistSongs();
            if(shuffleRadioButton.isSelected())
                Collections.shuffle(tmpSongList);
            for(Artist.Album.Song song: tmpSongList)
                songsListView.getItems().add(song.getNameOfTheSong());
        });
        Iterator<Artist.Album.Song> songIterator = playlist.getPlaylistSongs().iterator();
        songsListView.getItems().clear();

        while (songIterator.hasNext()) {
            songsListView.getItems().addAll(songIterator.next().getNameOfTheSong());
        }
        if(listener!=null)
            songsListView.getSelectionModel().selectedItemProperty().removeListener(listener);

        listener=(e)->{
            System.out.println(" Listener playlist");
            String item = songsListView.getSelectionModel().getSelectedItem();
            if(item==null)
                return;
            startPlaySong(playlist.getSong(item));
        };
        songsListView.getSelectionModel().selectedItemProperty().addListener(listener);
    }
    public void setUpSceneForPlayingAllSongs(){
        songsListView.getItems().clear();
        shuffleRadioButton.setDisable(false);
        shuffleRadioButton.setOnAction((e)->{
            songsListView.getItems().clear();
            ArrayList<Artist.Album.Song> songs=new ArrayList<>();
            for(Artist artist: concertPlayer.getArtists()){
                for(Artist.Album album: artist.getAlbums()){
                    songs.addAll(album.getAlbumSongs());
                }
            }
            if(shuffleRadioButton.isSelected())
                Collections.shuffle(songs);
            for(Artist.Album.Song song: songs)
                songsListView.getItems().add(" "+song.getNameOfTheSong()+ " - "+song.getNameOfTheArtist()+ " ("+song.getNameOfTheAlbum()+")");
        });
        removeButton.setVisible(false);
        removeButton.setText(" Delete chosen");
        removeButton.setOnAction(e -> {
            String item=songsListView.getSelectionModel().getSelectedItem();
            if(item==null)
                return;
            if(mediaPlayer!=null)
                mediaPlayer.stop();
            mediaPlayer=null;
            playPauseImageView.setImage(new Image("file:/"+System.getProperty("user.dir")+"/src/Service%20Information/play.png"));
            String nameOfTheSong;
            String nameOfTheArtist;
            String nameOfTheAlbum;
            nameOfTheAlbum=item.substring(item.lastIndexOf('(')+1,item.length()-1);
            nameOfTheSong=item.substring(1,item.indexOf('-')-1);
            nameOfTheArtist=item.substring(nameOfTheSong.length()+4,item.indexOf('(')-1);
            System.out.println(" Delete song: ("+nameOfTheSong+")("+nameOfTheAlbum+")("+nameOfTheArtist+")");
            concertPlayer.getArtist(nameOfTheArtist).getAlbum(nameOfTheAlbum).deleteSong(nameOfTheSong);
        });
        for(Artist artist: concertPlayer.getArtists()){
            for(Artist.Album album: artist.getAlbums()){
                for(Artist.Album.Song song: album.getAlbumSongs()){
                    songsListView.getItems().add(" "+song.getNameOfTheSong()+ " - "+song.getNameOfTheArtist()+ " ("+song.getNameOfTheAlbum()+")");
                }
            }
        }
        if(listener!=null)
            songsListView.getSelectionModel().selectionModeProperty().removeListener(listener);
        System.out.println(" set up for songs");
        listener=(e)->{
            System.out.println(" listener of all songs");
            String item=songsListView.getSelectionModel().getSelectedItem();
            try {
                item = songsListView.getSelectionModel().getSelectedItem().toString();
            }catch (NullPointerException exception){}
            if(item==null)
                return;
            String nameOfTheSong;
            String nameOfTheArtist;
            String nameOfTheAlbum;

            nameOfTheAlbum=item.substring(item.lastIndexOf('(')+1,item.length()-1);
            nameOfTheSong=item.substring(1,item.indexOf('-')-1);
            nameOfTheArtist=item.substring(nameOfTheSong.length()+4,item.indexOf('(')-1);

            startPlaySong(concertPlayer.getArtist(nameOfTheArtist).getAlbum(nameOfTheAlbum).getSongFromAlbum(nameOfTheSong));
        };
        songsListView.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void startPlaySong(Artist.Album.Song song){
        if(song==null)
            return;
        if(mediaPlayer!=null)
            mediaPlayer.stop();
        mediaPlayer=new MediaPlayer(song.getSong());
        mediaPlayer.play();
        playing = true;
        playPauseImageView.setImage(new Image("file:/"+System.getProperty("user.dir")+"/src/Service%20Information/pause.png"));
        mainImageView.setImage(song.getImage());
        primaryInformationLabel.setText(song.getNameOfTheSong());
        secondaryInformationLabel.setText(song.getNameOfTheArtist() + " | " + song.getNameOfTheAlbum());


        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if(!this.timeSlider.isValueChanging()) {
                this.timeSlider.setValue(newTime.toSeconds());
            }

        });
        mediaPlayer.totalDurationProperty().addListener((obs, oldDuration, newDuration) -> {
            timeSlider.setMax(newDuration.toSeconds());
        });
        mediaPlayer.setVolume(valueOfVolume);
        mediaPlayer.setRate(valueOfRate);
        rateSlider.setValue(mediaPlayer.getRate()*100.0D);
        rateSlider.valueProperty().addListener((e)->{
            valueOfRate=rateSlider.getValue()/100.0D;
            mediaPlayer.setRate(valueOfRate);
        });
        volumeSlider.setValue(mediaPlayer.getVolume() * 100.0D);
        volumeSlider.valueProperty().addListener((e) -> {
            valueOfVolume=volumeSlider.getValue() / 100.0D;
            mediaPlayer.setVolume(valueOfVolume);
        });
    }

    public void playNextSong(){
        System.out.println("next");
        if(songsListView.getSelectionModel().getSelectedItem()!=null)
            songsListView.getSelectionModel().selectNext();
    }
    public void playPreviousSong(){
        System.out.println("previous");
        if(songsListView.getSelectionModel().getSelectedItem()!=null) {
            System.out.println(" We can select");
            songsListView.getSelectionModel().selectPrevious();
        }
    }
    protected MediaPlayer mediaPlayer;
    protected boolean playing=false;
    // Проигравание музыки
    // Пауза Играть
    public void playpause() {
        timeSlider.setMax(mediaPlayer.getStopTime().toSeconds());
        //System.out.println(mediaPlayer.getStopTime().toSeconds());
        if(playing) {
            mediaPlayer.pause();
            playing = false;
            playPauseImageView.setImage(new Image("file:/"+System.getProperty("user.dir")+"/src/Service%20Information/play.png"));
        } else {
            mediaPlayer.play();
            playing = true;
            playPauseImageView.setImage(new Image("file:/"+System.getProperty("user.dir")+"/src/Service%20Information/pause.png"));
        }

    }
    // Меню
    @FXML
    public Menu songsMenu;
    @FXML
    public MenuItem addSongMenuItem;
    @FXML
    public MenuItem openSongsMenuItem;

    @FXML
    public Menu playlistMenu;
    @FXML
    public MenuItem addPlaylistMenuItem;


    @FXML
    public Menu artistMenu;
    @FXML
    public MenuItem addArtistMenuItem;

    @FXML
    public  Menu albumMenu;
    @FXML
    public MenuItem addAlbumMenuItem;

    // Картинка и информация
    @FXML
    public ImageView mainImageView;
    @FXML
    public Label primaryInformationLabel;
    @FXML
    public Label secondaryInformationLabel;

    // Управление состоянием
    @FXML
    public Label currentDurationLabel;
    @FXML
    public Slider timeSlider;
    @FXML
    public Label totalDurationLabel;

    @FXML
    public Button someButton;

    // Перемотка, пауза, громкость
    @FXML
    public Label playPauseLabel;
    @FXML
    public ImageView playPauseImageView;
    @FXML
    public Slider volumeSlider;
    @FXML
    public Slider rateSlider;
    @FXML
    public RadioButton shuffleRadioButton;

    // Лист с песнями
    @FXML
    public ListView<String> songsListView;

    @FXML
    public Button removeButton;

    protected double valueOfVolume=0.5;
    protected double valueOfRate=1;
}
