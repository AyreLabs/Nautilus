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
public class NautilusVRClientManager implements NautilusRoomUpdateListener, NautilusVRWebsocketListener {
    
    private NautilusWebSocketClientPool<NautilusVRClient> poolOfConnectedVRClients;
    private NautilusVRWebsocketServer nautilusVRWebsocketServer;
    private NautilusRoomObserver nautilusRoomObserver;

    public static NautilusVRClientManager createNautilusVRClientManagerAndStartListeningForClientsOnPortWithNautilusRoomObserver(int vrPortNumber, NautilusRoomObserver nautilusRoomObserver) {
        NautilusVRClientManager newNautilusVRClientManager = new NautilusVRClientManager(vrPortNumber, nautilusRoomObserver);
        return newNautilusVRClientManager;
    }

    private NautilusVRClientManager(int vrPortNumber, NautilusRoomObserver nautilusRoomObserver) {
        this.nautilusRoomObserver = nautilusRoomObserver;
        this.poolOfConnectedVRClients = new NautilusWebSocketClientPool<NautilusVRClient>();
        this.nautilusVRWebsocketServer = NautilusVRWebsocketServer.createNautilusVRClientManagerAndStartListeningForClientsOnPortWithNautilusVRWebsocketListener(vrPortNumber, this);
    }

    public void receiveOnOpenEvent(WebSocket vrClientWebSocketConnection) {
        NautilusVRClient newNautilusVRClient = new NautilusVRClient(vrClientWebSocketConnection);
        this.poolOfConnectedVRClients.addClientToPoolWithWebSocketConnection(newNautilusVRClient, vrClientWebSocketConnection);
        for (NautilusRoomTerminal nautilusRoomTerminal : this.nautilusRoomObserver.getCurrentNautilusRoomTerminals()) {
            this.sendNautilusRoomTerminalPositionStateToVRClient(nautilusRoomTerminal, newNautilusVRClient);
            this.sendNautilusRoomTerminalDisplayStateToVRClient(nautilusRoomTerminal, newNautilusVRClient);
        }
    }

    public void receiveOnCloseEvent(WebSocket vrClientWebSocketConnection) {
        this.poolOfConnectedVRClients.removeClientFromPoolForWebSocketConnection(vrClientWebSocketConnection);
    }

    @Override
    public void receivedNotificationThatANautilusRoomTerminalWasUpdated(NautilusRoomTerminal terminalThatWasUpdated) {
        for (NautilusVRClient vrClientInPool : poolOfConnectedVRClients.getListOfAllClientsInPool()) {
            this.sendNautilusRoomTerminalDisplayStateToVRClient(terminalThatWasUpdated, vrClientInPool);
        }
    }

    private void sendNautilusRoomTerminalPositionStateToVRClient(NautilusRoomTerminal nautilusRoomTerminal, NautilusVRClient nautilusVRClient) {
        nautilusVRClient.sendStringMessageToClient(nautilusRoomTerminal.constructNautilusRoomTerminalPositionStateUpdateMessage());
    }

    private void sendNautilusRoomTerminalDisplayStateToVRClient(NautilusRoomTerminal nautilusRoomTerminal, NautilusVRClient nautilusVRClient) {
        nautilusVRClient.sendStringMessageToClient(nautilusRoomTerminal.constructNautilusRoomTerminalDisplayStateUpdateMessage());
    }
}
