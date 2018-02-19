package com.mfrockola.classes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by runegame on 14/02/2018.
 */

public class SingerList extends JPanel {
    public static final int MOVE_TO_FIRST = 0;
    public static final int MOVE_TO_LAST = 1;
    public static final int MOVE_TO_LEFT = 2;
    public static final int MOVE_TO_RIGHT = 3;

    private int width;
    private int height;

    private int selectedSinger;
    private int selectedCover;

    private ArrayList<Singer> singers;
    private ArrayList<JLabel> covers;

    public SingerList (int width, int height, ArrayList<Singer> singers) {
        this.width = width;
        this.height = height;
        this.singers = singers;

        selectedSinger = 0;
        selectedCover = 0;

        setLayout(new GridLayout(1,4));
        setOpaque(false);

        covers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ImageIcon cover = printCoverIcon(singers.get(i).getPathCover());
            JLabel label = new JLabel(cover, JLabel.CENTER);
            label.setPreferredSize(new Dimension(height, height));
            label.setOpaque(false);
            covers.add(label);
            add(covers.get(i));
        }
        updateBorder(MOVE_TO_FIRST);
    }

    public void setSelectedSinger(int moveTo) {
        switch (moveTo) {
            case MOVE_TO_LEFT: {
                if (selectedCover == 0 && selectedSinger > 0) {
                    selectedCover = 3;
                    selectedSinger--;
                    updateCovers(MOVE_TO_LEFT);
                    updateBorder(MOVE_TO_LAST);
                } else if (selectedCover > 0) {
                    selectedCover--;
                    selectedSinger--;
                    updateBorder(MOVE_TO_LEFT);
                }
                break;
            }
            case MOVE_TO_RIGHT: {
                if (selectedCover == 3 && selectedSinger < singers.size()-1) {
                    selectedCover = 0;
                    selectedSinger++;
                    updateCovers(MOVE_TO_RIGHT);
                    updateBorder(MOVE_TO_FIRST);
                } else if (selectedCover < 3 && selectedSinger < singers.size()-1) {
                    selectedCover++;
                    selectedSinger++;
                    updateBorder(MOVE_TO_RIGHT);
                }
                break;
            }
        }
    }

    public int getSelectedSinger() {
        return this.selectedSinger;
    }

    private void updateBorder (int moveTo) {
        switch (moveTo) {
            case MOVE_TO_FIRST: {
                covers.get(3).setBorder(null);
                covers.get(0).setBorder(BorderFactory.createLineBorder(Color.RED, 4));
                break;
            }
            case MOVE_TO_LEFT: {
                covers.get(selectedCover+1).setBorder(null);
                covers.get(selectedCover).setBorder(BorderFactory.createLineBorder(Color.RED, 4));
                break;
            }
            case MOVE_TO_RIGHT: {
                covers.get(selectedCover-1).setBorder(null);
                covers.get(selectedCover).setBorder(BorderFactory.createLineBorder(Color.RED, 4));
                break;
            }
            case MOVE_TO_LAST: {
                covers.get(0).setBorder(null);
                covers.get(3).setBorder(BorderFactory.createLineBorder(Color.RED, 4));
                break;
            }
        }
    }

    public void updateCovers (int moveTo) {
        removeAll();
        for (int i = 0; i < 4; i++) {
            covers.add(new JLabel(printDefaultCover()));
        }

        switch (moveTo) {
            case (MOVE_TO_LEFT): {
                for (int i = 0; i < 4; i++) {
                    JLabel label = new JLabel(printCoverIcon(singers.get((selectedSinger-3) + i).getPathCover()), JLabel.CENTER);
//                    JLabel label = new JLabel(printCoverIcon(String.valueOf(((selectedSinger-3) +i+1))), JLabel.CENTER);
                    covers.set(i, label);
                    add(covers.get(i));
                }
                repaint();
                break;
            }
            case (MOVE_TO_RIGHT): {
                int limit = singers.size() - selectedSinger;
                for (int i = 0; i < limit; i++) {
                    JLabel label = new JLabel(printCoverIcon(singers.get(selectedSinger+i).getPathCover()), JLabel.CENTER);
                    covers.set(i, label);
                    add(covers.get(i));
                }
                repaint();
                break;
            }
        }
    }

    private ImageIcon printDefaultCover() {
        return null;
    }

    private ImageIcon printCoverIcon(String path) {
        BufferedImage image = null;
        try {
            File file = new File(path + ".jpg");
            if (file.exists()) {
                image = ImageIO.read(new File(path + ".jpg"));
            } else {
                image = ImageIO.read(this.getClass().getResource("/com/mfrockola/imagenes/default.jpg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image resizedImage = image.getScaledInstance(height, height, Image.SCALE_FAST);

        ImageIcon ii = new ImageIcon(resizedImage);
        return ii;
    }
}
