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
 * This class is the center of MFRockola. From this class we instantiate all the others
 * and it is what we see from the screen
 */
class Interface extends JFrame {

    private boolean selectVIP;
    private int selectionVIP = -1;
    private int randomSong;
    private int resetSongs;

    private int amountOfCredits;
    private boolean free;
    private boolean lockScreen;
    private boolean enableVip;
    private boolean modeGenreList;
    private int fontSelectorSize;

    private String pathSongs;
    private String pathVideosMP3;
    private String pathVLC;

    private int keyUpList;
    private int keyDownList;
    private int keyUpGenre;
    private int keyDownGenre;
    private int keyUpListModeSingers;
    private int keyDownListModeSingers;
    private int keyLeftCoversModeSingers;
    private int keyRightCoversModeSingers;
    private int keyPlaySongModeSingers;
    private int keyFullScreen;
    private int keyDeleteNumber;
    private int keyNextSong;
    private int keyAddCredit;
    private int keyDeleteCredit;
    private int clickOfCredits;
    private boolean rightClickCancelMusic;
    private String password;
    private int keyUpVolume;
    private int keyDownVolume;
    private int initialVolume;

    private boolean defaultInterface;
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

    // This variable stores the amount of credits entered
    private int credits;

    // This pair of variables store the size of the screen to draw the interface
    private int widthScreen;
    private int heightScreen;

    // List of songs that are blocked
    private BlockedSongs mBlockedSongs;

    // Instance that stores the playlist
    private PlayList mPlayList = new PlayList(); // Objeto de las musicas en reproduccion.

    // Boolean that determines whether the screen is complete
    private boolean isFullScreen = false;

    // Variable that stores the interface background
    private BackgroundImagePanel mBackgroundImagePanel;

    // Panel containing the video surface
    private JPanel videoPanel;
    private JPanel panel;
    private VipSelector mPanelVipSelector;
    private AlphabetSelector mAlphabetSelector;
    private PlaySelector mPlaySelector;

    // Panel containing the bottom interface information
    private JPanel bottomPanel;

    // Dimension object that contains the width and height of the screen
    private Dimension resolution;

    // ListMusic object that keeps the list of songs
    private ListMusic listMusicData;

    // JList object containing in the interface all available videos in the configured directory
    private JList mSongListInterface;
    // JList object containing all the songs in playback in the interface
    private JList playListInterface;

    // JLabels of the interface
    private JLabel labelMusicalGenre;
    private JLabel labelSongPlayingBottom;
    private JLabel labelSongPlayingRight;
    private JLabel labelCredits;
    private JLabel labelPromotions;
    private JList listVIP;

    // MediaPlayer object that has the video player
    private MediaPlayer mMediaPlayer;

    // SongSelector object used to select songs
    private SongSelector mSongSelector;

    // cancel song flag
    private boolean cancelSong;

    // counter cancel click flag
    private int counterClick;

    // when this integer reached the user settings, MFRockola give a prize
    private int insertedCreditsForAditionalCredits;

    // when this integer reached the user settings, MFRockola give a prize
    private int insertedCreditsForPrize;

    // Bars for the list of songs
    private JScrollPane mScrollPane;
    private JScrollPane mScrollpaneVIP;

    private JScrollPane mSingerPane;
    private SingerList mSingerList;

    // This timer controls the text that shows the credit tag
    private Timer timerChangerLabelCredits;

    // this timer controls the automatic fullscreen
    private Timer timerFullScreen;

    // this timer controls the automatic fullscreen
    private Timer timerAlphabet;

    // This timer controls the error that sometimes occurs when the video screen is complete
    private Timer timer;

    // This timer controls the playback of a random song when there are no credits
    private Timer timerRandomSong;

    private KeyboardManager mKeyboardManager;

    // keep the promotional sound
    private Clip promotionalSound;

    Interface() {
        try {
            mUserSettings = new SettingsManager();

            // init vars

            randomSong = (int) mUserSettings.getSetting(KEY_RANDOM_SONG);
            resetSongs = (int) mUserSettings.getSetting(KEY_RESET_SONGS);

            amountOfCredits = (int) mUserSettings.getSetting(KEY_AMOUNT_OF_CREDITS);
            free = (boolean) mUserSettings.getSetting(KEY_FREE);
            lockScreen = (boolean) mUserSettings.getSetting(KEY_LOCK_SCREEN);
            enableVip = (boolean) mUserSettings.getSetting(KEY_ENABLE_VIP);
            fontSelectorSize = (int) mUserSettings.getSetting(KEY_FONT_SELECTOR_SIZE);

            pathSongs = (String) mUserSettings.getSetting(KEY_PATH_SONGS);
            pathVideosMP3 = (String) mUserSettings.getSetting(KEY_PATH_VIDEOS_MP3);
            pathVLC = (String) mUserSettings.getSetting(KEY_PATH_VLC);

            keyUpList = (int) mUserSettings.getSetting(KEY_UP_LIST);
            keyDownList = (int) mUserSettings.getSetting(KEY_DOWN_LIST);
            keyUpGenre = (int) mUserSettings.getSetting(KEY_UP_GENRE);
            keyDownGenre = (int) mUserSettings.getSetting(KEY_DOWN_GENRE);
            keyUpListModeSingers = (int) mUserSettings.getSetting(KEY_UP_LIST_MODE_SINGERS);
            keyDownListModeSingers = (int) mUserSettings.getSetting(KEY_DOWN_LIST_MODE_SINGERS);
            keyLeftCoversModeSingers = (int) mUserSettings.getSetting(KEY_LEFT_COVERS_MODE_SINGERS);
            keyRightCoversModeSingers = (int) mUserSettings.getSetting(KEY_RIGHT_COVERS_MODE_SINGERS);
            keyPlaySongModeSingers = (int) mUserSettings.getSetting(KEY_PLAY_SONG_MODE_SINGERS);
            keyFullScreen = (int) mUserSettings.getSetting(KEY_FULL_SCREEN);
            keyDeleteNumber = (int) mUserSettings.getSetting(KEY_DELETE_NUMBER);
            keyNextSong = (int) mUserSettings.getSetting(KEY_NEXT_SONG);
            keyAddCredit = (int) mUserSettings.getSetting(KEY_ADD_CREDIT);
            keyDeleteCredit = (int) mUserSettings.getSetting(KEY_DELETE_CREDIT);
            clickOfCredits = (int) mUserSettings.getSetting(KEY_CLICK_OF_CREDITS);
            rightClickCancelMusic = (boolean) mUserSettings.getSetting(KEY_RIGHT_CLICK_CANCEL_MUSIC);
            password = (String) mUserSettings.getSetting(KEY_PASSWORD);
            keyUpVolume = (int) mUserSettings.getSetting(KEY_UP_VOLUME);
            keyDownVolume = (int) mUserSettings.getSetting(KEY_DOWN_VOLUME);

            try {
                int initial = Integer.parseInt((String) mUserSettings.getSetting(KEY_INITIAL_VOLUME));
                if (initial < 0 || initial > 100) initial = 50;
                initialVolume = initial;
            } catch (NumberFormatException e) {
                initialVolume = 50;
            }

            defaultInterface = false;
            defaultBackground = (boolean) mUserSettings.getSetting(KEY_DEFAULT_BACKGROUND);
            pathBackground = (String) mUserSettings.getSetting(KEY_PATH_BACKGRONUD);
            modeGenreList = (boolean) mUserSettings.getSetting(KEY_MODE_GENRE_LIST);
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
                                songJSON.getString(KEY_SONG_NAME), modeGenreList),
                                Song.NORMAL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                savedSongs.remove(0);
                mUserSettings.writeSetting(true,new KeyPairValue(KEY_SAVED_SONGS,savedSongs));
            }

            // End of init vars

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
                JOptionPane.showMessageDialog(
                        null,
                        "VLC no se encuentra instalado o el directorio no se encuentra.",
                        "Error de VLC",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }

            file = new File(pathSongs);

            if (!file.exists()) {
                JOptionPane.showMessageDialog(
                        null,
                        "El directorio de musicas no se encuentra, verifiquelo e intente nuevamente.",
                        "Error de Directorios",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }

            file = new File(pathVideosMP3);

            if (!file.exists()) {
                JOptionPane.showMessageDialog(
                        null,
                        "El directorio de los videos predeterminados no se encuentra, verifiquelo e intente nuevamente.",
                        "Error de Directorios",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }
        catch (NullPointerException excepcion) {
            excepcion.printStackTrace();
            new SettingsWindow();
        }

        if (defaultInterface) {
            initComponents();
        } else {
            initOptionalInterface();
        }

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

        ActionListener showAlphabet = e -> {
            if (!mPlaySelector.isVisible() && !mPanelVipSelector.isVisible()) {
                mAlphabetSelector.setVisible(true);
            }
        };

        ActionListener play = e -> {
            playRandomSong();
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

        timerAlphabet = new Timer(1000, showAlphabet);
        timerAlphabet.setRepeats(false);

        timerRandomSong = new Timer(randomSong*1000*60,play);
        timerRandomSong.setRepeats(false);

        getContentPane().add(mBackgroundImagePanel);

        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(resolution);
        setVisible(true);

        MediaPlayerManager sMediaPlayerManager = new MediaPlayerManager();

        mMediaPlayer.embeddedMediaPlayer.addMediaPlayerEventListener(sMediaPlayerManager);
        mMediaPlayer.embeddedMediaPlayerMp3.addMediaPlayerEventListener(sMediaPlayerManager);
        mMediaPlayer.setVolume(initialVolume);
        mMediaPlayer.initVolume(initialVolume);

        if (mPlayList.songToPlay()==null) {
            if (randomSong == 0) {
                playRandomSong();
            } else {
                if (randomSong != 0) {
                    timerRandomSong.start();
                }
            }
        }

        if (mPlayList.songToPlay()!=null) {
            int extension = Utils.getExtension(String.format("%s\\%s\\%s\\%s", pathSongs,mPlayList.getSongGender(),mPlayList.getSinger(), mPlayList.songToPlay()));

            if (extension == EXT_MP4 || extension == EXT_AVI || extension == EXT_MPG || extension == EXT_FLV || extension == EXT_MKV) {
                mMediaPlayer.playVideo(mPlayList.getSongGender(),mPlayList.getSinger(),mPlayList.songToPlay());
            } else if (extension == EXT_MP3 || extension == EXT_WMA || extension == EXT_WAV || extension == EXT_AAC) {
                mMediaPlayer.playAudio(
                        mPlayList.getSongGender(),
                        mPlayList.getSinger(),
                        mPlayList.songToPlay(),
                        pathVideosMP3 + "\\" + listMusicData.getPromVideo());
            }

            if (modeGenreList) {
                labelSongPlayingBottom.setText(String.format("%05d - %s - %s - %s",
                        mPlayList.getSongNumber(),
                        mPlayList.getSongGender(),
                        mPlayList.getSinger(),
                        mPlayList.songToPlay()));
                labelSongPlayingRight.setText(String.format("%05d - %s - %s - %s",
                        mPlayList.getSongNumber(),mPlayList.getSongGender(),
                        mPlayList.getSinger(), mPlayList.songToPlay()));
            } else {
                labelSongPlayingBottom.setText(String.format("%s - %s",
                        mPlayList.getSinger(),
                        mPlayList.songToPlay()));
                labelSongPlayingRight.setText(String.format("%s - %s",
                        mPlayList.getSinger(), mPlayList.songToPlay()));
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e)
            {

                //  isMetaDown = rigth CLick

                // Cuando se presiona click izquierdo y las canciones no se pueden cancelar

                if(clickOfCredits==0 && !e.isMetaDown() && !free) {
                    credits = credits + amountOfCredits;
                    labelCredits.setText(String.format("Creditos: %d", credits));
                    updateCreditsSettings();
                    labelCredits.setForeground(Color.WHITE);
                    if (isFullScreen) {
                        setFullScreen();
                        if (!lockScreen) timerFullScreen.restart();
                    }

                    entregarPremiosYCreditosAdicionales();

                    // Click derecho y las canciones se pueden eliminar

                } else if (cancelSong && e.isMetaDown() && mPlayList.songToPlay()!=null) {

                    if (isFullScreen) {
                        setFullScreen();
                        if (!lockScreen) timerFullScreen.restart();
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

                        if (optionPane.getValue()!=null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
                            if (new String(passwordPanel.getPassword()).equals(password)) {
                                if (mMediaPlayer.embeddedMediaPlayerMp3.isPlaying()) {
                                    mMediaPlayer.embeddedMediaPlayerMp3.stop();
                                } else {
                                    mMediaPlayer.embeddedMediaPlayer.stop();
                                }
                                dlg.dispatchEvent(new WindowEvent(dlg, WindowEvent.WINDOW_CLOSING));
                                dlg.dispose(); // else java VM will wait for dialog to be disposed of (forever)
                            }
                        } else {
                            dlg.dispatchEvent(new WindowEvent(dlg, WindowEvent.WINDOW_CLOSING));
                            dlg.dispose(); // else java VM will wait for dialog to be disposed of (forever)
                        }

                        counterClick = 0;
                    }

                    // click derecho y las canciones no se pueden eliminar

                } else if (e.isMetaDown() && (int) mUserSettings.getSetting(KEY_CLICK_OF_CREDITS) == 1 && !free && !cancelSong) {

                    credits = credits + amountOfCredits;
                    labelCredits.setText(String.format("Creditos: %d", credits));
                    updateCreditsSettings();
                    labelCredits.setForeground(Color.WHITE);
                    if (isFullScreen) {
                        setFullScreen();
                        if (!lockScreen) timerFullScreen.restart();
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

        // Iniciar los labels
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

        // Iniciar las listas

        listMusicData = new ListMusic(modeGenreList,pathSongs,pathVideosMP3); //Aqui falta la direccion de los videos promocionales

        // Iniciar los panel

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
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

        mainPanel.add(playListInterface);
    }

    private void initOptionalInterface() {

        credits = savedCredits;

        resolution = Toolkit.getDefaultToolkit().getScreenSize();

        widthScreen = (int) resolution.getWidth();
        heightScreen = (int) (resolution.getHeight() - 54);

        setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/com/mfrockola/imagenes/icono.png")));

        // Iniciar los labels

        labelMusicalGenre = new JLabel();
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

        mPanelVipSelector = new VipSelector();
        mPanelVipSelector.setBounds((widthScreen/2)-200,(heightScreen/2)-90,400,180);
        mPanelVipSelector.setVisible(false);

        mAlphabetSelector = new AlphabetSelector();
        mAlphabetSelector.setBounds((widthScreen/2)-500,(heightScreen/2)-40,1000,80);
        mAlphabetSelector.setVisible(false);

        mPlaySelector = new PlaySelector();
        mPlaySelector.setBounds((widthScreen/2)-200,(heightScreen/2)-50,400,100);
        mPlaySelector.setVisible(false);

        // Iniciar las listas

        listMusicData = new ListMusic(modeGenreList,pathSongs,pathVideosMP3); //Aqui falta la direccion de los videos promocionales

        mSongListInterface = new JList();
        mSongListInterface.setCellRenderer(new RowRenderer(new Font(fontCells,
                fontCellsBold,fontCellsSize),fontCellsColor,
                color1, color2));
        mSongListInterface.setListData(listMusicData.getGenderSongs(0));
        if (modeGenreList) {
            mSongListInterface.setListData(listMusicData.getGenderSongs(0));
        } else {
            mSongListInterface.setListData(listMusicData.getSingerSongs());
        }
        mSongListInterface.addKeyListener(mKeyboardManager);
        mSongListInterface.setVisibleRowCount(20);
        mSongListInterface.setFocusable(false);
        mSongListInterface.setMaximumSize(getMaximumSize());
        mSongListInterface.setSelectedIndex(0);
        mScrollPane = new JScrollPane(mSongListInterface);

        if (modeGenreList) {
            mSingerList = new SingerList((int)(widthScreen/1.7603), (int) (heightScreen/4.5), listMusicData.getSingers());
            mSingerList.setBounds((int)(widthScreen/45.533), (int)(heightScreen/13.84),(int)(widthScreen/1.7603), (int)(heightScreen/4.5));
            mScrollPane.setBounds((int)(widthScreen/45.533), (int)(heightScreen/13.84),(int)(widthScreen/1.7603), (int)(heightScreen/1.09));
        } else {
            mSingerList = new SingerList((int)(widthScreen/1.7603), (int) (heightScreen/4.5), listMusicData.getSingers());
            mSingerList.setBounds((int)(widthScreen/45.533), (int)(heightScreen/13.84),(int)(widthScreen/1.7603), (int)(heightScreen/4.5));
            mScrollPane.setBounds((int)(widthScreen/45.533), (int)(heightScreen/3.3),(int)(widthScreen/1.7603), (int)(heightScreen/1.43));
        }

        mScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        playListInterface = new JList();
        playListInterface.setListData(mPlayList.getPlayList());
        playListInterface.setCellRenderer(new RowRenderer(new Font(fontCells,
                fontCellsBold, fontCellsSize),fontCellsColor,
                color1, color2));
        playListInterface.setBounds((int)(widthScreen/1.633), (int)(heightScreen/1.52), (int)(widthScreen/2.732), (int)(heightScreen/3.051));
//        listaDeReproduccion.setBounds(ancho - 530, alto - 260, 500, alto-461);
        playListInterface.setFocusable(false);

        if (modeGenreList) {
            labelMusicalGenre.setText("Genero Musical: "+ listMusicData.getNameOfGender());
        } else {
            labelMusicalGenre.setText("Album: " + listMusicData.getNameOfSinger());
        }

        // Iniciar los panel

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        mainPanel.add(labelPromotions);
        mainPanel.add(mPanelVipSelector);
        mainPanel.add(mAlphabetSelector);
        mainPanel.add(mPlaySelector);
        mainPanel.add(mScrollPane);

        mainPanel.add(labelMusicalGenre);

        if (!modeGenreList)
            mainPanel.add(mSingerList);

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

    private void setFullScreen()
    {
        if (!isFullScreen)
        {
            if (!modeGenreList) {
                mPlaySelector.setVisible(false);
                mAlphabetSelector.setVisible(false);
                mSingerList.setVisible(false);
            }

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
            if (!modeGenreList)
                mSingerList.setVisible(true);

            mScrollPane.setVisible(true);
            mSongListInterface.setVisible(true);
            panel.setVisible(true);
            labelMusicalGenre.setVisible(true);
            isFullScreen = false;
        }
    }

    private void entregarPremiosYCreditosAdicionales() {
        // creditosInsertados es la variable de control de los clicks

        if (addAditionalCredit && !free) {
            insertedCreditsForAditionalCredits++;
            if (insertedCreditsForAditionalCredits >= everyAmountOfCredit) {
                // Aqui va la cuestion para controlar el cartel de los creditos adicionales
                insertedCreditsForAditionalCredits = 0;
                credits = credits + numberAditionalCredits;
                labelCredits.setText(String.format("Creditos: %d", credits));
                labelPromotions.setText(String.format("Ganaste %s creditos adicionales",
                        numberAditionalCredits));
                labelPromotions.setVisible(true);
            }
        }

        if (awardPrize && !free) {
            insertedCreditsForPrize++;
            mUserSettings.writeSetting(true,new KeyPairValue(KEY_INSERTED_CREDITS_FOR_PRIZE,insertedCreditsForPrize));
            if (insertedCreditsForPrize % creditsForPrize == 0) {
                // Aqui va la cuestion para controlar el cartel de premio
                labelPromotions.setText(String.format("Ganaste %s %s!", prizeAmount,typeOfPrize));
                labelPromotions.setVisible(true);
                playSound();
            }
        }
    }

    private class KeyboardManager extends KeyAdapter
    {
        SQLiteConsultor consultor = new SQLiteConsultor();

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == keyLeftCoversModeSingers && !modeGenreList && !selectVIP) {
                timerAlphabet.stop();
            }

            if (e.getKeyCode() == keyRightCoversModeSingers && !modeGenreList && !selectVIP) {
                timerAlphabet.stop();
            }
        }

        public void keyPressed(KeyEvent evento)
        {
            // tecla bloque numerico = 144
            if (evento.getKeyCode()==KeyEvent.VK_NUM_LOCK) {
                Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK,true);
            }

            if (evento.getKeyCode()==keyUpVolume) {
                mMediaPlayer.upVolume();
                labelCredits.setText("Volumen " + mMediaPlayer.getVolume());
                timerChangerLabelCredits.restart();
            }

            if (evento.getKeyCode()==keyDownVolume) {
                mMediaPlayer.downVolume();
                labelCredits.setText("Volumen " + mMediaPlayer.getVolume());
                timerChangerLabelCredits.restart();
            }

            if (!lockScreen) {
                timerFullScreen.restart();
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

            if (evento.getKeyCode()== keyPlaySongModeSingers && !modeGenreList) {
                if (isFullScreen) {
                    setFullScreen();
                } else if (!mPlaySelector.isVisible() && credits > 0 && !mAlphabetSelector.isVisible()) {
                    mPlaySelector.setInitialSelection();
                    mPlaySelector.setVisible(true);
                } else if (mAlphabetSelector.isVisible()) {
                    mSingerList.findAlbum(mAlphabetSelector.getSelection());
                    mAlphabetSelector.setVisible(false);
                    listMusicData.setSelectedSinger(mSingerList.getSelectedSinger());
                    labelMusicalGenre.setText("Album: " + listMusicData.getNameOfSinger());
                    mSongListInterface.setListData(listMusicData.getSingerSongs());
                    mSongListInterface.setSelectedIndex(0);
                    mSongListInterface.ensureIndexIsVisible(0);
                } else if (mPlaySelector.isVisible()) {
                    if (mPlaySelector.getSelection() == 0) {
                        Song song = (Song) mSongListInterface.getSelectedValue();
                        playOneSong(song.getSongNumber());
                    }
                    mPlaySelector.setVisible(false);
                } else  if (mPanelVipSelector.isVisible()) {
                    switch (mPanelVipSelector.getSelection()) {
                        case 0: {
                            selectionVIP = Song.NORMAL;
                            break;
                        }
                        case 1: {
                            selectionVIP = Song.VIP;
                            break;
                        }
                        case 2: {
                            selectionVIP = Song.SUPER_VIP;
                            break;
                        }
                    }

                    mPanelVipSelector.setInitialSelection();
                    mPanelVipSelector.setVisible(false);
                    Song song = (Song) mSongListInterface.getSelectedValue();
                    playOneSong(song.getSongNumber());
                    selectVIP = false;
                } else if (credits > 1 && enableVip) {
                    enableVIP();
                } else if (credits > 0) {
                    Song song = (Song) mSongListInterface.getSelectedValue();
                    playOneSong(song.getSongNumber());
                }
            }

            if (((evento.getKeyCode() > 47 && evento.getKeyCode() < 58) || (evento.getKeyCode() > 95 && evento.getKeyCode() < 106)) && (credits > 0 || !lockScreen) && modeGenreList) {
                if (selectVIP) {
                    checkVIP(evento);
                } else {
                    mSongSelector.labelSelector.setText(mSongSelector.keyEventHandler(evento));
                    if (credits>1 && enableVip && mSongSelector.play && modeGenreList) {
                        enableVIP();
                    } else if (mSongSelector.play) {
                        int numero = Integer.parseInt(String.format("%s%s%s%s%s", mSongSelector.values[0], mSongSelector.values[1],
                            mSongSelector.values[2], mSongSelector.values[3], mSongSelector.values[4]));

                        playOneSong(numero);
                    }
                }
            }

            if (evento.getKeyCode()==122) {
                String contrasenia = JOptionPane.showInputDialog("Introduzca la clave");
                if (contrasenia.equals("12345")) {
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
            } else if (evento.getKeyCode()==keyDownList && (credits > 0 || !lockScreen) && modeGenreList) {
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
            } else if(evento.getKeyCode()==keyUpListModeSingers && (credits > 0 || !lockScreen) && !modeGenreList) {
                if (isFullScreen) {
                    setFullScreen();
                }

                if (mAlphabetSelector.isVisible()) {
                    evento.consume();
                } else if (mPanelVipSelector.isVisible()) {
                    mPanelVipSelector.setSelection(VipSelector.KEY_UP);
                } else if (mPlaySelector.isVisible()) {
                    mPlaySelector.setSelection(PlaySelector.KEY_UP);
                } else if(mSongListInterface.getSelectedIndex() > 0 && !selectVIP) {
                    mSongListInterface.setSelectedIndex(mSongListInterface.getSelectedIndex()-1);
                    mSongListInterface.ensureIndexIsVisible(mSongListInterface.getSelectedIndex());
                } else {
                    evento.consume();
                }
            } else if(evento.getKeyCode()==keyDownListModeSingers && (credits > 0 || !lockScreen) && !modeGenreList) {
                if (isFullScreen) {
                    setFullScreen();
                }

                if (mAlphabetSelector.isVisible()) {
                    evento.consume();
                } else if (mPanelVipSelector.isVisible()) {
                    mPanelVipSelector.setSelection(VipSelector.KEY_DOWN);
                } else if (mPlaySelector.isVisible()) {
                    mPlaySelector.setSelection(PlaySelector.KEY_DOWN);
                } else if(mSongListInterface.getSelectedIndex()+1 >= listMusicData.getSingerSongs().length && !selectVIP) {
                    evento.consume();
                } else {
                    mSongListInterface.setSelectedIndex(mSongListInterface.getSelectedIndex()+1);
                    mSongListInterface.ensureIndexIsVisible(mSongListInterface.getSelectedIndex());
                }
            } else if (evento.getKeyCode()==keyUpList && (credits > 0 || !lockScreen) && modeGenreList) {
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
            } else if (evento.getKeyCode() == keyUpGenre && (credits > 0 || !lockScreen) && modeGenreList) {
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
            // Empieza la rutina para cambiar de album hacia la izquierda
            } else if (evento.getKeyCode() == keyLeftCoversModeSingers && (credits > 0 || !lockScreen) && !modeGenreList && !selectVIP) {
                if (!timerAlphabet.isRunning()) {
                    timerAlphabet.restart();
                }

                if (isFullScreen) {
                    setFullScreen();
                }

                if (mPlaySelector.isVisible()) {
                    evento.consume();
                } else if (mAlphabetSelector.isVisible()) {
                    mAlphabetSelector.setSelection(AlphabetSelector.KEY_LEFT);
                    evento.consume();
                } else {
                    mSingerList.setSelectedSinger(SingerList.MOVE_TO_LEFT);
                    listMusicData.setSelectedSinger(mSingerList.getSelectedSinger());
                    labelMusicalGenre.setText("Album: " + listMusicData.getNameOfSinger());
                    mSongListInterface.setListData(listMusicData.getSingerSongs());
                    mSongListInterface.setSelectedIndex(0);
                    mSongListInterface.ensureIndexIsVisible(0);
                }
            // Empieza la rutina para cambiar de album hacia la derecha
            } else if (evento.getKeyCode() == keyRightCoversModeSingers && (credits > 0 || !lockScreen) && !modeGenreList && !selectVIP) {
                if (!timerAlphabet.isRunning()) {
                    timerAlphabet.restart();
                }

                if (isFullScreen) {
                    setFullScreen();
                }

                if (mPlaySelector.isVisible()) {
                    evento.consume();
                } else if (mAlphabetSelector.isVisible()) {
                    mAlphabetSelector.setSelection(AlphabetSelector.KEY_RIGHT);
                    evento.consume();
                } else {
                    mSingerList.setSelectedSinger(SingerList.MOVE_TO_RIGHT);
                    listMusicData.setSelectedSinger(mSingerList.getSelectedSinger());
                    labelMusicalGenre.setText("Album: " + listMusicData.getNameOfSinger());
                    mSongListInterface.setListData(listMusicData.getSingerSongs());
                    mSongListInterface.setSelectedIndex(0);
                    mSongListInterface.ensureIndexIsVisible(0);
                }
            } else if (evento.getKeyCode() == keyDownGenre && (credits > 0 || !lockScreen) && modeGenreList) {
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

        public void checkVIP (KeyEvent evento) {
            if (((evento.getKeyCode() > 48 && evento.getKeyCode() < 52) || (evento.getKeyCode() > 96 && evento.getKeyCode() < 100))) {
                selectVIP = false;

                switch (evento.getKeyCode()) {
                    case 49: {
                        selectionVIP = Song.NORMAL;
                        break;
                    }
                    case 50: {
                        selectionVIP = Song.VIP;
                        break;
                    }
                    case 51: {
                        if (credits > 2) selectionVIP = Song.SUPER_VIP;
                        break;
                    }
                    case 97: {
                        selectionVIP = Song.NORMAL;
                        break;
                    }
                    case 98: {
                        selectionVIP = Song.VIP;
                        break;
                    }
                    case 99: {
                        if (credits > 2) selectionVIP = Song.SUPER_VIP;
                        break;
                    }
                    default: {
                        selectionVIP = Song.UNSELECTED_VIP;
                    }
                }

                if (!modeGenreList) {
                    Song song = (Song) mSongListInterface.getSelectedValue();
                    playOneSong(song.getSongNumber());
                } else {
                    int numero = Integer.parseInt(String.format("%s%s%s%s%s", mSongSelector.values[0], mSongSelector.values[1], mSongSelector.values[2], mSongSelector.values[3], mSongSelector.values[4]));
                    playOneSong(numero);
                }

                labelPromotions.setVisible(false);
            } else {
                selectionVIP = Song.UNSELECTED_VIP;
            }
        }

        public void playOneSong(int number) {
            boolean condicion;

            if (mBlockedSongs.checkBlockedSongs(number))
                condicion = true;
            else
                condicion = false;

            if (number >= listMusicData.getSizeListOfSongs()) {
                labelCredits.setText("Canción no encontrada");
                mSongSelector.play = false;
                mSongSelector.resetValues();
                mSongSelector.labelSelector.setText("- - - - -");
                timerChangerLabelCredits.start();
            } else {
                if ((credits > 0 && condicion) || (free && condicion)) {
                    consumirCancion(number);
                } else {
                    labelCredits.setForeground(Color.RED);
                    labelCredits.setText("La canción que ha seleccionado no se puede reproducir antes de " + resetSongs + " mins");
                    timerChangerLabelCredits.start();
                    mSongSelector.play = false;
                    mSongSelector.resetValues();
                    mSongSelector.labelSelector.setText("- - - - -");
                    selectionVIP = -1;
                }
            }
        }

        private void enableVIP() {
            selectVIP = true;
            if (credits == 2) {
                labelPromotions.setText("Presione 1 Normal, 2 VIP");
            } else {
                labelPromotions.setText("Presione 1 Normal, 2 VIP, 3 Super VIP");
            }

            if (modeGenreList) {
                labelPromotions.setVisible(true);
            } else {
                mPanelVipSelector.setLabelsEnabled(credits);
                mPanelVipSelector.setVisible(true);
            }
        }

        private void consumirCancion(int numero) {
            if (mPlayList.songToPlay()==null)
            {
                if (timerRandomSong.isRunning()) {
                    timerRandomSong.stop();
                }

                Song cancionAReproducir =  listMusicData.getSong(numero);

                mPlayList.addSong(cancionAReproducir, selectionVIP);

                int extension = Utils.getExtension(String.format("%s\\%s\\%s\\%s", pathSongs,mPlayList.getSongGender(),mPlayList.getSinger(), mPlayList.songToPlay()));

                if (extension == EXT_MP4 || extension == EXT_AVI || extension == EXT_MPG || extension == EXT_FLV || extension == EXT_MKV) {
                    mMediaPlayer.playVideo(mPlayList.getSongGender(),mPlayList.getSinger(),mPlayList.songToPlay());
                } else if (extension == EXT_MP3 || extension == EXT_WMA || extension == EXT_WAV || extension == EXT_AAC) {
                    mMediaPlayer.playAudio(
                            mPlayList.getSongGender(),
                            mPlayList.getSinger(),
                            mPlayList.songToPlay(),
                            pathVideosMP3 + "\\" + listMusicData.getPromVideo());
                }

                playListInterface.setListData(mPlayList.getPlayList());
                if (!free) {
                    cobrarCreditos();
                    mUserSettings.writeSetting(true,new KeyPairValue(KEY_SAVED_CREDITS,credits));
                    labelCredits.setText(String.format("%s: %d","Creditos",credits));
                }
                selectionVIP = Song.NORMAL;
                mSongSelector.play = false;
                mSongSelector.counterValue = 0;
                mSongSelector.resetValues();
                mSongSelector.labelSelector.setText("- - - - -");
                mBlockedSongs.blockSong(numero);
                if (modeGenreList) {
                    labelSongPlayingBottom.setText(String.format("%05d - %s - %s - %s",
                            mPlayList.getSongNumber(),
                            mPlayList.getSongGender(),
                            mPlayList.getSinger(),
                            mPlayList.songToPlay()));
                    labelSongPlayingRight.setText(String.format("%05d - %s - %s - %s",
                            mPlayList.getSongNumber(),mPlayList.getSongGender(),
                            mPlayList.getSinger(), mPlayList.songToPlay()));

                } else {
                    labelSongPlayingBottom.setText(String.format("%s - %s",
                            mPlayList.getSinger(),
                            mPlayList.songToPlay()));
                    labelSongPlayingRight.setText(String.format("%s - %s",
                            mPlayList.getSinger(), mPlayList.songToPlay()));

                }
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
                Song cancionAReproducir = listMusicData.getSong(numero);
                //Song cancion = new Song(numero, cancionAReproducir);
                mPlayList.addSong(cancionAReproducir, selectionVIP);
                JSONObject jsonSong = mPlayList.getSongJSONObject(cancionAReproducir);
                savedSongs.put(jsonSong);
                mUserSettings.writeSetting(false,new KeyPairValue(KEY_SAVED_SONGS,savedSongs));
                playListInterface.setListData(mPlayList.getPlayList());
                if (!free) {
                    cobrarCreditos();
                    mUserSettings.writeSetting(true,new KeyPairValue(KEY_SAVED_CREDITS,credits));
                    labelCredits.setForeground(Color.WHITE);
                    labelCredits.setText(String.format("%s: %d","Creditos",credits));
                }
                selectionVIP = Song.NORMAL;
                mSongSelector.play = false;
                mSongSelector.counterValue = 0;
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
        }

        private void cobrarCreditos() {
            switch (selectionVIP) {
                case 2: {
                    credits -= 2;
                    break;
                }
                case 3: {
                    credits -= 3;
                    break;
                }
                default: {
                    credits--;
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

    private class MediaPlayerManager extends MediaPlayerEventAdapter
    {
        @Override
        public void stopped(uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer) {
            if (mPlayList.songToPlay()!=null) {
                nextSong();
            }
        }

        public void finished(uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer) {
            if (mMediaPlayer.embeddedMediaPlayerMp3.isPlaying()) {
                String path = pathVideosMP3 + "\\" + listMusicData.getPromVideo();
                File file = new File(path);
                mMediaPlayer.playVideo(file.getAbsolutePath());
            } else {
                nextSong();
            }
        }

        public void nextSong() {
            mPlayList.removeSong();
            if (savedSongs.length()>0) {
                savedSongs.remove(0);
                mUserSettings.writeSetting(true,new KeyPairValue(KEY_SAVED_SONGS,savedSongs));
            }
            playListInterface.setListData(mPlayList.getPlayList());

            if (mPlayList.songToPlay() == null) {
                if (randomSong == 0) {
                    playRandomSong();
                } else {
                    timerRandomSong.start();
                }
                labelSongPlayingBottom.setText("MFRockola");
                labelSongPlayingRight.setText("Su selección musical");
            } else {
                int extension = Utils.getExtension(String.format("%s\\%s\\%s\\%s", pathSongs,mPlayList.getSongGender(),mPlayList.getSinger(), mPlayList.songToPlay()));

                if (extension == EXT_MP4 || extension == EXT_AVI || extension == EXT_MPG || extension == EXT_FLV || extension == EXT_MKV) {
                    mMediaPlayer.playVideo(
                            mPlayList.getSongGender(),
                            mPlayList.getSinger(),
                            mPlayList.songToPlay());

                } else if (extension == EXT_MP3 || extension == EXT_WMA || extension == EXT_WAV || extension == EXT_AAC) {
                    mMediaPlayer.playAudio(
                            mPlayList.getSongGender(),
                            mPlayList.getSinger(),
                            mPlayList.songToPlay(),
                            pathVideosMP3 + "\\" + listMusicData.getPromVideo());
                }

                if (modeGenreList) {
                    labelSongPlayingBottom.setText(String.format("%05d - %s - %s - %s",
                            mPlayList.getSongNumber(),
                            mPlayList.getSongGender(),
                            mPlayList.getSinger(),
                            mPlayList.songToPlay()));
                    labelSongPlayingRight.setText(String.format("%05d - %s - %s - %s",
                            mPlayList.getSongNumber(),mPlayList.getSongGender(),
                            mPlayList.getSinger(), mPlayList.songToPlay()));
                } else {
                    labelSongPlayingBottom.setText(String.format("%s - %s",
                            mPlayList.getSinger(),
                            mPlayList.songToPlay()));

                    labelSongPlayingRight.setText(String.format("%s - %s",
                            mPlayList.getSinger(),
                            mPlayList.songToPlay()));
                }
            }
        }
    }

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

    public void playRandomSong(){
        Random random = new Random();
        if (randomSong == 0) {
            File file = new File(pathSongs + "\\Promocionales\\Promocionales");
            if (file.isDirectory()) {
                String [] list = file.list();
                if (list.length == 0) return;
                random = new Random();
                int rand = random.nextInt(list.length);
                String song = list[rand];
                mPlayList.addSong(new Song(0,"Promocionales", "Promocionales", song));

                int extension = Utils.getExtension(song);

                if (extension == EXT_MP4 || extension == EXT_AVI || extension == EXT_MPG || extension == EXT_FLV || extension == EXT_MKV) {
                    mMediaPlayer.playVideo("Promocionales", "Promocionales",mPlayList.songToPlay());
                } else if (extension == EXT_MP3 || extension == EXT_WMA || extension == EXT_WAV || extension == EXT_AAC) {
                    String promVideo = listMusicData.getPromVideo();
                    System.out.println(promVideo);
                    mMediaPlayer.playAudio(
                            mPlayList.getSongGender(),
                            mPlayList.getSinger(),
                            mPlayList.songToPlay(),
                            pathVideosMP3 + "\\" + promVideo);
                }

                playListInterface.setListData(mPlayList.getPlayList());
                labelSongPlayingBottom.setText(String.format("%05d - %s - %s - %s",
                        mPlayList.getSongNumber(),
                        mPlayList.getSongGender(),
                        mPlayList.getSinger(),
                        mPlayList.songToPlay()));
                labelSongPlayingRight.setText(String.format("%05d - %s - %s - %s",
                        mPlayList.getSongNumber(),mPlayList.getSongGender(),
                        mPlayList.getSinger(), mPlayList.songToPlay()));
                return;
            }
        }

        mPlayList.addSong(listMusicData.getSong(random.nextInt(listMusicData.getSizeListOfSongs())), selectionVIP);

        int extension = Utils.getExtension(String.format("%s\\%s\\%s\\%s", pathSongs,mPlayList.getSongGender(),mPlayList.getSinger(), mPlayList.songToPlay()));

        if (extension == EXT_MP4 || extension == EXT_AVI || extension == EXT_MPG || extension == EXT_FLV || extension == EXT_MKV) {
            mMediaPlayer.playVideo(mPlayList.getSongGender(),mPlayList.getSinger(),mPlayList.songToPlay());
        } else if (extension == EXT_MP3 || extension == EXT_WMA || extension == EXT_WAV || extension == EXT_AAC) {
            mMediaPlayer.playAudio(
                    mPlayList.getSongGender(),
                    mPlayList.getSinger(),
                    mPlayList.songToPlay(),
                    pathVideosMP3 + "\\" + listMusicData.getPromVideo());
        }

        playListInterface.setListData(mPlayList.getPlayList());

        labelSongPlayingBottom.setText(String.format("%05d - %s - %s - %s",
                mPlayList.getSongNumber(),
                mPlayList.getSongGender(),
                mPlayList.getSinger(),
                mPlayList.songToPlay()));
        labelSongPlayingRight.setText(String.format("%05d - %s - %s - %s",
                mPlayList.getSongNumber(),mPlayList.getSongGender(),
                mPlayList.getSinger(), mPlayList.songToPlay()));

        /*Random random = new Random();
        File path = new File(pathSongs);
        if (path.exists()) {
            File [] genres = path.listFiles();
            File selectedGenre = genres[random.nextInt(genres.length)];
            if (selectedGenre.exists()) {
                File [] singers = selectedGenre.listFiles();
                File selectedSinger = singers[random.nextInt(singers.length)];
                if (selectedSinger.exists()) {
                    File [] songs = selectedSinger.listFiles();

                    String randomSong = songs[random.nextInt(songs.length)].getAbsolutePath();

                    int extension = Utils.getExtension(randomSong);

                    if (extension == EXT_MP4 || extension == EXT_AVI || extension == EXT_MPG || extension == EXT_FLV || extension == EXT_MKV) {
                        mMediaPlayer.playVideo(randomSong);
                    } else if (extension == EXT_MP3 || extension == EXT_WMA || extension == EXT_WAV || extension == EXT_AAC) {
                        mMediaPlayer.playAudio(
                                randomSong,
                                (String) mUserSettings.getSetting(KEY_PATH_VIDEOS_MP3) + "\\" + listMusicData.getPromVideo());
                    }
                }
            }
        }*/
    }

    public void addSongToPlayList(ArrayList numbers) {
        if (numbers.size()>0) {
            for (int i = 0; i < numbers.size(); i++) {
                Song cancionAReproducir = listMusicData.getSong((int) numbers.get(i));
                //Song cancion = new Song(numero, cancionAReproducir);
                mPlayList.addSong(cancionAReproducir, selectionVIP);

            }
        }
        playListInterface.setListData(mPlayList.getPlayList());
    }
}
