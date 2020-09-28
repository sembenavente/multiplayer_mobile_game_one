package com.carrizane.multiplayergame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {

    Button rock;
    Button paper;
    Button scissors;

    Button retry;

    TextView title;
    TextView state;

    String playerName = "";
    String roomName = "";
    String role = "";

    FirebaseDatabase database;
    DatabaseReference stateRef;
    DatabaseReference hostRef;
    DatabaseReference guestRef;
    DatabaseReference checkWinRef;
    DatabaseReference roomRef;
    DatabaseReference secondPlayerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        rock = (Button) findViewById(R.id.rockBtn);
        paper = (Button) findViewById(R.id.paperBtn);
        scissors = (Button) findViewById(R.id.scissorsBtn);

        retry = (Button) findViewById(R.id.retry);
        retry.setVisibility(View.GONE);

        title = (TextView) findViewById(R.id.gameTitle);
        state = (TextView) findViewById(R.id.state);

        database = FirebaseDatabase.getInstance();

        SharedPreferences prefs = getSharedPreferences("PREFS", 0);
        playerName = prefs.getString("playerName", "");

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            roomName = extras.getString("roomName");
            if(roomName.equals(playerName)){
                role = "host";
            } else {
                role = "guest";
            }
        }

        stateRef = database.getReference("rooms/" + roomName + "/state");
        stateRef.setValue("waiting");
        hostRef = database.getReference("rooms/" + roomName + "/host");
        hostRef.setValue("none");
        guestRef = database.getReference("rooms/" + roomName + "/guest");
        hostRef.setValue("none");
        checkWinRef = database.getReference("rooms/" + roomName + "/winner");

        roomRef = database.getReference("rooms/" + roomName);
        secondPlayerRef = database.getReference("rooms/" + roomName + "/players");

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.equals("host") || role.equals("guest")){
                    rock.setEnabled(true);
                    paper.setEnabled(true);
                    scissors.setEnabled(true);
                    rock.setVisibility(View.VISIBLE);
                    paper.setVisibility(View.VISIBLE);
                    scissors.setVisibility(View.VISIBLE);
                    stateRef.setValue("retry");
                    state.setText("");
                    retry.setVisibility(View.GONE);
                    title.setText("Choose");
                    hostRef.setValue("none");
                    guestRef.setValue("none");
                }
            }
        });

        stateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(role.equals("host")){
                    if(snapshot.getValue(String.class).contains("guest")){
                        rock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setEnabled(false);
                                paper.setVisibility(View.GONE);
                                scissors.setVisibility(View.GONE);
                                hostRef.setValue("rock");
                                stateRef.setValue("both: ready");
                                title.setText("You chose ROCK");
                            }
                        });
                        paper.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setVisibility(View.GONE);
                                paper.setEnabled(false);
                                scissors.setVisibility(View.GONE);
                                hostRef.setValue("paper");
                                stateRef.setValue("both: ready");
                                title.setText("You chose PAPER");
                            }
                        });
                        scissors.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setVisibility(View.GONE);
                                paper.setVisibility(View.GONE);
                                scissors.setEnabled(false);
                                hostRef.setValue("scissors");
                                stateRef.setValue("both: ready");
                                title.setText("You chose SCISSORS");
                            }
                        });
                    }else if(snapshot.getValue(String.class).contains("waiting")){
                        rock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setEnabled(false);
                                paper.setVisibility(View.GONE);
                                scissors.setVisibility(View.GONE);
                                hostRef.setValue("rock");
                                stateRef.setValue("host: ready");
                                title.setText("You chose ROCK");
                                state.setText("Waiting Opponent...");
                            }
                        });
                        paper.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setVisibility(View.GONE);
                                paper.setEnabled(false);
                                scissors.setVisibility(View.GONE);
                                hostRef.setValue("paper");
                                stateRef.setValue("host: ready");
                                title.setText("You chose PAPER");
                                state.setText("Waiting Opponent...");
                            }
                        });
                        scissors.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setVisibility(View.GONE);
                                paper.setVisibility(View.GONE);
                                scissors.setEnabled(false);
                                hostRef.setValue("scissors");
                                stateRef.setValue("host: ready");
                                title.setText("You chose SCISSORS");
                                state.setText("Waiting Opponent...");
                            }
                        });
                    }else if(snapshot.getValue(String.class).contains("both")){
                        addEventResultListener();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        stateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(role.equals("guest")){
                    if(snapshot.getValue(String.class).contains("host")){
                        rock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setEnabled(false);
                                paper.setVisibility(View.GONE);
                                scissors.setVisibility(View.GONE);
                                guestRef.setValue("rock");
                                stateRef.setValue("both: ready");
                                title.setText("You chose ROCK");
                            }
                        });
                        paper.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setVisibility(View.GONE);
                                paper.setEnabled(false);
                                scissors.setVisibility(View.GONE);
                                guestRef.setValue("paper");
                                stateRef.setValue("both: ready");
                                title.setText("You chose PAPER");
                            }
                        });
                        scissors.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setVisibility(View.GONE);
                                paper.setVisibility(View.GONE);
                                scissors.setEnabled(false);
                                guestRef.setValue("scissors");
                                stateRef.setValue("both: ready");
                                title.setText("You chose SCISSORS");
                            }
                        });
                    }else if(snapshot.getValue(String.class).contains("waiting")){
                        rock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setEnabled(false);
                                paper.setVisibility(View.GONE);
                                scissors.setVisibility(View.GONE);
                                guestRef.setValue("rock");
                                stateRef.setValue("guest: ready");
                                title.setText("You chose ROCK");
                                state.setText("Waiting Opponent...");
                            }
                        });
                        paper.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setVisibility(View.GONE);
                                paper.setEnabled(false);
                                scissors.setVisibility(View.GONE);
                                guestRef.setValue("paper");
                                stateRef.setValue("guest: ready");
                                title.setText("You chose PAPER");
                                state.setText("Waiting Opponent...");
                            }
                        });
                        scissors.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rock.setVisibility(View.GONE);
                                paper.setVisibility(View.GONE);
                                scissors.setEnabled(false);
                                guestRef.setValue("scissors");
                                stateRef.setValue("guest: ready");
                                title.setText("You chose SCISSORS");
                                state.setText("Waiting Opponent...");
                            }
                        });
                    }else if(snapshot.getValue(String.class).contains("both")){
                        addEventResultListener();
                    }else if(snapshot.getValue(String.class).contains("retry")){
                        rock.setEnabled(true);
                        paper.setEnabled(true);
                        scissors.setEnabled(true);
                        rock.setVisibility(View.VISIBLE);
                        paper.setVisibility(View.VISIBLE);
                        scissors.setVisibility(View.VISIBLE);
                        state.setText("");
                        retry.setVisibility(View.GONE);
                        title.setText("Choose");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEventResultListener() {
        hostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot hostSnapshot) {
                guestRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot guestSnapshot) {
                        if(hostSnapshot.getValue(String.class).contains("rock") && guestSnapshot.getValue(String.class).contains("rock")){
                            checkWinRef.setValue("Tie!");
                            state.setText("Tie!");
                            if(role.equals("host")){
                                retry.setVisibility(View.VISIBLE);
                            }
                        }else if(hostSnapshot.getValue(String.class).contains("paper") && guestSnapshot.getValue(String.class).contains("paper")){
                            checkWinRef.setValue("Tie!");
                            state.setText("Tie!");
                            if(role.equals("host")){
                                retry.setVisibility(View.VISIBLE);
                            }
                        }else if(hostSnapshot.getValue(String.class).contains("scissor") && guestSnapshot.getValue(String.class).contains("scissors")){
                            checkWinRef.setValue("Tie!");
                            state.setText("Tie!");
                            if(role.equals("host")){
                                retry.setVisibility(View.VISIBLE);
                            }
                        }else if(hostSnapshot.getValue(String.class).contains("rock") && guestSnapshot.getValue(String.class).contains("paper")){
                            checkWinRef.setValue("Guest wins!");
                            if(role.equals("host")){
                                state.setText("You lost!");
                                retry.setVisibility(View.VISIBLE);
                            }else{
                                state.setText("You win!");
                            }
                        }else if(hostSnapshot.getValue(String.class).contains("rock") && guestSnapshot.getValue(String.class).contains("scissors")){
                            checkWinRef.setValue("Host wins!");
                            if(role.equals("host")){
                                state.setText("You win!");
                                retry.setVisibility(View.VISIBLE);
                            }else{
                                state.setText("You lost!");
                            }
                        }else if(hostSnapshot.getValue(String.class).contains("paper") && guestSnapshot.getValue(String.class).contains("rock")){
                            checkWinRef.setValue("Host wins!");
                            if(role.equals("host")){
                                state.setText("You win!");
                                retry.setVisibility(View.VISIBLE);
                            }else{
                                state.setText("You lost!");
                            }
                        }else if(hostSnapshot.getValue(String.class).contains("paper") && guestSnapshot.getValue(String.class).contains("scissors")){
                            checkWinRef.setValue("Guest wins!");
                            if(role.equals("host")){
                                state.setText("You lost!");
                                retry.setVisibility(View.VISIBLE);
                            }else{
                                state.setText("You win!");
                            }
                        }else if(hostSnapshot.getValue(String.class).contains("scissors") && guestSnapshot.getValue(String.class).contains("rock")){
                            checkWinRef.setValue("Guest wins!");
                            if(role.equals("host")){
                                state.setText("You lost!");
                                retry.setVisibility(View.VISIBLE);
                            }else{
                                state.setText("You win!");
                            }
                        }else if(hostSnapshot.getValue(String.class).contains("scissors") && guestSnapshot.getValue(String.class).contains("paper")){
                            checkWinRef.setValue("Host wins!");
                            if(role.equals("host")){
                                state.setText("You win!");
                                retry.setVisibility(View.VISIBLE);
                            }else{
                                state.setText("You lost!");
                            }
                        }else if(hostSnapshot.getValue(String.class).contains("none") && guestSnapshot.getValue(String.class).contains("none")){
                            state.setText("");
                        }else{
                            state.setText("Waiting Opponent...");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}