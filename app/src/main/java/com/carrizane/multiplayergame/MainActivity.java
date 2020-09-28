package com.carrizane.multiplayergame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText username;
    Button login;

    String playerName = "";

    FirebaseDatabase database;
    DatabaseReference playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.playerName);
        login = (Button) findViewById(R.id.loginBtn);

        database = FirebaseDatabase.getInstance();

        SharedPreferences prefs = getSharedPreferences("PREFS", 0);
        playerName = prefs.getString("playerName", "");
        if(!playerName.equals("")){
            playerRef = database.getReference("players/" + playerName);
            addEventListener();
            playerRef.setValue("");
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerName = username.getText().toString();
                username.setText("");
                if(!playerName.equals("")){
                    login.setText("Logging in");
                    login.setEnabled(false);
                    playerRef = database.getReference("players/" + playerName);
                    addEventListener();
                    playerRef.setValue("");
                }
            }
        });

    }

    private void addEventListener() {
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!playerName.equals("")){
                    SharedPreferences prefs = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("playerName", playerName);
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), OptionsActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                login.setText("Log In");
                login.setEnabled(true);
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}