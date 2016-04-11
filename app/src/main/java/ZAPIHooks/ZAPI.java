package ZAPIHooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.List;

/**
 * Created by Zacc on 2016-04-06.
 */

public final class ZAPI {

    private static GoogleApiClient mGoogleApiClient;
    private static ArrayList<ZAPIHook> lZAPIHooks;

    //Prevents instantiation
    private ZAPI() {
    }

    public static GoogleApiClient getClient() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            return mGoogleApiClient;
        } else if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            int chow = 0;
            while (mGoogleApiClient.isConnecting()) {
            }
            Debug.Error("Client is not connected");
            return null;
        } else {
            Debug.Error("Client has not been initialized");
            return null;
        }
    }

    public static void AddAPI(ZAPIHook zAPIHook) {
        try {
            lZAPIHooks.add(zAPIHook);
        } catch (Exception e) {
            lZAPIHooks = new ArrayList<ZAPIHook>();
            lZAPIHooks.add(zAPIHook);
        }
    }

    //Builds the mGoogleApiClient with all build instructions from the ZAPIHooks listed in lZAPIHooks
    public static void Initialize(FragmentActivity frag, Context context) {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context);
        for (ZAPIHook zAPI : lZAPIHooks) {
            builder = zAPI.buildInstruct().build(builder, context);
        }
        mGoogleApiClient = builder.enableAutoManage(frag, ConnectionFailedListener.get()).build();
    }

    //Attempts to connect to the mGoogleApiClient
    public static void connect() {
        try {
            mGoogleApiClient.registerConnectionCallbacks(ConnectionCallback.get());
            mGoogleApiClient.registerConnectionFailedListener(ConnectionFailedListener.get());
            mGoogleApiClient.connect();
        } catch (Exception e) {
            Debug.Error("Connection Critically Failed");
            Debug.Status("Make sure ZAPI has been Initialized at least once.");
        }
    }


    private static class ConnectionCallback implements ConnectionCallbacks {
        private static ConnectionCallback obj = null;

        public static ConnectionCallback get() {
            if (obj == null) {
                obj = new ConnectionCallback();
            }
            return obj;
        }

        //Prevents instantiation
        private ConnectionCallback() {
        }

        //Runs upon connection to the mGoogleApiClient
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Debug.Status("Connected to services");

        }

        //Handles suspended connections from the mGoogleApiClient
        //(Aside: I have never received a status code from the function, not entirely sure of it's purpose)
        @Override
        public void onConnectionSuspended(int i) {
            Debug.Status("Connection to services suspended (" + i + ")");
        }
    }

    private static class ConnectionFailedListener implements OnConnectionFailedListener {

        private static ConnectionFailedListener obj = null;

        public static ConnectionFailedListener get() {
            if (obj == null) {
                obj = new ConnectionFailedListener();
            }
            return obj;
        }

        //Prevents instantiation
        private ConnectionFailedListener() {
        }

        //Handles failed connections to the mGoogleApiClient
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Debug.Error("Connection failure(" + connectionResult + ")");

            if (connectionResult.hasResolution()) {
                Debug.Status("Attempting to resolve");
                try {
                    connectionResult.startResolutionForResult(new Activity(), connectionResult.getErrorCode());
                    Debug.Status("resolution successful");
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    Debug.Error("Failed to resolve");
                }
            } else {
                Debug.Error("No available solution");
            }
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK){
            mGoogleApiClient.connect();
        }else{
            Toaster("Attempted to resolve; Resolution not OK");

        }
    }*/


    //A ZAPIHook is a class that handles a single Google API.
    //A Google API requires specific build instructions when the mGoogleApiClient is initialized
    //As such, each ZAPIHook requires the BuildInstruct interface to be overridden with the appropriate instructions
    public interface ZAPIHook {
        public BuildInstruct buildInstruct();
    }

    public interface BuildInstruct {
        public GoogleApiClient.Builder build(GoogleApiClient.Builder builder, Context context);
    }
}