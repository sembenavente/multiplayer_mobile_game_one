package com.carrizane.multiplayergame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DataRoomAdapter extends ArrayAdapter<Room> {
    private Context mContext;
    private int mResource;
    public DataRoomAdapter(@NonNull Context context, int resource, @NonNull List<Room> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView roomName = convertView.findViewById(R.id.roomName);
        TextView playersRoom = convertView.findViewById(R.id.playersInRoom);

        roomName.setText(getItem(position).getNameRoom());

        String players = getItem(position).getPlayers();
        if(players.equals("1")){
            playersRoom.setText("1/2");
        }else if(players.equals("2")){
            playersRoom.setText("2/2");
        }

        return convertView;
    }
}
