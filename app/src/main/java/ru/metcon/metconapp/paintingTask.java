package ru.metcon.metconapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class paintingTask extends AppCompatActivity implements View.OnClickListener {
    Integer npp, delete_or_edit_or_insert;
    String date, doc_num, zakaz, uchastok, fio;
    EditText eNPP,eDocNum,eDate,eZakaz;
    Spinner spUchastok,spMaster;
    Button btnSave,btnCancel,btnTableMarks;
    DBHelper dbHelper = new DBHelper(this);
    ContentValues cv = new ContentValues();
    Calendar dateForView = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_task2);

        btnSave = (Button) findViewById(R.id.bPaintingTaskSave);
        btnCancel = (Button) findViewById(R.id.bPaintingTaskCancel);
        btnTableMarks = (Button) findViewById(R.id.bTableMarks);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnTableMarks.setOnClickListener(this);

        Intent intent = getIntent();
        delete_or_edit_or_insert = intent.getIntExtra("delete_or_edit_or_insert", 2);
        if (delete_or_edit_or_insert == 2) {
            npp = 0;
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String dateText = dateFormat.format(currentDate);
            date = dateText;
            doc_num = "";
            zakaz = "";
            uchastok = "";
            fio = "";
        } else {
            npp = intent.getIntExtra("npp", 0);
            date = intent.getStringExtra("date");
            doc_num = intent.getStringExtra("doc_num");
            zakaz = intent.getStringExtra("zakaz");
            uchastok = intent.getStringExtra("uchastok");
            fio = intent.getStringExtra("fio");
        }


        eNPP = (EditText) findViewById(R.id.eNPP);
        eDocNum = (EditText) findViewById(R.id.eDocNum);
        eDate = (EditText) findViewById(R.id.eDate);
        eDate.setOnClickListener(this);
        eDate.setFocusable(false);
        spUchastok = (Spinner) findViewById(R.id.spUchastok);
        spMaster = (Spinner) findViewById(R.id.spMaster);
        eZakaz = (EditText) findViewById(R.id.eZakaz);
        fillSpinner("select uch_name from uchastok", uchastok, R.id.spUchastok);
        fillSpinner("select ms_name from Mastera", fio, R.id.spMaster);

        switch (delete_or_edit_or_insert) {
            case (0): //Delete

                break;
            case (1): //Edit
                eNPP.setText(Integer.toString(npp));
                eDocNum.setText(doc_num);
                eDate.setText(date);

                eZakaz.setText(zakaz);
                break;
            case (2): //Insert

                break;
        }//
    }

    @Override
    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        switch (v.getId()) {
            case R.id.bPaintingTaskSave:
                // кнопка ОК
                savePaintingTask();
                break;
            case R.id.bPaintingTaskCancel:
                // кнопка Cancel
                finish();
                break;
            case R.id.bTableMarks:
                // кнопка Таблица марок
                showPtContent();

                break;

            case R.id.eDate:
//                Преобразовать дату из вьюшки в формат Date
                try {
                    String dateString = "" + eDate.getText();
                    Date onlyDate = new SimpleDateFormat("dd.MM.yyyy").parse(dateString);
                    dateForView.setTime(onlyDate);
//                    dateForView.set(Calendar.YEAR, year);
//                    dateForView.set(Calendar.MONTH, monthOfYear);
//                    dateForView.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    new DatePickerDialog(paintingTask.this, d,
                            dateForView.get(Calendar.YEAR),
                            dateForView.get(Calendar.MONTH),
                            dateForView.get(Calendar.DAY_OF_MONTH))
                            .show();
//                    android.text.format.DateFormat df = new android.text.format.DateFormat();
//                    String str = "" + df.format("dd.MM.yyyy", dateForView);
//                    eDate.setText(str);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public void showPtContent(){
        //Функция для отображения формы с таблицей марок для "Задания на покраску"
        Intent intent = new Intent(this, ptContent.class);
        if (delete_or_edit_or_insert != 2) {
            intent.putExtra("npp", (Integer) npp);
        }
        intent.putExtra("delete_or_edit_or_insert", delete_or_edit_or_insert);

        startActivity(intent);
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(paintingTask.this, d,
                dateForView.get(Calendar.YEAR),
                dateForView.get(Calendar.MONTH),
                dateForView.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateForView.set(Calendar.YEAR, year);
            dateForView.set(Calendar.MONTH, monthOfYear);
            dateForView.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            String str = "" + df.format("dd.MM.yyyy", dateForView);
            eDate.setText(str);
        }
    };

    private void fillSpinner(String sql, String selected_item, Integer id) {
        ArrayList<String> dataForSpin = new ArrayList<>();
        Integer selectedId = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(sql, new String[]{});
        Integer counter = -1;
        String str;

        if (c != null) {
            if (c.moveToFirst()) {
//                Integer colInd;
                do {
                    counter++;
                    str = c.getString(0);
                    dataForSpin.add(str);
                    if (str.equals(selected_item)) {
                        selectedId = counter;
                    }
                    ;
//                    Log.d(sv.getLOG_TAG(), str);
                } while (c.moveToNext());
            }
            c.close();
        } else
//            Log.d(sv.getLOG_TAG(), "Cursor is null");
            ;


        Spinner spinner = (Spinner) findViewById(id);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataForSpin);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        if (selectedId != 0) {
            spinner.setSelection(selectedId);
        }

    }

    private void savePaintingTask() {
        //Получим ID из спиннеров
        String uch_id = getTableId("select uch_id from uchastok where uch_name='" + spUchastok.getSelectedItem().toString() + "'");
        String ms_id = getTableId("select ms_id from mastera where ms_name='" + spMaster.getSelectedItem().toString() + "'");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Расшифровка значений переменной
        // delete_or_edit_or_insert=1 -> Update, delete_or_edit_or_insert=2 -> Insert
        String sql = "";

        if (delete_or_edit_or_insert == 2) {
            sql = "INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES " +
                    "('" + eDocNum.getText()
                    + "','" + eDate.getText()
                    + "'," + uch_id + "," + ms_id + ",'" + eZakaz.getText() + "')";
        } else if (delete_or_edit_or_insert == 1) {
            sql = "UPDATE PaintingTasks set pt_number=" + eDocNum.getText() + ",pt_date='" + eDate.getText() + "',uch_id=" + uch_id
                    + ",ms_id=" + ms_id + ",pt_zakaz='" + eZakaz.getText() + "' where pt_id=" + eNPP.getText();
        }

        if (sql != "") {
            db.execSQL(sql);
        }

        finish();

    }

    private String getTableId(String sql) {
        String i = "";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(sql, new String[]{});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i = c.getString(0);
                } while (c.moveToNext());
            }
            c.close();
        }

        return i;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

//    private void setViewDate() {
//
//        eDate.setText(DateUtils.formatDateTime(this,
//                dateForView.getTimeInMillis(),
//                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
////                        | DateUtils.FORMAT_SHOW_TIME
//        ));
//    }

}