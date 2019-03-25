package com.mfrockola.classes;

import oshi.hardware.Disks;
import oshi.hardware.platform.windows.WindowsDisks;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class LicenseManager {
    public static void getSerialKey () {
        Disks disks = new WindowsDisks();
        String identifier = disks.getDisks()[0].getSerial();
        System.out.println(identifier);

        try {
//            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            return keyFactory.generatePublic(publicSpec);
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
}
