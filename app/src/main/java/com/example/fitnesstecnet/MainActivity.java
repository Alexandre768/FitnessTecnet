package com.example.fitnesstecnet;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
public class MainActivity extends AppCompatActivity {
private View Btnic;
    private View Btni;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Btnic=findViewById(R.id.BotaoImc);
        Btni=findViewById(R.id.Botao);
        Btni.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this,TbmActivity.class);
            startActivity(intent);
        });


        Btnic.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ImcActivity.class);
            startActivity(intent);
        });
        }
    }

