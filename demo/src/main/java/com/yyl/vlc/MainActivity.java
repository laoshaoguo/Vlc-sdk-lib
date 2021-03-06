package com.yyl.vlc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yyl.vlc.ijk.IjkPlayerActivity;
import com.yyl.vlc.vlc.VlcPlayerActivity;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.vlc.ThumbnailUtils;
import org.videolan.vlc.util.VLCInstance;

import java.util.ArrayList;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {
    public static final String path = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    // public static final String path = "http://192.168.1.27/demo2.mp4";
    //public static final String path = "rtsp://video.fjtu.com.cn/vs01/flws/flws_01.rm";
    String tag = "MainActivity";
    private ImageView thumbnail;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载库文件
        if (VLCInstance.testCompatibleCPU(this)) {
            new IjkMediaPlayer();
            Log.i(tag, "support   cpu");
            setContentView(R.layout.activity_main);
        } else {
            Log.i(tag, "not support  cpu");
        }
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        context = this;
    }

    public void myClick1(View view) {
        startActivity(new Intent(this, IjkPlayerActivity.class));
    }

    public void myClick2(View view) {
        startActivity(new Intent(this, VlcPlayerActivity.class));
    }

    public void myClick3(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }


    public void myClick4(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> options = new ArrayList<String>(10);
                //   options.add("vlc --help");
                options.add("-vvv");
                LibVLC libVLC = new LibVLC(context, options);
                Media media = new Media(libVLC, Uri.parse(path));//网络截图效率不高
                media.addOption("--vout=android_display,none");
                //   media.addOption("--no-audio");
                //   media.addOption("--start-time=20");
                //  media.setHWDecoderEnabled(false, false);
                media.parse(Media.Parse.ParseNetwork);
                Log.i("yyl", "getTrackCount=" + media.getTrackCount());
                Media.VideoTrack track = (Media.VideoTrack) media.getTrack(Media.Track.Type.Video);
                Log.i("yyl", "track=" + track.toString());
                final Bitmap bitmap = ThumbnailUtils.getThumbnail(media, track.width / 2, track.height / 2);
                Log.i("yyl", "bitmap=" + bitmap);
                thumbnail.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null)
                            thumbnail.setImageBitmap(bitmap);
                    }
                });

            }
        }).start();

    }
}
