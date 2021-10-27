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
    private static final int SCHEMA = 10; // версия базы данных


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
        db.execSQL("INSERT INTO Orders (Orders_name) VALUES ('002400')");
        db.execSQL("INSERT INTO Orders (Orders_name) VALUES ('002570')");
        db.execSQL("INSERT INTO Orders (Orders_name) VALUES ('002669')");

        String orders_id = GetMaxId("select min(orders_id) from orders",db);

         // Создаем таблицу чертежей
        db.execSQL("create table drafts ("
                + "drafts_id integer primary key autoincrement not null,"
                + "orders_id   integer NOT NULL REFERENCES orders(orders_id),"
                + "drafts_name integer);");
        //Заполняем локальную таблицу чертежей (временно, до написания процедуры синхронизации)
        db.execSQL("INSERT INTO drafts (orders_id,drafts_name) VALUES ("+orders_id+",'002669_КМД-52-12')");
        String drafts_id = GetMaxId("select max(drafts_id) from drafts",db);

        // Создаем таблицу марок
        db.execSQL("create table marks ("
                + "marks_id integer primary key autoincrement not null,"
                + "drafts_id   integer NOT NULL REFERENCES drafts(drafts_id),"
                + "marks_count integer,"    //Количество марок в чертеже
                + "marks_name text);");
        //Заполняем локальную таблицу марок (временно, до написания процедуры синхронизации)
        db.execSQL("INSERT INTO marks (drafts_id,marks_count,marks_name) VALUES ("+drafts_id+",10,'002669_КМД-52-12_ГС1-19')");
        db.execSQL("INSERT INTO marks (drafts_id,marks_count,marks_name) VALUES ("+drafts_id+",9,'002669_КМД-52-12_ГС1-17')");

        db.execSQL("INSERT INTO drafts (orders_id,drafts_name) VALUES ("+orders_id+",'002669_КМД-138-12')");
        drafts_id = GetMaxId("select max(drafts_id) from drafts",db);

        db.execSQL("INSERT INTO marks (drafts_id,marks_count,marks_name) VALUES ("+drafts_id+",8,'002669_КМД-138-12_Ф2-44')");
        db.execSQL("INSERT INTO marks (drafts_id,marks_count,marks_name) VALUES ("+drafts_id+",7,'002669_КМД-138-12_Ф2-45')");

        db.execSQL("INSERT INTO drafts (orders_id,drafts_name) VALUES ("+orders_id+",'002669_КМД-51-10')");
        drafts_id = GetMaxId("select max(drafts_id) from drafts",db);

        db.execSQL("INSERT INTO marks (drafts_id,marks_count,marks_name) VALUES ("+drafts_id+",8,'0002669_КМД-51-10_ГС1-23')");
        db.execSQL("INSERT INTO marks (drafts_id,marks_count,marks_name) VALUES ("+drafts_id+",7,'002669_КМД-51-10_ГС1-24')");


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
        db.execSQL("create table paintingTasks ("
                + "pt_id integer primary key autoincrement not null,"
                + "pt_number text,"
                + "pt_date text,"
                + "uch_id    integer NOT NULL REFERENCES Uchastok(uch_id),"
                + "ms_id     integer NOT NULL REFERENCES Mastera(ms_id),"
                + "orders_id integer NOT NULL REFERENCES orders(orders_id))");
        db.execSQL("create table PaintingTasksCnt ("
                + "ptc_id integer primary key autoincrement not null,"
                + "pt_id  integer NOT NULL REFERENCES PaintingTasks(pt_id),"
                + "drafts_id integer NOT NULL REFERENCES drafts(drafts_id),"
                + "marks_id integer NOT NULL REFERENCES marks(marks_id),"
                + "ptc_value integer)");


        //Заполняем локальную таблицу заданий на покраску (временно, до написания процедуры синхронизации)
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000103','01.10.2021',1,4,"+orders_id+")");
        String pt_id = GetMaxId("select max(pt_id) from PaintingTasks",db);
        String marks_id = GetMaxId("select max(marks_id) from marks where drafts_id="+drafts_id,db);
        db.execSQL("INSERT INTO PaintingTasksCnt (pt_id,drafts_id,marks_id,ptc_value) VALUES ("+pt_id+","+drafts_id+","+marks_id+",4)");
        marks_id = GetMaxId("select max(marks_id) - 1 from marks where drafts_id="+drafts_id,db);
        db.execSQL("INSERT INTO PaintingTasksCnt (pt_id,drafts_id,marks_id,ptc_value) VALUES ("+pt_id+","+drafts_id+","+marks_id+",5)");


        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000104','02.10.2021',2,6,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000105','03.10.2021',4,4,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000106','04.10.2021',5,5,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000107','05.10.2021',4,4,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000108','06.10.2021',1,5,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000109','07.10.2021',2,5,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000110','08.10.2021',8,4,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000111','09.10.2021',9,6,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000112','10.10.2021',4,5,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000112','10.10.2021',5,3,"+orders_id+")");
        db.execSQL("INSERT INTO PaintingTasks (pt_number,pt_date,uch_id,ms_id,orders_id) VALUES ('000112','10.10.2021',2,1,"+orders_id+")");



    }
    private String GetMaxId(String sql,SQLiteDatabase db){
        String _id ="0";

        Cursor c = db.rawQuery(sql, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    _id = c.getString(0);
                } while (c.moveToNext());
            }
            c.close();
        }
        return _id;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PaintingTasksCnt");
        db.execSQL("DROP TABLE IF EXISTS PaintingTasks");
        db.execSQL("DROP TABLE IF EXISTS Uchastok");
        db.execSQL("DROP TABLE IF EXISTS Mastera");
        db.execSQL("DROP TABLE IF EXISTS marks");
        db.execSQL("DROP TABLE IF EXISTS drafts");
        db.execSQL("DROP TABLE IF EXISTS Orders");

        onCreate(db);
    }
}
