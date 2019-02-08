package com.rikilg.idlethoughts.dropbox;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;

public class GetFileMetadata extends AsyncTask<Metadata, Void, FileMetadata> {

    private DbxClientV2 client;
    private Callback callback;

    public GetFileMetadata(DbxClientV2 dbxClientV2, Callback callback) {
        client = dbxClientV2;
        this.callback = callback;
    }

    public interface Callback {
        void onFileMetadataDownloaded(FileMetadata result);
    }

    @Override
    protected void onPostExecute(FileMetadata fileMetadata) {
        super.onPostExecute(fileMetadata);
        if(fileMetadata != null) {
            callback.onFileMetadataDownloaded(fileMetadata);
        }
    }

    @Override
    protected FileMetadata doInBackground(Metadata... params) {
        for(Metadata temp: params) {
            try {
                Metadata holder = client.files().getMetadata(temp.getPathLower());
                if (holder instanceof FileMetadata) {
                    return (FileMetadata)holder;
                }
            } catch (DbxException e) {
                Log.d("My Tag", "doInBackground: Issue Happened");
            }
        }
        return null;
    }
}
