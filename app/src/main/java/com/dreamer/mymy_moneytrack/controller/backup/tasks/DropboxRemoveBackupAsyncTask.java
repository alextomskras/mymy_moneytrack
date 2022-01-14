package com.dreamer.mymy_moneytrack.controller.backup.tasks;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.dreamer.mymy_moneytrack.controller.backup.BackupController;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;

public class DropboxRemoveBackupAsyncTask extends AsyncTask<Void, String, String> {

    private final DbxClientV2 dbClient;
    private final String backupName;

    @Nullable
    private final BackupController.OnBackupListener listener;

    public DropboxRemoveBackupAsyncTask(DbxClientV2 dbClient, String backupName,
                                        @Nullable BackupController.OnBackupListener listener) {
        this.dbClient = dbClient;
        this.backupName = backupName;
        this.listener = listener;
    }

    @Override protected String doInBackground(Void... params) {
        try {
            Metadata metadata = dbClient.files().deleteV2("/" + backupName).getMetadata();
            return metadata == null ? null : BackupController.OnBackupListener.SUCCESS;
        } catch (DbxException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener == null) return;

        if (BackupController.OnBackupListener.SUCCESS.equals(result)) {
            listener.onRemoveSuccess();
        } else {
            listener.onRemoveFailure(result);
        }
    }
}
