package com.mfrockola.classes;

// This class contains the basic data of a song, such as its number, name, singer and genre.

class Song {
	public static final int UNSELECTED_VIP = -1;
	public static final int NORMAL = 1;
	public static final int VIP = 2;
	public static final int SUPER_VIP = 3;

	private int songNumber;
	private String songGenre;
	private String singer;
	private String songName;
	private int type;
	private boolean mode;

	// Constructor to start variables
	Song(int songNumber, String songGenre, String singer, String songName, boolean mode) {
		setSongNumber(songNumber);
		setSongGenre(songGenre);
		setSinger(singer);
		setSongName(songName);
		setType(NORMAL);
	}

	// Public methods to obtain the values ​​of the variables of the Song object
	
	String getSongName() {
		return songName;
	}
	
	int getSongNumber() {
		return songNumber;
	}
	
	private void setSongNumber(int songNumber) {
		this.songNumber = songNumber;
	}
	
	private void setSongName(String songName) {
		this.songName = songName;
	}

	String getSongGenre() {
		return songGenre;
	}

	private void setSongGenre(String songGenre) {
		this.songGenre = songGenre;
	}

	String getSinger() {
		return singer;
	}

	private void setSinger(String singer) {
		this.singer = singer;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	// Override the toString method in order to print the song name correctly on the interface
	@Override
	public String toString() {
		if (isMode()) {
			return String.format("%05d - %s - %s", getSongNumber(),getSinger(), getSongName());
		} else {
			return getSongName();
		}
	}

	public boolean isMode() {
		return mode;
	}

	public void setMode(boolean mode) {
		this.mode = mode;
	}
}
