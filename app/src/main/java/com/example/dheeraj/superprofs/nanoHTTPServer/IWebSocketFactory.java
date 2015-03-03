package com.example.dheeraj.superprofs.nanoHTTPServer;

public interface IWebSocketFactory {
    WebSocket openWebSocket(NanoHTTPD.IHTTPSession handshake);
}
