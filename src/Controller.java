import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private String path = "";

    @FXML
    private MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaViewID;

    @FXML
    private Slider playbackSlider;

    @FXML
    private Slider volumeSlider;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void chooseFileMethod(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        path = file.toURI().toString();

        /*
            Next code is for verifying if it is legit file, or is it null
            We can do file verification in next step
         */

        if (path != null){
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaViewID.setMediaPlayer(mediaPlayer);
            DoubleProperty widthProperty = mediaViewID.fitWidthProperty();
            DoubleProperty heightProperty = mediaViewID.fitHeightProperty();
            widthProperty.bind(Bindings.selectDouble(mediaViewID.sceneProperty(), "width"));
            heightProperty.bind(Bindings.selectDouble(mediaViewID.sceneProperty(), "height"));

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                    playbackSlider.setValue(newValue.toSeconds());
                }
            });

            // using anonymous class concept
            playbackSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mediaPlayer.seek(Duration.seconds(playbackSlider.getValue()));
                }
            });

            // used lambda expression whereas in above we used anonymous class concept
            playbackSlider.setOnMouseDragged(mouseEvent -> mediaPlayer.seek(Duration.seconds(playbackSlider.getValue())));

            // setting progress bar max value every time new media get inserted into media player
            mediaPlayer.setOnReady(() -> {
                Duration total = media.getDuration();
                playbackSlider.setMax(total.toSeconds());
            });

            // setting up volume slider
            volumeSlider.setMax(100);
            volumeSlider.setValue(mediaPlayer.getVolume() * 100);

            volumeSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    double volume = volumeSlider.getValue();
                    mediaPlayer.setVolume(volume/100);
                }
            });

            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });

            mediaPlayer.play();
        }
    }

    /*
        Adding functionality action NOM will perform when "Play" button is pressed
     */

    public void play(ActionEvent event){
        mediaPlayer.play();
    }

    /*
        Adding functionality, action NOM will perform when "Pause" button is pressed
     */
    public void pause(ActionEvent event){
        mediaPlayer.pause();
    }

    /*
        Adding functionality to stop media player
     */
    public void stop(ActionEvent event){
        mediaPlayer.stop();
    }

    /*
        Adding functionality to increase playback rate of media
     */
    public void increaseSpeed(ActionEvent event){
        mediaPlayer.setRate(mediaPlayer.getRate() + 0.25);
    }


    /*
        Adding functionality to decrease playback rate of the media
     */
    public void decreaseSpeed(ActionEvent event){
        mediaPlayer.setRate(mediaPlayer.getRate() - 0.25);
    }

    /*
        Adding functionality to go forward by 5 second
     */

    public void goForward(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5)));
    }

    /*
        Adding functionality to go backward by 5 second
     */

    public void goBackward(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5)));
    }
}
