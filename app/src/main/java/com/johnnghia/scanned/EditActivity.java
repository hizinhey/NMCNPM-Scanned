package com.johnnghia.scanned;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.johnnghia.scanned.models.objects.TextFile;
import com.johnnghia.scanned.models.services.MCFirebaseResourceTool;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {


    private MCFirebaseResourceTool mcFirebaseResourceTool;
    private TextFile textFile;
    private TextView tvDate;
    private TextView tvTitle;
    private EditText edtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mcFirebaseResourceTool = new MCFirebaseResourceTool(getApplicationContext());

        tvDate = findViewById(R.id.tv_date);
        tvTitle = findViewById(R.id.tv_title);
        edtContent = findViewById(R.id.edt_content);

        Intent intent = getIntent();
        textFile = (TextFile)intent.getSerializableExtra("file");

        String date = DateFormat.getDateInstance().format(textFile.getDate());
        tvDate.setText(date);
        tvTitle.setText(textFile.getTitle());
        edtContent.setText(textFile.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_check){
            Intent returnedIntent = new Intent();
            textFile.setText(edtContent.getText().toString());
            textFile.setDate(new Date());
            mcFirebaseResourceTool.updateServerResource(textFile);
            setResult(Activity.RESULT_OK, returnedIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
