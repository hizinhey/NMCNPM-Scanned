package com.johnnghia.scanned.models.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.johnnghia.scanned.models.objects.TextFile;
import com.johnnghia.scanned.models.objects.User;
import com.johnnghia.scanned.utils.MyAdapter;

import static android.content.ContentValues.TAG;


// Thuc hien tuong tac du lieu TextFile voi FirebaseDatabase
public class MCFirebaseResourceTool {
    private Context context;
    public boolean connected = false; // Connection status of server


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MCFirebaseResourceTool(Context context) {
        this.context = context;



        // Listen to connection
        if(mAuth.getUid() != null){
            connected = true;
        }
        checkConnection();
    }

    private FirebaseDatabase dbInstance;

//    public void getSync(boolean isChecked){
//        dbInstance = FirebaseDatabase.getInstance();
//
//        if(mAuth.getUid() != null){
//            DatabaseReference databaseRef = dbInstance.getReference("resource").child(mAuth.getUid());
//            databaseRef.keepSynced(isChecked);
//        } else {
//            Toast.makeText(context, "You must be logged.", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    public void createUserFirebaseDB(User user){
        if(mAuth.getUid() != null){
            dbInstance = FirebaseDatabase.getInstance();
            DatabaseReference dbReference = dbInstance.getReference("users");

            dbReference.child(mAuth.getUid()).setValue(user);
            Toast.makeText(context, "Tạo tài khoản thành công.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Tài khoản chưa đăng nhập.", Toast.LENGTH_SHORT).show();
        }


    }

    private void checkConnection(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connect = snapshot.getValue(Boolean.class);
                if (connect) {
                    connected = true;
                } else {
                    connected = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Listener was cancelled");
            }
        });
    }



    public void sendServerResource(TextFile _data){
        if(_data == null || mAuth.getUid() == null){
            //TODO: Loi du lieu
            Log.e("List status (send)", "Empty");
        } else {
            //TODO: Gửi dữ liệu lên server
            dbInstance = FirebaseDatabase.getInstance();
            DatabaseReference dbReference = dbInstance.getReference("resource").child(mAuth.getUid());
            //dbReference.keepSynced(true);

            String dataID = dbReference.push().getKey();
            dbReference.child(dataID).setValue(_data);

            Toast.makeText(context, "Update du lieu thanh cong.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllServerResource(final MyAdapter adapter, final Handler handler, final Runnable endDialog){
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("resource");
        //databaseReference.keepSynced(true);
        if(mAuth.getUid() == null){
            //TODO; Loi du lieu
            Log.e("List status (get)", "Empty");
            handler.post(endDialog);
        } else {
            databaseReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapter.clear();
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        TextFile textFile = child.getValue(TextFile.class);
                        adapter.add(textFile);
                    }
                    handler.post(endDialog);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Cancel", "Cancel");
                    handler.post(endDialog);
                }
            });
        }
    }

    public Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}
