package com.example.artmetronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class Settings {
    private MainActivity main;
    private String[] titles;
    private String[] settings;
    private SettingStarters[] actions;
    private String[] sampleRates;
    private String[] saveFormat;
    private String[] microphones;
    public static int bufferSize;
    public static final int SAMPLE_RATE = 48000;
    public static double waitTime;
    public static int sampleRate = 48000;
    Settings(MainActivity main){
        this.main = main;
        waitTime = 1000;
        settings = new String[] {"Wait Time","Save Format","Sample Rate","Microphone","Tempo"};
        actions = new SettingStarters[] {
                this::setWaitTime,
                this::setSaveFormat,
                this::setSampleRate,
                this::setMicrophone,
                this::setTempo,
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
    private void createSingleSettingDialog(String title, String[] names,int checkedItem, SettingsAction action){
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


    private void createSettingsDialog(){
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

    private void setWaitTime(){
        createSingleSettingDialog("Sample Rate",titles,0,(i)->{
            String time = titles[i].replaceAll("[^0-9]","");
            waitTime = Double.parseDouble(time)*1000;
        });
    }

    private void setSaveFormat(){
        createSingleSettingDialog("Save Format",saveFormat,0,(i)->{

        });
    }

    private void setSampleRate(){
        createSingleSettingDialog("Sample Rate",sampleRates,0,(i)->{
            String s = sampleRates[i].replaceAll("[^0-9]","");
            sampleRate = Integer.parseInt(s);
        });
    }

    private void setMicrophone(){
        createSingleSettingDialog("Microphone",microphones,0,(i)->{

        });
    }

    private void setTempo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle("Set Tempo");

        final EditText input = new EditText(main);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tempoString = input.getText().toString();
                if (!tempoString.equals("")) {
                    Metronome.bpm = Integer.parseInt(tempoString);
                    Metronome.reset();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



}
