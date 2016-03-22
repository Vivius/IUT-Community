package fr.iutcommunity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        TextView txtTitre = (TextView) convertView.findViewById(R.id.txtTitre);
        TextView txtNom = (TextView) convertView.findViewById(R.id.txtNom);
        TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
        // Populate the data into the template view using the data object
        txtTitre.setText(message.getTitre());
        txtNom.setText(message.getUtilisateur().getNom());
        txtMessage.setText(message.getMessage());
        // Return the completed view to render on screen
        return convertView;
    }
}
