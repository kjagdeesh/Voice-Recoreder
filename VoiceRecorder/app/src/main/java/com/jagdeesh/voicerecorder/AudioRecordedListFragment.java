package com.jagdeesh.voicerecorder;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioRecordedListFragment extends Fragment implements AudioListAdapter.onItemListClick {

    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView audioList;
    private File[] allfiles;

    private AudioListAdapter audioListAdapter;

    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    private File fileToPlay;


    private ImageButton playBtn;
    private TextView playHeader;
    private TextView playerFileName;

    private ImageButton forwardAudio;
    private ImageButton backwardAudio;

    private SeekBar player_seekbar;
    private Handler seekbar_handler;
    private Runnable update_seekbar;

    private TextView totalSongDuration;
    private TextView currentTime;

    public AudioRecordedListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_recorded_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerSheet = view.findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        audioList = view.findViewById(R.id.audio_listView);

        forwardAudio =  view.findViewById(R.id.forwardButton);
        backwardAudio = view.findViewById(R.id.backwardButton);
        playBtn = view.findViewById(R.id.player_play_btn);
        playHeader = view.findViewById(R.id.player_header_title);
        playerFileName = view.findViewById(R.id.file_name);
        player_seekbar = view.findViewById(R.id.seek_bar);
        currentTime = view.findViewById(R.id.RunningTime);
        totalSongDuration = view.findViewById(R.id.songTotalDuration);

        // files stored location path
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Voice Recorder";
        File directory = new File(path);
        allfiles = directory.listFiles();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        // onclick options
        audioListAdapter = new AudioListAdapter(allfiles, this);
        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(linearLayoutManager);
        audioList.setAdapter(audioListAdapter);


        // Bottom sheet
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // Audio play
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    pauseAudio();
                }else {
                    if(fileToPlay != null){
                        resumeAudio();
                    }
                }
            }
        });

        // Backward audio
        backwardAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null){
                    if((mediaPlayer.getCurrentPosition() - 10000) > 0)
                    {
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                    }
                    else
                    {
                        mediaPlayer.seekTo(0);
                    }
                    resumeAudio();
                }
            }
        });

        // Forward Audio
        forwardAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null){
                    if((mediaPlayer.getCurrentPosition() + 10000) <= mediaPlayer.getDuration())
                    {
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                    }
                    else
                    {
                        mediaPlayer.seekTo(mediaPlayer.getDuration());
                    }
                    resumeAudio();
                }
            }
        });

        // Seekbar position change to update audio file
        player_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer != null){
                    pauseAudio();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer != null){
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    resumeAudio();
                }
            }
        });
    }
    @Override
    public void onClickListener(File file, int position) {
        fileToPlay= file;
        if(isPlaying)
        {
            StopPlaying();
            PlayAudio(fileToPlay);
        }
        else
        {
            PlayAudio(fileToPlay);
        }
    }


    // Resume Audio
    private void resumeAudio(){
        mediaPlayer.start();
        isPlaying = true;
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn, null));
        UpdateRunnable();
        seekbar_handler.postDelayed(update_seekbar,0);
        playHeader.setText("Playing");

    }

    // Pause Audio
    private void pauseAudio(){
        mediaPlayer.pause();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn, null));
        isPlaying = false;
        seekbar_handler.removeCallbacks(update_seekbar);
        playHeader.setText("Paused");
    }

    private void StopPlaying() {
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn, null));
        playHeader.setText("Playing");
        isPlaying = false;
        mediaPlayer.stop();
        seekbar_handler.removeCallbacks(update_seekbar);
    }

    // On Click Audio file to play
    private void PlayAudio(File fileToPlay) {
        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn, null));
        playerFileName.setText(fileToPlay.getName());
        playHeader.setText("Playing");
        isPlaying= true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                pauseAudio();
                playHeader.setText("Finished");
//                StopPlaying();
//                resumeAudio();
//                playHeader.setText("Finished");
            }
        });
        totalSongDuration.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(mediaPlayer.getDuration()))) );
        player_seekbar.setMax(mediaPlayer.getDuration());
        seekbar_handler =  new Handler();
        UpdateRunnable();
        seekbar_handler.postDelayed(update_seekbar,0);
    }

    // Updating time and seekbar position
    private void UpdateRunnable() {
        update_seekbar = new Runnable() {
            @Override
            public void run() {
                player_seekbar.setProgress(mediaPlayer.getCurrentPosition());
                currentTime.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition()),
                        TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition()))) );
                seekbar_handler.postDelayed(this,0);

            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if(fileToPlay != null){
            pauseAudio();
        }
    }
}