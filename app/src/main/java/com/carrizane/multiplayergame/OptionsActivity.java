package com.carrizane.multiplayergame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OptionsActivity extends AppCompatActivity {

    Button create;
    Button join;
    Button logout;

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference playersRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        create = (Button)findViewById(R.id.createBtn);
        join = (Button) findViewById(R.id.joinBtn);
        logout = (Button) findViewById(R.id.logoutBtn);

        database = FirebaseDatabase.getInstance();

        SharedPreferences prefs = getSharedPreferences("PREFS", 0);
        playerName = prefs.getString("playerName", "");
        roomName = playerName;

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.setEnabled(false);
                join.setEnabled(false);
                logout.setEnabled(false);

                startActivity(new Intent(getApplicationContext(), RoomsActivity.class));
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear().commit();

                create.setEnabled(false);
                join.setEnabled(false);
                logout.setEnabled(false);

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.setText("Creating Room");

                create.setEnabled(false);
                join.setEnabled(false);
                logout.setEnabled(false);

                roomName = playerName;
                roomRef = database.getReference("rooms/" + roomName + "/playerOne");
                playersRoom = database.getReference("rooms/" + roomName + "/players");
                playersRoom.setValue("1");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });

    }

    private void addRoomEventListener() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                create.setText("Create Room");
                create.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                create.setText("Create Room");
                create.setEnabled(true);
                Toast.makeText(OptionsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}