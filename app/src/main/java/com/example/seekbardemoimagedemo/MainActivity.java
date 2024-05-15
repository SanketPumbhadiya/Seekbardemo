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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;

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
    Spinner countrySpinner;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbImage = findViewById(R.id.rb_image);
        rbText = findViewById(R.id.rb_text);
        countrySpinner = findViewById(R.id.country_spinner);
        gridColumnSeekbar = findViewById(R.id.gridColumn);
        model = new ModelStoreData();

        sharedPref = new SharedPreferencesClass(this);

        String[] countryList = getResources().getStringArray(R.array.selectCountryArray);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countryList);

        path = "storage/emulated/0/SeekBarDemo";

        requestPermissionIfNeeded();

        countrySpinner.setAdapter(adapter);

        createFolder();

        Log.d("CalledFunction", "checkPrefKeyAndPutValueInPref : 1");
        checkPrefKeyAndPutValueInPref();

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
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String spinnerItem = countryList[position];
                Log.d("SpinnerItem", spinnerItem);
                sharedPref.putSpinnerItem(spinnerItem);
                model.setSelectedCountryText(spinnerItem);
                makeJsonString(model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    public void checkPrefKeyAndPutValueInPref() {

// start
        createFolder();

        boolean isSetRowConfig = sharedPref.isGridRowCheckKey();
        boolean isSetColumnConfig = sharedPref.isGridColumnCheckKey();
        boolean isSetCountrySpinnerConfig = sharedPref.isSpinnerItemCheckKey();

        Log.d("CheckPrefValue", "RowConfig : " + isSetRowConfig);
        Log.d("CheckPrefValue", "ColumnConfig : " + isSetColumnConfig);
        Log.d("CheckPrefValue", "SpinnerConfig : " + isSetCountrySpinnerConfig);

        if (isSetRowConfig && isSetColumnConfig && isSetCountrySpinnerConfig) {
            setUIFromSharedPreference();
        } else {
            String jsonString = readFile();
            Log.d("JsonString", jsonString);

            if (jsonString.length() > 0) {
                Gson gson = new Gson();
                model = gson.fromJson(jsonString, ModelStoreData.class);

                isSetCountrySpinnerConfig = false;

                if (!isSetRowConfig) {
                    sharedPref.putGridRowConfigShowImage(model.isShowRowGridImage());
                }
                if (!isSetColumnConfig) {
                    sharedPref.putGridColumnConfigProgress(model.getShowColumnGridProgress());
                }
                if (!isSetCountrySpinnerConfig) {
                    sharedPref.putSpinnerItem(model.getSelectedCountryText());
                }
                setUIFromSharedPreference();
            }
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

        String spinnerItem = sharedPref.getSpinnerItem();
        countrySpinner.setSelection(adapter.getPosition(spinnerItem));
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("CalledFunction", "checkPrefKeyAndPutValueInPref : 2");
                checkPrefKeyAndPutValueInPref();
            }
        }
    }
}