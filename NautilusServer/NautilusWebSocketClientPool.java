import org.java_websocket.WebSocket;
import java.util.HashMap;
import java.net.InetSocketAddress;
import java.util.*;

public class NautilusWebSocketClientPool<CLIENT_OBJECT> {
  private HashMap<InetSocketAddress,CLIENT_OBJECT> mappingOfWebSocketAddressesToClientObjects = new HashMap<InetSocketAddress,CLIENT_OBJECT>();

  public NautilusWebSocketClientPool() { }

  public void addClientToPoolWithWebSocketConnection(CLIENT_OBJECT clientObjectToAddToPool, WebSocket webSocketConnection) {
    mappingOfWebSocketAddressesToClientObjects.put(webSocketConnection.getRemoteSocketAddress(), clientObjectToAddToPool);
  }

  public CLIENT_OBJECT getClientFromPoolForWebSocketConnection(WebSocket webSocketConnection) {
    return mappingOfWebSocketAddressesToClientObjects.get(webSocketConnection.getRemoteSocketAddress());
  }

  public void removeClientFromPoolForWebSocketConnection(WebSocket webSocketConnection) {
    mappingOfWebSocketAddressesToClientObjects.remove(webSocketConnection.getRemoteSocketAddress());
  }

  public List<CLIENT_OBJECT> getListOfAllClientsInPool() {
    return this.mappingOfWebSocketAddressesToClientObjects.values();
  }

}
