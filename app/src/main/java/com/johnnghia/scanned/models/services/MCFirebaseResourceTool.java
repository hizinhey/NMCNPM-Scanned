package com.johnnghia.scanned.models.services;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.johnnghia.scanned.models.objects.TextFile;
import com.johnnghia.scanned.models.objects.User;
import com.johnnghia.scanned.utils.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


// Thuc hien tuong tac du lieu TextFile voi FirebaseDatabase
public class MCFirebaseResourceTool {
    private Context context;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase dbInstance = FirebaseDatabase.getInstance();

    public MCFirebaseResourceTool(Context context) {
        this.context = context;
    }

    public void createUserFirebaseDB(User user){
        if(mAuth.getUid() != null){
            DatabaseReference dbReference = dbInstance.getReference("users");

            dbReference.child(mAuth.getUid()).setValue(user);
            Toast.makeText(context, "Tạo tài khoản thành công.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Tài khoản chưa đăng nhập.", Toast.LENGTH_SHORT).show();
        }

    }

    public void sendServerResource(TextFile _data){
        if(_data == null || mAuth.getUid() == null){
            //TODO: Loi du lieu
            Log.e("List status (send)", "Empty");
        } else {
            //TODO: Gửi dữ liệu lên server
            DatabaseReference dbReference = dbInstance.getReference("resource").child(mAuth.getUid());
            //dbReference.keepSynced(true);

            String dataID = dbReference.push().getKey();
            dbReference.child(dataID).setValue(_data);

            Toast.makeText(context, "Update du lieu thanh cong.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllServerResource(final MyAdapter adapter){
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("resource");
        adapter.clear();
        databaseReference.keepSynced(true);
        if(mAuth.getUid() == null){
            //TODO; Loi du lieu
            Log.e("List status (get)", "Empty");
        } else {
            databaseReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        TextFile textFile = child.getValue(TextFile.class);
                        adapter.add(textFile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Cancel", "Cancel");
                }
            });
        }
    }


    // Test send and receiver data to firebase server
//    public static void test(){
//        MCFirebaseResourceTool mcFirebaseResourceTool = new MCFirebaseResourceTool();
//        try{
//            Log.i("Username", mAuth.getUid() + "");
//        } catch (NullPointerException e){
//            Log.e("error", "Userid: " + e);
//        }
//    }
}
