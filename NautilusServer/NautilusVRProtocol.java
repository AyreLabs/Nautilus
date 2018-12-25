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

//----------------------------------------------------------------------------------------
// CLASS DEFINITION
//----------------------------------------------------------------------------------------
public class NautilusVRProtocol {

  public static String nautilusRoomTerminalPositionStateUpdateMessageWithTerminalIDPositionXYZAndRotationXYZAndSpatialTerminalWidth(int terminalID, double positionX, double positionY, double positionZ, double rotationX, double rotationY, double rotationZ, double spatialTerminalWidth) {
      return String.format("Q%d:%f,%f,%f,%f,%f,%f,%f", terminalID, positionX, positionY, positionZ, rotationX, rotationY, rotationZ, spatialTerminalWidth);
  }

  public static String nautilusRoomTerminalDisplayStateUpdateMessageWithTerminalIDAndDisplayString(int terminalID, String displayString) {
      return String.format("Z%d:%s", terminalID, displayString);
  }
}
