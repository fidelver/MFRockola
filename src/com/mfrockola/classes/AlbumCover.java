package com.mfrockola.classes;

/**
 * Created by runegame on 17/02/2018.
 */
public class AlbumCover {
    private String pathImage;

    public AlbumCover(String pathImage) {
        setPathImage(pathImage);
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }
}
