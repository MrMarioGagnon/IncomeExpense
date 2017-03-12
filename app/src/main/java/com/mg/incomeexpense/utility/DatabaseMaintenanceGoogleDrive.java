package com.mg.incomeexpense.utility;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.mg.incomeexpense.core.ApplicationConstant;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IncomeExpenseDbHelper;

import org.threeten.bp.LocalDateTime;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2017-03-04.
 */

public class DatabaseMaintenanceGoogleDrive implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DatabaseMaintenanceHandler {


    private final List<DatabaseMaintenanceListener> mListeners = new ArrayList<>();

    private static final String LOG_TAG = UtilityActivity.class.getSimpleName();
    private static final int REQUEST_CODE_RESOLUTION = 1;
    private final Activity mActivity;
    private final ResultCallback<DriveApi.DriveContentsResult> mFileOpenCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {

            if (!driveContentsResult.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "File cannot be downloaded");
                return;
            }

            DriveContents contents = driveContentsResult.getDriveContents();

            Tools.writeBytesToFile(contents.getInputStream(), IncomeExpenseDbHelper.getDatabaseFullPath());

        }
    };
    // 2e step for backup
    private final ResultCallback<DriveFolder.DriveFileResult> mFileCreatedCallback = new ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(DriveFolder.DriveFileResult result) {

            if (!result.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Error while trying to create the file");
                return;
            }
            Log.i(LOG_TAG, "File created");
//            DriveFile d = result.getDriveFile().getDriveId().asDriveFile();
//
//            d.getMetadata(mGoogleApiClient)
//                    .setResultCallback(mNewFileMetadataResultCallback);

        }
    };
    private DriveId mFolderId;
    private final ResultCallback<DriveFolder.DriveFolderResult> mBackupFolderCreatedCallback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {

            if (!result.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "****** Error while trying to create the folder ******");
                return;
            }

            mFolderId = result.getDriveFolder().getDriveId();
            Log.i(LOG_TAG, "Created a folder: " + result.getDriveFolder().getDriveId());

        }
    };
    private String mLocalFilePath;
    private GoogleApiClient mGoogleApiClient;
    // 1er step for backup
    private final ResultCallback<DriveApi.DriveContentsResult> mDriveContentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(DriveApi.DriveContentsResult result) {

            if (!result.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "****** Error while trying to create new file contents ******");
                return;
            }

            Log.i(LOG_TAG, "Drive content recover");

            DriveFolder folder = mFolderId.asDriveFolder();

            final DriveContents driveContents = result.getDriveContents();

            OutputStream outputStream = driveContents.getOutputStream();

            byte[] out = Tools.readBytesFromFile(IncomeExpenseDbHelper.getDatabaseFullPath());
            try {
                outputStream.write(out);
                outputStream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "");
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
    private final ResultCallback<DriveApi.MetadataBufferResult> mRootFolderMetadataResultCallback = new ResultCallback<DriveApi.MetadataBufferResult>() {
        @Override
        public void onResult(@NonNull DriveApi.MetadataBufferResult result) {

            if (!result.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "****** Problem while retrieving metadata ******");
                return;
            }

            Log.i(LOG_TAG, "Root folder metadata recovered");

            // Tentative pour recuperer le drive id du repertoire de backup sur Google Drive
            for (Metadata md : result.getMetadataBuffer()) {
                if (md.getTitle().equalsIgnoreCase(ApplicationConstant.backupDirectoryName) && md.isFolder() && !md.isExplicitlyTrashed()) {
                    mFolderId = md.getDriveId();
                    break;
                }
            }

            // Le repertoire de backup n'existe pas sur Google Drive, il faut le créer
            if (mFolderId == null) {
                Log.i(LOG_TAG, "Folder IncomeExpense not found");
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(ApplicationConstant.backupDirectoryName).build();
                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                        .createFolder(mGoogleApiClient, changeSet)
                        .setResultCallback(mBackupFolderCreatedCallback);
                return;
            }

            result.release();

            connected();

        }
    };
    private String mDestinationPath;
    private final ResultCallback<DriveApi.DriveContentsResult> mDriveContentsResultForRestoreCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
            if (!driveContentsResult.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "File cannot be downloaded");
                return;
            }

            DriveContents contents = driveContentsResult.getDriveContents();

            Tools.writeBytesToFile(contents.getInputStream(), mDestinationPath);
        }
    };

    public DatabaseMaintenanceGoogleDrive(Activity activity) {

        mActivity = activity;

    }

    public GoogleApiClient getGoogleApiClient() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        return mGoogleApiClient;
    }

    public void finalize() {

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.w(LOG_TAG, "Must use close to disconnect from Google API");
        }

    }

    public void connect() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    public void close() {
        if (mGoogleApiClient != null) {
            Log.i(LOG_TAG, "Closing Drive connection");
            mGoogleApiClient.disconnect();
        }
    }

    public void backup(@NonNull String localFilePath) {

        Objects.requireNonNull(localFilePath, "Parameter 'localFilePath' of type String is mandatory");

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            Log.e(LOG_TAG, "Not connected to Google Drive API");
            return;
        }

        mLocalFilePath = localFilePath;

        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(mDriveContentsCallback);

    }

    public void restore(@NonNull DriveId sourceFile, @NonNull final String destinationDatabase) {

        Objects.requireNonNull(sourceFile, "Parameter 'sourceFile' of type DriveId is mandatory");
        Objects.requireNonNull(destinationDatabase, "Parameter 'destinationDatabase' of type String is mandatory");

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            Log.e(LOG_TAG, "Not connected to Google Drive API");
            return;
        }

        Log.i(LOG_TAG, "Start restoring");

        mDestinationPath = destinationDatabase;
        DriveFile file = sourceFile.asDriveFile();

        file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(mDriveContentsResultForRestoreCallback);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.i(LOG_TAG, "Connected to Google Drive API");

        Drive.DriveApi.getRootFolder(mGoogleApiClient)
                .listChildren(mGoogleApiClient)
                .setResultCallback(mRootFolderMetadataResultCallback);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Disconnected from Google Drive API");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(mActivity, REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                Log.i(LOG_TAG, e.getMessage());
            }
        } else {

            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), mActivity, 0).show();
        }

    }

    @Override
    public void addListener(@NonNull DatabaseMaintenanceListener listener) {
        if(null == listener){
            return;
        }

        if(!mListeners.contains(listener)){
            mListeners.add(listener);
        }
    }

    @Override
    public void connected() {
        for (DatabaseMaintenanceListener listener : mListeners) {
            listener.onConnected();
        }
    }

    @Override
    public void restoreDone() {
        for (DatabaseMaintenanceListener listener : mListeners) {
            listener.onDatabaseRestored();
        }
    }

    @Override
    public void backupDone() {
        for (DatabaseMaintenanceListener listener : mListeners) {
            listener.onDatabaseBackuped();
        }
    }

    @Override
    public void error() {
        for (DatabaseMaintenanceListener listener : mListeners) {
            listener.onError();
        }
    }

    public boolean isConnected(){
        return mGoogleApiClient.isConnected();
    }
}
