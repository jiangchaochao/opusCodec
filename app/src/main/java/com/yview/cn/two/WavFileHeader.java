package com.yview.cn.two;

/**
 * Created by jiangc on 17-5-24.
 */
public class WavFileHeader {

    public String mChunkID = "RIFF"; //规范WAVE格式以RIFF标头开始
    public int mChunkSize = 0;       //整个 wav 文件的字节数 （除去前8个字节   jie）
    public String mFormat = "WAVE";  //标 表示这是一个wav文件

    public String mSubChunk1ID = "fmt ";  //表示音频格式块
    public int mSubChunk1Size = 16;       //表示除去fmt kuai  的前8个zi jie 的大小
    public short mAudioFormat = 1;        //表示 后面data kuai 的数据格式，1 表示 PCM格式
    public short mNumChannel = 1;         //通道数，1表示单声道，2表示立体声
    public int mSampleRate = 8000;        //cai yang lv
    public int mByteRate = 0;             // chuan shu su lv
    public short mBlockAlign = 0;         //shu jv kuai de tiao zheng shu
    public short mBitsPerSample = 8;      //wei kuan

    public String mSubChunk2ID = "data";  //biao ji zhe shi shu ju kuai
    public int mSubChunk2Size = 0;    //shu ju chang du

    public WavFileHeader() {

    }

    public WavFileHeader(int sampleRateInHz, int bitsPerSample, int channels) {
        mSampleRate = sampleRateInHz;
        mBitsPerSample = (short) bitsPerSample;
        mNumChannel = (short) channels;
        mByteRate = mSampleRate * mNumChannel * mBitsPerSample / 8;
        mBlockAlign = (short) (mNumChannel * mBitsPerSample / 8);
    }
}
