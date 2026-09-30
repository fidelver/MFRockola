package com.mfrockola.classes;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/*
    This class is responsible for calling the VLC instance to create the media player
 */

class MediaPlayer {

    EmbeddedMediaPlayer embeddedMediaPlayer;
    EmbeddedMediaPlayer embeddedMediaPlayerMp3;

    String pathSongs;

    private JPanel mediaPlayerContainer;

    MediaPlayer(String pathVLC, String pathSongs) {
        this.pathSongs = pathSongs;

        try {
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), pathVLC);
            Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

            Canvas canvas = new Canvas();
            canvas.setBackground(Color.BLACK);

            mediaPlayerContainer = new JPanel();
            mediaPlayerContainer.setLayout(new BorderLayout());
            mediaPlayerContainer.add(canvas, BorderLayout.CENTER);

MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(
        "--avcodec-hw=none",
        "--quiet",
        "--no-video-title-show",
        "--verbose=-1");
            embeddedMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
            embeddedMediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
            embeddedMediaPlayerMp3 = mediaPlayerFactory.newEmbeddedMediaPlayer();

        } catch (UnsatisfiedLinkError error) {
            JOptionPane.showMessageDialog(null,
                    "No se encuentran las librerias de VLC, intente reinstalar VLC y configure el directorio correctamente. Si el problema persiste y su version de Java es de 64Bits pruebe instalando VLC de 64 Bits.");
            System.exit(1);
        }
    }

    JPanel getMediaPlayerContainer() {
        return mediaPlayerContainer;
    }

    // NUEVO: detener el reproductor de video
    void stopVideo() {
        if (embeddedMediaPlayer.isPlaying()) {
            embeddedMediaPlayer.stop();
        }
    }

    // NUEVO: detener el reproductor de mp3
    void stopMp3() {
        if (embeddedMediaPlayerMp3.isPlaying()) {
            embeddedMediaPlayerMp3.stop();
        }
    }

    // NUEVO: detener ambos
    void stopAll() {
        stopVideo();
        stopMp3();
    }

    // NUEVO: video de fondo de un MP3, en bucle, SIN audio
    void playBackgroundVideo(String path) {
        if (path == null || path.isEmpty()) return;

        File promotionalVideo = new File(path);
        if (!promotionalVideo.isFile()) return;

        // input-repeat=65535 → repetir indefinidamente
        // no-audio → silenciar aunque el archivo tenga pista de audio
        embeddedMediaPlayer.playMedia(
                promotionalVideo.getAbsolutePath(),
                ":input-repeat=65535",
                ":no-audio");
    }

    // Reproducir audio + video de fondo en bucle
    void playAudio(String path, String pathPromotionalVideo) {
        stopVideo();
        embeddedMediaPlayerMp3.playMedia(path);
        playBackgroundVideo(pathPromotionalVideo);
    }

    // Reproducir audio + video de fondo en bucle
    void playAudio(String gender, String singer, String songName, String pathPromotionalVideo) {
        stopVideo();
        embeddedMediaPlayerMp3.playMedia(
                new File(pathSongs, gender + File.separator + singer + File.separator + songName).getPath());
        playBackgroundVideo(pathPromotionalVideo);
    }

    // Reproducir video (detiene cualquier MP3 anterior)
    void playVideo(String path) {
        stopMp3();
        embeddedMediaPlayer.playMedia(path);
    }
        // Reproducir video en bucle infinito (para promocional.mpg en reposo)
    void playVideoLoop(String path) {
        stopMp3();
        embeddedMediaPlayer.playMedia(path, ":input-repeat=65535");
    }

    // Reproducir video (detiene cualquier MP3 anterior)
    void playVideo(String gender, String singer, String songName) {
        stopMp3();
        embeddedMediaPlayer.playMedia(
                new File(pathSongs, gender + File.separator + singer + File.separator + songName).getPath());
    }
}