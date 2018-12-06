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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;

public class NautilusKeyboardClientDisplay extends JPanel implements KeyListener {

    private NautilusKeyListener nautilusKeyListener;

    public NautilusKeyboardClientDisplay() {
        this.setPanelParameters();
        this.showWindowAndAddPanelAsMainDisplay();
    }

    /*----------------------------------------------------------------------------------------
    Public Methods
    ----------------------------------------------------------------------------------------*/
    public void setNautilusKeyListener(NautilusKeyListener keyListenerInQuestion) {
        this.nautilusKeyListener = keyListenerInQuestion;
    }

    /*----------------------------------------------------------------------------------------
    Private Methods
    ----------------------------------------------------------------------------------------*/
    private void setPanelParameters() {
        this.setPreferredSize(new Dimension(500, 500));
        addKeyListener(this);
    }

    private void showWindowAndAddPanelAsMainDisplay() {
        JFrame mainFrameContainingNautilusKeyboardClient = new JFrame();
        mainFrameContainingNautilusKeyboardClient.getContentPane().add(this);
        mainFrameContainingNautilusKeyboardClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrameContainingNautilusKeyboardClient.pack();
        mainFrameContainingNautilusKeyboardClient.setVisible(true);
    }

    /*----------------------------------------------------------------------------------------
    KeyListener Implemented Methods
    ----------------------------------------------------------------------------------------*/
    public void keyPressed(KeyEvent keyEvent) {
        if(this.nautilusKeyListener != null) {
            this.nautilusKeyListener.listenToKeyPressEvent(keyEvent);
        }
    }

    public void keyReleased(KeyEvent keyEvent) { /* Nothing */ }

    public void keyTyped(KeyEvent keyEvent) { /* Nothing */ }

    /*----------------------------------------------------------------------------------------
    JPanel Implemented Methods
    ----------------------------------------------------------------------------------------*/
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }
}