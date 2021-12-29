package me.pankajchoudhary.memeshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        addItemsOnSpinner();

    }

    private void addItemsOnSpinner() {
        List<String> list = new ArrayList<>();
        list.add("CanadaMeme");
        list.add("memes");
        list.add("PrequelMemes");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(dataAdapter);

    }

    public void startApp(View view) {
        String source = String.valueOf(spinner.getSelectedItem());
        Intent i = new Intent(getApplicationContext(),MemesPage.class);
        i.putExtra("source", source);
        startActivity(i);

    }
}