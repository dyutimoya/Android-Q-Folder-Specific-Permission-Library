package com.novastar.folderspacificpermission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.widget.Toast;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;

public class GetFolderPermission {

    Context context;
    SharedPreferences sh;

    public GetFolderPermission(Context context) {
        this.context = context;
        sh = context.getSharedPreferences("folderSpacificPermission", Context.MODE_PRIVATE);
    }

    @SuppressLint("NewApi")
    public void TakePermission(String folderPath){
        File makeFile = new File(Environment.getExternalStorageDirectory().toString() + folderPath);
        if (!makeFile.exists()){
            makeFile.mkdirs();
        }
        String path = Environment.getExternalStorageDirectory() + folderPath;
        File file = new File(path);
        String startDir = "", finalDirPath;
        if (file.exists()) {
            startDir = folderPath.replaceFirst("/","").replace("/","%2F");
        }

        System.out.println(folderPath.replaceFirst("/","").replace("/","%2F"));

        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
        String scheme = uri.toString();
        scheme = scheme.replace("/root/", "/document/");
        finalDirPath = scheme + "%3A" + startDir;
        uri = Uri.parse(finalDirPath);
        intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        try {
            ((Activity) context).startActivityForResult(intent, 6);
        } catch (ActivityNotFoundException ignored) {

        }
    }

    @SuppressLint("CommitPrefEdits")
    public DocumentFile[] GetResult(Intent data, String key){
        DocumentFile[] documentFiles = new DocumentFile[0];
        if (data != null){
            Uri uri = data.getData();
            final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.getContentResolver().takePersistableUriPermission(uri, takeFlags);


            SharedPreferences.Editor editor = sh.edit();
            editor.putString(key, uri.toString());
            editor.apply();

            if (Build.VERSION.SDK_INT >= 29) {
                // uri is the path which we've saved in our shared pref
                DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, uri);
                assert fromTreeUri != null;
                documentFiles = fromTreeUri.listFiles();
            }
        }
        return documentFiles;
    }

    public boolean isPermissionAlreadyTaken(String key){
        return !sh.getString(key, "").equals("");
    }

    public DocumentFile[] GetDocumentFiles(String key){
        DocumentFile[] documentFiles = new DocumentFile[0];
        if (!sh.getString(key, "").equals("")){
            if (Build.VERSION.SDK_INT >= 29) {
                // uri is the path which we've saved in our shared pref
                DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, Uri.parse(sh.getString(key,"")));
                assert fromTreeUri != null;
                documentFiles = fromTreeUri.listFiles();
            }

        } else {
            Toast.makeText(context, "Take permission first", Toast.LENGTH_SHORT).show();
        }
        return documentFiles;
    }
}
