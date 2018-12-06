

public class NautilusVRProtocol {

  public static String nautilusRoomTerminalPositionStateUpdateMessageWithTerminalIDPositionXYZAndRotationXYZ(int terminalID, double positionX, double positionY, double positionZ, double rotationX, double rotationY, double rotationZ) {
      return String.format("Q%d:%f,%f,%f,%f,%f,%f", terminalID, positionX, positionY, positionZ, rotationX, rotationY, rotationZ);
  }

  public static String nautilusRoomTerminalDisplayStateUpdateMessageWithTerminalIDAndDisplayString(int terminalID, String displayString) {
      return String.format("Z%d:%s", terminalID, displayString);
  }

}
