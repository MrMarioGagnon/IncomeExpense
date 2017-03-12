package com.mg.incomeexpense.utility;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;
import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ApplicationConstant;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IncomeExpenseDbHelper;

import org.threeten.bp.LocalDateTime;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mario on 2016-07-19.
 */
public class UtilityActivity extends AppCompatActivity implements DatabaseMaintenanceListener {

    public static enum CurrentAction {
        None, OnCreate,OnRestore, OnBackup;
    }

    private static final String LOG_TAG = UtilityActivity.class.getSimpleName();

    private static final int REQUEST_CODE_OPENER = 1;
    private final View.OnClickListener mButtonOnClickListener;
    private final DatabaseMaintenanceGoogleDrive mCloudClient;
    private Button mButtonBackup;
    private Button mButtonRestore;
    private CurrentAction mCurrentAction;
    private DriveId mDriveIdForRestore;

    public UtilityActivity() {

        mCloudClient = new DatabaseMaintenanceGoogleDrive(this);
        mCloudClient.addListener(this);

        mButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_backup:
                        startBackup();
                        break;
                    case R.id.button_restore:
                        startRestore();
                        break;
                }
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentAction = CurrentAction.OnCreate;

        setContentView(R.layout.utility_activity);

        mButtonBackup = (Button) findViewById(R.id.button_backup);
        mButtonRestore = (Button) findViewById(R.id.button_restore);

        mButtonBackup.setOnClickListener(mButtonOnClickListener);
        mButtonBackup.setEnabled(false);

        mButtonRestore.setOnClickListener(mButtonOnClickListener);
        mButtonRestore.setEnabled(false);
    }

    private void startBackup() {
        mCurrentAction = CurrentAction.OnBackup;
        mCloudClient.backup(IncomeExpenseDbHelper.getDatabaseFullPath());
    }

    private void startRestore() {
        mCurrentAction = CurrentAction.OnRestore;
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setSelectionFilter(Filters.contains(SearchableField.TITLE, ".db"))
                .build(mCloudClient.getGoogleApiClient());
        try {
            startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.w(LOG_TAG, "Unable to send intent", e);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mCloudClient.connect();
    }

    @Override
    public void onPause() {
        if (mCloudClient != null) {
            mCloudClient.close();
        }
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK) {

                    mDriveIdForRestore = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    Log.i(LOG_TAG, "Selected file's ID: " + mDriveIdForRestore);

                    if(mCloudClient.isConnected()) {
                        tryRestore();
                    }else{
                        mCloudClient.connect();
                    }

                }else{
                    mCurrentAction = CurrentAction.None;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnected() {

        switch (mCurrentAction){
            case None:
                break;
            case OnBackup:
                break;
            case OnRestore:
                tryRestore();
                break;
            case OnCreate:
                mButtonBackup.setEnabled(true);
                mButtonRestore.setEnabled(true);
                break;
        }
    }

    private void tryRestore(){
        mCloudClient.restore(mDriveIdForRestore, IncomeExpenseDbHelper.getDatabaseFullPath());
    }

    @Override
    public void onDatabaseBackuped() {
        mCurrentAction = CurrentAction.None;
    }

    @Override
    public void onDatabaseRestored() {
        mCurrentAction = CurrentAction.None;
    }

    @Override
    public void onError() {

    }
}
