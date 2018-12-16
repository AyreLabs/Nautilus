import java.util.ArrayList;

public class NautilusRoom implements NautilusRoomObserver, NautilusKeyReceiver {
  private ArrayList<NautilusRoomTerminal> terminalsInTheNautilusRoom = new ArrayList<NautilusRoomTerminal>();
  private NautilusRoomUpdateListener nautilusRoomUpdateListener = null;

  private NautilusRoom() {
    this.startRunningThreadToContinuouslyUpdateDirtyTerminalDisplays();
  }

  private void startRunningThreadToContinuouslyUpdateDirtyTerminalDisplays() {
    new Thread() {
        public void run() {
          while(true) {
            try {
              Thread.sleep(150);
            } catch (Exception exception) {
              exception.printStackTrace();
            }
            NautilusRoom.this.updateDirtyTerminalDisplays();
          }
        }
      }.start();
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
    if (this.nautilusRoomUpdateListener != null) {
      this.nautilusRoomUpdateListener.receivedNotificationThatANautilusRoomTerminalWasUpdated(nautilusRoomTerminalThatWasUpdated);
    }
  }  


  private void updateDirtyTerminalDisplays() {
    for (NautilusRoomTerminal terminalInTheNautilusRoom : this.terminalsInTheNautilusRoom) {
      if (terminalInTheNautilusRoom.terminalHasDirtyDisplay()) {
        terminalInTheNautilusRoom.updateDirtyTerminalDisplay();
        this.notifyNautilusRoomUpdateListenersThatATerminalWasUpdated(terminalInTheNautilusRoom);
      }
    }
  }

}








