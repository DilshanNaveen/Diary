package com.example.diary.app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diary.app.R;

import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    EditText subjectEt,descriptionEt;
    Button cancelBt,updateBt;
    SQLiteHelper dbUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);

        //passing Update Activity's context to database alass
        dbUpdate = new SQLiteHelper(this);
        SQLiteDatabase sqliteDatabase = dbUpdate.getWritableDatabase();

        subjectEt = findViewById(R.id.subjectEditTextIdUpdate);
        descriptionEt = findViewById(R.id.descriptionEditTextIdUpdate);

        cancelBt = findViewById(R.id.cacelButtonIdUpdate);
        updateBt = findViewById(R.id.saveButtonIdUpdate);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String sub = intent.getStringExtra("subject");
        String des = intent.getStringExtra("description");
        final String id = intent.getStringExtra("listId");


        subjectEt.setText(sub);
        descriptionEt.setText(des);

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //for updating database data
        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String d = (String) android.text.format.DateFormat.format("dd/MM/yyyy  hh:mm:ss",date);

                if(dbUpdate.update(subjectEt.getText().toString(),descriptionEt.getText().toString(),d,id, MainActivity.USER_ID)==true){
                    Toast.makeText(getApplicationContext(),"Data updated",Toast.LENGTH_SHORT).show();
                    backToMain();
                }
            }
     });
    }

    //this method to clearing top activity and starting new activity
    public void backToMain()
    {
        Intent intent = new Intent(UpdateActivity.this, DiaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
