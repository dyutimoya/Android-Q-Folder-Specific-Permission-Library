package com.novastar.library;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.os.Bundle;

import com.novastar.folderspacificpermission.GetFolderPermission;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    GetFolderPermission permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permission = new GetFolderPermission(this);

        if (permission.isPermissionAlreadyTaken("path2")){
            System.out.println(Arrays.toString(permission.GetDocumentFiles("path2")));
        } else {
            permission.TakePermission("/Download/Test");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DocumentFile[] documentFiles = permission.GetResult(data, "key");
        System.out.println(Arrays.toString(permission.GetResult(data, "path2")));
    }
}