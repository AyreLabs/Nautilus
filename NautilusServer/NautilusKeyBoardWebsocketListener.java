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

//----------------------------------------------------------------------------------------
// INTERFACE DEFINITION
//----------------------------------------------------------------------------------------
public interface NautilusKeyBoardWebsocketListener {
	
	public void receiveWebSocketOpenEvent(WebSocket keyboardClientWebSocketConnection);
  
    public void receiveWebSocketCloseEvent(WebSocket keyboardClientWebSocketConnection);

    public void receiveWebSocketMessageEvent(WebSocket keyboardClientWebSocketConnection, String message);
} 