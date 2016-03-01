package fr.iutcommunity;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Utilisation d'un service web en asynchrone.
        new HttpRequestManager().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Exemple d'appel à un web service avec la classe HttpRequest.
    private class HttpRequestManager extends AsyncTask<HashMap<?,?>, String, JSONObject>{

        // On peut passer une HashMap avec Clé/Valeurs pour envoyer en POST ou en GET.
        @Override
        protected JSONObject doInBackground(HashMap... params) {
            HttpRequest http = new HttpRequest("http://iut-community.vpeillex.fr",HttpRequest.POST);
            return http.connection();
        }

        // Une fois le résultat obtenu, on en fait ce que l'on veut. Il s'agit ici d'un objet JSON.
        @Override
        protected void onPostExecute(JSONObject data) {
            EditText txtLogin = (EditText)findViewById(R.id.txtLogin);
            try {
                txtLogin.setText(data.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
