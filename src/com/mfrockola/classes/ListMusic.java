package com.mfrockola.classes;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Angel C on 03/05/2016.
 */
public class ListMusic {

    public static final int COUNT_SONGS_BY_GENDER = 0;
    public static final int COUNT_SONGS_BY_SINGERS = 1;


    private ArrayList <Song> listOfSongs;
    private ArrayList<Gender> gender;
    private ArrayList<Singer> singers;
    private String path;
    private String pathPromVideos;
    private int selectedGender;
    private int selectedSinger;
    private int songNumber;

    private String [] promVideos;

    private Random random;

    public ListMusic (String path, String pathPromVideos) {
        setPath(path);
        setPathPromVideos(pathPromVideos);
        setGender(new ArrayList<>());
        setSingers(new ArrayList<>());
        setListOfSongs(new ArrayList<>());

        selectedGender = 0;
        selectedSinger = 0;
        countGender();

        random = new Random();
    }

    public int getSelectedGender (){
        return selectedGender;
    }

    public void setListOfSongs(ArrayList<Song> listOfSongs) {
        this.listOfSongs = listOfSongs;
    }

    public int getSizeListOfSongs () {
        return listOfSongs.size();
    }

    public Song getSong(int number) {
        return listOfSongs.get(number);
    }

    public void countGender() {
        String [] listaArchivos = null;

        File directorio = new File(path);

        if (directorio.isDirectory())
            listaArchivos = directorio.list();

        for (int i = 0; i< listaArchivos.length; i++) {
            File archivoActual = new File(String.format("%s\\%s", path, listaArchivos[i]));

            if (archivoActual.isDirectory()) {
                gender.add(new Gender(listaArchivos[i],countSongs(archivoActual)));
            }
        }

        gender.add(getGenderTop30());

        File directorioVideosPromocionales = new File(getPathPromVideos());
        if (directorioVideosPromocionales.isDirectory()) {
            promVideos = directorioVideosPromocionales.list();
        }

        Collections.sort(singers, Comparator.comparing(Singer::getName));
    }

    public boolean upGender() {
        if (selectedGender + 1 < gender.size()) {
            selectedGender++;
            return true;
        } else {
            selectedGender = 0;
            return true;
        }
    }

    public boolean downGender() {
        if (selectedGender != 0) {
            selectedGender--;
            return true;
        } else {
            selectedGender = gender.size()-1;
            return true;
        }
    }

    public String getNameOfGender() {
        return gender.get(selectedGender).getName();
    }

    public String getNameOfSinger() {
        return singers.get(selectedSinger).getName();
    }

    public Song[] countSongs(File file) {
        String [] artistas = file.list();

        ArrayList<Song> provisionalGender = new ArrayList<>();
        ArrayList<Song> provisionalSinger = new ArrayList<>();

        Song[] gender = new Song[0];
        Song[] singer;

        for (int i = 0; i < artistas.length; i++) {

            File artista = new File(String.format("%s\\%s\\%s", path,file.getName(), artistas[i]));

            if (artista.isDirectory()) {
                provisionalSinger = new ArrayList<>();
                String [] canciones = artista.list();

                for (int j = 0; j < canciones.length; j++) {
                    if (Utils.getExtension(String.format("%s\\%s\\%s\\%s",path,file.getName(),artista.getName(),canciones[j]))!= Utils.EXT_UNKNOWN) {
                        provisionalGender.add(new Song(songNumber,file.getName(),artista.getName(),canciones[j]));
                        provisionalSinger.add(new Song(songNumber, file.getName(), artista.getName(), canciones[j]));
                        listOfSongs.add(new Song(songNumber,file.getName(),artista.getName(),canciones[j]));
                        songNumber++;
                    }
                }
            }

            singer = new Song[provisionalSinger.size()];

            for (int k = 0; k < provisionalSinger.size(); k++) {
                singer[k] = provisionalSinger.get(k);
            }
            singers.add(new Singer(artista.getName(), singer, String.format("%s\\%s\\%s\\cover",path,file.getName(),artista.getName())));

            gender = new Song[provisionalGender.size()];

            for (int k = 0; k < provisionalGender.size(); k++) {
                gender[k] = provisionalGender.get(k);
            }
        }
        return gender;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setGender(ArrayList<Gender> gender) {
        this.gender = gender;
    }

    public Song[] getGenderSongs(int i) {
        return gender.get(i).getSongs();
    }

    public Song[] getSingerSongs() {
        return singers.get(selectedSinger).getSongs();
    }

    public String getPromVideo() {
        String promVideo = promVideos[random.nextInt(promVideos.length)];
        return promVideo;
    }

    public String getPathPromVideos() {
        return pathPromVideos;
    }

    public void setPathPromVideos(String pathPromVideos) {
        this.pathPromVideos = pathPromVideos;
    }

    private Gender getGenderTop30() {
        Song[] songs = new Song[30];
        Gender genderTop30 = null;
        SQLiteConsultor sqLiteConsultor = new SQLiteConsultor();

        try {
            ResultSet resultSet = sqLiteConsultor.query("SELECT * FROM most_popular ORDER BY times DESC, number ASC");
            int i = 0;
            while (resultSet.next() && i < 30) {
                songs[i] = new Song(resultSet.getInt("number"),
                        resultSet.getString("genre"),
                        resultSet.getString("artist"),
                        resultSet.getString("name"));
                i++;
            }

            genderTop30 = new Gender("Top 30",songs);

            resultSet.close();
            sqLiteConsultor.closeConnection();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return genderTop30;
    }

    public ArrayList<Singer> getSingers() {
        return this.singers;
    }

    public void setSingers(ArrayList<Singer> singers) {
        this.singers = singers;
    }

    public void setSelectedSinger(int selectedSinger) {
        this.selectedSinger = selectedSinger;
    }
}