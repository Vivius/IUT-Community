package fr.iutcommunity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDepartementsTitles;

    private int idUtilisateur = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Récupération de l'ID utilisateur.
        Bundle extras = getIntent().getExtras();
        idUtilisateur = extras.getInt("idUtilisateur");
        Log.d("IdUtilisateur", String.valueOf(idUtilisateur));

        mTitle = mDrawerTitle = getTitle();
        mDepartementsTitles = getResources().getStringArray(R.array.dept_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Mise en place des de la liste des départements dans le listview du menu + ajout d'un click listener.
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDepartementsTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Modification du bouton Home.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    // Attribution d'un nouveau titre à l'activity.
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    //----------------------------------------------------------------------------------------------
    // GESTION DE L'ACTION_BAR
    //----------------------------------------------------------------------------------------------

    // Création du menu de l'ActionBar en utilisant le layout menu_main.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // On cache le menu de l'ActionBar quand le menu latéral est ouvert.
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_new_message).setVisible(!drawerOpen);
        menu.findItem(R.id.action_logout).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    // Gestion de le sélection des items du menu de l'action bar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Switch prenant en compte chaque item du menu.
        switch(item.getItemId()) {
            //Déconnexion
            case R.id.action_logout:
                Intent logout = new Intent(this, LoginActivity.class);
                // Vérification que l'activity existe avant de l'appeller.
                if (logout.resolveActivity(getPackageManager()) != null) {
                    startActivity(logout);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_new_message:
                Intent newMessage = new Intent(this, SendMessageActivity.class);
                newMessage.putExtra("idUtilisateur", idUtilisateur);
                if (newMessage.resolveActivity(getPackageManager()) != null) {
                    startActivity(newMessage);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // ---------------------------------------------------------------------------------------------
    // GESTION DU MENU
    // ---------------------------------------------------------------------------------------------

    // Listener du clic sur un item de la liste du menu.
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    // Méthode exécutée lors d'un clic sur un élément du menu.
    private void selectItem(int position) {
        Fragment fragment = new DepartementFragment();
        // Ajout du numéro du département de la liste en argument.
        Bundle args = new Bundle();
        args.putInt(DepartementFragment.ARG_DEPT_NUMBER, position);
        fragment.setArguments(args);
        // On remplae le content_frame par un nouveau DepartementFragment.
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        // Mise à jour du titre et fermeture du menu.
        mDrawerList.setItemChecked(position, true);
        setTitle(mDepartementsTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

        // Chargement des commentaires du département.
        loadCommentaires();
    }

    // Fragment gérant l'affichage des messages d'un département.
    public static class DepartementFragment extends Fragment {
        public static final String ARG_DEPT_NUMBER = "NUM_DEPARTEMENT";

        public DepartementFragment() {
            // Constructeur vide pour les classes filles.
        }

        // Création de la vue du fragment.
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_departement, container, false);
            int i = getArguments().getInt(ARG_DEPT_NUMBER);
            String departement = getResources().getStringArray(R.array.dept_array)[i];
            getActivity().setTitle(departement);
            return rootView;
        }
    }

    // Méthode exécutant la recherche des commentaires pour le département en cours.
    public void loadCommentaires(){
        HashMap<String, String> departement = new HashMap<>();
        departement.put("lblGroupe", mTitle.toString());
        new HttpRequestManager().execute(departement);
    }
    // ---------------------------------------------------------------------------------------------
    // Classe permettant de charger les commentaires d'un groupe (département)
    // ---------------------------------------------------------------------------------------------
    private class HttpRequestManager extends AsyncTask<HashMap<?,?>, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(HashMap... params) {
            HttpRequest http = new HttpRequest("http://iut-community.vpeillex.fr/groupe/getMessages", HttpRequest.POST, params[0]);
            return http.connection();
        }
        @Override
        protected void onPostExecute(JSONObject data) {
            // Chargement de la liste messages avec data.
            Iterator i = data.keys();
            Integer count = 0;
            ArrayList<Message> messages = new ArrayList<Message>();
            while(i.hasNext()){
                i.next();
                try {
                    // Initialisation de l'objet JSON du message
                    JSONObject msgJSONObject = data.getJSONObject(count.toString());

                    // Calcul de la date.
                    String dtStart = msgJSONObject.getString("date");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    try {
                        date = format.parse(dtStart);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Déclaration de l'objet message.
                    Message msg = new Message(
                        msgJSONObject.getString("titre"),
                        msgJSONObject.getString("message"),
                        date,
                        new Utilisateur(
                            msgJSONObject.getString("login"),
                            msgJSONObject.getString("prenom"),
                            msgJSONObject.getString("nom")
                        )
                    );
                    //Ajout du message au tableau de messages.
                    messages.add(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                count++;
            }

            // Utilisation d'un MessageAdapter pour alimenter la liste des messages.
            if(!messages.isEmpty()){
                MessagesAdapter adapter = new MessagesAdapter(getApplicationContext(), messages);
                ListView listMessages = (ListView) findViewById(R.id.listMessages);
                listMessages.setAdapter(adapter);
            }
        }
    }
}