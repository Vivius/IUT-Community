package fr.iutcommunity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by peillexv on 22/03/2016.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {
    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Message message = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }
        // Lookup view for data population
        TextView txtTitre = (TextView) convertView.findViewById(R.id.txtTitreMessage);
        TextView txtInfosMessage = (TextView) convertView.findViewById(R.id.txtInfosMessage);
        TextView txtMessage = (TextView) convertView.findViewById(R.id.txtContenuMessage);
        // Populate the data into the template view using the data object
        txtTitre.setText(message.getTitre());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        String newDateStr = dateFormat.format(message.getDate());
        String newHourStr = hourFormat.format(message.getDate());
        txtInfosMessage.setText(message.getUtilisateur().getNom()+" "+message.getUtilisateur().getPrenom()+", le "+ newDateStr +" à "+newHourStr);

        // Ajout du message.
        txtMessage.setText(message.getMessage());
        // Retour de la vue complète.
        return convertView;
    }
}
