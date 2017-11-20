package com.yview.cn.two;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Environment;

import com.example.hellojni.OpusTools;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by jiangc on 17-5-23.
 */
public class audioRead implements Runnable {


    private int flag = 0;
    private AudioRecord audioRecord;
    private int sampleRateInHz;
    private int mBuffSize;
    private int audioFormat;
    private int channelConfig;
    private int bufferSizeInBytes;
    private AudioTrack mAudioTrack;
    private FileWrite fileWrite;
    private int outchannels = 2;

    public audioRead(AudioRecord audioRecord, int framBufferSize, int sampleRateInHz, int audioFormat, int channelConfig) {

        this.audioRecord = audioRecord;
        this.mBuffSize = framBufferSize;
        this.sampleRateInHz = sampleRateInHz;
        this.audioFormat = audioFormat;
        this.channelConfig = channelConfig;
        //bufferSizeInBytes = 48000 * 16 * 1 * 2 * 2; //缓冲区大小，采样率 x 量化位数 x 采样时间 x 通道数
        bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);
    }

    public void outputchannels(int outchannels)
    {
        this.outchannels = outchannels;
    }
    public void flag(int num)
    {
        flag = num;
    }
    @Override
    public void run() {
        int res;
        int cont;
        int lSts;
        int lRet;
        short [] enshort;
        short [] deshort;
        byte [] debyte;
        byte [] enbuffer;
        byte [] ChannlesBuffer;             //left channels

        res = 0;
        cont = 0;
        lSts = 0;
        lRet = 0;
        File f= new File("/sdcard/test.wav");

        OpusTools opusTools = new OpusTools();                        //Create opus Object
        System.out.println("mBuffSize----->" + mBuffSize);
        byte [] buffer = new byte[mBuffSize];
        enshort = new short[mBuffSize/2];
        debyte = new byte[mBuffSize];

        fileWrite = new FileWrite();
        fileWrite.SetFilePath(Environment.getExternalStorageDirectory() + "/test.wav");
        try {
            fileWrite.openfile(sampleRateInHz, 16, channelConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
         /*encoder mode #define OPUS_APPLICATION_VOIP                2048*/
        lSts = opusTools.OpusEncInit(sampleRateInHz, channelConfig, 24000, channelConfig, 2048);
        /*init decoder*/
        lSts = opusTools.OpusDecInit(sampleRateInHz, channelConfig);

        while(flag == 1) {
            res = audioRecord.read(buffer, 0, mBuffSize);
            System.out.println("cont-------------------" + cont);
            cont ++;
            if (res == audioRecord.ERROR_BAD_VALUE) {
                System.out.println("Audio reading failed");
            }
            System.out.println("buffer.length---->>>" + buffer.length);
            ChannlesBuffer = opusTools.PcmLeftOrRight(buffer, outchannels);
            ByteBuffer.wrap(ChannlesBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(enshort);
            /*encoder*/
            enbuffer = opusTools.OpusEncode(enshort, 1920, 4000);
            System.out.println("encoder channels ---->>>"+ opusTools.GetChannels(enbuffer));
            System.out.println("encoder sample number --->>>" + opusTools.GetSamplesPerFrame(enbuffer,sampleRateInHz));
            System.out.println("苏enbuffer.length:" + enbuffer.length);

            try {
                fileWrite.writeDate(ChannlesBuffer, 0, ChannlesBuffer.length);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("file.length:" + f.length());
            /*decoder*/
            deshort = opusTools.OpusDecode(enbuffer, enbuffer.length, channelConfig, 1920, 0);
            System.out.println("deshort.length--->>>" + deshort.length);
            ByteBuffer.wrap(debyte).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(deshort);
            System.out.println("decoder buffer length---->>>" + deshort.length * 2);
          //  mAudioTrack.write(debyte, 0, deshort.length * 2);
           // mAudioTrack.play();
        }
        try {
            fileWrite.closefile();
            opusTools.OpusEncoderDestroy();
            opusTools.OpusDecoderDestroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
