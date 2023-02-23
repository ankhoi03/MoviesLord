package com.example.movieslord.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.movieslord.DAO.MoviesDAO;
import com.example.movieslord.Models.MoviesModel;
import com.example.movieslord.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PlayerActivity extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    private String VIDEO_URL;
    long lastPosition;
    private View decorView;
    MoviesModel video;
    String user;
    MoviesDAO moviesDAO =new MoviesDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        video=(MoviesModel) getIntent().getSerializableExtra("vid");
        user=getIntent().getStringExtra("user");
        VIDEO_URL=video.getVdurl();
        decorView=getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideBar());
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideBar() );
        }
    }

    public int hideBar(){
        return
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                ;
    }

    @Override
    protected void onStart() {
        super.onStart();
        playerView=findViewById(R.id.video_player);
        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter.Builder(getApplicationContext()).build();
        TrackSelector trackSelector=new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
        playerView.setPlayer(simpleExoPlayer);

        DataSource.Factory dataSRCfactory=new DefaultDataSourceFactory(this, Util.getUserAgent(this,getString(R.string.app_name)));

        MediaSource videoSource=new ProgressiveMediaSource.Factory(dataSRCfactory).createMediaSource(Uri.parse(VIDEO_URL));

        simpleExoPlayer.prepare(videoSource);

        simpleExoPlayer.setPlayWhenReady(true);
        if(video.getLastPosition()!=0){
           simpleExoPlayer.seekTo(video.getLastPosition());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lastPosition=simpleExoPlayer.getCurrentPosition();
        System.out.println(lastPosition);

        moviesDAO.addHistoryMovie("/user/"+user+"/history",video,lastPosition);
        simpleExoPlayer.release();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.finish();
        }
        return super.onOptionsItemSelected(item);
    }


}