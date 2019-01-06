package net.ayman.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import net.ayman.R;
import net.ayman.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddDataActivity extends AppCompatActivity {


    private Button buttonChoose, buttonSave;
    private Spinner spinnerType;
    private EditText editTextInput;
    private Uri videoUri;
    private TextView textViewFile;
    private DatabaseHelper db;
    private ImageView imageView;

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        db = new DatabaseHelper(this);

        buttonChoose = findViewById(R.id.buttonChooseFile);
        buttonSave = findViewById(R.id.buttonSave);
        spinnerType = findViewById(R.id.spinnerType);
        editTextInput = findViewById(R.id.input);
        textViewFile = findViewById(R.id.textViewFile);
        videoView = findViewById(R.id.videoview);
        imageView = findViewById(R.id.imageView);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                mediaChooser.setType("*/*");
                startActivityForResult(mediaChooser, 101);

            }
        });
    }

    private void saveData() throws IOException {

        String input = editTextInput.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString().trim();

        if (videoUri == null) {
            Toast.makeText(this, "Select the video file first", Toast.LENGTH_LONG).show();
            return;
        }

        if (input.isEmpty()) {
            editTextInput.setError("Field required");
            editTextInput.requestFocus();
            return;
        }

        InputStream is = getContentResolver().openInputStream(videoUri);
        byte[] inputData = getBytes(is);
        is.close();
        String encoded = Base64.encodeToString(inputData, Base64.DEFAULT);

        if (db.saveData(encoded, type, input)) {
            String s = videoUri.toString();

            if (spinnerType.getSelectedItem().toString().equalsIgnoreCase("Alphabet")) {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), videoUri);
                imageView.setImageBitmap(bmp);
            } else {
                videoView.setVideoURI(Uri.parse(s));
                videoView.start();
            }

            Toast.makeText(this, "Data Saved", Toast.LENGTH_LONG).show();

            Log.d("AddDataActivity", videoUri.toString());
        } else
            Toast.makeText(this, "Data already saved", Toast.LENGTH_LONG).show();

    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            videoUri = data.getData();
            if (videoUri != null)
                textViewFile.setText(videoUri.getPath());
        }
    }
}
