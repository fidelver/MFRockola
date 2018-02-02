package com.mfrockola.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class PlayList {
    private ArrayList<Song> mPlayList;

    PlayList(){
        mPlayList = new ArrayList<>();
    }

    void addSong (Song song, int type) {
        song.setType(type);
        if (mPlayList.size()> 0) {
            mPlayList.add(findPosition(type) + 1, song);
        } else {
            mPlayList.add(song);
        }
    }

    String songToPlay() {
        if (mPlayList.size()==0) {
            return null;
        } else {
            return mPlayList.get(0).getSongName();
        }
    }

    int getSongNumber() {
        return mPlayList.get(0).getSongNumber();
    }

    String getSongGender() {
        return mPlayList.get(0).getSongGenre();
    }

    String getSinger() {
        return mPlayList.get(0).getSinger();
    }

    void removeSong() {
        if (mPlayList.size()>0) {
            mPlayList.remove(0);
        }
    }

    public JSONObject getSongJSONObject(Song song) {
        JSONObject songJsonObject = new JSONObject();

        try {
            songJsonObject.put("songNumber",song.getSongNumber());
            songJsonObject.put("songGenre",song.getSongGenre());
            songJsonObject.put("songSinger",song.getSinger());
            songJsonObject.put("songName",song.getSongName());
            songJsonObject.put("songType", song.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songJsonObject;
    }

    private int findPosition(int type) {
        int position = 0;
        int lastSuperVIP = 0;
        int size = mPlayList.size();

        Song song;

        for (int i = 0; i < size; i++) {
            song = mPlayList.get(i);
            if (song.getType() == Song.SUPER_VIP) {
                lastSuperVIP = position = i;
            }
        }

        for (int i = lastSuperVIP; i < size; i++) {
            song = mPlayList.get(i);
            if (song.getType() == type) {
                position = i;
            }
        }
        return position;
    }

    Object[] getPlayList(){
        return mPlayList.toArray();
    }
}