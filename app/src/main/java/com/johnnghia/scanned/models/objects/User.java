package com.johnnghia.scanned.models.objects;

import android.util.Log;
import android.widget.Toast;

public class User {
    private String user = "", pass = "", ho = "", ten = "";

    public User(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public User(String user, String pass, String ho, String ten) {
        this.user = user;
        this.pass = pass;
        this.ho = ho;
        this.ten = ten;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int checkUser(){
        /*function return:
        0: null objects
        -1: User empty (is not real)
        -2: Length of user is not suitable
        -3: User is not a email
        -4, -5: Fail at pass
         */

//        if(user == null || pass == null){
//            Log.e("Check user", "object is null");
//            return 0;
//        }

        if(user.equals("")){
            return -1;
        }

        if(user.length() < 6 || user.length() > 320){
            return -2;
        }

        if(!user.contains("@")){
            return -3;
        }

        if(pass.equals("")){
            return -4;
        }

        if(pass.length() < 6 || pass.length() > 255){
            return -5;
        }

        return 1;
    }

    public int checkInfor(){
        if(checkUser() != 1){
            return checkUser();
        } else {
            if(ho.equals("")){
                return -6;
            }
            if(ten.equals("")){
                return -7;
            }
            return 1;
        }
    }
}
