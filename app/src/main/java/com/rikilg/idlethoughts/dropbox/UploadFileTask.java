package com.rikilg.idlethoughts.dropbox;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.rikilg.idlethoughts.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Async task to upload a file to a directory
 */
public class UploadFileTask extends AsyncTask<FileMetadata, Void, FileMetadata> {

    private final Context mContext;
    private final DbxClientV2 mDbxClient;
    private final Callback mCallback;
    private Exception mException;

    public interface Callback {
        void onUploadComplete(FileMetadata result);
        void onError(Exception e);
    }

    public UploadFileTask(Context context, DbxClientV2 dbxClient, Callback callback) {
        mContext = context;
        mDbxClient = dbxClient;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(FileMetadata result) {
        super.onPostExecute(result);
        if (mException != null) {
            Log.d("dskfdsfdsfgwf", "doInBackground: In Upload page with exception" + mException.toString());
            mCallback.onError(mException);
        } else if (result == null) {
            Log.d("dskfdsfdsfgwf", "doInBackground: In Upload page with null result");
            mCallback.onError(null);
        } else {
            Log.d("dskfdsfdsfgwf", "doInBackground: In Upload page success!!!!!!!!!");
            mCallback.onUploadComplete(result);
        }
    }

    @Override
    protected FileMetadata doInBackground(FileMetadata... params) {
        FileMetadata remoteFileMeta = params[0];
        File localFile = new File(mContext.getFilesDir().getAbsolutePath() + "/", mContext.getResources().getString(R.string.local_filename));
//        File localFile = UriHelpers.getFileForUri(mContext, Uri.parse(localUri)); // UriHelpers class deleted. get it from dropbox-sdk
//
//        if (localFile == null) {
//            return null;
//        }
//        String remoteFolderPath = params[1];
//
//        // Note - this is not ensuring the name is a valid dropbox file name
//        String remoteFileName = mContext.getResources().getString(R.string.server_filename);

        String totalPath = remoteFileMeta.getPathLower();

        try {
            Log.d("dskfdsfdsfgwf", "doInBackground: In Upload page");
            InputStream inputStream = new FileInputStream(localFile);
            return mDbxClient.files().uploadBuilder(totalPath)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(inputStream);
        } catch (DbxException | IOException e) {
            mException = e;
        }

        return null;
    }
}
