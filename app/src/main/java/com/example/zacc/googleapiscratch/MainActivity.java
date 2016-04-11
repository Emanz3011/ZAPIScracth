package com.example.zacc.googleapiscratch;

import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.*;
import com.google.android.gms.drive.DriveApi.*;
import com.google.android.gms.drive.DriveFolder.*;


import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;

import ZAPIHooks.ZAPI;
import ZAPIHooks.ZAPIDrive;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZAPI.AddAPI(new ZAPIDrive());
        ZAPI.Initialize(this,this);
        ZAPI.connect();

        ZAPIDrive zDrive = new ZAPIDrive();
        zDrive.CreateFile(ZAPI.getClient(),"File", new ArrayList<String>());

    }

    public void RunDriveRequest() {
    }
    public void Toaster(String result) {
        Toast.makeText(this, "Result:" + result, Toast.LENGTH_LONG).show();
        System.out.println("Toaster: " + result);
    }

    public void BtnQuery(View view){
        RunDriveRequest();
    }
}
