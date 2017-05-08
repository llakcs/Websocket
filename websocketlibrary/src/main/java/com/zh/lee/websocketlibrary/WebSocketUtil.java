package com.zh.lee.websocketlibrary;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by lee on 2017/5/8.
 */

public class WebSocketUtil {
    /**
     * websocke链接状态
     */

    private static final byte WS_STATUS_NULL = 0x11;
    private static final byte WS_STATUS_CONNECTING = 0x12;
    private static final byte WS_STATUS_CLOSE= 0x13;
    private static final byte WS_STATUS_CONNECT = 0X14;
    private static String TAG ="WebsocketSerivce";
    final  static OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
            .build();
    /**
     * 检测链接状态（是否在连接中）
     */
    private static byte wsStatus = WS_STATUS_NULL;
    private RequestBody response = null;
    private static WebSocket mWebSocket;
    private static final ExecutorService sendExecutor = Executors.newSingleThreadExecutor();
    public static WebSocketUtil mUtil = new WebSocketUtil();
    private WebSocketListener mWebSocketListener = null;

    /**
     * 回调接口
     * @param webSocketListener
     */
    public void setWebSocketListener(WebSocketListener webSocketListener){
        this.mWebSocketListener = webSocketListener;
    }
    /**
     * 获取socket连接状态
     * @return
     */
    public byte getWsStatus() {
        return wsStatus;
    }

    public static WebSocketUtil getIns(){
        return mUtil;
    }


    /**
     * 初始化
     * @param url
     */
    public  void init(String url){
        wsStatus =WS_STATUS_CONNECTING;
        Request request = new Request.Builder().url(url).build();
        WebSocketCall webSocketCall = WebSocketCall.create(mOkHttpClient, request);
        webSocketCall.enqueue(new WebSocketListener(){
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d(TAG, "########onOpen");
                mWebSocket=webSocket;
                wsStatus = WS_STATUS_CONNECT;
                mWebSocketListener.onOpen(webSocket,response);
            }
            /**
             * 连接失败
             * @param e
             * @param response Present when the failure is a direct result of the response (e.g., failed
             * upgrade, non-101 response code, etc.). {@code null} otherwise.
             */
            @Override
            public void onFailure(IOException e, Response response) {
                Log.d(TAG,"onFailure.response:" + response);
                wsStatus = WS_STATUS_NULL;
                mWebSocketListener.onFailure(e,response);
            }

            /**
             * 接收到消息
             * @param message
             * @throws IOException
             */
            @Override
            public void onMessage(ResponseBody message) throws IOException {

                Log.e(TAG, "onMessage:" + message.source().readByteString().utf8());
                mWebSocketListener.onMessage(message);
            }

            @Override
            public void onPong(Buffer payload) {
                Log.d("WebSocketCall", "onPong:");
                mWebSocketListener.onPong(payload);
            }


            /**
             * 关闭
             * @param code The <a href="http://tools.ietf.org/html/rfc6455#section-7.4.1">RFC-compliant</a>
             * status code.
             * @param reason Reason for close or an empty string.
             */
            @Override
            public void onClose(int code, String reason) {
                Log.e(TAG,"###websocket.close");
                mWebSocketListener.onClose(code,reason);
                wsStatus = WS_STATUS_CLOSE;
                sendExecutor.shutdown();
            }
        });
    }

    /**
     * 需要新开一条线程来发送消息
     * @param message
     */
    public void sendMessage(RequestBody message){
        if(mWebSocket != null) {
            try{
                mWebSocket.sendMessage(message);
            }catch (Exception e){
               e.printStackTrace();
            }
        }else{
            Log.e(TAG,"#######websocket is null");
        }
    }

    /**
     * 发送buffer
     * @param payload
     */
    public void sendPing(Buffer payload){
        if(mWebSocket != null) {
            try{
                mWebSocket.sendPing(payload);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Log.e(TAG,"#######websocket is null");
        }
    }

    /**
     * 向服务器发送关闭帧
     * @param code
     * @param reason
     */
    public void close(int code, String reason){
        if(mWebSocket != null) {
            try{
                mWebSocket.close(code,reason);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Log.e(TAG,"#######websocket is null");
        }
    }



}


