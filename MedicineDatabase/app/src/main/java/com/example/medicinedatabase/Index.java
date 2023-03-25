package com.example.medicinedatabase;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Index extends AppCompatActivity {

    private Button insert;
    private Button about, showData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        insert = findViewById(R.id.insertBtn);
        about = findViewById(R.id.aboutBtn);
        showData = findViewById(R.id.ShowData);

        // starting other activity when clicked on insert
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Index.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // about method click listener
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutUs();
            }
        });

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index.this, DisplayData.class);
                startActivity(intent);
            }
        });
    }

    private void aboutUs() {

        // Creating a dialog box
        final Dialog dialog = new Dialog(Index.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        // setting design file to this dialog
        dialog.setContentView(R.layout.custom_dialog);
        Button okbtn = dialog.findViewById(R.id.okbtn);

        // disappearing dialog when clicked on ok button
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

