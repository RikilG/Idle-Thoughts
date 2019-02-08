package com.rikilg.idlethoughts;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import com.rikilg.idlethoughts.dropbox.DownloadFileTask;
import com.rikilg.idlethoughts.dropbox.GetCurrentAccountTask;
import com.rikilg.idlethoughts.dropbox.ListFolderTask;
import com.rikilg.idlethoughts.dropbox.GetFileMetadata;
import com.rikilg.idlethoughts.dropbox.UploadFileTask;
import com.rikilg.idlethoughts.FileIO;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetCurrentAccountTask.Callback, ListFolderTask.Callback,
        GetFileMetadata.Callback, DownloadFileTask.Callback, UploadFileTask.Callback {

    private static final String ACCESS_TOKEN = "<YOUR_ACCESS_TOKEN_HERE>";

    TextView tvStatusBar;
    TextView tvcontent;
    TextView tvAccountName;

    File thoughtsFile;
    FileMetadata thoughtsDbMeta=null;

    DbxClientV2 client;
    FullAccount account;
    DbxRequestConfig config;
    GetCurrentAccountTask accountTask;
    ListFolderTask listFolderTask;
    DownloadFileTask downloadFileTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatusBar = findViewById(R.id.tvStatusBar);
        tvAccountName = findViewById(R.id.tvAccountName);
        tvcontent = findViewById(R.id.tvContent);

        //check for existing thoughts.txt file
        thoughtsFile = new File(this.getFilesDir().getAbsolutePath() + "/",getResources().getString(R.string.local_filename));

        // Create Dropbox client
        config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);

        accountTask = new GetCurrentAccountTask(client, this);
        accountTask.execute();

    }

    @Override
    public void onComplete(FullAccount result) { //GetCurrentAccount callback
        tvAccountName.setText(result.getName().getDisplayName());
        account = result;
        // now list folder after successful connection.
        listFolderTask = new ListFolderTask(client, this);
        listFolderTask.execute("");
        tvStatusBar.setText(getResources().getString(R.string.account_connected));
    }

    @Override
    public void onError(Exception e) { //GetCurrentAccount callback
        String error = getResources().getString(R.string.exception) + e.toString();
        tvStatusBar.setText(error);
    }

    @Override
    public void onDataLoaded(ListFolderResult result) { // ListFolderTask callback
        Metadata temp=null;
        List<Metadata> files = result.getEntries();
        for(Metadata file : files) {
            Log.d("LJFHIUHKLgjkldhsjaf", "onDataLoaded: file name : "+file.getName());
            if(file.getName().equals(getResources().getString(R.string.server_filename))) {
                temp = file;
            }
        }
        if(temp==null) {
            tvStatusBar.setText(getResources().getString(R.string.file_unavailable));
        }
        else{
            GetFileMetadata fileMetadata = new GetFileMetadata(client, this);
            fileMetadata.execute(temp);
        }
        tvStatusBar.setText(getResources().getString(R.string.sync_meta));
    }

    @Override
    public void onFileMetadataDownloaded(FileMetadata result) { // GetFileMetadata callback
        thoughtsDbMeta = result;
        if(thoughtsDbMeta == null) {
            tvStatusBar.setText(getResources().getString(R.string.file_unavailable));
            return;
        }
        Log.d("MYTAG", "onDataLoaded: thoughtsFile.lastModified() " + thoughtsFile.lastModified());
        Log.d("MYTAG", "onDataLoaded: thoughtsDbMeta.getServerModified() " + thoughtsDbMeta.getServerModified().getTime());
        if(thoughtsFile.exists() && thoughtsFile.lastModified()<thoughtsDbMeta.getServerModified().getTime()) {
            //download file.
            Log.d("MYTAG", "onDataLoaded: thoughts file needs to be downloaded.");
            downloadFileTask = new DownloadFileTask( client, thoughtsFile, this);
            downloadFileTask.execute(thoughtsDbMeta);
            tvStatusBar.setText(getResources().getString(R.string.download_file));
        }
        else {
            Log.d("MYTAG", "onDataLoaded: latest thoughts file exists.");
            //upload to server
            UploadFileTask uploadFileTask = new UploadFileTask(this, client, this);
            uploadFileTask.execute(thoughtsDbMeta);
            tvStatusBar.setText(getResources().getString(R.string.sync_complete));
        }
    }

    @Override
    public void onDownloadComplete(File result) { // DownloadFileTask callback
        Log.d("hello world!!!!!!", "onDownloadComplete: Download Completed.....");
        tvStatusBar.setText(getResources().getString(R.string.sync_complete));
        readThoughts();
    }

    @Override
    public void onUploadComplete(FileMetadata result) {
        readThoughts();
    }

    private void readThoughts() {
        FileIO fileIO = new FileIO(this, getResources().getString(R.string.local_filename));
        fileIO.read(tvcontent);
    }
}
