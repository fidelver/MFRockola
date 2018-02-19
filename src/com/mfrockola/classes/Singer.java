package com.mfrockola.classes;

/**
 * Created by runegame on 14/02/2018.
 */
public class Singer {
    private String name;
    private Song[] songs;
    private String pathCover;

    public Singer(String name, Song[] songs, String pathCover) {
        setName(name);
        setSongs(songs);
        setPathCover(pathCover);
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

    public String getPathCover() {
        return pathCover;
    }

    public void setPathCover(String pathCover) {
        this.pathCover = pathCover;
    }
}
