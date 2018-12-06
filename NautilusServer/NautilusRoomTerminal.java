public class NautilusRoomTerminal {
  //private boolean isAValidTerminal = false;

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
    String[] terminalConfigurationStringComponents = terminalConfigurationString.split("~");

  }

  public NautilusRoomTerminal newNautilusTerminalFromConfigurationStringAndTerminalID(String terminalConfigurationString, int terminalID) {
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

runCommandReturningResult
  constructNautilusRoomTerminalPositionStateUpdateMessage()
constructNautilusRoomTerminalDisplayStateUpdateMessage
}
