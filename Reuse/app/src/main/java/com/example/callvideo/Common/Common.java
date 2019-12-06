package com.example.callvideo.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.callvideo.Model.Entities.User;

public class Common {
  public static User currentUser;
  public static final String UPDATE = "Update";
  public static final String DELETE= "Delete";
  public static final String USER_KEY = "User";
  public static final String PWD_KEY= "Password";
  public static final int LOGIN_SUCCESS=1;
  public static final int LOGIN_FAILED=0;
  public static final int WRONG_PASS=2;
  public static final int CHECK_ACCOUNT=0;

  public static final int PICK_IMAGE_REQUEST=71;
  public static boolean isConnectedToInternet(Context context){
    ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if(connectivityManager!=null){
      NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();
      if(infos!=null){
        for (int i=0;i<infos.length;i++){
          if (infos[i].getState()==NetworkInfo.State.CONNECTED)
            return true;
        }
      }
    }
    return false;
  }

}
