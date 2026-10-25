package com.mfrockola.classes;

import com.mfrockola.android.InternetConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.mfrockola.classes.SettingsManager.*;
import static com.mfrockola.classes.Utils.*;

/**
 * This class is the center of MFRockola.
 */
class Interface extends JFrame {

    // ------------------------------------------------------------------
    // Estado del sistema
    // ------------------------------------------------------------------
    private enum EstadoSistema { REPOSO, COLA }

    private volatile EstadoSistema estadoActual = EstadoSistema.REPOSO;
    private volatile boolean deteniendoInternamente = false;
    private volatile boolean itemActualEsMp3 = false;
    private volatile long ultimaTransicionMs = 0;

    // ------------------------------------------------------------------

    private String buildPath(String... parts) {
        File file = new File(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            file = new File(file, parts[i]);
        }
        return file.getPath();
    }

    private int randomSong;
    private int resetSongs;

    private int amountOfCredits;
    private boolean free;
    private boolean lockScreen;
    private int fontSelectorSize;

    private String pathSongs;
    private String pathVideosMP3;
    private String pathVLC;

    private int keyUpList;
    private int keyDownList;
    private int keyUpGenre;
    private int keyDownGenre;
    private int keyFullScreen;
    private int keyDeleteNumber;
    private int keyNextSong;
    private int keyAddCredit;
    private int keyDeleteCredit;
    private int clickOfCredits;
    private boolean rightClickCancelMusic;
    private String password;

    private boolean defaultBackground;
    private String pathBackground;
    private Color color1;
    private Color color2;
    private String fontCells;
    private int fontCellsSize;
    private Color fontCellsColor;
    private int fontCellsBold;

    private int usedCredits;
    private int insertedCredits;

    private boolean addAditionalCredit;
    private int numberAditionalCredits;
    private int everyAmountOfCredit;
    private boolean continuousCredits;
    private boolean awardPrize;
    private int prizeAmount;
    private int creditsForPrize;
    private String typeOfPrize;

    private int savedCredits;
    private JSONArray savedSongs;

    private SettingsManager mUserSettings;

    private int credits;

    private int widthScreen;
    private int heightScreen;

    private BlockedSongs mBlockedSongs;

    private PlayList mPlayList = new PlayList();

    private boolean isFullScreen = false;

    private BackgroundImagePanel mBackgroundImagePanel;

    private JPanel videoPanel;
    private JPanel panel;

    private JPanel bottomPanel;

    private Dimension resolution;

    private ListMusic listMusicData;

    private JList mSongListInterface;
    private JList playListInterface;

    private JLabel labelMusicalGenre;
    private JLabel labelSongPlayingBottom;
    private JLabel labelSongPlayingRight;
    private JLabel labelCredits;
    private JLabel labelPromotions;

    private MediaPlayer mMediaPlayer;

    private SongSelector mSongSelector;

    private boolean cancelSong;

    private int counterClick;

    private int insertedCreditsForAditionalCredits;
    private int insertedCreditsForPrize;

    private JScrollPane mScrollPane;

    private Timer timerChangerLabelCredits;
    private Timer timerFullScreen;
    private Timer timer;
    private Timer timerRandomSong;

    private KeyboardManager mKeyboardManager;

    private Clip promotionalSound;

    Interface() {
        try {
            mUserSettings = new SettingsManager();

            randomSong = (int) mUserSettings.getSetting(KEY_RANDOM_SONG);
            resetSongs = (int) mUserSettings.getSetting(KEY_RESET_SONGS);

            amountOfCredits = (int) mUserSettings.getSetting(KEY_AMOUNT_OF_CREDITS);
            free = (boolean) mUserSettings.getSetting(KEY_FREE);
            lockScreen = (boolean) mUserSettings.getSetting(KEY_LOCK_SCREEN);
            fontSelectorSize = (int) mUserSettings.getSetting(KEY_FONT_SELECTOR_SIZE);

            pathSongs = (String) mUserSettings.getSetting(KEY_PATH_SONGS);
            pathVideosMP3 = (String) mUserSettings.getSetting(KEY_PATH_VIDEOS_MP3);
            pathVLC = (String) mUserSettings.getSetting(KEY_PATH_VLC);

            keyUpList = (int) mUserSettings.getSetting(KEY_UP_LIST);
            keyDownList = (int) mUserSettings.getSetting(KEY_DOWN_LIST);
            keyUpGenre = (int) mUserSettings.getSetting(KEY_UP_GENRE);
            keyDownGenre = (int) mUserSettings.getSetting(KEY_DOWN_GENRE);
            keyFullScreen = (int) mUserSettings.getSetting(KEY_FULL_SCREEN);
            keyDeleteNumber = (int) mUserSettings.getSetting(KEY_DELETE_NUMBER);
            keyNextSong = (int) mUserSettings.getSetting(KEY_NEXT_SONG);
            keyAddCredit = (int) mUserSettings.getSetting(KEY_ADD_CREDIT);
            keyDeleteCredit = (int) mUserSettings.getSetting(KEY_DELETE_CREDIT);
            clickOfCredits = (int) mUserSettings.getSetting(KEY_CLICK_OF_CREDITS);
            rightClickCancelMusic = (boolean) mUserSettings.getSetting(KEY_RIGHT_CLICK_CANCEL_MUSIC);
            password = (String) mUserSettings.getSetting(KEY_PASSWORD);

            defaultBackground = (boolean) mUserSettings.getSetting(KEY_DEFAULT_BACKGROUND);
            pathBackground = (String) mUserSettings.getSetting(KEY_PATH_BACKGRONUD);
            color1 = Utils.getColor((String) mUserSettings.getSetting(KEY_COLOR_1));
            color2 = Utils.getColor((String) mUserSettings.getSetting(KEY_COLOR_2));
            fontCells = (String) mUserSettings.getSetting(KEY_FONT_CELLS);
            fontCellsSize = (int) mUserSettings.getSetting(KEY_FONT_CELLS_SIZE);
            fontCellsColor = Utils.getColor((String) mUserSettings.getSetting(KEY_FONTS_CELLS_COLOR));
            fontCellsBold = (int) mUserSettings.getSetting(KEY_FONT_CELL_BOLD);

            usedCredits = (int) mUserSettings.getSetting(KEY_USED_CREDITS);
            insertedCredits = (int) mUserSettings.getSetting(KEY_INSERTED_CREDITS);

            addAditionalCredit = (boolean) mUserSettings.getSetting(KEY_ADD_ADITIONAL_CREDIT);
            numberAditionalCredits = (int) mUserSettings.getSetting(KEY_NUMBER_ADITIONAL_CREDITS);
            everyAmountOfCredit = (int) mUserSettings.getSetting(KEY_EVERY_AMOUNT_OF_CREDITS);
            continuousCredits = (boolean) mUserSettings.getSetting(KEY_CONTINUOUS_CREDITS);
            awardPrize = (boolean) mUserSettings.getSetting(KEY_AWARD_PRIZE);
            prizeAmount = (int) mUserSettings.getSetting(KEY_PRIZE_AMOUNT);
            creditsForPrize = (int) mUserSettings.getSetting(KEY_CREDITS_FOR_PRICE);
            typeOfPrize = (String) mUserSettings.getSetting(KEY_TYPE_OF_PRIZE);
            insertedCreditsForPrize = (int) mUserSettings.getSetting(KEY_INSERTED_CREDITS_FOR_PRIZE);

            savedCredits = (int) mUserSettings.getSetting(KEY_SAVED_CREDITS);
            savedSongs = (JSONArray) mUserSettings.getSetting(KEY_SAVED_SONGS);

            if ((boolean) mUserSettings.getSetting(KEY_SAVE_SONGS) && savedSongs.length()>0) {
                for (int i = 0; i < savedSongs.length(); i++) {
                    try {
                        JSONObject songJSON = savedSongs.getJSONObject(i);
                        mPlayList.addSong(new Song(
                                songJSON.getInt(KEY_SONG_NUMBER),
                                songJSON.getString(KEY_SONG_GENRE),
                                songJSON.getString(KEY_SONG_SINGER),
                                songJSON.getString(KEY_SONG_NAME)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                savedSongs.remove(0);
                mUserSettings.writeSetting(true,new KeyPairValue(KEY_SAVED_SONGS,savedSongs));
            }

            mBlockedSongs = new BlockedSongs(resetSongs);

            mSongSelector = new SongSelector(
                    keyDeleteNumber,
                    keyUpList,
                    keyDownList,
                    keyUpGenre,
                    keyDownGenre,
                    fontSelectorSize);

            cancelSong = rightClickCancelMusic;

            File file = new File(pathVLC);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,
                        "VLC no se encuentra instalado o el directorio no se encuentra.",
                        "Error de VLC", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }

            file = new File(pathSongs);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,
                        "El directorio de musicas no se encuentra, verifiquelo e intente nuevamente.",
                        "Error de Directorios", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }

            file = new File(pathVideosMP3);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,
                        "El directorio de los videos predeterminados no se encuentra, verifiquelo e intente nuevamente.",
                        "Error de Directorios", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
            PromotionalVideoInstaller.install(pathVideosMP3);
        }
        catch (NullPointerException excepcion) {
            excepcion.printStackTrace();
            new SettingsWindow();
        }

        initComponents();

        ActionListener changeLblCredits = e -> {
            if (free) {
                labelCredits.setText("Creditos Libres");
            } else {
                labelCredits.setText(String.format("Creditos: %d", credits));
            }
            labelCredits.setForeground(Color.WHITE);
        };

        timerChangerLabelCredits = new Timer(5000, changeLblCredits);
        timerChangerLabelCredits.setRepeats(false);

        ActionListener changeFullScreen = e -> {
            if (!isFullScreen) {
                setFullScreen();
                timer.restart();
            }
        };

        // Al vencer el timer de cortesía se encola una cortesía aleatoria
        ActionListener play = e -> {
            encolarCortesiaPorTimer();
        };

        ActionListener pressKey = e -> {
            try {
                Robot robot = new Robot();
                robot.keyPress(120);
                robot.keyRelease(120);
            } catch (AWTException exception) {
                exception.printStackTrace();
            }
        };

        timer = new Timer(500, pressKey);
        timer.setRepeats(false);

        timerFullScreen = new Timer(10000, changeFullScreen);
        timerFullScreen.setRepeats(false);

        timerRandomSong = new Timer(randomSong*1000*60,play);
        timerRandomSong.setRepeats(false);

        getContentPane().add(mBackgroundImagePanel);

        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(resolution);
        setVisible(true);
        requestFocusInWindow();

        MediaPlayerManager sMediaPlayerManager = new MediaPlayerManager();

        mMediaPlayer.embeddedMediaPlayer.addMediaPlayerEventListener(sMediaPlayerManager);
        mMediaPlayer.embeddedMediaPlayerMp3.addMediaPlayerEventListener(sMediaPlayerManager);

        // Arranque: si hay cola guardada, la reproducimos. Si no, REPOSO.
        if (mPlayList.songToPlay() == null) {
            if (randomSong == 0) {
                playRandomSong();
            } else {
                entrarEnReposo();
            }
        } else {
            estadoActual = EstadoSistema.COLA;
            procesarSiguienteEnCola();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {

                if(clickOfCredits==0 && !e.isMetaDown() && !free)
                {
                    credits = credits + amountOfCredits;
                    labelCredits.setText(String.format("Creditos: %d", credits));
                    updateCreditsSettings();
                    labelCredits.setForeground(Color.WHITE);
                    if (isFullScreen) {
                        setFullScreen();
                    }
                    entregarPremiosYCreditosAdicionales();

                } else if (cancelSong && e.isMetaDown() && mPlayList.songToPlay()!=null) {

                    if (isFullScreen) {
                        setFullScreen();
                    }
                    counterClick++;
                    if (counterClick == 3) {

                        PasswordPanel passwordPanel = new PasswordPanel();
                        JOptionPane optionPane = new JOptionPane(passwordPanel, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        JDialog dlg = optionPane.createDialog("Eliminar Canción");

                        dlg.addWindowFocusListener(new WindowAdapter() {
                            @Override
                            public void windowGainedFocus(WindowEvent e) {
                                passwordPanel.gainedFocus();
                            }
                        });

                        dlg.setVisible(true);
                        requestFocusInWindow();

                        if (optionPane.getValue()!=null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
                            if (new String(passwordPanel.getPassword()).equals(password)) {
                                if (mMediaPlayer.embeddedMediaPlayerMp3.isPlaying()) {
                                    mMediaPlayer.embeddedMediaPlayerMp3.stop();
                                } else {
                                    mMediaPlayer.embeddedMediaPlayer.stop();
                                }
                                dlg.dispatchEvent(new WindowEvent(dlg, WindowEvent.WINDOW_CLOSING));
                                dlg.dispose();
                            }
                        } else {
                            dlg.dispatchEvent(new WindowEvent(dlg, WindowEvent.WINDOW_CLOSING));
                            dlg.dispose();
                        }

                        counterClick = 0;
                    }

                } else if (e.isMetaDown() && (int) mUserSettings.getSetting(KEY_CLICK_OF_CREDITS) == 1 && !free && !cancelSong) {

                    credits = credits + amountOfCredits;
                    labelCredits.setText(String.format("Creditos: %d", credits));
                    updateCreditsSettings();
                    labelCredits.setForeground(Color.WHITE);
                    if (isFullScreen) {
                        setFullScreen();
                    }
                    entregarPremiosYCreditosAdicionales();
                }
            }
        });

        mKeyboardManager = new KeyboardManager();

        this.addKeyListener(mKeyboardManager);

        bottomPanel.addKeyListener(mKeyboardManager);
    }

    private void initComponents() {

        credits = savedCredits;

        resolution = Toolkit.getDefaultToolkit().getScreenSize();

        widthScreen = (int) resolution.getWidth();
        heightScreen = (int) (resolution.getHeight() - 54);

        setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/com/mfrockola/imagenes/icono.png")));

        labelMusicalGenre = new JLabel("Genero");
        labelMusicalGenre.setForeground(Color.WHITE);
        labelMusicalGenre.setFont(new Font("Calibri", Font.BOLD, 23));
        labelMusicalGenre.setBounds((int)(widthScreen/45.533), (int)(heightScreen/51.2), (int)(widthScreen/1.7603), 35);

        if (free) {
            labelCredits= new JLabel("Creditos Libres");
        } else {
            labelCredits = new JLabel(String.format("Creditos: %d", savedCredits));
        }

        labelCredits.setForeground(Color.WHITE);
        labelCredits.setFont(new Font("Calibri", Font.BOLD, 23));
        labelCredits.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        Icon log = new ImageIcon(this.getClass().getResource("/com/mfrockola/imagenes/nombre.png"));
        JLabel labelLogo = new JLabel(log);
        labelLogo.setHorizontalAlignment(SwingConstants.RIGHT);
        labelLogo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        labelSongPlayingRight = new JLabel("Su selección musical");
        labelSongPlayingRight.setForeground(Color.WHITE);
        labelSongPlayingRight.setFont(new Font("Calibri", Font.BOLD, 23));
        labelSongPlayingRight.setHorizontalAlignment(SwingConstants.CENTER);

        labelSongPlayingBottom = new JLabel();
        labelSongPlayingBottom.setText("MFRockola");
        labelSongPlayingBottom.setHorizontalAlignment(SwingConstants.CENTER);
        labelSongPlayingBottom.setForeground(Color.WHITE);
        labelSongPlayingBottom.setFont(new Font("Calibri", Font.BOLD, 23));

        Icon icon = new ImageIcon(this.getClass().getResource("/com/mfrockola/imagenes/promocionLabel.png"));
        labelPromotions = new JLabel("Aqui van las promociones",icon,JLabel.CENTER);
        labelPromotions.setVerticalTextPosition(JLabel.BOTTOM);
        labelPromotions.setHorizontalTextPosition(JLabel.CENTER);
        labelPromotions.setForeground(Color.BLACK);
        labelPromotions.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        labelPromotions.setFont(new Font("Calibri", Font.BOLD, 23));
        labelPromotions.setHorizontalAlignment(SwingConstants.CENTER);
        labelPromotions.setVisible(false);
        labelPromotions.setOpaque(true);
        labelPromotions.setBackground(Color.WHITE);
        labelPromotions.setBounds((widthScreen/2)-250,(heightScreen/2)-80,500,160);

        listMusicData = new ListMusic(pathSongs,pathVideosMP3);

        mSongListInterface = new JList();
        mSongListInterface.setCellRenderer(new RowRenderer(new Font(fontCells,
                fontCellsBold,fontCellsSize),fontCellsColor,
                color1, color2));
        mSongListInterface.setListData(listMusicData.getGenderSongs(0));
        mSongListInterface.addKeyListener(mKeyboardManager);
        mSongListInterface.setVisibleRowCount(20);
        mSongListInterface.setFocusable(false);
        mSongListInterface.setMaximumSize(getMaximumSize());

        mScrollPane = new JScrollPane(mSongListInterface);
        mScrollPane.setBounds((int)(widthScreen/45.533), (int)(heightScreen/12.8),(int)(widthScreen/1.7603), (int)(heightScreen/1.0924));
        mScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        playListInterface = new JList();
        playListInterface.setListData(mPlayList.getPlayList());
        playListInterface.setCellRenderer(new RowRenderer(new Font(fontCells,
                fontCellsBold, fontCellsSize),fontCellsColor,
                color1, color2));
        playListInterface.setBounds((int)(widthScreen/1.633), (int)(heightScreen/1.52), (int)(widthScreen/2.732), (int)(heightScreen/3.051));
        playListInterface.setFocusable(false);

        labelMusicalGenre.setText("Genero Musical: "+ listMusicData.getNameOfGender());

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        mainPanel.add(labelPromotions);
        mainPanel.add(mScrollPane);

        mainPanel.add(labelMusicalGenre);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(labelLogo,BorderLayout.EAST);

        try {
            if (defaultBackground) {
                mBackgroundImagePanel = new BackgroundImagePanel(this.getClass().getResource("/com/mfrockola/imagenes/fondo.jpg"));
            } else {
                mBackgroundImagePanel = new BackgroundImagePanel(new URL("file:"+pathBackground));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mBackgroundImagePanel.setLayout(new BorderLayout());
        mBackgroundImagePanel.add(bottomPanel,BorderLayout.SOUTH);

        mBackgroundImagePanel.add(mainPanel,BorderLayout.CENTER);

        mMediaPlayer = new MediaPlayer(pathVLC,pathSongs);
        videoPanel = new JPanel();
        videoPanel.setLayout(new BorderLayout());
        videoPanel.setBounds((int)(widthScreen/1.633), (int)(heightScreen/16.340),(int)(widthScreen/2.732), (int)(heightScreen/2.7137));
        videoPanel.add(mMediaPlayer.getMediaPlayerContainer(),BorderLayout.CENTER);
        mainPanel.add(videoPanel);

        panel = new JPanel();
        panel.setOpaque(false);
        panel.setBounds((int)(widthScreen/1.633), (int)(heightScreen/2.2222), (int)(widthScreen/2.732), (int)(heightScreen/2.021));
        mainPanel.add(panel);
        panel.setLayout(new GridLayout(2, 1, 20, 0));

        bottomPanel.add(labelCredits,BorderLayout.WEST);
        bottomPanel.add(labelSongPlayingBottom, BorderLayout.CENTER);

        JPanel panelRight = new JPanel();
        panelRight.setOpaque(false);
        panel.add(panelRight);
        panelRight.setLayout(new GridLayout(3, 1, 0, 0));
        panelRight.add(labelSongPlayingRight);

        panelRight.add(mSongSelector.labelSelector);

        mainPanel.add(playListInterface);
    }

    // ==================================================================
    // Gestión de fullscreen
    // ==================================================================
    private void setFullScreen() {
        if (!isFullScreen)
        {
            mScrollPane.setVisible(false);
            mSongListInterface.setVisible(false);
            panel.setVisible(false);
            labelMusicalGenre.setVisible(false);
            videoPanel.setBounds(0, 0, widthScreen, heightScreen);
            isFullScreen = true;
        }
        else
        {
            videoPanel.setBounds((int)(widthScreen/1.633), (int)(heightScreen/16.340),(int)(widthScreen/2.732), (int)(heightScreen/2.7137));
            mScrollPane.setVisible(true);
            requestFocusInWindow();
            mSongListInterface.setVisible(true);
            requestFocusInWindow();
            panel.setVisible(true);
            requestFocusInWindow();
            labelMusicalGenre.setVisible(true);
            requestFocusInWindow();
            isFullScreen = false;
        }
    }

    // ==================================================================
    // Reproducción — modelo de estados REPOSO / COLA
    // ==================================================================

    private void detenerReproductores() {
        deteniendoInternamente = true;
        try {
            mMediaPlayer.stopAll();
        } finally {
            deteniendoInternamente = false;
        }
    }

    private void entrarEnReposo() {
        estadoActual = EstadoSistema.REPOSO;
        itemActualEsMp3 = false;

        File promotionalFile = new File(buildPath(pathVideosMP3, "promocional.mpg"));

        if (promotionalFile.isFile()) {
            log("=== ENTRANDO EN REPOSO: promocional.mpg ===");
            log("Archivo: " + promotionalFile.getAbsolutePath());

            if (!isFullScreen) {
                setFullScreen();
            }
            mMediaPlayer.playVideoLoop(promotionalFile.getAbsolutePath());
            timerRandomSong.restart();
            log("=== TIMER DE CORTESIA INICIADO ===");
        } else {
            log("=== NO EXISTE promocional.mpg; solo TIMER ===");
            timerRandomSong.restart();
        }

        labelSongPlayingBottom.setText("MFRockola");
        labelSongPlayingRight.setText("Su selección musical");
    }

    private void procesarSiguienteEnCola() {
        if (mPlayList.songToPlay() == null) {
            entrarEnReposo();
            return;
        }

        estadoActual = EstadoSistema.COLA;
        timerRandomSong.stop();

        int extension = Utils.getExtension(buildPath(
                pathSongs,
                mPlayList.getSongGender(),
                mPlayList.getSinger(),
                mPlayList.songToPlay()));

        log("=== REPRODUCIENDO ITEM DE LA COLA ===");
        log("Genero: " + mPlayList.getSongGender());
        log("Artista: " + mPlayList.getSinger());
        log("Archivo: " + mPlayList.songToPlay());

        if (esVideo(extension)) {
            itemActualEsMp3 = false;
            if (!isFullScreen) {
                setFullScreen();
            }
            mMediaPlayer.playVideo(
                    mPlayList.getSongGender(),
                    mPlayList.getSinger(),
                    mPlayList.songToPlay());
        } else if (esAudio(extension)) {
            itemActualEsMp3 = true;
            String promVideo = listMusicData.getPromVideo();
            mMediaPlayer.playAudio(
                    mPlayList.getSongGender(),
                    mPlayList.getSinger(),
                    mPlayList.songToPlay(),
                    buildPath(pathVideosMP3, promVideo));
        } else {
            log("=== FORMATO NO VALIDO: " + mPlayList.songToPlay() + " ===");
            nextSong();
            return;
        }

        labelSongPlayingBottom.setText(String.format("%05d - %s - %s - %s",
                mPlayList.getSongNumber(),
                mPlayList.getSongGender(),
                mPlayList.getSinger(),
                mPlayList.songToPlay()));
        labelSongPlayingRight.setText(String.format("%05d - %s - %s - %s",
                mPlayList.getSongNumber(),mPlayList.getSongGender(),
                mPlayList.getSinger(), mPlayList.songToPlay()));
    }

    private void encolarYArrancarSiProcede(Song song) {
        boolean estabaEnReposo = (estadoActual == EstadoSistema.REPOSO);

        mPlayList.addSong(song);
        playListInterface.setListData(mPlayList.getPlayList());

        if (estabaEnReposo) {
            timerRandomSong.stop();
            detenerReproductores();
            estadoActual = EstadoSistema.COLA;
            procesarSiguienteEnCola();
        }
    }

    private void encolarCortesiaPorTimer() {
        if (estadoActual == EstadoSistema.COLA) {
            return;
        }

        int size = listMusicData.getSizeListOfSongs();
        if (size <= 0) {
            log("=== NO HAY CANCIONES PARA CORTESIA ===");
            return;
        }

        Song cortesia = listMusicData.getSong(new Random().nextInt(size));
        log("=== CORTESIA POR TIMER ===");
        log("Genero: " + cortesia.getSongGenre());
        log("Artista: " + cortesia.getSinger());
        log("Archivo: " + cortesia.getSongName());

        encolarYArrancarSiProcede(cortesia);
    }

    public void playRandomSong(){
        Random random = new Random();
        if (randomSong == 0) {
            File file = new File(buildPath(pathSongs, "Promocionales", "Promocionales"));
            if (file.isDirectory()) {
                String [] list = file.list();
                if (list == null || list.length == 0) return;
                random = new Random();
                int rand = random.nextInt(list.length);
                String song = list[rand];
                Song cancion = new Song(0,"Promocionales", "Promocionales", song);
                log("=== VIDEO DE CORTESIA (Promocionales) ===");
                log("Archivo: " + song);
                encolarYArrancarSiProcede(cancion);
                return;
            }
        }

        Song cancion = listMusicData.getSong(random.nextInt(listMusicData.getSizeListOfSongs()));
        encolarYArrancarSiProcede(cancion);
    }

    private static boolean esVideo(int ext) {
        return ext == EXT_MP4 || ext == EXT_AVI || ext == EXT_MPG
                || ext == EXT_FLV || ext == EXT_MKV;
    }

    private static boolean esAudio(int ext) {
        return ext == EXT_MP3 || ext == EXT_WMA
                || ext == EXT_WAV || ext == EXT_AAC;
    }

    // ==================================================================
    // Listener de VLCJ
    // ==================================================================
    private class MediaPlayerManager extends MediaPlayerEventAdapter {

        @Override
        public void stopped(uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer) {
            if (deteniendoInternamente) return;

            boolean esMp3 = (mediaPlayer == mMediaPlayer.embeddedMediaPlayerMp3);
            boolean esVideo = (mediaPlayer == mMediaPlayer.embeddedMediaPlayer);

            log("=== VLCJ STOPPED ===");
            log("player=" + (esMp3 ? "MP3" : (esVideo ? "VIDEO" : "OTRO")));
            log("estado=" + estadoActual);
            log("itemActualEsMp3=" + itemActualEsMp3);
            log("playlistSong=" + mPlayList.songToPlay());
            log("====================");

            if (estadoActual != EstadoSistema.COLA || mPlayList.songToPlay() == null) {
                return;
            }

            // Si el item actual es MP3 y se detuvo el VIDEO → es el fondo. Ignorar.
            if (itemActualEsMp3 && esVideo) {
                log("=== VIDEO DE FONDO DETENIDO: IGNORADO ===");
                return;
            }

            // Si el item actual es VIDEO y se detuvo el MP3 → residual. Ignorar.
            if (!itemActualEsMp3 && esMp3) {
                log("=== MP3 RESIDUAL DETENIDO: IGNORADO ===");
                return;
            }

            nextSong();
        }

        @Override
        public void playing(uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer) {
            // Log opcional (silenciado)
        }

        @Override
        public void finished(uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer) {
            boolean esMp3 = (mediaPlayer == mMediaPlayer.embeddedMediaPlayerMp3);

            log("=== VLCJ FINISHED ===");
            log("player=" + (esMp3 ? "MP3" : "VIDEO"));
            log("estado=" + estadoActual);
            log("itemActualEsMp3=" + itemActualEsMp3);
            log("playlistSong=" + mPlayList.songToPlay());
            log("====================");

            if (estadoActual != EstadoSistema.COLA || mPlayList.songToPlay() == null) {
                return;
            }

            // Si el item actual es MP3 y terminó el VIDEO → es el fondo. Ignorar.
            if (itemActualEsMp3 && !esMp3) {
                log("=== FINISHED DEL VIDEO DE FONDO: IGNORADO ===");
                return;
            }

            // Si el item actual es VIDEO y terminó el MP3 → residual. Ignorar.
            if (!itemActualEsMp3 && esMp3) {
                log("=== FINISHED DEL MP3 RESIDUAL: IGNORADO ===");
                return;
            }

            nextSong();
        }
    }

    public void nextSong() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimaTransicionMs < 500) {
            log("=== nextSong() IGNORADO (debounce) ===");
            return;
        }
        ultimaTransicionMs = ahora;

        if (mPlayList.songToPlay() != null) {
            mPlayList.removeSong();
        }
        if (savedSongs.length()>0) {
            savedSongs.remove(0);
            mUserSettings.writeSetting(true,new KeyPairValue(KEY_SAVED_SONGS,savedSongs));
        }
        playListInterface.setListData(mPlayList.getPlayList());

        if (mPlayList.songToPlay() == null) {
            entrarEnReposo();
        } else {
            procesarSiguienteEnCola();
        }
    }

    // ==================================================================
    // Créditos / premios
    // ==================================================================
    public void updateCreditsSettings() {
        timerFullScreen.stop();
        usedCredits = usedCredits + amountOfCredits;
        insertedCredits++;
        mUserSettings.writeSetting(false,new KeyPairValue(KEY_USED_CREDITS,usedCredits));
        mUserSettings.writeSetting(false,new KeyPairValue(KEY_INSERTED_CREDITS,insertedCredits));
        mUserSettings.writeSetting(true,new KeyPairValue(KEY_SAVED_CREDITS,credits));
    }

    public void playSound() {
        try {
            BufferedInputStream bis = new BufferedInputStream(getClass().getResourceAsStream("/com/mfrockola/sounds/felicitaciones.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
            promotionalSound = AudioSystem.getClip();
            promotionalSound.open(ais);
            promotionalSound.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void entregarPremiosYCreditosAdicionales() {
        if (addAditionalCredit && !free) {
            insertedCreditsForAditionalCredits++;
            if (insertedCreditsForAditionalCredits >= everyAmountOfCredit) {
                insertedCreditsForAditionalCredits = 0;
                credits = credits + numberAditionalCredits;
                labelCredits.setText(String.format("Creditos: %d", credits));
                labelPromotions.setText(String.format("Ganaste %s creditos adicionales",
                        numberAditionalCredits));
                labelPromotions.setVisible(true);
                requestFocusInWindow();
            }
        }

        if (awardPrize && !free) {
            insertedCreditsForPrize++;
            mUserSettings.writeSetting(true,new KeyPairValue(KEY_INSERTED_CREDITS_FOR_PRIZE,insertedCreditsForPrize));
            if (insertedCreditsForPrize % creditsForPrize == 0) {
                labelPromotions.setText(String.format("Ganaste %s %s!", prizeAmount,typeOfPrize));
                labelPromotions.setVisible(true);
                requestFocusInWindow();
                playSound();
            }
        }
    }

    // ==================================================================
    // Log limpio
    // ==================================================================
    private static void log(String msg) {
        System.out.println(msg);
    }

    // ==================================================================
    // KeyboardManager
    // ==================================================================
    private class KeyboardManager extends KeyAdapter
    {
        SQLiteConsultor consultor = new SQLiteConsultor();

        public void keyPressed(KeyEvent evento)
        {
            if (evento.getKeyCode()==KeyEvent.VK_NUM_LOCK) {
                Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK,true);
            }

            if (evento.getKeyCode()==keyFullScreen && (credits > 0 || !lockScreen))
            {
                if (labelPromotions.isVisible()) {
                    labelPromotions.setVisible(false);
                    if (promotionalSound!=null) {
                        promotionalSound.stop();
                        promotionalSound.close();
                    }
                } else {
                    setFullScreen();
                }
            }

            if(evento.getKeyCode()== keyDeleteNumber && mSongSelector.counterValue > 0)
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }

            if ((evento.getKeyCode()==48 || evento.getKeyCode()==96) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if ((evento.getKeyCode()==49 || evento.getKeyCode()==97) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if ((evento.getKeyCode()==50 || evento.getKeyCode()==98) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if ((evento.getKeyCode()==51 || evento.getKeyCode()==99) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if ((evento.getKeyCode()==52 || evento.getKeyCode()==100) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if ((evento.getKeyCode()==53 || evento.getKeyCode()==101) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if ((evento.getKeyCode()==54 || evento.getKeyCode()==102) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if ((evento.getKeyCode()==55 || evento.getKeyCode()==103) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if ((evento.getKeyCode()==56 || evento.getKeyCode()==104) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if ((evento.getKeyCode()==57 || evento.getKeyCode()==105) && (credits > 0 || !lockScreen))
            {
                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
            }
            else if (evento.getKeyCode()==77) {
                Thread ic = new Thread(new InternetConnection());
            }

            if (mSongSelector.play)
            {
                int numero;
                boolean condicion;

                numero = Integer.parseInt(String.format("%s%s%s%s%s", mSongSelector.values[0],mSongSelector.values[1],
                        mSongSelector.values[2],mSongSelector.values[3],mSongSelector.values[4]));

                if (mBlockedSongs.checkBlockedSongs(numero))
                    condicion = true;
                else
                    condicion = false;

                if (numero >= listMusicData.getSizeListOfSongs()) {
                    labelCredits.setText("Canción no encontrada");
                    mSongSelector.play = false;
                    mSongSelector.resetValues();
                    mSongSelector.labelSelector.setText("- - - - -");
                    timerChangerLabelCredits.start();
                }
                else
                {
                    if ((credits > 0 && condicion)||(free && condicion))
                    {
                        Song cancionAReproducir = listMusicData.getSong(numero);

                        encolarYArrancarSiProcede(cancionAReproducir);

                        if (estadoActual == EstadoSistema.COLA
                                && mPlayList.songToPlay() != null
                                && !mPlayList.songToPlay().equals(cancionAReproducir.getSongName())) {
                            JSONObject jsonSong = mPlayList.getSongJSONObject(cancionAReproducir);
                            savedSongs.put(jsonSong);
                            mUserSettings.writeSetting(false,new KeyPairValue(KEY_SAVED_SONGS,savedSongs));
                        }

                        if (!free) {
                            --credits;
                            mUserSettings.writeSetting(true,new KeyPairValue(KEY_SAVED_CREDITS,credits));
                            labelCredits.setForeground(Color.WHITE);
                            labelCredits.setText(String.format("%s: %d","Creditos",credits));
                        }

                        mSongSelector.play = false;
                        mSongSelector.resetValues();
                        mSongSelector.labelSelector.setText("- - - - -");
                        mBlockedSongs.blockSong(numero);

                        if (credits == 0 && !free) {
                            timerFullScreen.restart();
                        }

                        if (continuousCredits) {
                            insertedCredits = 0;
                        }

                        updateDataBase(cancionAReproducir);
                    }
                    else
                    {
                        labelCredits.setForeground(Color.RED);
                        labelCredits.setText("La canción que ha seleccionado no se puede reproducir antes de " + resetSongs +" mins");
                        timerChangerLabelCredits.start();
                        mSongSelector.play = false;
                        mSongSelector.resetValues();
                        mSongSelector.labelSelector.setText("- - - - -");
                    }
                }
            }
            else if (evento.getKeyCode()==122) {
                String contrasenia = JOptionPane.showInputDialog("Introduzca la clave");
                if (contrasenia != null && contrasenia.equals("12345")) {
                    if (!free) {
                        free = true;
                        labelCredits.setText("Creditos: Libres");
                    } else {
                        free = false;
                        labelCredits.setText(String.format("Creditos: %d", credits));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña Incorrecta","Error",JOptionPane.ERROR_MESSAGE);
                }
            } else if (evento.getKeyCode()==123) {
                @SuppressWarnings("unused")
                SettingsWindow config = new SettingsWindow();
            } else if (evento.getKeyCode()==keyDownList && (credits > 0 || !lockScreen)) {
                if (isFullScreen) {
                    setFullScreen();
                }

                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));

                labelPromotions.setVisible(false);

                if (mSongListInterface.getSelectedIndex() - 20 < 0) {
                    if (mSongListInterface.getSelectedIndex() == 0) {
                        if (listMusicData.downGender()) {
                            mSongListInterface.setListData(listMusicData.getGenderSongs(listMusicData.getSelectedGender()));
                            mSongListInterface.setSelectedIndex(listMusicData.getGenderSongs(listMusicData.getSelectedGender()).length-1);
                            mSongListInterface.ensureIndexIsVisible(listMusicData.getGenderSongs(listMusicData.getSelectedGender()).length-1);
                            labelMusicalGenre.setText("Genero Musical: " + listMusicData.getNameOfGender());
                        }
                    } else {
                        mSongListInterface.setSelectedIndex(0);
                        mSongListInterface.ensureIndexIsVisible(0);
                    }
                } else {
                    mSongListInterface.setSelectedIndex(mSongListInterface.getSelectedIndex()-20);
                    mSongListInterface.ensureIndexIsVisible(mSongListInterface.getSelectedIndex());
                }
            } else if (evento.getKeyCode()==keyUpList && (credits > 0 || !lockScreen)) {
                if (isFullScreen) {
                    setFullScreen();
                }

                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));

                labelPromotions.setVisible(false);

                if(mSongListInterface.getSelectedIndex()+20 >= listMusicData.getGenderSongs(listMusicData.getSelectedGender()).length) {
                    if (mSongListInterface.getSelectedIndex()==listMusicData.getGenderSongs(listMusicData.getSelectedGender()).length-1) {
                        if (listMusicData.upGender()) {
                            mSongListInterface.setListData(listMusicData.getGenderSongs(listMusicData.getSelectedGender()));
                            mSongListInterface.setSelectedIndex(0);
                            mSongListInterface.ensureIndexIsVisible(0);
                            labelMusicalGenre.setText("Genero Musical: " + listMusicData.getNameOfGender());
                        }
                    } else {
                        mSongListInterface.setSelectedIndex(listMusicData.getGenderSongs(listMusicData.getSelectedGender()).length-1);
                        mSongListInterface.ensureIndexIsVisible(mSongListInterface.getSelectedIndex());
                    }
                } else {
                    mSongListInterface.setSelectedIndex(mSongListInterface.getSelectedIndex()+20);
                    mSongListInterface.ensureIndexIsVisible(mSongListInterface.getSelectedIndex());
                }
            } else if (evento.getKeyCode() == keyUpGenre && (credits > 0 || !lockScreen)) {
                if (isFullScreen) {
                    setFullScreen();
                }

                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
                if (listMusicData.upGender()) {
                    labelPromotions.setVisible(false);
                    mSongListInterface.setListData(listMusicData.getGenderSongs(listMusicData.getSelectedGender()));
                    mSongListInterface.setSelectedIndex(0);
                    mSongListInterface.ensureIndexIsVisible(0);
                    labelMusicalGenre.setText("Genero Musical: " + listMusicData.getNameOfGender());
                }
            } else if (evento.getKeyCode() == keyDownGenre && (credits > 0 || !lockScreen)) {
                if (isFullScreen) {
                    setFullScreen();
                }

                mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));

                if (listMusicData.downGender()) {
                    labelPromotions.setVisible(false);
                    mSongListInterface.setListData(listMusicData.getGenderSongs(listMusicData.getSelectedGender()));
                    mSongListInterface.setSelectedIndex(0);
                    mSongListInterface.ensureIndexIsVisible(0);
                    labelMusicalGenre.setText("Genero Musical: " + listMusicData.getNameOfGender());
                }
            } else if (evento.getKeyCode()==keyNextSong && mPlayList.songToPlay()!=null) {
                if (mMediaPlayer.embeddedMediaPlayerMp3.isPlaying()) {
                    mMediaPlayer.embeddedMediaPlayerMp3.stop();
                } else {
                    mMediaPlayer.embeddedMediaPlayer.stop();
                }
            }
            else if (evento.getKeyCode()==keyAddCredit) {
                credits = credits + amountOfCredits;
                labelCredits.setText(String.format("Creditos: %d", credits));
                updateCreditsSettings();
                labelCredits.setForeground(Color.WHITE);
                if (isFullScreen) {
                    setFullScreen();
                }

                entregarPremiosYCreditosAdicionales();
            } else if (evento.getKeyCode()==keyDeleteCredit && credits > 0) {
                --credits;
                mUserSettings.writeSetting(true,new KeyPairValue(KEY_SAVED_CREDITS,credits));
                labelCredits.setText(String.format("Creditos: %d", credits));
                if (credits == 0 && !free) {
                    timerFullScreen.restart();
                }
            }
        }

        private void updateDataBase(Song cancionAReproducir) {
            try {
                String consulta = "SELECT * FROM most_popular WHERE number = " + cancionAReproducir.getSongNumber();

                ResultSet resultSet = consultor.query(consulta);

                if (resultSet.isClosed()) {
                    resultSet.close();

                    String insertar = "INSERT INTO most_popular(number, name, artist, genre, times, last_date)" +
                            " VALUES ("+cancionAReproducir.getSongNumber()+",'" +
                            cancionAReproducir.getSongName()+ "','" +
                            cancionAReproducir.getSinger() + "','" +
                            cancionAReproducir.getSongGenre() + "'," +
                            1 + ", "+ new Date().getTime() +");";

                    consultor.insert(insertar);
                } else {

                    int times = 0;
                    while (resultSet.next()) {
                        times = resultSet.getInt("times") + 1;
                    }

                    resultSet.close();

                    consultor.update("UPDATE most_popular SET times = " + times + ", last_date = " + new Date().getTime() + " WHERE number = " + cancionAReproducir.getSongNumber());

                    consultor.closeConnection();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // ==================================================================
    // Compatibilidad
    // ==================================================================
    public void addSongToPlayList(ArrayList numbers) {
        if (numbers.size()>0) {
            for (int i = 0; i < numbers.size(); i++) {
                Song cancionAReproducir = listMusicData.getSong((int) numbers.get(i));
                mPlayList.addSong(cancionAReproducir);
            }
        }
        playListInterface.setListData(mPlayList.getPlayList());

        if (estadoActual == EstadoSistema.REPOSO && mPlayList.songToPlay() != null) {
            timerRandomSong.stop();
            detenerReproductores();
            estadoActual = EstadoSistema.COLA;
            procesarSiguienteEnCola();
        }
    }
}