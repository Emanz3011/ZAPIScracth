package ZAPIHooks;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.*;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Zacc on 2016-04-06.
 */
public class ZAPIDrive implements ZAPI.ZAPIHook, ResultCallback{

    public void CreateFile(GoogleApiClient mClient, String fileName, ArrayList<String> fileContents) {
        Drive.DriveApi.newDriveContents(ZAPI.getClient()).setResultCallback(new uponDriveContentsRequest());
    }

    private class uponDriveContentsRequest implements ResultCallback<DriveContentsResult>{
        @Override
        public void onResult(@NonNull DriveContentsResult driveContentsResult) {
            if (!driveContentsResult.getStatus().isSuccess()) {
                Debug.Error("Error while trying to create new file contents");
                return;
            }else{
                Debug.Status("Successfully created new file contents");
            }
        }
    }


    @Override
    public ZAPI.BuildInstruct buildInstruct() {
        return new ZAPI.BuildInstruct() {
            @Override
            public GoogleApiClient.Builder build(GoogleApiClient.Builder builder, Context context) {
                builder.addApi(Drive.API).addScope(Drive.SCOPE_FILE);
                return builder;
            }

        };
    }

    @Override
    public void onResult(@NonNull Result result) {
        Debug.Status("Receiving a result with content code:"+result.getStatus().describeContents());
    }
}