package com.johnnghia.scanned;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.johnnghia.scanned.models.objects.TextFile;
import com.johnnghia.scanned.models.services.MCFirebaseResourceTool;
import com.johnnghia.scanned.utils.MyAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 9001;
    private static final int GALLERY_REQUEST_CODE = 400;
    private static final int EDIT_REQUEST_CODE = 201;
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rvFiles;
    private MyAdapter adapter;
    private FloatingActionButton mAdd;
    private AlertDialog mLoadingDialog;
    private Handler UIHandler;

    private FirebaseAuth mAuth;
    private MCFirebaseResourceTool mcFirebaseResourceTool;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mcFirebaseResourceTool = new MCFirebaseResourceTool(getApplicationContext());

        rvFiles = findViewById(R.id.rv_files);
        mAdd = findViewById(R.id.fab_add);
        // show dialog
        UIHandler = new Handler();
        mLoadingDialog = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setTitle("Getting data ...")
                .setView(R.layout.loading_layout)
                .create();


        // Prepair to get all data from server or local
        mLoadingDialog.show(); // ---> Show AlertDialog
        adapter = new MyAdapter();
        mcFirebaseResourceTool.getAllServerResource(adapter, UIHandler, dismissLoadingDialog);

        setUpRecyclerview(adapter);
        getTextFile(adapter);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickResource();
            }
        });
    }

    private void getTextFile(MyAdapter adapter) {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("Title")) {
                String title = intent.getStringExtra("Title");
                String text = intent.getStringExtra("Result");
                //TODO: Upload text  to database

                mcFirebaseResourceTool.sendServerResource(title, text);
            }
        }
    }

    private void pickResource() {
        String[] items = {"Camera", "Gallery"};
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                                                .setTitle("Select image")
                                                .setItems(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (which == 0) {
                                                            //camera option
                                                            if (!isCameraPermissionGranted()){
                                                                requestCameraPermission();
                                                            }
                                                            else{
                                                                pickCamera();
                                                            }
                                                        }
                                                        else{
                                                            if (!isGalleryPermissionGranted()){
                                                                requestGalleryPermission();
                                                            }
                                                            else{
                                                                pickGallery();
                                                            }
                                                        }
                                                    }
                                                })
                                                .create();
        alertDialog.show();
    }

    private void pickGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        pickGallery();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }


    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            pickCamera();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private boolean isGalleryPermissionGranted() {
        return  ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private boolean isCameraPermissionGranted() {
        boolean cameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean writeExternalPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraPermission && writeExternalPermission;
    }



    private void setUpRecyclerview(final MyAdapter adapter) {
        rvFiles.setLayoutManager(new LinearLayoutManager(this));
        adapter.setListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onShareClick(int position) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, adapter.getItem(position).getText());
                sendIntent.setType("text/plain");
                Intent chooser = Intent.createChooser(sendIntent, getString(R.string.share));
                if (sendIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(chooser);
                }
            }

            @Override
            public void onItemClick(int position) {
                Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
                editIntent.putExtra("file",adapter.getItem(position));
                startActivityForResult(editIntent,EDIT_REQUEST_CODE);
            }
        });
        rvFiles.setAdapter(adapter);
        rvFiles.setHasFixedSize(true);
        rvFiles.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mAdd.getVisibility() == View.VISIBLE) {
                    mAdd.hide();
                } else if (dy < 0 && mAdd.getVisibility() != View.VISIBLE) {
                    mAdd.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_log_out){
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //crop image
                case CAMERA_REQUEST_CODE:
                    CropImage.activity(imageUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                    break;
                case GALLERY_REQUEST_CODE:
                    CropImage.activity(data.getData())
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                    break;
                case EDIT_REQUEST_CODE:
                    Log.d(TAG, "onActivityResult: ok");
                    mLoadingDialog.show(); // ---> Show AlertDialog
                    mcFirebaseResourceTool.getAllServerResource(adapter, UIHandler, dismissLoadingDialog);
                    break;
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Intent previewIntent = new Intent(MainActivity.this, PreviewActivity.class);
                previewIntent.putExtra("image", resultUri.toString());
                startActivity(previewIntent);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Test Dialog progress
    // Dismiss Loading dialog
    Runnable dismissLoadingDialog = new Runnable() {
        @Override
        public void run() {
            mLoadingDialog.dismiss();
        }
    };
//
//    // Thread getdata in background
//    Runnable getData = new Runnable() {
//        @Override
//        public void run() {
//
//        }
//    };

}
