public class NautilusRoomTerminal {
  //private boolean isAValidTerminal = false;

  private int terminalID = 0;

  private String currentTerminalCommandResultString = "";
  private String currentTerminalEnteredCommandString = "";

  private double positionX = 0;
  private double positionY = 0;
  private double positionZ = 0;
  private double rotationX = 0;
  private double rotationY = 0;
  private double rotationZ = 0;


  public static String getNautilusFormatDescriptionForTerminalConfigurationString() {
    return "terminal_initilisation_string~position-x~position-y~position-z~rotation-x~rotation-y~rotation-z";
  }

  private NautilusRoomTerminal(String terminalConfigurationString, int terminalID) {
    this.terminalID = terminalID;

    String[] terminalConfigurationStringComponents = terminalConfigurationString.split("~");
    boolean terminalConfigurationStringIsValid = terminalConfigurationStringComponents.length >= 7;
    if (terminalConfigurationStringIsValid) {
      try {
        this.positionX = Double.valueOf(terminalConfigurationStringComponents[1]);
        this.positionY = Double.valueOf(terminalConfigurationStringComponents[2]);
        this.positionZ = Double.valueOf(terminalConfigurationStringComponents[3]);
        this.rotationX = Double.valueOf(terminalConfigurationStringComponents[4]);
        this.rotationY = Double.valueOf(terminalConfigurationStringComponents[5]);
        this.rotationZ = Double.valueOf(terminalConfigurationStringComponents[6]);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
  }

  public static NautilusRoomTerminal newNautilusTerminalFromConfigurationStringAndTerminalID(String terminalConfigurationString, int terminalID) {
    return new NautilusRoomTerminal(terminalConfigurationString, terminalID);
  }

  public void pressKeyOnTerminal(NautilusKey keyThatWasPressed) {
    if (keyThatWasPressed.isEnterKey()) {
      this.currentTerminalCommandResultString = this.runCommandReturningResult(this.currentTerminalEnteredCommandString);
      this.currentTerminalEnteredCommandString = "";
    } else {
      this.currentTerminalCommandResultString = "";
      this.currentTerminalEnteredCommandString = this.currentTerminalEnteredCommandString + keyThatWasPressed.getStringRepresentationOfKey();
    }
  }


  public String constructNautilusRoomTerminalPositionStateUpdateMessage() {
    return NautilusVRProtocol.nautilusRoomTerminalPositionStateUpdateMessageWithTerminalIDPositionXYZAndRotationXYZ(this.terminalID, this.positionX, this.positionY, this.positionZ, this.rotationX, this.rotationY, this.rotationZ);
  } 

  public String constructNautilusRoomTerminalDisplayStateUpdateMessage() {
    return NautilusVRProtocol.nautilusRoomTerminalDisplayStateUpdateMessageWithTerminalIDAndDisplayString(this.terminalID, this.currentTerminalCommandResultString+this.currentTerminalEnteredCommandString);
  }

  public String runCommandReturningResult(String commandToRun) {
    return "EXEC: " + commandToRun;
  }

}



