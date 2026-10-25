package com.mfrockola.classes;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Owns the native VLCJ resources used by MFRockola.
 *
 * VLCJ is intentionally hidden behind this class so the main Swing interface
 * does not depend on the VLCJ API or its callback threading model.
 */
class MediaPlayer {

    enum Channel {
        VIDEO,
        AUDIO
    }

    interface Listener {
        void finished(Channel channel);
        void error(Channel channel);
    }

    private final EmbeddedMediaPlayer videoPlayer;
    private final EmbeddedMediaPlayer audioPlayer;
    private final MediaPlayerFactory mediaPlayerFactory;
    private final JPanel mediaPlayerContainer;
    private final String pathSongs;

    private Listener listener;
    private boolean released;

    MediaPlayer(String pathVLC, String pathSongs) {
        this.pathSongs = pathSongs;

        try {
            configureNativeSearchPath(pathVLC);

            Canvas canvas = new Canvas();
            canvas.setBackground(Color.BLACK);

            mediaPlayerContainer = new JPanel(new BorderLayout());
            mediaPlayerContainer.add(canvas, BorderLayout.CENTER);

            mediaPlayerFactory = new MediaPlayerFactory(
                    "--avcodec-hw=vaapi",
                    "--vout=xcb_x11",
                    "--quiet",
                    "--no-video-title-show",
                    "--no-stats",
                    "--no-snapshot-preview",
                    "--verbose=-1");

            videoPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
            videoPlayer.videoSurface().set(mediaPlayerFactory.videoSurfaces().newVideoSurface(canvas));
            audioPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();

            registerEvents(videoPlayer, Channel.VIDEO);
            registerEvents(audioPlayer, Channel.AUDIO);
        } catch (UnsatisfiedLinkError | RuntimeException error) {
            throw new IllegalStateException(
                    "No fue posible inicializar VLC/LibVLC. Verifique la instalación y arquitectura de VLC.", error);
        }
    }

    private static void configureNativeSearchPath(String pathVLC) {
        if (pathVLC == null || pathVLC.trim().isEmpty()) {
            return;
        }
        File directory = new File(pathVLC);
        if (directory.isDirectory()) {
            NativeLibrary.addSearchPath("vlc", directory.getAbsolutePath());
            NativeLibrary.addSearchPath("vlccore", directory.getAbsolutePath());
        }
    }

    private void registerEvents(EmbeddedMediaPlayer player, Channel channel) {
        player.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                dispatchToEdt(() -> {
                    if (listener != null && !released) {
                        listener.finished(channel);
                    }
                });
            }

            @Override
            public void error(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                dispatchToEdt(() -> {
                    if (listener != null && !released) {
                        listener.error(channel);
                    }
                });
            }
        });
    }

    private static void dispatchToEdt(Runnable task) {
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    JPanel getMediaPlayerContainer() {
        return mediaPlayerContainer;
    }

    boolean isAudioPlaying() {
        return !released && audioPlayer.status().isPlaying();
    }

    void stopCurrent() {
        if (released) {
            return;
        }
        if (audioPlayer.status().isPlaying()) {
            audioPlayer.controls().stop();
        } else if (videoPlayer.status().isPlaying()) {
            videoPlayer.controls().stop();
        }
    }

    void stopVideo() {
        if (!released && videoPlayer.status().isPlaying()) {
            videoPlayer.controls().stop();
        }
    }

    void stopAudio() {
        if (!released && audioPlayer.status().isPlaying()) {
            audioPlayer.controls().stop();
        }
    }

    void stopAll() {
        stopVideo();
        stopAudio();
    }

    void playBackgroundVideo(String path) {
        if (released || path == null || path.isEmpty()) {
            return;
        }

        File promotionalVideo = new File(path);
        if (!promotionalVideo.isFile()) {
            return;
        }

        videoPlayer.media().play(promotionalVideo.getAbsolutePath(), ":no-audio");
    }

    void playAudio(String path, String pathPromotionalVideo) {
        stopVideo();
        audioPlayer.media().play(path);
        playBackgroundVideo(pathPromotionalVideo);
    }

    void playAudio(String gender, String singer, String songName, String pathPromotionalVideo) {
        stopVideo();
        audioPlayer.media().play(
                new File(pathSongs, gender + File.separator + singer + File.separator + songName).getPath());
        playBackgroundVideo(pathPromotionalVideo);
    }

    void playVideo(String path) {
        stopAudio();
        videoPlayer.media().play(path);
    }

    void playVideoLoop(String path) {
        stopAudio();
        videoPlayer.media().play(path, ":input-repeat=65535");
    }

    void playVideo(String gender, String singer, String songName) {
        stopAudio();
        videoPlayer.media().play(
                new File(pathSongs, gender + File.separator + singer + File.separator + songName).getPath());
    }

    void release() {
        if (released) {
            return;
        }
        released = true;
        listener = null;
        try {
            videoPlayer.controls().stop();
        } catch (RuntimeException ignored) {
        }
        try {
            audioPlayer.controls().stop();
        } catch (RuntimeException ignored) {
        }
        videoPlayer.release();
        audioPlayer.release();
        mediaPlayerFactory.release();
    }
}
