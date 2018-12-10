package net.ayman.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.ayman.R;

public class AddDataActivity extends AppCompatActivity {


    private Button buttonChoose, buttonSave;
    private Spinner spinnerType;
    private EditText editTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);


        buttonChoose = findViewById(R.id.buttonChooseFile);
        buttonSave = findViewById(R.id.buttonSave);
        spinnerType = findViewById(R.id.spinnerType);
        editTextInput = findViewById(R.id.input);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AddDataActivity.this, "Saved", Toast.LENGTH_LONG).show();
            }
        });

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                mediaChooser.setType("video/*, image/*");
                startActivityForResult(mediaChooser, 101);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
