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
import java.util.ArrayList;

//----------------------------------------------------------------------------------------
// CLASS DEFINITION
//----------------------------------------------------------------------------------------
public class NautilusRoom implements NautilusRoomObserver, NautilusKeyReceiver {
  
    private ArrayList<NautilusRoomTerminal> terminalsInTheNautilusRoom;
    private NautilusRoomUpdateListener nautilusRoomUpdateListener;

    private NautilusRoom() {
        this.terminalsInTheNautilusRoom = new ArrayList<NautilusRoomTerminal>();
        this.nautilusRoomUpdateListener = null;
        this.startRunningThreadToContinuouslyUpdateDirtyTerminalDisplays();
    }

    private void startRunningThreadToContinuouslyUpdateDirtyTerminalDisplays() {
        Thread threadForContinuouslyUpdateDirtyTerminalDisplays = this.createThreadForContinuouslyUpdateDirtyTerminalDisplays();
        threadForContinuouslyUpdateDirtyTerminalDisplays.start();
    }

    private Thread createThreadForContinuouslyUpdateDirtyTerminalDisplays() {
        return new Thread() {
            public void run() {
                NautilusRoom.this.continuouslyUpdateDirtyTerminalDisplays();
            }
        };
    }

    public void continuouslyUpdateDirtyTerminalDisplays() {
        while(true) {
            this.attemptSleepForMilliSeconds(200);
            this.updateDirtyTerminalDisplays();
        }
    }

    private void attemptSleepForMilliSeconds(int sleepTimeInMilliSeconds) {
        try {
            Thread.sleep(sleepTimeInMilliSeconds);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static NautilusRoom newEmptyNautilusRoom() {
        return new NautilusRoom();
    }

    public void addTerminalToRoom(NautilusRoomTerminal terminalToAddToRoom) {
        this.terminalsInTheNautilusRoom.add(terminalToAddToRoom);
        this.notifyNautilusRoomUpdateListenersThatATerminalWasUpdated(terminalToAddToRoom);
    }

    public void receiveNautilusKeyPressForNautilusRoomNumber(NautilusKey keyThatWasPressed, int nautilusRoomNumber) {
        boolean terminalExistsCorrespondingToRoomNumber = this.terminalsInTheNautilusRoom.size() > nautilusRoomNumber && nautilusRoomNumber >= 0;
        if (terminalExistsCorrespondingToRoomNumber) {
            NautilusRoomTerminal theTerminalOnWhichToPressAKey = this.terminalsInTheNautilusRoom.get(nautilusRoomNumber);
            theTerminalOnWhichToPressAKey.pressKeyOnTerminal(keyThatWasPressed);
        }
    }  

    public ArrayList<NautilusRoomTerminal>  getCurrentNautilusRoomTerminals() {
        return this.terminalsInTheNautilusRoom;
    }

    public void setNautilusRoomUpdateListener(NautilusRoomUpdateListener nautilusRoomUpdateListener) {
        this.nautilusRoomUpdateListener = nautilusRoomUpdateListener;
    }

    private void notifyNautilusRoomUpdateListenersThatATerminalWasUpdated(NautilusRoomTerminal nautilusRoomTerminalThatWasUpdated) {
        if(this.nautilusRoomUpdateListener != null) {
            this.nautilusRoomUpdateListener.receivedNotificationThatANautilusRoomTerminalWasUpdated(nautilusRoomTerminalThatWasUpdated);
        }
    }  

    private void updateDirtyTerminalDisplays() {
        for (NautilusRoomTerminal terminalInTheNautilusRoom : this.terminalsInTheNautilusRoom) {
            this.updateDirtyTerminalDisplayForDisplay(terminalInTheNautilusRoom);
        } 
    }

    private void updateDirtyTerminalDisplayForDisplay(NautilusRoomTerminal terminalInTheNautilusRoom) {
        if (terminalInTheNautilusRoom.terminalHasDirtyDisplay()) {
            terminalInTheNautilusRoom.updateDirtyTerminalDisplay();
            this.notifyNautilusRoomUpdateListenersThatATerminalWasUpdated(terminalInTheNautilusRoom);
        }
    }
}