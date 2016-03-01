package fr.iutcommunity;

import android.annotation.TargetApi;
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
import java.util.Map;

/**
 * Created by Vincent on 01/03/2016.
 * Attention cette classe ne peut être utiliée qu'en ASYNCHRONE.
 * Il faut donc l'utiliser avec AsyncTask.
 */
public class HttpRequest {

    // Propriétés
    private String url;
    private String method;
    private JSONObject data;
    private HashMap<?,?> parameters;

    public static String POST = "POST";
    public static String GET = "GET";

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
    public HashMap<?, ?> getParameters() {
        return parameters;
    }
    public void setParameters(HashMap<?, ?> parameters) {
        this.parameters = parameters;
    }

    //Constructeurs
    public HttpRequest(String url){
        setUrl(url);
        setMethod(HttpRequest.POST);
        setParameters(new HashMap<String, String>());
    }
    public HttpRequest(String url, String method){
        this(url);
        if(method.equals(POST) || method.equals(GET)){
            setMethod(method);
        }else{
            setMethod(POST);
        }
    }
    public HttpRequest(String url, String method, HashMap<?,?> parameters){
        this(url, method);
        if(!parameters.isEmpty()){
            setParameters(parameters);
        }
    }

    //Méthode permmettant de convertir un InputStream en String.
    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public JSONObject connection(){
        JSONObject jsonResponse = new JSONObject();
        try {
            // Création de la connexion.
            URL url = new URL(getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(getMethod());

            // On vérifie qu'il y ait des paramètres à envoyer.
            if(!getParameters().isEmpty()){
                // Construction de la requête POST ou GET.
                String urlParameters = "";
                Boolean firstParameter = true;
                for (Map.Entry m : getParameters().entrySet()){
                    if(firstParameter){
                        urlParameters += m.getKey() + "=" + m.getValue();
                        firstParameter = false;
                    }else{
                        urlParameters += "&" + m.getKey() + "=" + m.getValue();
                    }
                }

                // Encodage en UTF-8 de la requête.
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                // Définition de la taille du message.
                connection.setRequestProperty("Content-Length", "" + postData.length);
                // On crée et envoi le DataOutputStream.
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.write(postData);
                out.close();
            }

            // Connexion pour la réception des données.
            connection.connect();
            // Réception des données en InputStream.
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
        setData(jsonResponse);
        return jsonResponse;
    }
}
