package fr.iutcommunity;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by peillexv on 01/03/2016.
 */
public class HttpRequestTaskManager extends AsyncTask<HashMap, String, JSONObject>{
    private String url;
    private String method;
    private JSONObject data;

    // Getters & Setters
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public JSONObject getData() {
        return data;
    }
    public void setData(JSONObject data) {
        this.data = data;
    }

    // Constructeur
    public HttpRequestTaskManager(String url, String method){
        if(!url.isEmpty()){
            setUrl(url);
        }else{
            setUrl("http://iut-community.vpeillex.fr");
        }
        setUrl(url);
        if(method.equals("POST") || method.equals("GET")){
            setMethod(method);
        }else{
            setMethod("POST");
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected JSONObject doInBackground(HashMap[] params) {
        JSONObject jsonResponse = new JSONObject();
        try {
            // Connexion
            URL url = new URL(getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(getMethod());

            if(params.length > 0){
                // Construction de la requête POST.
                String urlParameters = "";
                Boolean firstParameter;
                for(HashMap h : params){
                    firstParameter = true;
                    Iterator iterator = h.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry m = (Map.Entry) iterator.next();
                        if(firstParameter){
                            urlParameters += m.getKey() + "=" + m.getValue();
                            firstParameter = false;
                        }else{
                            urlParameters += "&" + m.getKey() + "=" + m.getValue();
                        }
                    }
                }
                // Encodage en UTF-8 de la requête POST.
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                //On modifie l'entête de la requette HTTP en indiquant la taille des données avec Content-Lengh qui correspond à postData.length (la taille du message).
                connection.setRequestProperty("Content-Length", "" + postData.length);
                // On crée et envoi le DataOutputStream.
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.write(postData);
                out.close();
            }

            // Connexion...
            connection.connect();

            // Réception des données.
            InputStream input = new BufferedInputStream(connection.getInputStream());
            // Conversion des données en Json.
            jsonResponse = new JSONObject(convertStreamToString(input));

            //Gestion des diverses exceptions...
        } catch (IOException e) {
            Log.e("IO", e.getMessage());
        } catch (JSONException e) {
            Log.e("JSON", e.getMessage());
        } catch (NetworkOnMainThreadException e) {
            Log.e("NetworkOnMainThread", e.getMessage());
        }
        // Retour de l'objet Json.
        return jsonResponse;
        //A la fin de l'exécution de cette méthode doInBackground, la méthode onPostExecute va être appelée si elle est déclarée.
    }

    protected void onPostExecute(JSONObject data){
        setData(data);
        Log.e("Data",data.toString());
    }

    //Méthode permmettant de convertir un InputStream en String.
    protected String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
