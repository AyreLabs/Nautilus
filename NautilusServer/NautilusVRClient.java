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
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

//----------------------------------------------------------------------------------------
// CLASS DEFINITION
//----------------------------------------------------------------------------------------
public class NautilusVRClient {

	public final WebSocket webSocketConnectionToVRClient;
    
    public NautilusVRClient(WebSocket webSocketConnectionToVRClient) {
        this.webSocketConnectionToVRClient = webSocketConnectionToVRClient;
    }
    
    public void sendStringMessageToClient(String stringMessage) {
        this.webSocketConnectionToVRClient.send(stringMessage);
    }
}
