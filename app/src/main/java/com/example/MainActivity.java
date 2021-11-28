package com.example;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.R;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chris
 * 2021-11-28
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.saveButton);
        TextView et = findViewById(R.id.et);
        EditText text = findViewById(R.id.inText);

        String input = text.getText().toString().trim();

        et.setBackgroundColor(Color.LTGRAY);

        //点击事件
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                et.setText("dataFile:" + dataFile);
                MyLog.e("LOG", "点击事件");
                try {
                    /*MyLog.e("LOG", "Environment.getExternalStorageDirectory: " +
                            Environment.getExternalStorageDirectory().getAbsolutePath());
                    MyLog.e("LOG", "getExternalFilesDir(null): " +
                            getExternalFilesDir(null).getAbsolutePath());
                    MyLog.e("LOG", "getExternalFilesDir(Environment.DIRECTORY_PICTURES): " +
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());

                    MyLog.e("LOG", "Environment.getExternalStorageState(): " +
                            Environment.getExternalStorageState());*/
                    /*File path = getExternalFilesDir(dataFile);
                    //File path = getDataDirectory();//"/data"
                    //File directory = Environment.getStorageDirectory();
                    LogUtils.e(LogUtils.TAG, "dataFile---" + dataFile);
                    LogUtils.e(LogUtils.TAG, "PATH---" + path.getAbsolutePath());
                    if (!path.exists()) {
                        et.setText(dataFile + "不存在");
                        LogUtils.e(LogUtils.TAG, dataFile + "不存在");
                        return;
                    }*/

                    List<File> files = getAllFiles();
                    if (files == null || files.size() < 0) {
                        MyLog.e("LOG", dataFile + "无文件");
                        return;
                    }

                    StringBuilder sb = new StringBuilder();
                    for (File f : files) {
                        try {
                            String newPath = destFile + f.getName().replaceAll(".cnt",".jpg");
                            File dest = new File(newPath);
                            copyFile(f, dest);
                            sb.append(newPath + "\r\n");
                        } catch (IOException e) {
                            MyLog.e("ERROR", e.getMessage());
                        }
                    }
                    MyLog.e("LOG", sb.toString());
                    et.setText("完成");
                } catch (Exception exception) {
                    MyLog.e("ERROR", exception.getMessage());
                }
            }
        });
    }

    /**
     * 复制文件
     */
    private void copyFile(File source, File dest)
            throws IOException {
        FileUtils.copyFile(source, dest);
    }

    // 数据文件夹
    //private final String dataFile = "content://com.android.externalstorage.documents/document/0B20-3C1A%3AAlarms";
    //private final String dataFile = "com.android.externalstorage.documents/document/primary/Download/";
    //private final String dataFile = "/storage/emulated/0/Alarms/";
    private final String dataFile = "/storage/emulated/0/Android/data/com.xingin.xhs/cache/image_manager_disk_cache/";
    private final String destFile = "/storage/emulated/0/AMD/";
    //"/storage/emulated/0/Alarms/"
    //"content://com.android.externalstorage.documents/document/0B20-3C1A%3AAlarms";

    /**
     * 获取dataFile文件夹下的所有文件
     */
    public List<File> getAllFiles() {
        List<File> fileList = new ArrayList<>();
        try {
            File file = new File(dataFile);
            if (file == null || !file.exists()) {
                return fileList;
            }
            fileList = findFileList(file, fileList);
        } catch (Exception e) {
            MyLog.e("ERROR", e.getMessage());
        }
        return fileList;
    }

    /**
     * 读取目录下的所有文件
     *
     * @param path 目录
     * @param list 保存文件名的集合
     * @return
     */
    private List<File> findFileList(File path, List<File> list) {
        if (!path.exists() || !path.isDirectory()) {
            return list;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        // 读取目录下的所有目录文件信息
        File[] files = path.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 回调自身继续查询
                findFileList(file, list);
            } else {
                list.add(file);
            }
        }
        return list;
    }

}