package ru.metcon.metconapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ptContent extends AppCompatActivity {
    Integer npp, delete_or_edit_or_insert;
    ListView lvData;
    SimpleCursorAdapter scAdapter;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_content);
        Intent intent = getIntent();
        delete_or_edit_or_insert = intent.getIntExtra("delete_or_edit_or_insert", 2);

    }
}