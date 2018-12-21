/*----------------------------------------------------------------------------------------
    PROJECT
    -------
    Project Nautilus

    DESCRIPTION
    -----------
    Websocket server connection to Nautilus Application Server

    AUTHOR
    ------
    Ayre Labs (2018)
----------------------------------------------------------------------------------------*/
import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.*;
import java.util.ArrayList;

public class NautilusServerConnection implements NautilusKeyListener {
    
    private boolean isConnected = false;
    private WebSocketClient webSocketToServerConnection = null;

    private ConcurrentLinkedQueue<KeyEvent> keyEventQueueBuffer = new ConcurrentLinkedQueue<KeyEvent>();

    /*----------------------------------------------------------------------------------------
    Public Methods
    ----------------------------------------------------------------------------------------*/
    public boolean isConnectedToServer() {
        return this.isConnected;
    }

    public NautilusKeyListener listenerToSendKeyPressesToNautilusServer() {
        return this;
    }

    /*----------------------------------------------------------------------------------------
    Private Methods
    ----------------------------------------------------------------------------------------*/
    private NautilusServerConnection(URI nautilusServerURI) {
        try {
            this.webSocketToServerConnection = new NautilusWebSocket( nautilusServerURI );
            this.webSocketToServerConnection.connect();
            this.isConnected = true;
            this.startRunningThreadToContinuouslySendKeyEventsToServer();
        } catch ( Exception exception ) {
            exception.printStackTrace();
        }
    }

    private void startRunningThreadToContinuouslySendKeyEventsToServer() {
        new Thread() {
        public void run() {
          while(true) {
            try {
              Thread.sleep(500);
            } catch (Exception exception) {
              exception.printStackTrace();
            }
            NautilusServerConnection.this.sendKeyEventsToServer();
          }
        }
      }.start();
    }

    private void sendKeyEventsToServer() {
        ArrayList<KeyEvent> keyEventsToSendToServer = new ArrayList<KeyEvent>();
        while (true) {
            KeyEvent nextKeyEvent = this.keyEventQueueBuffer.poll();
            if (nextKeyEvent == null) {
                break;
            } else {
                keyEventsToSendToServer.add(nextKeyEvent);
            }
        }

        if (keyEventsToSendToServer.size() > 0) {
            String keyPressesMessageToSend = NautilusKeyboardProtocol.keyPressesEventMessageStringWithListOfKeyEvents(keyEventsToSendToServer);
            System.out.printf("asd: %s\n", keyPressesMessageToSend);
            this.webSocketToServerConnection.send(keyPressesMessageToSend);
        }
    }

    /*----------------------------------------------------------------------------------------
    Static Public Methods
    ----------------------------------------------------------------------------------------*/
    public static NautilusServerConnection attemptToConnectToNautilusServerWithURI(URI nautilusServerURI) {
        return new NautilusServerConnection(nautilusServerURI);
    }

    /*----------------------------------------------------------------------------------------
    Nautilus KeyListener Implemented Methods
    ----------------------------------------------------------------------------------------*/
    public void listenToKeyPressEvent(KeyEvent keyEvent) {
        this.keyEventQueueBuffer.add(keyEvent);
    }
}




