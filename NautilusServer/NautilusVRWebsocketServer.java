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
public class NautilusVRWebsocketServer extends WebSocketServer {
    
    private NautilusVRWebsocketListener nautilusVRWebsocketListener;

    public static NautilusVRWebsocketServer createNautilusVRClientManagerAndStartListeningForClientsOnPortWithNautilusVRWebsocketListener(int vrPortNumber, NautilusVRWebsocketListener nautilusVRWebsocketListener) {
        NautilusVRWebsocketServer newNautilusVRWebsocketServer = new NautilusVRWebsocketServer(vrPortNumber, nautilusVRWebsocketListener);
        newNautilusVRWebsocketServer.start();
        return newNautilusVRWebsocketServer;
    }

    private NautilusVRClientManager(int vrPortNumber, NautilusVRWebsocketListener nautilusVRWebsocketListener) {
        super(new InetSocketAddress(vrPortNumber));
        this.nautilusVRWebsocketListener = nautilusVRWebsocketListener;
    }

    @Override
    public void onOpen(WebSocket vrClientWebSocketConnection, ClientHandshake handshake) {
        System.out.println(vrClientWebSocketConnection.getRemoteSocketAddress().getAddress().getHostAddress() + " connected as a VR client");
        this.nautilusVRWebsocketListener.recieveOnOpenEvent(vrClientWebSocketConnection);
    }

    @Override
    public void onClose(WebSocket vrClientWebSocketConnection, int code, String reason, boolean remote) {
        System.out.println(vrClientWebSocketConnection.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected from being a VR client");
        this.nautilusVRWebsocketListener.recieveOnCloseEvent(vrClientWebSocketConnection);
    }

    @Override
    public void onMessage(WebSocket vrClientWebSocketConnection, String message) {
        System.out.println("Got a trashy string message from VR client should not see this");
    }

    @Override
    public void onMessage(WebSocket vrClientWebSocketConnection, ByteBuffer message) {
        System.out.println("Got a trashy byte message from VR client should not see this");
    }

    @Override
    public void onError(WebSocket vrClientWebSocketConnection, Exception exception) {
        exception.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Started listening for VR client connections");
    }
}