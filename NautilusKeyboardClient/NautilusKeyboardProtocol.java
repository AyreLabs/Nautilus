/*----------------------------------------------------------------------------------------
    PROJECT
    -------
    Project Nautilus
    DESCRIPTION
    -----------
    ...
    AUTHOR
    ------
    Ayre Labs (2018)
----------------------------------------------------------------------------------------*/
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NautilusKeyboardProtocol {

  /*----------------------------------------------------------------------------------------
  Static Public Methods
  ----------------------------------------------------------------------------------------*/
  public static String keyPressEventMessageToStringWithKeyEvent(KeyEvent keyEventInQuestion) {
      return String.format("%d", keyEventInQuestion.getKeyCode());
  }

  public static String nautilusKeyStringDescriptionFromKeyboardClientMessage(String keyboardClientMessage) {
      return keyboardClientMessage;
  }

}
