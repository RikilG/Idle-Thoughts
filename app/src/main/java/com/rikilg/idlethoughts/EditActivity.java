package com.rikilg.idlethoughts;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    Button btnBack;
    Button btnSave;
    EditText etContent;

    Context context;
    String networkStatus;
    //Intent returnIntent;

    final int PURPOSE_SHOW = 1;
    final int PURPOSE_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        etContent = findViewById(R.id.etContent);
        context = this;

        //final String content = getIntent().getStringExtra("content");
        FileIO fileIO = new FileIO(this,getResources().getString(R.string.local_filename));
        String content = fileIO.read(PURPOSE_EDIT);
        networkStatus = getIntent().getStringExtra("networkStatus");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                Toast.makeText(context, "Changes discarded", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newContent = etContent.getText().toString();
                FileIO fileIO = new FileIO(context, getResources().getString(R.string.local_filename));
                fileIO.write(newContent);
                //returnIntent = new Intent();
                //returnIntent.putExtra();
                //setResult(RESULT_OK,returnIntent);
                setResult(RESULT_OK);
                finish();
            }
        });

        etContent.setText(content);
    }

}
