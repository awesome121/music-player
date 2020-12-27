package com.example.musicplayer;

import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int currentSongIndex;
    private ArrayList<Integer> playList = new ArrayList<Integer>();
    private MediaPlayer mp;
    private ImageView imgIcon;
    private ProgressBar probar;

    private TextView songName;
    private TextView timeOnLeft;
    private TextView timeOnRight;

    private Button playBtn;
    private Button preBtn;
    private Button nextBtn;
    private Button volumeUpBtn;
    private Button volumeDownBtn;

    private SeekBar volumeBar;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        currentSongIndex = 0;
        initPlayList();

        initViews();

        initMediaPlayer();
        mp.setVolume(0.5f, 0.5f);


        prepareViews();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
        }).start();

    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            probar.setProgress(currentPosition);
            updateTimeLabel(currentPosition);
        }
    };

    public void updateTimeLabel(int currentPosition) {
        int minute = (currentPosition / 1000) / 60;
        int sec = (currentPosition / 1000) % 60;
        timeOnLeft.setText(minute+":"+sec);
    }



    public void initPlayList() {
        playList.add(R.raw.morning);
        playList.add(R.raw.xiaoyaohan);
        playList.add(R.raw.mangzhong);
    }


    public void initMediaPlayer() {
        mp = MediaPlayer.create(this, playList.get(currentSongIndex));
        mp.setLooping(true);
    }


    public void initViews() {
        imgIcon = findViewById(R.id.imgIcon);
        probar = findViewById(R.id.progressBar);
        songName = findViewById(R.id.songName);
        timeOnLeft = findViewById(R.id.timeOnLeft);
        timeOnRight = findViewById(R.id.timeOnRight);

        playBtn = findViewById(R.id.playBtn);
        playBtn.setBackground(getDrawable(R.drawable.ic_play_arrow_black_24dp));
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp.start();
                    playBtn.setBackground(getDrawable(R.drawable.ic_pause_black_24dp));
                } else {
                    mp.pause();
                    playBtn.setBackground(getDrawable(R.drawable.ic_play_arrow_black_24dp));
                }
            }
        });

        preBtn = findViewById(R.id.preBtn);
        preBtn.setBackground(getDrawable(R.drawable.ic_skip_previous_black_24dp));
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSongIndex -= 1;
                if (currentSongIndex == -1) {
                    currentSongIndex = playList.size() - 1;
                }

                mp.stop();
                mp = null;
                initMediaPlayer();
                prepareViews();
                mp.start();

            }
        });

        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setBackground(getDrawable(R.drawable.ic_skip_next_black_24dp));
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSongIndex += 1;
                if (currentSongIndex == playList.size()) {
                    currentSongIndex = 0;
                }

                mp.stop();
                mp = null;
                initMediaPlayer();
                prepareViews();
                mp.start();

            }
        });

        volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setMax(100);
        volumeBar.setProgress(50);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mp.setVolume(progress/100f, progress/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volumeDownBtn = findViewById(R.id.volumeDownBtn);
        volumeDownBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                volumeBar.setProgress(volumeBar.getProgress()-10);
            }
        });
        volumeDownBtn.setBackground(getDrawable(R.drawable.ic_volume_down_black_24dp));

        volumeUpBtn = findViewById(R.id.volumeUpBtn);
        volumeUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                volumeBar.setProgress(volumeBar.getProgress()+10);
            }
        });
        volumeUpBtn.setBackground(getDrawable(R.drawable.ic_volume_up_black_24dp));

    }


    public void prepareViews() {

        probar.setMax(mp.getDuration());
        probar.setProgress(0);
        timeOnLeft.setText("00:00");
        songName.setText(getResources().getResourceEntryName(playList.get(currentSongIndex)));
        int minute = (mp.getDuration()/1000)/60;
        int sec = (mp.getDuration()/1000)%60;
        timeOnRight.setText(minute+":"+sec);
        imgIcon.setImageIcon(Icon.createWithResource(this, R.drawable.morning_img));


    }


}
