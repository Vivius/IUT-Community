package fr.iutcommunity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnConnexion;
    private EditText txtLogin;
    private EditText txtPassword;
    private TextView lblMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnConnexion = (Button)findViewById(R.id.btnConnexion);
        btnConnexion.setOnClickListener(this);

        txtLogin = (EditText)findViewById(R.id.txtLogin);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConnexion:
                HashMap<String, String> credentials = new HashMap<>();
                credentials.put("login", txtLogin.getText().toString());
                credentials.put("password", txtPassword.getText().toString());
                new HttpRequestManager().execute(credentials);
                break;
        }
    }

    // Exemple d'appel à un web service avec la classe HttpRequest.
    private class HttpRequestManager extends AsyncTask<HashMap<?,?>, String, JSONObject>{
        // On peut passer une HashMap avec Clé/Valeurs pour envoyer en POST ou en GET.
        @Override
        protected JSONObject doInBackground(HashMap... params) {
            HttpRequest http = new HttpRequest("http://iut-community.vpeillex.fr/login", HttpRequest.POST, params[0]);
            return http.connection();
        }

        // Une fois le résultat obtenu, on en fait ce que l'on veut. Il s'agit ici d'un objet JSON.
        @Override
        protected void onPostExecute(JSONObject data) {
            try {
                if(data.getBoolean("message")){
                    Intent mainAct = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(mainAct);
                }else{
                    Toast.makeText(getApplicationContext(),"Echec de connexion. Veuillez vérifier vos identifiants", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
