//----------------------------------------------------------------------------------------
//    PROJECT
//    -------
//    Project Nautilus
//
//    AUTHOR
//    ------
//    Ayre Labs (2018)
//----------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------
// IMPORTS
//----------------------------------------------------------------------------------------
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

//----------------------------------------------------------------------------------------
// CLASS DEFINITION
//----------------------------------------------------------------------------------------
public class NautilusKeyboardWebsocketServer extends WebSocketServer {
    
    private NautilusKeyBoardWebsocketListener nautilusKeyBoardWebsocketListener;

    public static NautilusKeyboardWebsocketServer createNautilusKeyboardClientManagerAndStartListeningForClientsOnPortWithNautilusKeyboardWebsocketListener(int keyboardControlPortNumber, NautilusKeyBoardWebsocketListener nautilusKeyBoardWebsocketListener) {
        NautilusKeyboardWebsocketServer newNautilusKeyboardClientManager = new NautilusKeyboardWebsocketServer(keyboardControlPortNumber, nautilusKeyBoardWebsocketListener);
        newNautilusKeyboardClientManager.start();
        return newNautilusKeyboardClientManager;
    }

    private NautilusKeyboardWebsocketServer(int keyboardControlPortNumber, NautilusKeyBoardWebsocketListener nautilusKeyBoardWebsocketListener) {
        super(new InetSocketAddress(keyboardControlPortNumber));
        this.nautilusKeyBoardWebsocketListener = nautilusKeyBoardWebsocketListener;
    }

    @Override
    public void onOpen(WebSocket keyboardClientWebSocketConnection, ClientHandshake handshake) {
        System.out.println(keyboardClientWebSocketConnection.getRemoteSocketAddress().getAddress().getHostAddress() + " connected as a keyboard client");
        this.nautilusKeyBoardWebsocketListener.receiveWebSocketOpenEvent(keyboardClientWebSocketConnection);
    }
  
    @Override
    public void onClose(WebSocket keyboardClientWebSocketConnection, int code, String reason, boolean remote) {
        System.out.println(keyboardClientWebSocketConnection.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected from being a keyboard client");
        this.nautilusKeyBoardWebsocketListener.receiveWebSocketCloseEvent(keyboardClientWebSocketConnection);
    }

    @Override
    public void onMessage(WebSocket keyboardClientWebSocketConnection, String message) {
        this.nautilusKeyBoardWebsocketListener.receiveWebSocketMessageEvent(keyboardClientWebSocketConnection, message);
    }

    @Override
    public void onMessage(WebSocket keyboardClientWebSocketConnection, ByteBuffer message) {
        System.out.println("Got a trashy byte message from keyboard client should not see this");
    } 

    @Override
    public void onError(WebSocket keyboardClientWebSocketConnection, Exception exception) {
        exception.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Started listening for keyboard client connections");
    }
}