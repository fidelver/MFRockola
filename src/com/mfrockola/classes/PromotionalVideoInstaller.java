package com.mfrockola.classes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PromotionalVideoInstaller {

    private static final String RESOURCE_FOLDER = "promocionales/";

    private PromotionalVideoInstaller() {
        // Utility class
    }

    public static void install(String destinationPath) {

        if (destinationPath == null || destinationPath.trim().isEmpty()) {
            return;
        }

        File destinationDirectory = new File(destinationPath);

        if (!destinationDirectory.isDirectory()) {
            return;
        }

        try {
            URL resourceUrl = PromotionalVideoInstaller.class
                    .getClassLoader()
                    .getResource(RESOURCE_FOLDER);

            if (resourceUrl == null) {
                System.err.println(
                        "No se encontraron los videos promocionales dentro del programa.");
                return;
            }

            if ("file".equalsIgnoreCase(resourceUrl.getProtocol())) {
                installFromDirectory(resourceUrl, destinationDirectory);
            } else if ("jar".equalsIgnoreCase(resourceUrl.getProtocol())) {
                installFromJar(resourceUrl, destinationDirectory);
            } else {
                System.err.println(
                        "Protocolo de recursos no soportado: "
                        + resourceUrl.getProtocol());
            }

        } catch (Exception e) {
            System.err.println(
                    "Error al instalar los videos promocionales:");
            e.printStackTrace();
        }
    }

    private static void installFromDirectory(
            URL resourceUrl,
            File destinationDirectory) throws Exception {

        File sourceDirectory = new File(resourceUrl.toURI());

        File[] files = sourceDirectory.listFiles();

        if (files == null) {
            return;
        }

        for (File sourceFile : files) {
            if (!sourceFile.isFile()) {
                continue;
            }

            if (!isVideoFile(sourceFile.getName())) {
                continue;
            }

            File destinationFile =
                    new File(destinationDirectory, sourceFile.getName());

            if (destinationFile.exists()) {
                continue;
            }

            copyFile(sourceFile, destinationFile);
        }
    }

    private static void installFromJar(
            URL resourceUrl,
            File destinationDirectory) throws Exception {

        JarURLConnection connection =
                (JarURLConnection) resourceUrl.openConnection();

        JarFile jarFile = connection.getJarFile();

        try {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {

                JarEntry entry = entries.nextElement();

                String entryName = entry.getName();

                if (entry.isDirectory()) {
                    continue;
                }

                if (!entryName.startsWith(RESOURCE_FOLDER)) {
                    continue;
                }

                String fileName =
                        entryName.substring(RESOURCE_FOLDER.length());

                if (!isVideoFile(fileName)) {
                    continue;
                }

                File destinationFile =
                        new File(destinationDirectory, fileName);

                if (destinationFile.exists()) {
                    continue;
                }

                InputStream inputStream =
                        jarFile.getInputStream(entry);

                try {
                    copyStream(inputStream, destinationFile);
                } finally {
                    inputStream.close();
                }
            }

        } finally {
            jarFile.close();
        }
    }

    private static boolean isVideoFile(String fileName) {
        String name = fileName.toLowerCase();

        return name.endsWith(".mp4")
                || name.endsWith(".mpg")
                || name.endsWith(".mpeg");
    }

    private static void copyFile(
            File source,
            File destination) throws IOException {

        InputStream inputStream =
                new java.io.FileInputStream(source);

        try {
            copyStream(inputStream, destination);
        } finally {
            inputStream.close();
        }
    }

    private static void copyStream(
            InputStream inputStream,
            File destination) throws IOException {

        FileOutputStream outputStream =
                new FileOutputStream(destination);

        try {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

        } finally {
            outputStream.close();
        }
    }
}
