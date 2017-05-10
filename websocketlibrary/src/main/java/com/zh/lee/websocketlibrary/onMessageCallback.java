package com.zh.lee.websocketlibrary;

import okio.Buffer;

/**
 * Created by lee on 2017/5/10.
 */

public interface onMessageCallback {
    void onMessage(String message);
    void onPong(Buffer payload);
}
