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
    @Override
    public void keyPressed(KeyEvent keyEvent) {
    	boolean isAFunctionKey = (keyEvent.getKeyCode() >= KeyEvent.VK_F1 && keyEvent.getKeyCode() <= KeyEvent.VK_F12) || keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE || keyEvent.getKeyCode() == KeyEvent.VK_LEFT|| keyEvent.getKeyCode() == KeyEvent.VK_RIGHT;
    	if (isAFunctionKey) {
			keyEvent.setKeyCode(keyEvent.getKeyCode()+1000);
    		if(this.nautilusKeyListener != null) {
            	this.nautilusKeyListener.listenToKeyPressEvent(keyEvent);
        	}
    	}
    }

 	@Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { 
    	int keyCodeThatHasBeenKackedTogether = (int)keyEvent.getKeyChar();
    	keyEvent.setKeyCode(keyCodeThatHasBeenKackedTogether);
		if(this.nautilusKeyListener != null) {
            this.nautilusKeyListener.listenToKeyPressEvent(keyEvent);
        }
    }

    /*----------------------------------------------------------------------------------------
    JPanel Implemented Methods
    ----------------------------------------------------------------------------------------*/
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }
}