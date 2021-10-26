package ru.metcon.metconapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SUBD_Main extends AppCompatActivity {
    DBHelper dbHelper;
    shareValues sv = new shareValues();
    private ArrayList<String> Tables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subd_main);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(sv.getLOG_TAG(), "--- Rows in PaintingTasks: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
//        Cursor c = db.query("PaintingTasks", null, null, null, null, null, null);
//        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from todo where _id = ?", new String[] { id });
        String sql = "SELECT name FROM sqlite_master\n" +
                "WHERE type IN ('table', 'view') AND name NOT LIKE 'sqlite %' \n" +
                "UNION  ALL\n" +
                "SELECT name FROM sqlite_temp_master\n" +
                "WHERE type IN  ('tаblе', 'view')\n"
//               + "ORDER ВY  1"
                ;
        String id = "";


        Cursor c = db.rawQuery(sql, new String[]{});

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
                        Tables.add(c.getString(colInd));
//                        Toast.makeText(this,str , Toast.LENGTH_SHORT).show();
                    }
                    Log.d(sv.getLOG_TAG(), str);

                } while (c.moveToNext());
            }
            c.close();
        } else
            Log.d(sv.getLOG_TAG(), "Cursor is null");

        dbHelper.close();

        // находим список
        ListView lvMain = (ListView) findViewById(R.id.lvSUBDTableList);
        // создаем адаптер
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                R.layout.subd_table_item, Tables);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String str;
//                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
//                        + id);
                str = "itemClick: position = " + position + ", TableName = "
                        + Tables.get((int) id);
//                Log.d(sv.getLOG_TAG(), str);
                showTable(Tables.get((int) id));
//                Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showTable(String tableName) {
        Intent intent = new Intent(this, SUBD_Table_View.class);
        intent.putExtra("tname", tableName);
        startActivity(intent);
    }

}