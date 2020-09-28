package com.carrizane.multiplayergame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomsActivity extends AppCompatActivity {

    ListView roomsView;

    List<Room> roomsList;

    Room r;

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomsRef;
    DatabaseReference playersRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        database = FirebaseDatabase.getInstance();

        SharedPreferences prefs = getSharedPreferences("PREFS", 0);
        playerName = prefs.getString("playerName", "");
        roomName = playerName;

        addRoomsEventListener();

        roomsView = (ListView) findViewById(R.id.roomsList);

        roomsList = new ArrayList<>();

        roomsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                r = roomsList.get(position);
                roomName = r.getNameRoom();
                playersRoom = database.getReference("rooms/" + roomName + "/players");
                playersRoom.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.getValue(String.class).equals("2")){
                            playersRoom.setValue("2");
                            roomsRef = database.getReference("rooms/" + roomName + "/playerTwo");
                            addRoomEventListener();
                            roomsRef.setValue(playerName);
                        }else{
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RoomsActivity.this);
                                    builder.setTitle("Full Room!");
                                    builder.setMessage("Try with another one.");

                                    builder.setPositiveButton("OK", null);

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }, 1000);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void addRoomEventListener() {
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RoomsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRoomsEventListener() {
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot roomSnapshot) {
                roomsList.clear();
                Iterable<DataSnapshot> rooms = roomSnapshot.getChildren();
                for (DataSnapshot datasnapshot : rooms){
                    roomsList.add(new Room(datasnapshot.child("players").getValue(String.class), datasnapshot.getKey()));
                    DataRoomAdapter roomAdapter = new DataRoomAdapter(RoomsActivity.this, R.layout.list_row, roomsList);
                    roomsView.setAdapter(roomAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}