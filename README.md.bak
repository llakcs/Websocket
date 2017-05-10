# Websocket

how to use

Step 1. Add the JitPack repository to your build file

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
Step 2. Add the dependency
	
		dependencies {
	        compile 'com.github.llakcs:Websocket:2.1'
	}


Step 3. Okhttpclient init

             final  static OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
            .build();
            
         
            
step 4. websocket init

        Request request = new Request.Builder().url(url).build();
        webSocketCall = WebSocketCall.create(mOkHttpClient, request);
        webSocketCall.enqueue(new WebSocketListener(){
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.e(TAG, "########onOpen");
                mWebSocket=webSocket;
//                wsStatus = WS_STATUS_CONNECT;

            }
            /**
             * 连接失败
             * @param e
             * @param response Present when the failure is a direct result of the response (e.g., failed
             * upgrade, non-101 response code, etc.). {@code null} otherwise.
             */
            @Override
            public void onFailure(IOException e, Response response) {
                Log.e(TAG,"onFailure.response:" + response);
            }

            /**
             * 接收到消息
             * @param message
             * @throws IOException
             */
            @Override
            public void onMessage(ResponseBody message) throws IOException {
                String result;
                if (message.contentType() == WebSocket.TEXT) {
                    Log.e(TAG, "onMessage:" + message.source().readByteString().utf8());
                    result =message.source().readByteString().utf8();
                } else {
                    BufferedSource source = message.source();
                    Log.d("WebSocketCall", "onMessage:" + source.readByteString());
                    result = source.readByteString().toString();

                }
                message.source().close();

            }

            @Override
            public void onPong(Buffer payload) {
                Log.e(TAG, "onPong:");

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
            }
        });     
        
step 5. SendMessage

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
           
          