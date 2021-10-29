package ru.metcon.metconapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ptContent extends AppCompatActivity {
    Integer npp, delete_or_edit_or_insert;
    String pt_id;
    ListView lvData;
    SimpleCursorAdapter scAdapter;
    DBHelper dbHelper = new DBHelper(this);
    Cursor cursor;
    private static final int CM_DELETE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_content);
        Intent intent = getIntent();
        delete_or_edit_or_insert = intent.getIntExtra("delete_or_edit_or_insert", 2);
        pt_id = intent.getStringExtra("npp");
        // получаем курсор
        cursor = getAllData();
        startManagingCursor(cursor);

        // формируем столбцы сопоставления
        String[] from = new String[]{"drafts_name", "marks_name", "ptc_value"};
        int[] to = new int[]{R.id.twDraft, R.id.twMark, R.id.twValue};

        // создаем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.ptcontent_item, cursor, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

//        lvData.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            protected void onListItemClick(ListView l, View v, int pos, long id) {
                Toast.makeText(getApplicationContext(),
                        "onItemSelected = " + Integer.toString(pos), Toast.LENGTH_SHORT).show();
                Log.d("MyLog", "onItemSelected: position = " + pos + ", id = " + id);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "onItemSelected = " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                Log.d("MyLog", "onItemSelected: position = " + position + ", id = " + id);
            }
        });

        lvData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "onItemSelected = " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                Log.d("MyLog", "onItemSelected: position = " + position + ", id = " + id);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),
                        "onNothingSelected ", Toast.LENGTH_SHORT).show();
                Log.d("MyLog", "onNothingSelected:  ");
            }
        });


        Toast.makeText(getApplicationContext(),
                "onCreate ", Toast.LENGTH_SHORT).show();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            // извлекаем id записи и удаляем соответствующую запись в БД
            //db.delRec(acmi.id);
            // обновляем курсор
            cursor.requery();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private Cursor getAllData() {
        String sql = "select ptc.ptc_id as _id, d.drafts_name,m.marks_name, ptc.ptc_value " +
                " from PaintingTasksCnt ptc left join Drafts d on ptc.drafts_id=d.drafts_id left join Marks m on ptc.marks_id=m.marks_id " +
                " where ptc.pt_id=" + pt_id;

        SQLiteDatabase sqd = dbHelper.getWritableDatabase();
        return sqd.rawQuery(sql, new String[]{});

    }


    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        dbHelper.close();
    }

}