import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class NautilusKeyboardProtocol {

  public static String keyPressesEventMessageStringWithListOfKeyEvents(List<KeyEvent> keyEventsInQuestion) {
  	String keyPressesEventMessageString = "";
  	String seperator = "";
  	for (KeyEvent keyEvent : keyEventsInQuestion) {
  		keyPressesEventMessageString += seperator + String.format("%d", keyEvent.getKeyCode());
  		seperator = ",";
  	}
     return keyPressesEventMessageString;
  }

  public static String[] nautilusKeyStringDescriptionsFromKeyboardClientMessage(String keyboardClientMessage) {
      return keyboardClientMessage.split(",");
  }

}
