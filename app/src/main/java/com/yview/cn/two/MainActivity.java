package com.yview.cn.two;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public int AudioSource;      //音频采集的输入源
    public int sampleRateInHz;    //采样率
    public int channelConfig;     //通道数的配置
    public int audioFormat;       //数据位宽
    public int mMinBufferSize;    //缓缓缓还 缓  缓 冲区大小
    private AudioRecord audioRecord;    //音频采集对象
    private Button button;
    private Button button1;
    private Button button2;
    private TextView channels;
    private  Thread thread;
    private audioRead audioread;
    private Chronometer chronometer;
    private int mMaxBufferSize;
    private int framBufferSize;
    private int Fram;               //fps
    private int i = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AudioSource = MediaRecorder.AudioSource.MIC;
        sampleRateInHz = 48000;
        Fram = 25;
        channelConfig = 2;
        audioFormat = 16/8;
        mMinBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz,AudioFormat.CHANNEL_IN_STEREO ,AudioFormat.ENCODING_PCM_16BIT);
        mMaxBufferSize = sampleRateInHz * audioFormat * channelConfig * 1;
        framBufferSize = mMaxBufferSize / Fram;
        //mMinBufferSize = sampleRateInHz * audioFormat * 1 * channelConfig * 2 ; //缓冲区大小，采样率 x 量化位数 x 采样时间 x 通道数
        //audioRecord = new AudioRecord(AudioSource, sampleRateInHz, AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT, mMinBufferSize);
        audioRecord = new AudioRecord(AudioSource, sampleRateInHz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, framBufferSize);
        System.out.println("mMinBufferSize = "+ framBufferSize);

        int a = audioRecord.getState();
        Listhenner listhenner = new Listhenner();
        button = (Button)findViewById(R.id.button);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        channels = (TextView)findViewById(R.id.channels);

        button.setOnClickListener(listhenner);
        button1.setOnClickListener(listhenner);
        button2.setOnClickListener(listhenner);

        audioread = new audioRead(audioRecord, framBufferSize, sampleRateInHz, audioFormat, channelConfig);
        chronometer =(Chronometer)findViewById(R.id.chronometer);
        button1.setEnabled(false);
    }
   class Listhenner implements View.OnClickListener{
       @Override
       public void onClick(View v) {
           if (v.getId() == R.id.button)
           {
               thread = new Thread(audioread);
               button.setEnabled(false);
               audioRecord.startRecording();    //开始采集
               audioread.flag(1);
               thread.start();
               chronometer.start();
               button1.setEnabled(true);
           }
           else if (v.getId() == R.id.button1)
           {
               button.setEnabled(true);
               button1.setEnabled(false);
               audioread.flag(0);
               audioRecord.stop(); //停止采集
               chronometer.stop();
               chronometer.setBase(SystemClock.elapsedRealtime());
           }
           else if (v.getId() == R.id.button2)
           {
               i ++;
               if (i > 2)
               {
                   i = 0;
               }
               if (2 == i) {
                   channels.setText("立体声");
               }
               else if (1 == i)
               {
                   channels.setText("右声道");
               }
               else if (0 == i)
               {
                   channels.setText("左声道");
               }
               audioread.outputchannels(i);
           }
       }
   }
}
