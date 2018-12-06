import java.util.ArrayList;

public class NautilusRoom implements NautilusRoomObserver NautilusKeyReceiver {
  private ArrayList<NautilusRoomTerminal> terminalsInTheNautilusRoom = new ArrayList<NautilusRoomTerminal>();
  private NautilusRoomUpdateListener nautilusRoomUpdateListener = nil;

  private NautilusRoom() {

  }

  //public int getNumberOfNautilusRooms() {
  //  return this.terminalsInTheNautilusRoom.length;
  //}

  public NautilusRoom newEmptyNautilusRoom() {
    return new NautilusRoom();
  }

  public void addTerminalToRoom(NautilusRoomTerminal terminalToAddToRoom) {
    this.terminalsInTheNautilusRoom.addObject(terminalToAddToRoom);
    this.notifyNautilusRoomUpdateListenersThatATerminalWasUpdated(terminalToAddToRoom);
  }

  public void receiveNautilusKeyPressForNautilusRoomNumber(NautilusKey keyThatWasPressed, int nautilusRoomNumber) {
    boolean terminalExistsCorrespondingToRoomNumber = this.terminalsInTheNautilusRoom.count > nautilusRoomNumber && nautilusRoomNumber >= 0;
    if (terminalExistsCorrespondingToRoomNumber) {
      NautilusRoomTerminal theTerminalOnWhichToPressAKey = this.terminalsInTheNautilusRoom[nautilusRoomNumber];
      theTerminalOnWhichToPressAKey.pressKeyOnTerminal(keyThatWasPressed);
      this.notifyNautilusRoomUpdateListenersThatATerminalWasUpdated(theTerminalOnWhichToPressAKey);
    }
  }

  public NautilusRoomTerminal getCurrentNautilusRoomTerminals() {
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
}
