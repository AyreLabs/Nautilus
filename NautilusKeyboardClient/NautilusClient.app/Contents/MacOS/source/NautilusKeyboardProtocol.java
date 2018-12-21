/*----------------------------------------------------------------------------------------
    PROJECT
    -------
    Project Nautilus
    
    DESCRIPTION
    -----------
    Protocol to encapsulate key presses between client and server
    
    AUTHOR
    ------
    Ayre Labs (2018)
----------------------------------------------------------------------------------------*/
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class NautilusKeyboardProtocol {

  /*----------------------------------------------------------------------------------------
  Static Public Methods
  ----------------------------------------------------------------------------------------*/
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
