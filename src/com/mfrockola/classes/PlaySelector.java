package com.mfrockola.classes;

import javax.swing.*;
import java.awt.*;

/**
 * Created by runegame on 21/02/2018.
 */
public class PlaySelector extends JPanel {
    public static final int KEY_UP = 0;
    public static final int KEY_DOWN = 1;

    private int selection;
    private OptionPlaySelector [] titulos;

    public PlaySelector () {
        selection = 0;
        initComponents();
    }

    public void initComponents () {
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));
        setLayout(new GridLayout(2,1));
        titulos = new OptionPlaySelector[2];
        titulos[0] = new OptionPlaySelector("Reproducir", false);
        titulos[1] = new OptionPlaySelector("Cancelar", false);

        for (JLabel label: titulos) {
            add(label);
        }

        titulos[0].setSelected(true);
    }

    public int getSelection() {
        return selection;
    }

    public void setInitialSelection() {
        titulos[0].setSelected(true);
        titulos[1].setSelected(false);
        selection = 0;
    }

    public void setSelection(int key) {
        switch (key) {
            case KEY_UP: {
                selection = 0;
                titulos[selection+1].setSelected(false);
                titulos[selection].setSelected(true);
                break;
            }
            case KEY_DOWN: {
                selection = 1;
                titulos[selection-1].setSelected(false);
                titulos[selection].setSelected(true);
                break;
            }
        }
    }

    private class OptionPlaySelector extends JLabel {
        private boolean selected;

        public OptionPlaySelector (String title, boolean selected) {
            setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            setFont(new Font("Arial", Font.BOLD, 20));
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
            setText(title);
            setSelected(selected);
            setForeground(Color.WHITE);
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            if (selected) {
                setBackground(new Color(0,104,17));
            } else {
                setBackground(Color.BLACK);
            }
            this.selected = selected;
        }
    }
}
