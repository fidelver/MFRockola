package com.mfrockola.classes;

/**
 * Created by runegame on 14/02/2018.
 */
public class Singer {
    private String name;
    private Song[] songs;

    public Singer(String name, Song[] songs) {
        setName(name);
        setSongs(songs);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Song[] getSongs() {
        return songs;
    }

    public void setSongs(Song[] songs) {
        this.songs = songs;
    }
}
