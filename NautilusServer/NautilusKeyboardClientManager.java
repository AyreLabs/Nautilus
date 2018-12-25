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
public class NautilusKeyboardClientManager implements NautilusKeyBoardWebsocketListener {
    
    private NautilusWebSocketClientPool<NautilusKeyboardClient> poolOfConnectedKeyboardClients;
    private NautilusKeyReceiver nautilusKeyReceiver;
    private NautilusKeyboardWebsocketServer nautilusKeyboardWebsocketServer;

    public static NautilusKeyboardClientManager createNautilusKeyboardClientManagerAndStartListeningForClientsOnPortWithNautilusKeyReceiver(int keyboardControlPortNumber, NautilusKeyReceiver nautilusKeyReceiver) {
        NautilusKeyboardClientManager newNautilusKeyboardClientManager = new NautilusKeyboardClientManager(keyboardControlPortNumber, nautilusKeyReceiver);
        newNautilusKeyboardClientManager.start();
        return newNautilusKeyboardClientManager;
    }

    private NautilusKeyboardClientManager(int keyboardControlPortNumber, NautilusKeyReceiver nautilusKeyReceiver) {
        this.poolOfConnectedKeyboardClients = new NautilusWebSocketClientPool<NautilusKeyboardClient>();
        this.nautilusKeyboardWebsocketServer = NautilusKeyboardWebsocketServer.createNautilusKeyboardClientManagerAndStartListeningForClientsOnPortWithNautilusKeyboardWebsocketListener(keyboardControlPortNumber, this);
        this.nautilusKeyReceiver = nautilusKeyReceiver;
    }

    public void receiveWebSocketOpenEvent(WebSocket keyboardClientWebSocketConnection) {
        this.poolOfConnectedKeyboardClients.addClientToPoolWithWebSocketConnection(new NautilusKeyboardClient(), keyboardClientWebSocketConnection);
    }
  
    public void receiveWebSocketCloseEvent(WebSocket keyboardClientWebSocketConnection) {
        this.poolOfConnectedKeyboardClients.removeClientFromPoolForWebSocketConnection(keyboardClientWebSocketConnection);
    }

    public void receiveWebSocketMessageEvent(WebSocket keyboardClientWebSocketConnection, String message) {
        for (String nautilusKeyStringDescription : NautilusKeyboardProtocol.nautilusKeyStringDescriptionsFromKeyboardClientMessage(message)) {
            this.processNautilusKeyStringDescription(nautilusKeyStringDescription,keyboardClientWebSocketConnection);
        }
    }

    private void processNautilusKeyStringDescription(String nautilusKeyStringDescription, WebSocket keyboardClientWebSocketConnection) {
        NautilusKey keyReceivedFromKeyboardClient = NautilusKey.nautilusKeyWithNautilusKeyStringDescription(nautilusKeyStringDescription);
        NautilusKeyboardClient keyboardClientThatSentTheKey = this.poolOfConnectedKeyboardClients.getClientFromPoolForWebSocketConnection(keyboardClientWebSocketConnection);
        if (keyReceivedFromKeyboardClient.isANautilusRoomNumberKey()) {
            keyboardClientThatSentTheKey.setCurrentlySelectedNautilusRoomNumber(keyReceivedFromKeyboardClient.getSelectedNautilusRoomNumber());
        } else {
            this.nautilusKeyReceiver.receiveNautilusKeyPressForNautilusRoomNumber(keyReceivedFromKeyboardClient, keyboardClientThatSentTheKey.getCurrentlySelectedNautilusRoomNumber());
        }
    }
}
