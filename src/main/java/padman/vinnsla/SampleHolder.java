package padman.vinnsla;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 The SampleHolder handles importing audio samples and playing them.
 It can store up to N MediaPlayers, and when the limit is reached, the longest-playing
 MediaPlayer will be stopped and replaced with a new one.
 */
public class SampleHolder {
    private static final int N = 23; // number of media players
    private static Map<Integer, Media> mediasKeyMap = new HashMap<>(); // map of Medias to integer keys
    private static Queue<MediaPlayer> mediaPlayers = new LinkedList<>(); // queue currently used MediaPlayers
    public static Queue<MediaPlayer> getMediaPlayers() {
        return mediaPlayers;
    }
    private static double mainVolume = 1.0; // volume of all MediaPlayers

    private static double releaseTime = 0.85;

    /**
     Reads audio samples from a folder, creates the Media objects and adds them to the mediasKeyMap.
     @param folderPath the path to the folder containing the audio samples
     */
    private static void importSamples(String folderPath) {

        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name)
                -> name.endsWith(".wav") || name.endsWith(".mp3"));
        assert files != null;
        Arrays.sort(files);
        for (int i = 0; i < files.length; i++) {
            Media media = new Media(files[i].toURI().toString());
            mediasKeyMap.put(i, media);
        }
    }
    // Method to set the volume of all current media players
    public static void setVolume(double volume) {
        for (MediaPlayer mediaPlayer : SampleHolder.getMediaPlayers()) {
            mediaPlayer.setVolume(volume);
        }
        mainVolume = volume;
    }

    /**
     Adds a MediaPlayer to the Que and sets up an end of media event handler
     that will dispose of it and remove it from the queue when it finishes playing.
     @param mediaPlayer the MediaPlayer to set up
     */
    private static void setupMediaPlayer(MediaPlayer mediaPlayer) {
        mediaPlayers.offer(mediaPlayer);
        mediaPlayer.setVolume(mainVolume);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayers.remove(mediaPlayer);
        });
    }

    /**
     Checks if there is a MediaPlayer playing the given Media that can be reused.
     @param media the Media to check
     @return a MediaPlayer that can be reused, or null if none is found
     */
    private static MediaPlayer mediaplayerIsReusable(Media media) {
        for (MediaPlayer mediaPlayer : mediaPlayers) {
            if (mediaPlayer.getMedia().equals(media) && !(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)) {
                return mediaPlayer;
            }
        } return null;
    }

    /**
     Plays the media associated with the given key. If there is a reusable MediaPlayer
     for the sample, it will be used. If the limit of N MediaPlayers has not been reached,
     a new MediaPlayer will be created and used. If the limit has been reached, the longest-playing
     MediaPlayer will be stopped and replaced with a new one.
     @param key the key associated with media to be played
     */
    public static void playMedia(int key) {
        Media media = mediasKeyMap.get(key);
        MediaPlayer reusable = mediaplayerIsReusable(media);
        if (reusable != null){
            setupMediaPlayer(reusable);
            reusable.play();
        }
        if (mediaPlayers.size() < N) {
            MediaPlayer newMp = new MediaPlayer(media);
            setupMediaPlayer(newMp);
            newMp.play();
        } else {
            MediaPlayer longestPlaying = mediaPlayers.poll();
            assert longestPlaying != null;
            longestPlaying.stop();
            MediaPlayer newPlayer = new MediaPlayer(media);
            setupMediaPlayer(newPlayer);
            newPlayer.play();
        }
        setFade();
    }
    public static void setFade(int keyIndex) {
        Media media = mediasKeyMap.get(keyIndex);
        for (MediaPlayer mediaPlayer : mediaPlayers) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING && mediaPlayer.getMedia().equals(media)) {
                mediaPlayers.remove(mediaPlayer);
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(releaseTime), new KeyValue(mediaPlayer.volumeProperty(), 0)));
                timeline.setOnFinished(event -> {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                });
                timeline.play();
                return;
            }
        }
    }
    public static void setFade() {
        for (MediaPlayer mediaPlayer : mediaPlayers) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayers.remove(mediaPlayer);
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(releaseTime), new KeyValue(mediaPlayer.volumeProperty(), 0)));
                timeline.setOnFinished(event -> {
                    // mediaPlayer.stop();
                    mediaPlayer.dispose();
                });
                timeline.play();
                return;
            }
        }
    }

    /**
     * Calls the importSamples method with the given folder path.
     @param folderPath the path of the folder containing the media files to be imported
     */
    public static void importSamplesFromFolder(String folderPath) {
        importSamples(folderPath);
    }
}