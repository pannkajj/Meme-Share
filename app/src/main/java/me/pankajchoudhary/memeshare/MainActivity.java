package me.pankajchoudhary.memeshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;

    private DatabaseReference mRootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootRef = FirebaseDatabase.getInstance().getReference().child("memes");

        List<String> list = new ArrayList<>();
        spinner = findViewById(R.id.spinner);

        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String name = dataSnapshot.getKey();
                    list.add(name);
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, list);
                spinner.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void startApp(View view) {
        String name = String.valueOf(spinner.getSelectedItem());
        mRootRef.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String source = snapshot.getValue().toString();
                Intent i = new Intent(getApplicationContext(), MemesPage.class);
                i.putExtra("source", source);
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}