package ru.metcon.metconapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SUBD_Table_View extends AppCompatActivity {
    private ArrayList<String> Stroki = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subd_table_view);
//        tvView = (TextView) findViewById(R.id.tvView);
        Intent intent = getIntent();

        String tname = intent.getStringExtra("tname");
        // курсор
        Cursor c = null;

//        tvView.setText("Your name is: " + fName + " " + lName);
        // создаем объект для создания и управления версиями БД
        DBHelper dbHelper = new DBHelper(this);
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        c = db.query(tname, null, null, null, null, null, null);

        shareValues sv = new shareValues();

        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                Integer colInd;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        colInd = c.getColumnIndex(cn);
                        str = str.concat(cn + " = "
                                + c.getString(colInd) + "; ");
                    }
                    Stroki.add(str);
                    Log.d(sv.getLOG_TAG(), str);

                } while (c.moveToNext());
            }
            c.close();
        } else
            Log.d(sv.getLOG_TAG(), "Cursor is null");

        dbHelper.close();

        // находим список
        ListView lvMain = (ListView) findViewById(R.id.lvSUBDTableView);
        // создаем адаптер
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                R.layout.subd_table_item, Stroki);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);


    }
}