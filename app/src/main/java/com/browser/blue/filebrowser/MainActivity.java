package com.browser.blue.filebrowser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName() + "--->";
    public static final int FILE_RESULT_CODE = 1;
    private Button btn_open;
    private TextView changePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        btn_open = (Button) findViewById(R.id.btn_open);
        changePath = (TextView) findViewById(R.id.changePath);
    }

    private void initListener() {
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser();
            }
        });
    }

    private void openBrowser() {
        new AlertDialog.Builder(this).setTitle("选择存储区域").setIcon(
                R.drawable.icon_opnefile_browser).setSingleChoiceItems(
                new String[]{"内置sd卡", "外部sd卡"}, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, FileBrowserActivity.class);
                        if (which == 0)
                            intent.putExtra("area", false);
                        else
                            intent.putExtra("area", true);
                        startActivityForResult(intent, FILE_RESULT_CODE);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (FILE_RESULT_CODE == requestCode) {
            Bundle bundle = null;
            if (data != null && (bundle = data.getExtras()) != null) {
                String path = bundle.getString("file");
                Log.d(TAG, "onActivityResult: " + path);
                changePath.setText("选择路径为 : " + path);
            }
        }
    }
}
