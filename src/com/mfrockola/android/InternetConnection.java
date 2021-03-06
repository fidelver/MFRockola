package com.mfrockola.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Angel C on 04/11/2016.
 *
 * Esta clase permitirá realizar peticiones POST al servidor donde se almacenan algunos datos de
 * MFRockola.
 */
public class InternetConnection implements Runnable{

    public static void send() throws IOException {
        // Esta es la URL a la que enviareamos nuestras peticiones POST.
        URL url = new URL("http://www.tutormatematico.com/task_manager/v1/login");

        // Objeto Map donde incluiremos todos nuestros parametros de la peticion POST.
        Map<String, Object> params = new LinkedHashMap<>();

        // Agregamos nuestros parametros
        params.put("email","alm93alm@gmail.com");
        params.put("password","123456");

        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length()!=0){
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(),"UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length",String.valueOf(postDataBytes.length));
        connection.setDoOutput(true);
        connection.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream(),"UTF-8"
        ));

        StringBuilder responseStringBuilder = new StringBuilder();

        for (int c = in.read(); c != -1; c=in.read()) {
            responseStringBuilder.append((char) c);
        }

        System.out.println(responseStringBuilder.toString());

        try {
            JSONObject jsonObject = new JSONObject(responseStringBuilder.toString());

            System.out.println("Hola Sr(a) " + jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // con este metodo llamamos a la lista de reproduccion en cola que hayan introducido los usuarios a la base de
    // datos del servidor.

    public ArrayList getPlayList() {

        // creamos un ArrayList para manejar facilmente los elementos de la lista de musicas que vamos a obtener
        ArrayList arrayList = new ArrayList();

        try {
            // Esta es la URL a la que enviareamos nuestra peticion GET.
            URL url = new URL("http://www.tutormatematico.com/api/v1/playlist");

            final String API_KEY_USER = "7b449827785fccb2ba1cbb0aea226a88"; // esta es la api de usuario en la base de datos

            // creamos nuestra conexion GET para realizar nuestra peticion
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("authorization",API_KEY_USER);
            connection.connect();

            // creamos un buffer y un reader para almacenar los paquetes enviados por el servidor
            Reader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"
            ));

            // creamos un StringBuilder para pasar nuestra respuesta en paquetes a un String
            StringBuilder responseStringBuilder = new StringBuilder();

            for (int c = in.read(); c != -1; c=in.read()) {
                responseStringBuilder.append((char) c);
            }

            // convertimos nuestro String anterior en un JSONObject
            JSONObject jsonObject = new JSONObject(responseStringBuilder.toString());

            System.out.println(jsonObject.toString());

            // obtenemos del JSONObject el array "songs"
            JSONArray jsonArray = jsonObject.getJSONArray("songs");

            // si tenemos canciones en espera, las guardamos en el array
            if (jsonArray.length()>0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(jsonArray.getJSONObject(i).getInt("song"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @Override
    public void run() {
        getPlayList();
    }
}
