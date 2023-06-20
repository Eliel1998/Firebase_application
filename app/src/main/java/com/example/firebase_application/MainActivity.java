package com.example.firebase_application;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> moviesStr;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(getApplicationContext());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("movies");
        databaseListener(databaseReference);

        Button button = findViewById(R.id.btnSave);
        EditText editTextTitle = findViewById(R.id.etMovieName);
        EditText editTextNote = findViewById(R.id.etRating);
        moviesStr = new ArrayList<>();
        listView = findViewById(R.id.lvMovies);

        button.setOnClickListener(v -> {
            updateAndAddMovie(getData(editTextTitle.getText().toString(), editTextNote.getText().toString()), databaseReference);
        });

    }// onCreate

    public void databaseListener(DatabaseReference databaseReference){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: " + dataSnapshot.toString());
                getMoviesFirebase(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Erro ao ler dados: " + databaseError.getMessage());
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    public List<CustomMovie> getMoviesFirebase(DataSnapshot snapshot){
        List<CustomMovie> movies = new ArrayList<>();
        moviesStr.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            String movieName = dataSnapshot.getKey();
            String movieNote = dataSnapshot.child("note").getValue().toString();
            CustomMovie movie = new CustomMovie(movieName,movieNote);
            movies.add(movie);
            moviesStr.add(movie.toString());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, moviesStr);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            Log.i(TAG, "getMoviesFirebase: " + movie.toString());
        }
        Toast.makeText(this, "Filmes atualizados diretamente do firebase", Toast.LENGTH_SHORT).show();
        return movies;
    }

    public CustomMovie getData(String title, String note) {
        CustomMovie movie = new CustomMovie(title, note);
        return movie;
    }

    public void updateAndAddMovie(CustomMovie movie, DatabaseReference databaseReference) {
        databaseReference.child(movie.getTitle()).child("note").setValue(movie.getNote(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("Firebase", "Erro ao salvar o valor: " + databaseError.getMessage());
                } else {
                    Log.d("Firebase", "Valor salvo com sucesso!");
                }
            }
        });
    }
}