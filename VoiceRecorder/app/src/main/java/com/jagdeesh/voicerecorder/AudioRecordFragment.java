package com.jagdeesh.voicerecorder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioRecordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private ImageButton audioListBtn;
    private ImageButton recordAudio;
    private TextView filenameText;

    private boolean isRecording = false;

    private ImageButton aboutApp;

    private int PERMISSION_CODE_FILE_READ = 21;

    private MediaRecorder mediaRecorder;
    private String recordFile;

    private Chronometer timer;

    public AudioRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        audioListBtn = view.findViewById(R.id.record_list_btn);
        recordAudio = view.findViewById(R.id.record_btn);
        timer = view.findViewById(R.id.recording_time);
        filenameText = view.findViewById(R.id.audio_filename);
        aboutApp = view.findViewById(R.id.about_app_btn);

        aboutApp.setOnClickListener(this);
        audioListBtn.setOnClickListener(this);
        recordAudio.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_app_btn:
                navController.navigate(R.id.action_audioRecordFragment_to_aboutAppFragment);
                break;
            case R.id.record_list_btn:
                if(isRecording){
                    MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(isRecording){
                                stopRecording();
                                recordAudio.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                                isRecording = false;
                                navController.navigate(R.id.action_audioRecordFragment_to_audioRecordedListFragment);
                            }else{
                                navController.navigate(R.id.action_audioRecordFragment_to_audioRecordedListFragment);
                            }
                        }
                    });
                    alertDialog.setBackground(getResources().getDrawable(R.drawable.alert_dialog_bg, null));
                    alertDialog.setNegativeButton("CANCEL", null);
                    alertDialog.setTitle("Audio still recording");
                    alertDialog.setMessage("Are you sure, you want to stop the recording?");
                    alertDialog.create().show();
                }
                else{
                    if(checkPermission()){
                        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Voice Recorder");
                        if(!folder.exists()) {
                            folder.mkdirs();
                        }

                        if(isRecording){
                            stopRecording();
                            recordAudio.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                            isRecording = false;
                            navController.navigate(R.id.action_audioRecordFragment_to_audioRecordedListFragment);
                        }else{
                            navController.navigate(R.id.action_audioRecordFragment_to_audioRecordedListFragment);
                        }

                    }
                }
                break;
            case R.id.record_btn:
                if(isRecording)
                {
                    // Stop
                    stopRecording();
                    recordAudio.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    isRecording = false;
                }
                else
                {
                    if(checkPermission())
                    {
                        startRecording();
                        recordAudio.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                        isRecording = true;
                    }
                }
                break;

        }
    }



    //Stop audio recorder and set it to null for further use to record new audio
    private void stopRecording() {
        timer.stop();
        filenameText.setText("Recording Stopped, File Saved : " + recordFile);
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

    }


    // Start Audio recording
    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        //File storage location

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Voice Recorder");
        if(!folder.exists()) {
            folder.mkdirs();
        }

        String recordPath = folder.getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        recordFile = "Rec_" + formatter.format(new Date()) + ".mp3";
        filenameText.setText("Recording, File Name : " + recordFile);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    // Checking permission before recording or navigating to Audio list file
    private boolean checkPermission() {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED )
            return true;
        else
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, }, PERMISSION_CODE_FILE_READ);
            return false;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(isRecording){
            stopRecording();
            recordAudio.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
            isRecording = false;
        }
    }
}