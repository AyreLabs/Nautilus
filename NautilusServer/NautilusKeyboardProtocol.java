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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

//----------------------------------------------------------------------------------------
// CLASS DEFINITION
//----------------------------------------------------------------------------------------
public class NautilusKeyboardProtocol {

    public static String keyPressesEventMessageStringWithListOfKeyEvents(List<KeyEvent> keyEventsInQuestion) {
        String seperator = "";
        String keyPressesEventMessageString = createMessageStringForSeperatorMessageStringAndEventList(keyEventsInQuestion, seperator, "");
        return keyPressesEventMessageString;
    }

    public static String[] nautilusKeyStringDescriptionsFromKeyboardClientMessage(String keyboardClientMessage) {
        return keyboardClientMessage.split(",");
    }

    private static String createMessageStringForSeperatorMessageStringAndEventList(List<KeyEvent> keyEventsInQuestion, String seperator, String keyPressesEventMessageString) {
        for (KeyEvent keyEvent : keyEventsInQuestion) {
            keyPressesEventMessageString += seperator + String.format("%d", keyEvent.getKeyCode());
            seperator = ",";
        }
        return keyPressesEventMessageString;
    }
}
