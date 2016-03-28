package fr.iutcommunity;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class SendMessageActivity extends AppCompatActivity implements ListView.OnItemSelectedListener{
    private Spinner ddlDepartements;
    private ViewGroup rdgPromotions;
    private static int HTTP_REQUEST_PROMOTIONS = 1;
    private static int HTTP_REQUEST_GROUPES = 2;
    private int httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        rdgPromotions = (ViewGroup)findViewById(R.id.rdgPromotions);
        ddlDepartements = (Spinner)findViewById(R.id.ddlDepartements);
        ddlDepartements.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rdgPromotions.setVisibility(View.VISIBLE);
        httpRequest = HTTP_REQUEST_PROMOTIONS;
        HashMap<String, String> departement = new HashMap<>();
        Log.d("Departement", ddlDepartements.getSelectedItem().toString());
        departement.put("parent", ddlDepartements.getSelectedItem().toString());
        new HttpRequestManager().execute(departement);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        rdgPromotions.setVisibility(View.INVISIBLE);
    }

    // ---------------------------------------------------------------------------------------------
    // Classe permettant de charger les commentaires sous-groupes d'un département.
    // ---------------------------------------------------------------------------------------------
    private class HttpRequestManager extends AsyncTask<HashMap<?,?>, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(HashMap... params) {
            HttpRequest http = new HttpRequest("http://iut-community.vpeillex.fr/groupe/getChildrenByParentLibelle", HttpRequest.POST, params[0]);
            return http.connection();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(JSONObject data) {
            rdgPromotions.removeAllViews();
            if(data.names() != null){
                for(int i=0; i < data.names().length(); i++){
                    RadioButton button = new RadioButton(getApplicationContext());
                    try {
                        button.setText(data.getJSONObject(data.names().getString(i)).getString("libelle"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    button.setTextColor(Color.BLACK);
                    ColorStateList colorStateList = new ColorStateList(
                            new int[][]{
                                    new int[]{-android.R.attr.state_checked}, // Bouton désactivé
                                    new int[]{android.R.attr.state_checked} // Bouton activé
                            }, new int[] {Color.BLACK, Color.BLUE});
                    button.setButtonTintList(colorStateList);
                    rdgPromotions.addView(button);
                }
            }
        }
    }
}
