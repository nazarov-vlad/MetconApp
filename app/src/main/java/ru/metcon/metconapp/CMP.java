package ru.metcon.metconapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CMP extends AppCompatActivity implements View.OnClickListener{
    Button bPaintingTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmp);
        bPaintingTasks = (Button) findViewById(R.id.bPaintingTasks);
        bPaintingTasks.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bPaintingTasks:
                Intent intent = new Intent(this, PaintingTasks.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}