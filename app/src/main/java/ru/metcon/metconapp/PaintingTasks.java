package ru.metcon.metconapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaintingTasks extends AppCompatActivity implements View.OnClickListener {
    // имена атрибутов для Map
    final String ATTRIBUTE_NAME_NPP = "npp";
    final String ATTRIBUTE_NAME_DATE = "date";
    final String ATTRIBUTE_NAME_DOC_NUMBER = "number";
    final String ATTRIBUTE_NAME_UCHASOK = "uchastok";
    final String ATTRIBUTE_NAME_ZAKAZ = "zakaz";
    final String ATTRIBUTE_NAME_FIO = "fio";
    ListView lvSimple;
    shareValues sv = new shareValues();
    Button bAdd;
    DBHelper dbHelper = new DBHelper(this);
    ContentValues cv = new ContentValues();
    final int DIALOG_CONFIRM_DELETE = 1;
    Object NPP;
    // упаковываем данные в понятную для адаптера структуру
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_tasks);

        bAdd = (Button) findViewById(R.id.bAdd);
        bAdd.setOnClickListener(this);

        fillData();

        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Map<String, Object> m = data.get(position);
                Object NPP = m.get(ATTRIBUTE_NAME_NPP);
                Log.d(sv.getLOG_TAG(), "NPP = " + NPP);
                Object FIO = m.get(ATTRIBUTE_NAME_FIO);
                Log.d(sv.getLOG_TAG(), "FIO = " + FIO);
                showPaintTask((int) position, 1);
            }
        });

        ListView lvMain = (ListView) findViewById(R.id.lvSimple);
        registerForContextMenu(lvMain);
    }

    @Override
    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        switch (v.getId()) {
            case R.id.bAdd:
//                Map<String, Object> m = data.get(position);
//                Object NPP = m.get(ATTRIBUTE_NAME_NPP);
//                Log.d(sv.getLOG_TAG(), "NPP = " + NPP);
//                Object FIO = m.get(ATTRIBUTE_NAME_FIO);
//                Log.d(sv.getLOG_TAG(), "FIO = " + FIO);
                showPaintTask(0, 2);
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_paintingtask, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Map<String, Object> m = data.get(info.position);
        NPP = m.get(ATTRIBUTE_NAME_NPP);
        switch (item.getItemId()) {
            case R.id.ctx_menu_item_edit:
//                    editItem(info.position); // метод, выполняющий действие при редактировании пункта меню
                Log.d(sv.getLOG_TAG(), "Нажмали Edit: " + (Integer) NPP);
                showPaintTask((int) info.position, 1);
                return true;
            case R.id.ctx_menu_item_delete:
//                    deleteItem(info.position); //метод, выполняющий действие при удалении пункта меню
                Log.d(sv.getLOG_TAG(), "Нажмали Delete: " + data.get(info.position));
//                showPaintTask((int) NPP - 1, 0);
                // вызываем диалог
                showDialog(DIALOG_CONFIRM_DELETE);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_CONFIRM_DELETE) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(R.string.deleting);
            // сообщение
            adb.setMessage(R.string.delete_data);
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton(R.string.yes, myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton(R.string.no, myClickListener);
            // кнопка нейтрального ответа
            adb.setNeutralButton(R.string.cancel, myClickListener);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    deleteData();
//                    finish();
                    break;
                // негативная кнопка
                case Dialog.BUTTON_NEGATIVE:
//                    finish();
                    break;
                // нейтральная кнопка
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };

    void deleteData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "DELETE FROM PaintingTasks where pt_id=" + Integer.toString((Integer) NPP);
        db.execSQL(sql);
        fillData();

        Toast.makeText(this, R.string.is_deleted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }

    class MySimpleAdapter extends SimpleAdapter {

        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

//        @Override
//        public void setViewText(TextView v, String text) {
//            // метод супер-класса, который вставляет текст
//            super.setViewText(v, text);
//            // если нужный нам TextView, то разрисовываем
////            if (v.getId() == R.id.tvValue) {
////                int i = Integer.parseInt(text);
////                if (i < 0) v.setTextColor(Color.RED); else
////                if (i > 0) v.setTextColor(Color.GREEN);
////            }
//        }

//        @Override
//        public void setViewImage(ImageView v, int value) {
//            // метод супер-класса
//            super.setViewImage(v, value);
//            // разрисовываем ImageView
//            if (value == negative) v.setBackgroundColor(Color.RED); else
//            if (value == positive) v.setBackgroundColor(Color.GREEN);
//        }
    }

    private void showPaintTask(Integer position, Integer delete_or_edit_or_insert) {
//        Функция для отображения формы с карточкой "Задания на покраску"
        Intent intent = new Intent(this, paintingTask.class);
        if (delete_or_edit_or_insert != 2) {
            Map<String, Object> m = data.get(position);
            Object NPP = m.get(ATTRIBUTE_NAME_NPP);
            Object DATE = m.get(ATTRIBUTE_NAME_DATE);
            Object DOC_NUM = m.get(ATTRIBUTE_NAME_DOC_NUMBER);
            Object ZAKAZ = m.get(ATTRIBUTE_NAME_ZAKAZ);
            Object UCHASOK = m.get(ATTRIBUTE_NAME_UCHASOK);

            Object FIO = m.get(ATTRIBUTE_NAME_FIO);
            intent.putExtra("npp", (Integer) NPP);
            intent.putExtra("date", (String) DATE);
            intent.putExtra("doc_num", (String) DOC_NUM);
            intent.putExtra("zakaz", (String) ZAKAZ);
            intent.putExtra("uchastok", (String) UCHASOK);
            intent.putExtra("fio", (String) FIO);
        }
        intent.putExtra("delete_or_edit_or_insert", delete_or_edit_or_insert);

        startActivity(intent);

    }

    private void fillData() {

        data.clear();

        String sql = "select pt.pt_id,pt.pt_date, pt.pt_number,  pt.pt_zakaz, u.uch_name, m.ms_name  " +
                " from PaintingTasks pt left join Uchastok u on pt.uch_id=u.uch_id left join Mastera m on pt.ms_id=m.ms_id";
        // создаем объект для создания и управления версиями БД
        DBHelper dbHelper = new DBHelper(this);
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(sql, new String[]{});

//        String str;
//        // упаковываем данные в понятную для адаптера структуру
        Map<String, Object> m;

        if (c != null) {
            if (c.moveToFirst()) {
//                Integer colInd;
                do {
                    m = new HashMap<String, Object>();
                    m.put(ATTRIBUTE_NAME_NPP, c.getInt(0));
                    m.put(ATTRIBUTE_NAME_DATE, c.getString(1));
                    m.put(ATTRIBUTE_NAME_DOC_NUMBER, c.getString(2));
                    m.put(ATTRIBUTE_NAME_ZAKAZ, c.getString(3));
                    m.put(ATTRIBUTE_NAME_UCHASOK, c.getString(4));
                    m.put(ATTRIBUTE_NAME_FIO, c.getString(5));
                    data.add(m);

                } while (c.moveToNext());
            }
            c.close();
        } else
//            Log.d(sv.getLOG_TAG(), "Cursor is null");
            ;

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = {ATTRIBUTE_NAME_NPP, ATTRIBUTE_NAME_DATE,
                ATTRIBUTE_NAME_DOC_NUMBER, ATTRIBUTE_NAME_UCHASOK, ATTRIBUTE_NAME_ZAKAZ, ATTRIBUTE_NAME_FIO};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = {R.id.tvNpp, R.id.tvDate, R.id.tvNumber, R.id.tvUchastok, R.id.tvZakaz, R.id.tvFio};


        // создаем адаптер
        MySimpleAdapter sAdapter = new MySimpleAdapter(this, data, R.layout.painting_tasks_item, from, to);
        // определяем список и присваиваем ему адаптер
        lvSimple = (ListView) findViewById(R.id.lvSimple);
//        MySimpleAdapter oldAdapter = (MySimpleAdapter) lvSimple.getAdapter();

        lvSimple.setAdapter(sAdapter);
    }
    

}