package com.browser.blue.filebrowser;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by blue on 2016/10/23.
 */

public class FileBrowserActivity extends ListActivity {
    private static final String TAG = FileBrowserActivity.class.getSimpleName() + "--->";
    private String rootPath;
    private boolean pathFlag;
    private List<String> pathList;
    private List<String> itemsList;
    private TextView curPathTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser_acitivity);
        initView();
        initInfo();
    }

    private void initInfo() {
        pathFlag = getIntent().getBooleanExtra("area", false);
        rootPath = getRootPath();
        if (rootPath == null) {
            Toast.makeText(this, "所选SD卡为空！", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            getFileDir(rootPath);
        }
    }

    private void initView() {
        curPathTextView = (TextView) findViewById(R.id.curPath);
    }


    private void getFileDir(String filePath) {
        curPathTextView.setText(filePath);
        itemsList = new ArrayList<>();
        pathList = new ArrayList<>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (!filePath.equals(rootPath)) {
            itemsList.add("b1");
            pathList.add(rootPath);
            itemsList.add("b2");
            pathList.add(file.getParent());
        }
        if (files == null) {
            Toast.makeText(this, "所选SD卡为空！", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
           // if (checkSpecificFile(f)) {
                itemsList.add(f.getName());
                pathList.add(f.getPath());
         //   }
        }
        setListAdapter(new MyAdapter(this, itemsList, pathList));
    }

    public boolean checkSpecificFile(File file) {
        String fileNameString = file.getName();
        String endNameString = fileNameString.substring(
                fileNameString.lastIndexOf(".") + 1, fileNameString.length())
                .toLowerCase();
        Log.d(TAG, "checkShapeFile: " + endNameString);
        if (file.isDirectory()) {
            return true;
        }
        if (endNameString.equals("txt")) {
            return true;
        } else {
            return false;
        }
    }

    private String getRootPath() {

        try {
            String rootPath;
                if (pathFlag) {
                    Log.d(TAG, "getRootPath: 正在获取内置SD卡根目录");
                    rootPath = Environment.getExternalStorageDirectory()
                            .toString();
                    Log.d(TAG, "getRootPath: 内置SD卡目录为:" + rootPath);
                    return rootPath;
                } else {
                    rootPath = System.getenv("SECONDARY_STORAGE");
                    if ((rootPath.equals(Environment.getExternalStorageDirectory().toString())))
                        rootPath = null;
                    Log.d(TAG, "getRootPath:  外置SD卡路径为：" + rootPath);
                    return rootPath;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(pathList.get(position));
        if (file.isDirectory()) {
            getFileDir(file.getPath());
        } else {
            Intent data = new Intent(FileBrowserActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("file", file.getPath());
            data.putExtras(bundle);
            setResult(2, data);
            finish();
        }
    }

    public boolean checkSDcard() {
        String sdStutusString = Environment.getExternalStorageState();
        if (sdStutusString.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
