/*----------------------------------------------------------------------------------------
    PROJECT
    -------
    Project Nautilus

    DESCRIPTION
    -----------
    Key listener interface for Nautilus Keyboard Client

    AUTHOR
    ------
    Ayre Labs (2018)
----------------------------------------------------------------------------------------*/
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface NautilusKeyListener {

    public void listenToKeyPressEvent(KeyEvent keyEvent);
}
