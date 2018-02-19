package com.mfrockola.classes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by runegame on 14/02/2018.
 */
public class SingerRenderer implements ListCellRenderer {

    private int height;

    public SingerRenderer (int height) {
        this.height = height;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        AlbumCover albumCover = (AlbumCover) value;
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResource("/com/mfrockola/imagenes/" + albumCover.getPathImage() + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image resizedImage = image.getScaledInstance(height, height, Image.SCALE_FAST);

        ImageIcon ii = new ImageIcon(resizedImage);
        JLabel label = new JLabel(ii, JLabel.CENTER);
        label.setPreferredSize(new Dimension(height, height));
        label.setBackground(Color.BLACK);
        label.setOpaque(true);

        if (isSelected) {
            label.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        return label;
    }
}
