package com.example.hellojni;

import android.util.Log;

/**
 * Created by jiangc on 17-6-12.
 */
public class OpusTools {

    /*Create an encoder state
    * lSamplingRate:  sampling rate
    * lChannels: Channels num
    * lApplication:    encoder mode
    * */
    public native int OpusEncInit(int lSamplingRate, int lChannels, int lBitRate, int lCodedChannel, int lApplication);

    /*Encoder Release memory*/
    public native void OpusEncoderDestroy();

    /*
    * encode
    * szPcmData[]: Need to encode the data
    * lFrame_size: Number of samples
    * szByData[]:  Store the encoded data buffer
    * max_data_bytes: Effective memory allocation size ，recommended 4000
    * */
    public native byte []  OpusEncode(short szPcmData[], int lFrame_size,  int max_data_bytes);

    /*
    *channels processing
    * jbyte[]:PCM data
    * lChannels:The 0 represents the left channel, and the 1 represents the reserved right channel
    * */
    public native byte [] PcmLeftOrRight(byte szPcmData[], int lChannels);

    /*
    * Create an decoder state
    * lSamplingRate:  sampling rate
     * lChannels: channels num
    * */
    public native int OpusDecInit(int lSamplingRate, int lChannels);

    /*Deconder Release memory*/
    public native void OpusDecoderDestroy();

    /*
    * decoder
    * szByData[]:Need to decode the data
    * llenth:   Need to decode the data length
    * szpcmData[]: Store the decoded data buffer
    * lFrame_size:Number of samples
    * lDecode_fec: Flag (0 or 1) to request that any in-band forward error correction data be decoded. If no such data is available, the frame is decoded as if it were lost.
    * */
    public native short [] OpusDecode(byte szByData[], int llenth, int lChannels,int lFrame_size, int lDecode_fec);

    /*
    * Gets the number of channels for audio data
    * szByData: Audio Data
    * */
    public native int GetChannels(byte szByData[]);

    /*
    * Gets the mode of the audio data
    *szByData: Audio Data
    * */
  //  public native int GetDataMod(byte szByData[]);

    /*
    * Gets the bandwidth of the audio data
    *szByData: Audio Data
    * */
    public native int GetBandWidth(byte szByData[]);

    /*
    * szByData: Audio Data
    * lSamplingRate: sample Rate
    * */
    public native int GetSamplesPerFrame(byte szByData[], int lSamplingRate);
/*    static{
        System.loadLibrary("opus");
    }*/

    static{
        try{
            System.loadLibrary("opus");
        }
        catch(UnsatisfiedLinkError ulink){
            Log.i("p7====","Can not load library" + ulink.toString());
            ulink.printStackTrace();
        }
    }


}
