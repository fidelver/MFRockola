package com.mfrockola.classes;

import javax.swing.*;
import java.awt.*;

/**
 * Created by runegame on 21/02/2018.
 */
public class AlphabetSelector extends JPanel {
    public static final int KEY_LEFT = 0;
    public static final int KEY_RIGHT = 1;
    public static final String[] characters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    private int selection;
    private OptionAlphabetSelector [] letras;

    public AlphabetSelector() {
        selection = 0;
        initComponents();
    }

    public void initComponents () {
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));
        setLayout(new GridLayout(2,13));
        letras = new OptionAlphabetSelector[26];

        for (int i = 0; i < characters.length; i++) {
            letras[i] = new OptionAlphabetSelector(characters[i], false);
        }

        for (JLabel label: letras) {
            add(label);
        }

        letras[0].setSelected(true);
    }

    public String getSelection() {
        return characters[selection];
    }

    public void setInitialSelection() {
        letras[2].setEnabled(true);
        letras[selection].setSelected(false);
        letras[0].setSelected(true);
        selection = 0;
    }

    public void setSelection(int key) {
        switch (key) {
            case KEY_LEFT: {
                if (selection > 0) {
                    selection--;
                    letras[selection+1].setSelected(false);
                    letras[selection].setSelected(true);
                }
                break;
            }
            case KEY_RIGHT: {
                if (selection < characters.length - 1) {
                    selection++;
                    letras[selection-1].setSelected(false);
                    letras[selection].setSelected(true);
                }
                break;
            }
        }
    }

    private class OptionAlphabetSelector extends JLabel {
        private boolean selected;

        public OptionAlphabetSelector (String character, boolean selected) {
            setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            setFont(new Font("Arial", Font.BOLD, 20));
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
            setText(character);
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
