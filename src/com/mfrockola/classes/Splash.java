package com.mfrockola.classes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.*;

/**
 * Startup window that gives the operator a short opportunity to open the
 * configuration before MFRockola starts.
 */
public class Splash extends JFrame {

    private final Timer launchTimer;

    Splash() {
        initComponents();
        installConfigurationShortcut();

        launchTimer = new Timer(2000, this::continueStartup);
        launchTimer.setRepeats(false);
        launchTimer.start();
    }

    private void initComponents() {
        BackgroundImagePanel panel = new BackgroundImagePanel(
                getClass().getResource("/com/mfrockola/imagenes/fondoSmall.png"));

        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/com/mfrockola/imagenes/icono.png")));
        panel.setLayout(new BorderLayout());

        JLabel labelText = new JLabel("Presione Q para configurar MFRockola ");
        labelText.setForeground(Color.WHITE);
        labelText.setFont(new Font("Calibri", Font.BOLD, 16));
        labelText.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(labelText, BorderLayout.SOUTH);

        getContentPane().add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(480, 300);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
    }

    private void installConfigurationShortcut() {
        JRootPane rootPane = getRootPane();
        KeyStroke q = KeyStroke.getKeyStroke('Q');
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(q, "openConfiguration");
        rootPane.getActionMap().put("openConfiguration", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openConfiguration();
            }
        });
    }

    private void continueStartup(ActionEvent event) {
        if (new File("config.json").isFile()) {
            new Interface();
        } else {
            new SettingsWindow();
        }
        dispose();
    }

    private void openConfiguration() {
        launchTimer.stop();
        new SettingsWindow();
        dispose();
    }

    public static void main(String[] args) {
        System.setProperty("jna.encoding", "UTF8");
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(Splash::new);
    }
}
