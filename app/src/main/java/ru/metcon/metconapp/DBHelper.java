package ru.metcon.metconapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

class DBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "myLogs";
    private static final String DATABASE_NAME = "metcon.db"; // название бд
    private static final int SCHEMA = 7; // версия базы данных


    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // Создаем таблицу заказов
        db.execSQL("create table orders ("
                + "orders_id integer primary key autoincrement not null,"
                + "orders_name integer);");
        //Заполняем локальную таблицу заказов (временно, до написания процедуры синхронизации)
        db.execSQL("INSERT INTO Orders (Orders_name) VALUES ('002669')");
        String orders_id ="";

        String sql = "select max(orders_id) from orders";
        Cursor c = db.rawQuery(sql, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    orders_id = c.getString(0);
                } while (c.moveToNext());
            }
            c.close();
        }


        // Создаем таблицу чертежей
        db.execSQL("create table draft ("
                + "draft_id integer primary key autoincrement not null,"
                + "orders_id   integer NOT NULL REFERENCES orders(orders_id),"
                + "draft_name integer);");
        //Заполняем локальную таблицу чертежей (временно, до написания процедуры синхронизации)
        db.execSQL("INSERT INTO Draft (orders_id,drf_name) VALUES ("+orders_id+"'002669_КМД-51-10')");

        // Создаем таблицу марок
        db.execSQL("create table marks ("
                + "marks_id integer primary key autoincrement not null,"
                + "draft_id   integer NOT NULL REFERENCES draft(draft_id),"
                + "marks_count integer,"    //Количество марок в чертеже
                + "marks_name text);");
        //Заполняем локальную таблицу марок (временно, до написания процедуры синхронизации)
        db.execSQL("INSERT INTO Draft (drf_name) VALUES ('002669_КМД-51-10')");

        // Создаем таблицу справочник участков
        db.execSQL("create table Uchastok ("
                + "uch_id integer primary key autoincrement,"
                + "uch_name text);");
        //Заполняем локальную таблицу участков (временно, до написания процедуры синхронизации)
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Линда 1')");           // - 1
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Линда 2')");           // - 2
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Новая основная')");    // - 3
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Площадка А')");        // - 4
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Площадка Б')");        // - 5
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Площадка В')");        // - 6
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Площадка Г')");        // - 7
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Площадка Цеха №8')");  // - 8
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Простая')");           // - 9
        db.execSQL("INSERT INTO Uchastok (uch_name) VALUES ('Цех малярных работ')");// - 10
        // Создаем локальную таблицу справочник мастеров
        db.execSQL("create table Mastera ("
                + "ms_id integer primary key autoincrement,"
                + "ms_name text);");
        //Заполняем таблицу участков (временно, до написания процедуры синхронизации)
        db.execSQL("INSERT INTO Mastera (ms_name) VALUES ('Агеев Сергей Павлович')");           // - 1
        db.execSQL("INSERT INTO Mastera (ms_name) VALUES ('Сухарев Владислав Николаевич')");    // - 2
        db.execSQL("INSERT INTO Mastera (ms_name) VALUES ('Бурьянова Эльмира Ахадовна')");      // - 3
        db.execSQL("INSERT INTO Mastera (ms_name) VALUES ('Ромасенко Александр Владимирович')");// - 4
        db.execSQL("INSERT INTO Mastera (ms_name) VALUES ('Рябчиков Евгений Александрович')");  // - 5
        db.execSQL("INSERT INTO Mastera (ms_name) VALUES ('Ершов Евгений Александрович')");     // - 6
        // создаем локальную таблицу Задания на окраску
        db.execSQL("create table PaintingTasks ("
                + "pt_id integer primary key autoincrement not null,"
                + "pt_number text,"
                + "pt_date text,"
                + "uch_id  integer NOT NULL REFERENCES Uchastok(uch_id),"
                + "ms_id   integer NOT NULL REFERENCES Mastera(ms_id),"
                + "pt_zakaz text)");
        //Заполняем локальную таблицу заданий на покраску (временно, до написания процедуры синхронизации)
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000103','01.10.2021',1,4,'002667')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000104','02.10.2021',2,6,'002667')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000105','03.10.2021',4,4,'002667')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000106','04.10.2021',5,5,'002666')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000107','05.10.2021',4,4,'002666')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000108','06.10.2021',1,5,'002666')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000109','07.10.2021',2,5,'002653')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000110','08.10.2021',8,4,'002653')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000111','09.10.2021',9,6,'002653')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000112','10.10.2021',4,5,'002653')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000112','10.10.2021',5,3,'002777')");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,pt_zakaz) VALUES ('000112','10.10.2021',2,1,'002777')");

        db.execSQL("create table PaintingTasks ("
                + "pt_id integer primary key autoincrement,"
                + "pt_number text,"
                + "pt_date text,"
                + "uch_id   integer,"
                + "ms_id integer,"
                + "pt_zakaz text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PaintingTasks");
        db.execSQL("DROP TABLE IF EXISTS Uchastok");
        db.execSQL("DROP TABLE IF EXISTS Mastera");
        onCreate(db);
    }
}
