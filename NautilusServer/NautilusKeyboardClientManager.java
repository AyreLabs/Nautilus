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


public class NautilusKeyboardClientManager extends WebSocketServer {
  private NautilusWebSocketClientPool<NautilusKeyboardClient> poolOfConnectedKeyboardClients = new NautilusWebSocketClientPool<NautilusKeyboardClient>();
  private NautilusKeyReceiver nautilusKeyReceiver;

  private class NautilusKeyboardClient {
    private int currentlySelectedNautilusRoomNumber = 0;
    public NautilusKeyboardClient() {  }
    public void setCurrentlySelectedNautilusRoomNumber(int currentlySelectedNautilusRoomNumber) {
      this.currentlySelectedNautilusRoomNumber = currentlySelectedNautilusRoomNumber;
    }
    public int getCurrentlySelectedNautilusRoomNumber() {
      return this.currentlySelectedNautilusRoomNumber;
    }
  }

  public static NautilusKeyboardClientManager createNautilusKeyboardClientManagerAndStartListeningForClientsOnPortWithNautilusKeyReceiver(int keyboardControlPortNumber, NautilusKeyReceiver nautilusKeyReceiver) {
    NautilusKeyboardClientManager newNautilusKeyboardClientManager = new NautilusKeyboardClientManager(keyboardControlPortNumber, nautilusKeyReceiver);
    newNautilusKeyboardClientManager.start();
  }

  private NautilusKeyboardClientManager(int keyboardControlPortNumber, NautilusKeyReceiver nautilusKeyReceiver) {
    super(new InetSocketAddress(keyboardControlPortNumber));
    this.nautilusKeyReceiver = nautilusKeyReceiver;
  }

  @Override
  public void onOpen(WebSocket keyboardClientWebSocketConnection, ClientHandshake handshake) {
    System.out.println(keyboardClientWebSocketConnection.getRemoteSocketAddress().getAddress().getHostAddress() + " connected as a keyboard client");
    this.poolOfConnectedKeyboardClients.addClientToPoolWithWebSocketConnection(new NautilusKeyboardClient(), keyboardClientWebSocketConnection);
  }
  
  @Override
  public void onClose(WebSocket keyboardClientWebSocketConnection, int code, String reason, boolean remote) {
    System.out.println(keyboardClientWebSocketConnection.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected from being a keyboard client");
    this.poolOfConnectedKeyboardClients.removeClientFromPoolForWebSocketConnection(keyboardClientWebSocketConnection);
  }

  @Override
  public void onMessage(WebSocket keyboardClientWebSocketConnection, String message) {
    NautilusKey keyReceivedFromKeyboardClient = NautilusKey.nautilusKeyWithNautilusKeyStringDescription(NautilusKeyboardProtocol.nautilusKeyStringDescriptionFromKeyboardClientMessage(keyboardClientWebSocketConnection));
    NautilusKeyboardClient keyboardClientThatSentTheKey = this.poolOfConnectedKeyboardClients.getClientFromPoolForWebSocketConnection(keyboardClientWebSocketConnection);
    if (keyReceivedFromKeyboardClient.isANautilusRoomNumberKey()) {
      keyboardClientThatSentTheKey.setCurrentlySelectedNautilusRoomNumber(keyReceivedFromKeyboardClient.getSelectedNautilusRoomNumber());
    } else {
      this.nautilusKeyReceiver.receiveNautilusKeyPressForNautilusRoomNumber(keyReceivedFromKeyboardClient, keyboardClientThatSentTheKey.getCurrentlySelectedNautilusRoomNumber());
    }
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
    //setConnectionLostTimeout(0);
    //setConnectionLostTimeout(100);
  }


}
