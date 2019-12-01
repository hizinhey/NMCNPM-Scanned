package com.johnnghia.scanned;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PreviewActivity extends AppCompatActivity {

    private ImageView mSource;
    private EditText mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mSource = findViewById(R.id.iv_src);
        mResult = findViewById(R.id.edt_result);

        getContentFromImage();

    }

    private void getContentFromImage() {
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("image");
        Uri imageUri = Uri.parse(imagePath);
        mSource.setImageURI(imageUri);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) mSource.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()){
            mResult.setText("Error");
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < items.size(); i++){
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue())
                        .append("\n");
            }
            mResult.setText(sb.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_save){
            TextInputLayout textInputLayout = new TextInputLayout(this);
            textInputLayout.setHint("Title");
            textInputLayout.setPadding(getResources().getDimensionPixelOffset(R.dimen.dp_19),
                                        0,
                                        getResources().getDimensionPixelOffset(R.dimen.dp_19),
                                        0);
            final TextInputEditText titleEditText = new TextInputEditText(this);
            textInputLayout.addView(titleEditText);

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                                                        .setTitle("Save")
                                                        .setMessage("Please enter your file's title")
                                                        .setView(textInputLayout)
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String title = titleEditText.getText().toString();
                                                                String text = mResult.getText().toString();
                                                                if (TextUtils.isEmpty(title)){
                                                                    title = "File";
                                                                }
                                                                Intent intent = new Intent(PreviewActivity.this, MainActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                intent.putExtra("Title", title);
                                                                intent.putExtra("Result", text);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        })
                                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
