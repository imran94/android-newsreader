package com.imran.mynewsreader;

import android.net.NetworkInfo;

/**
 * Created by Administrator on 14-Jan-17.
 */
public interface DownloadCallback {
    int ERROR = -1;
    int CONNECT_SUCCESS = 0;
    int GET_INPUT_STREAM_SUCCESS = 1;
    int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
    int PROCESS_INPUT_STREAM_SUCCESS = 3;

    void updateFromDownload(String result);
    void finishDownloading(int result);
}
