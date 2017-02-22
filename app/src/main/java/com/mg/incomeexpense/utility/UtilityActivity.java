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
public class UtilityActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = UtilityActivity.class.getSimpleName();
//  private static final int REQUEST_WRITE_STORAGE = 112;

    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final int REQUEST_CODE_OPENER = 1;
    private final ResultCallback<DriveApi.MetadataBufferResult> mRootFolderMetadataResultCallback;
    private final ResultCallback<DriveFolder.DriveFolderResult> mBackupFolderCreatedCallback;
    private final ResultCallback<DriveResource.MetadataResult> mNewFileMetadataResultCallback;
    private final ResultCallback<DriveFolder.DriveFileResult> mFileCreatedCallback;
    private final ResultCallback<DriveApi.DriveContentsResult> mDriveContentsCallback;
    private final ResultCallback<DriveApi.MetadataBufferResult> mBackupFolderMetadataResultCallback;
    private final View.OnClickListener mButtonOnClickListener;
    //    private ListView mResultsListView;
//    private ResultsAdapter mResultsAdapter;
    private GoogleApiClient mGoogleApiClient;
    private DriveId mFolderId;

    public UtilityActivity() {

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

        mRootFolderMetadataResultCallback = new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult result) {

                if (!result.getStatus().isSuccess()) {
                    Log.e(LOG_TAG, "****** Problem while retrieving metadata ******");
                    return;
                }

                for (Metadata md : result.getMetadataBuffer()) {
                    if (md.getTitle().equals("IncomeExpense") && md.isFolder() && !md.isExplicitlyTrashed()) {
                        mFolderId = md.getDriveId();
                        break;
                    }
                }

                if (mFolderId == null) {
                    Log.i(LOG_TAG, "Folder IncomeExpense not found");
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("IncomeExpense").build();
                    Drive.DriveApi.getRootFolder(mGoogleApiClient)
                            .createFolder(mGoogleApiClient, changeSet)
                            .setResultCallback(mBackupFolderCreatedCallback);
                    return;
                }

                result.release();

                getBackupFolderFilesInfo();
            }
        };

        mBackupFolderCreatedCallback = new ResultCallback<DriveFolder.DriveFolderResult>() {
            @Override
            public void onResult(DriveFolder.DriveFolderResult result) {
                if (!result.getStatus().isSuccess()) {
                    Log.e(LOG_TAG, "****** Error while trying to create the folder ******");
                    return;
                }
                mFolderId = result.getDriveFolder().getDriveId();
                Log.i(LOG_TAG, "Created a folder: " + result.getDriveFolder().getDriveId());

                getBackupFolderFilesInfo();
            }
        };

        mNewFileMetadataResultCallback = new ResultCallback<DriveResource.MetadataResult>() {
            @Override
            public void onResult(@NonNull DriveResource.MetadataResult metadataResult) {

                if (!metadataResult.getStatus().isSuccess()) {
                    Log.e(LOG_TAG, "****** Error getting info for new file");
                    return;
                }

                Log.i(LOG_TAG, "New file created:" + metadataResult.getMetadata().getTitle());
            }
        };


        mFileCreatedCallback =
                new ResultCallback<DriveFolder.DriveFileResult>() {
                    @Override
                    public void onResult(DriveFolder.DriveFileResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e(LOG_TAG, "Error while trying to create the file");
                            return;
                        }

                        DriveFile d = result.getDriveFile().getDriveId().asDriveFile();

                        d.getMetadata(mGoogleApiClient)
                                .setResultCallback(mNewFileMetadataResultCallback);

                    }
                };

        mDriveContentsCallback =
                new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e(LOG_TAG, "****** Error while trying to create new file contents ******");
                            return;
                        }
                        DriveFolder folder = mFolderId.asDriveFolder();

                        final DriveContents driveContents = result.getDriveContents();

                        OutputStream outputStream = driveContents.getOutputStream();

                        byte[] out = Tools.readBytesFromFile(IncomeExpenseDbHelper.getDatabaseFullPath());
                        try {
                            outputStream.write(out);
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String now = Tools.formatDateTime(LocalDateTime.now(), ApplicationConstant.dateTimeFormat);

                        String destinationFilePath = "incexp" + now + ".db";

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(destinationFilePath)
                                .setStarred(true).build();
                        folder.createFile(mGoogleApiClient, changeSet, driveContents)
                                .setResultCallback(mFileCreatedCallback);

                    }
                };

        mBackupFolderMetadataResultCallback = new
                ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(DriveApi.MetadataBufferResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e(LOG_TAG, "****** Problem while retrieving files ******");
                            return;
                        }
//                        mResultsAdapter.clear();
//                        mResultsAdapter.append(result.getMetadataBuffer());
                        result.release();
                    }
                };


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.utility_activity);

        Button buttonBackup = (Button) findViewById(R.id.button_backup);
        Button buttonRestore = (Button) findViewById(R.id.button_restore);

        buttonBackup.setOnClickListener(mButtonOnClickListener);
        buttonRestore.setOnClickListener(mButtonOnClickListener);

//        mResultsListView = (ListView) findViewById(R.id.list_view_backup);
//        mResultsAdapter = new ResultsAdapter(this);
//        mResultsListView.setAdapter(mResultsAdapter);


//        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
//        if (!hasPermission) {
//
//            this.requestPermissions(
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_WRITE_STORAGE);
//        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode)
//        {
//            case REQUEST_WRITE_STORAGE: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                    //reload my activity with permission granted or use the features what required the permission
//                } else
//                {
//                    Toast.makeText(this.getParent(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//
//    }

    private void startBackup() {
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(mDriveContentsCallback);

    }

    private void startRestore() {

        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setSelectionFilter(Filters.contains(SearchableField.TITLE, ".db"))
                .build(mGoogleApiClient);
        try {
            startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.w(LOG_TAG, "Unable to send intent", e);
        }

    }

    private void getBackupFolderFilesInfo() {

        DriveFolder folder = mFolderId.asDriveFolder();
        folder.listChildren(mGoogleApiClient)
                .setResultCallback(mBackupFolderMetadataResultCallback);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "API client Connected ******");

        Drive.DriveApi.getRootFolder(mGoogleApiClient)
                .listChildren(mGoogleApiClient)
                .setResultCallback(mRootFolderMetadataResultCallback);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                Log.i(LOG_TAG, e.getMessage());
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            // Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    Log.i(LOG_TAG, "Selected file's ID: " + driveId);

                    DriveFile file = driveId.asDriveFile();

                    file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
                            .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                                @Override
                                public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                                    if (!driveContentsResult.getStatus().isSuccess()) {
                                        Log.e(LOG_TAG, "File cannot be downloaded");
                                        return;
                                    }

                                    DriveContents contents = driveContentsResult.getDriveContents();

                                    Tools.writeBytesToFile(contents.getInputStream(), IncomeExpenseDbHelper.getDatabaseFullPath());
                                }
                            });


                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
