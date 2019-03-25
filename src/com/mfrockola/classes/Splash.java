package com.mfrockola.classes;

import oshi.hardware.Disks;
import oshi.hardware.platform.windows.WindowsDisks;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.swing.*;

/**
	This class will be used to execute MFRockola. Since the configuration must be external, a splash is made so that the
	user can press a key that opens the configuration window. It also has additional controls that determine if some
 	MFRockola requirements are well configured
*/

public class Splash extends JFrame implements Runnable {
	// Determines whether the mouse can be moved or locked in the lower right corner of the screen
	static boolean moveMouse = true;

	// constructor without attributes
	Splash() {
        initComponents();
	}

	// Generate the splash window and init components
	private void initComponents() {
		// Will load JPanel external class that has the ability to have a background image
		BackgroundImagePanel panel = new BackgroundImagePanel(
				this.getClass().getResource("/com/mfrockola/imagenes/fondoSmall.png"));

		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/com/mfrockola/imagenes/icono.png")));
		panel.setLayout(new BorderLayout());
		JLabel labelText = new JLabel("Presione Q para configurar MFRockola ");
		labelText.setForeground(Color.WHITE);
		labelText.setFont(new Font("Calibri", Font.BOLD, 16));
		labelText.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(labelText,BorderLayout.SOUTH);
		getContentPane().add(panel);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(480,300);
		setLocationRelativeTo(null);
		setUndecorated(true);
		this.setVisible(true);

		getSerialKey();
	}

	public static void getSerialKey () {
        Disks disks = new WindowsDisks();
		String identifier = disks.getDisks()[0].getSerial();
		System.out.println(identifier);

		try {
			X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(publicSpec);
			KeyPair keyPair = buildKeyPair();
			PublicKey pubKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();

			System.out.println(new String(pubKey.getEncoded()));
			System.out.println(new String(privateKey.getEncoded()));

			// sign the message
			byte [] signed = encrypt(privateKey, "This is a secret message");
			System.out.println(new String(signed));  // <<signed message>>

			// verify the message
			byte[] verified = decrypt(pubKey, signed);
			System.out.println(new String(verified));    // This is a secret message
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Starts MFRockola
    public static void main(String args [])	{
		try {
            // Change the look and feel of windows to the running environment that is Windows
            JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

        // Execute an additional thread for the time we have to press Q and hold the mouse in the lower right corner
        new Thread(new Splash()).start();
	}

	public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
		final int keySize = 2048;
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keySize);
		return keyPairGenerator.genKeyPair();
	}

	public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(message.getBytes());
	}

	public static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(encrypted);
	}

    // thread for the time we have to press Q and hold the mouse in the lower right corner
	public void run() {

		final Timer timer = new Timer(1000*2, e -> {
            File file = new File("config.json");
            if (file.exists()) {
                file = null;
                new Interface();
                dispose();
            } else {
                file = null;
                moveMouse = false;
                new SettingsWindow();
                dispose();
            }
        });
		
		timer.setRepeats(false);
		timer.start();

		// These instructions listen to the keyboard events, in the event that the Q key is pressed,
		// the configuration window opens and unlocks the movement of the mouse
		addKeyListener(new KeyAdapter()	{
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==81) {
					timer.stop();
					moveMouse = false;
					new SettingsWindow();
					dispose();
				}
				else {
					e.consume();
				}
			}
		});

		Robot robot = null;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		while (moveMouse) {
			if (robot != null) {
				robot.mouseMove((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                        (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
			}
		}
	}
}
