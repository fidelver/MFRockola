package com.mfrockola.classes;

import javax.swing.*;
import java.awt.*;

/**
 * Created by runegame on 21/02/2018.
 */
public class VipSelector extends JPanel {
    public static final int KEY_UP = 0;
    public static final int KEY_DOWN = 1;

    private int selection;
    private OptionVipSelector [] titulos;

    public VipSelector () {
        selection = 0;
        initComponents();
    }

    public void initComponents () {
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));
        setLayout(new GridLayout(3,1));
        titulos = new OptionVipSelector[3];
        titulos[0] = new OptionVipSelector("Reproducir", false);
        titulos[1] = new OptionVipSelector("Modo VIP", false);
        titulos[2] = new OptionVipSelector("Modo Super VIP", false);

        for (JLabel label: titulos) {
            add(label);
        }

        titulos[0].setSelected(true);
    }

    public int getSelection() {
        return selection;
    }

    public void setInitialSelection() {
        titulos[2].setEnabled(true);
        titulos[selection].setSelected(false);
        titulos[0].setSelected(true);
        selection = 0;
    }

    public void setSelection(int key) {
        switch (key) {
            case KEY_UP: {
                if (selection > 0) {
                    selection--;
                    titulos[selection+1].setSelected(false);
                    titulos[selection].setSelected(true);
                }
                break;
            }
            case KEY_DOWN: {
                if (selection < 2) {
                    if (selection == 0) {
                        selection++;
                        titulos[selection-1].setSelected(false);
                        titulos[selection].setSelected(true);
                    } else if (selection == 1 && titulos[2].isEnabled()) {
                        selection++;
                        titulos[selection-1].setSelected(false);
                        titulos[selection].setSelected(true);
                    }
                }
                break;
            }
        }
    }

    public void setLabelsEnabled(int credits) {
        if (credits == 2) {
            titulos[2].setEnabled(false);
        }
    }

    private class OptionVipSelector extends JLabel {
        private boolean selected;

        public OptionVipSelector (String title, boolean selected) {
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
