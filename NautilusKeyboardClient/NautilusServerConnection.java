/*----------------------------------------------------------------------------------------
    PROJECT
    -------
    Project Nautilus

    DESCRIPTION
    -----------
    ....

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

public class NautilusServerConnection implements NautilusKeyListener {
    
    private boolean isConnected = false;
    private WebSocketClient webSocketToServerConnection = null;

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
        } catch ( Exception exception ) {
            exception.printStackTrace();
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
        this.webSocketToServerConnection.send(NautilusKeyboardProtocol.keyPressEventMessageToStringWithKeyEvent(keyEvent));
    }
}
