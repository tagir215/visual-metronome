package com.example.artmetronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;

public class Settings {
    MainActivity main;
    String[] titles;
    String[] settings;
    SettingStarters[] actions;
    String[] sampleRates;
    String[] saveFormat;
    String[] microphones;
    double waitTime;
    int sampleRate = 48000;
    Settings(MainActivity main){
        this.main = main;
        waitTime = 1000;
        settings = new String[] {"Wait Time","Save Format","Sample Rate","Microphone"};
        actions = new SettingStarters[] {
                this::setWaitTime,
                this::setSaveFormat,
                this::setSampleRate,
                this::setMicrophone,
        };
        titles = new String[] {"1s","2s","3s","4s","5s","10s"};
        sampleRates = new String[] {"44100Hz","48000Hz","88200Hz"};
        saveFormat = new String[] {"Mp3","Mp4","Wav"};
        microphones = new String[] {"camera","default","somethingidk"};
        ImageButton settingsBtn = main.findViewById(R.id.settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSettingsDialog();
            }

        });
    }

    interface SettingStarters {
        void startSetting();
    }
    interface SettingsAction{
        void doSettingsAction(int i);
    }
    void createSingleSettingDialog(String title, String[] names,int checkedItem, SettingsAction action){
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle(title);
        builder.setSingleChoiceItems(names, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                action.doSettingsAction(i);
            }
        });
        builder.show();
    }


    void createSettingsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle("Settings");
        builder.setItems(settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actions[i].startSetting();
            }
        });
        builder.show();
    }

    void setWaitTime(){
        createSingleSettingDialog("Sample Rate",titles,0,(i)->{
            String time = titles[i].replaceAll("[^0-9]","");
            waitTime = Double.parseDouble(time)*1000;
        });
    }

    void setSaveFormat(){
        createSingleSettingDialog("Save Format",saveFormat,0,(i)->{

        });
    }

    void setSampleRate(){
        createSingleSettingDialog("Sample Rate",sampleRates,0,(i)->{
            String s = sampleRates[i].replaceAll("[^0-9]","");
            sampleRate = Integer.parseInt(s);
        });
    }

    void setMicrophone(){
        createSingleSettingDialog("Microphone",microphones,0,(i)->{

        });
    }


}
