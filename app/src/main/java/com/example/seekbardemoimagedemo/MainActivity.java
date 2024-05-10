package com.example.seekbardemoimagedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    RadioButton rbImage, rbText;
    SeekBar gridColumnSeekbar;
    SharedPreferencesClass sharedPref;
    ModelStoreData model;
    String path;
    public final int PERMISSION_STORAGE_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbImage = findViewById(R.id.rb_image);
        rbText = findViewById(R.id.rb_text);
        gridColumnSeekbar = findViewById(R.id.gridColumn);
        model = new ModelStoreData();
        sharedPref = new SharedPreferencesClass(this);

        path = "storage/emulated/0/SeekBarDemo";
        requestPermissionIfNeeded();

//        File file = new File(path);
//        deleteFolder(file);

        createFolder();

        setUiFromSharedPrefAndPutSharedPref();

        gridColumnSeekbar.setMax(3);

        gridColumnSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sharedPref.putGridColumnConfigProgress(progress);
                model.setShowColumnGridProgress(progress);
                makeJsonString(model);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        rbImage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sharedPref.putGridRowConfigShowImage(true);
                model.setShowRowGridImage(true);
                makeJsonString(model);
            }
        });

        rbText.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sharedPref.putGridRowConfigShowImage(false);
                model.setShowRowGridImage(false);
                makeJsonString(model);
            }
        });
    }

    public void deleteFolder(File folder) {

        if (folder.isDirectory()) {
            for (File child : folder.listFiles()) {
                deleteFolder(child);
            }
        }
        folder.delete();
    }

    public void setUiFromSharedPrefAndPutSharedPref() {
// start

        boolean isSetRowConfig = sharedPref.isGridRowSet();
        boolean isSetColumnConfig = sharedPref.isGridColumnSet();

        Log.d("CheckValue", "RowConfig : " + isSetRowConfig);
        Log.d("CheckValue", "ColumnConfig : " + isSetColumnConfig);

        if (isSetRowConfig && isSetColumnConfig) {
            setUIFromSharedPreference();
        } else {
            String jsonString = readFile();
            Log.d("JsonString", jsonString);

            if (jsonString.length() > 0) {
                Gson gson = new Gson();
                model = gson.fromJson(jsonString, ModelStoreData.class);
                sharedPref.putGridRowConfigShowImage(model.isShowRowGridImage());
                sharedPref.putGridColumnConfigProgress(model.getShowColumnGridProgress());
            }
            setUIFromSharedPreference();
        }
// End

    }

    public void setUIFromSharedPreference() {
        boolean isShowImage = sharedPref.getGridRowImageConfig();

        if (isShowImage) {
            rbImage.setChecked(true);
        } else {
            rbText.setChecked(true);
        }
        int progress = sharedPref.getGridColumnProgressConfig();
        gridColumnSeekbar.setProgress(progress);
    }

    public void createFolder() {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public void writeFile(String data) {
        String file = path + "/Seekbar.txt";
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile() {
        String file = path + "/Seekbar.txt";

        String lastLine = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lastLine = line;
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastLine;
    }

    public void makeJsonString(ModelStoreData model) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(model);
        writeFile(jsonString);
    }

    public void requestPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PERMISSION_STORAGE_CODE);
            startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
        } else {
            setUiFromSharedPrefAndPutSharedPref();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUiFromSharedPrefAndPutSharedPref();
            }
        }
    }
}