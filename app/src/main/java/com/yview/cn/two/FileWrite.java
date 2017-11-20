package com.yview.cn.two;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by jiangc on 17-5-24.
 */
public class FileWrite {

    private DataOutputStream mDataOutputStream;
    private WavFileHeader wavFileHeader;
    private String filePath;
    private int FileSize = 0;

    public boolean openfile(int sampleRateInHz, int bitsPerSample, int channels) throws IOException{

            mDataOutputStream = new DataOutputStream(new FileOutputStream(filePath));    //创create a file
           return writeHeader( sampleRateInHz,  bitsPerSample,  channels);      //write Header

    }


    /*close file*/
    public boolean closefile() throws IOException {

        writeFileSize();
        mDataOutputStream.close();
        return true;
    }

    /*write Header*/
   public boolean writeHeader(int sampleRateInHz, int bitsPerSample, int channels){


        wavFileHeader = new WavFileHeader( sampleRateInHz,  bitsPerSample, channels);
        try{
            mDataOutputStream.writeBytes(wavFileHeader.mChunkID);
            mDataOutputStream.write(intToByteArray((int)wavFileHeader.mChunkSize), 0, 4);
            mDataOutputStream.writeBytes(wavFileHeader.mFormat);
            mDataOutputStream.writeBytes(wavFileHeader.mSubChunk1ID);
            mDataOutputStream.write(intToByteArray((int)wavFileHeader.mSubChunk1Size), 0, 4);
            mDataOutputStream.write(shortToByteArray((short)wavFileHeader.mAudioFormat), 0, 2);
            mDataOutputStream.write(shortToByteArray((short)wavFileHeader.mNumChannel), 0, 2);
            mDataOutputStream.write(intToByteArray((int)wavFileHeader.mSampleRate), 0, 4);
            mDataOutputStream.write(intToByteArray((int)wavFileHeader.mByteRate), 0, 4);
            mDataOutputStream.write(shortToByteArray((short)wavFileHeader.mBlockAlign), 0, 2);
            mDataOutputStream.write(shortToByteArray((short)wavFileHeader.mBitsPerSample), 0, 2);
            mDataOutputStream.writeBytes(wavFileHeader.mSubChunk2ID);
            mDataOutputStream.write(intToByteArray((int)wavFileHeader.mSubChunk2Size), 0, 4);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /*write date*/
    public boolean writeDate(byte[] buffer, int offset, int lens) throws IOException {


        if (null == mDataOutputStream)
        {
            return false;
        }
        mDataOutputStream.write(buffer, offset, lens);
        FileSize += lens;
        return true;
    }
    public int FileSizel()
    {
        return FileSize;
    }
    /*write FileSize*/
    public boolean writeFileSize() throws IOException {

         RandomAccessFile rf = new RandomAccessFile(filePath, "rw");
         rf.seek(4);
         rf.write(intToByteArray(FileSize + 36));
         rf.seek(40);
         rf.write(intToByteArray(FileSize));
        return true;
    }
    public void SetFilePath(String filePath){

        this.filePath = filePath;
    }
    private static byte[] intToByteArray(int data) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(data).array();
    }

    private static byte[] shortToByteArray(short data) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(data).array();
    }
}
