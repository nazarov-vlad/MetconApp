package ru.metcon.metconapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnCMP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCMP = (Button) findViewById(R.id.bCMP);
        btnCMP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCMP:
                Intent intent = new Intent(this, CMP.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

//    View.OnClickListener oclBtnOk = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            btnCMP.setText("Нажата кнопка ОК");
//        }
//    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
//        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, Integer.toString(item.getItemId()), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();


        switch (item.getItemId()) {
            case  (R.id.main_settings):
                Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show();
                break;
            case  (R.id.main_users):
                Toast.makeText(this, "Пользователи", Toast.LENGTH_SHORT).show();
                break;
            case  (R.id.main_SUBD):
                Intent intent = new Intent(this, SUBD_Main.class);
                startActivity(intent);
                break;
            case  (R.id.main_JSON):
                Intent intentJSON = new Intent(this, Web_Queryes.class);
                startActivity(intentJSON);
                break;

        }



        return super.onOptionsItemSelected(item);
    }
}