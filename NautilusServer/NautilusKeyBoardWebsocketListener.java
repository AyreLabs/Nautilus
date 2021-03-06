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
// INTERFACE DEFINITION
//----------------------------------------------------------------------------------------
public interface NautilusKeyBoardWebsocketListener {
	
	public void receiveWebSocketOpenEvent(WebSocket keyboardClientWebSocketConnection);
  
    public void receiveWebSocketCloseEvent(WebSocket keyboardClientWebSocketConnection);

    public void receiveWebSocketMessageEvent(WebSocket keyboardClientWebSocketConnection, String message);
} 