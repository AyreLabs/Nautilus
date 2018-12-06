
import java.util.ArrayList;


public class NautilusServer {
  private NautilusKeyboardClientManager nautilusKeyboardClientManager;
  private NautilusVRClientManager nautilusVRClientManager;
  private NautilusRoom nautilusRoom;

  public static void main( String[] commandLineArguments ) {
    try {
      boolean commandLineArgumentsProvidedAreValid = commandLineArguments.length > 1;
      if (commandLineArgumentsProvidedAreValid) {
        int keyboardControlPortNumber = Integer.parseInt(commandLineArguments[0]);
        int vrPortNumber = Integer.parseInt(commandLineArguments[1]);
        ArrayList<String> nautilusTerminalConfigurationStrings = new ArrayList<String>();
        for (int commandLineArgumentsIndex = 2; commandLineArgumentsIndex < commandLineArguments.length; commandLineArgumentsIndex++) {
          nautilusTerminalConfigurationStrings.addObject(commandLineArguments[commandLineArgumentsIndex]);
        }
        NautilusServer nautilusServer = NautilusServer.startAndRunNautilusServerWithKeyboardControlPortNumberAndVRPortNumberAndNautilusTerminalConfigurationStrings(
          keyboardControlPortNumber, vrPortNumber, nautilusTerminalConfigurationStrings
        );

      } else {
        String nautilusFormatDescriptionForTerminalConfigurationString = NautilusRoomTerminal.getNautilusFormatDescriptionForTerminalConfigurationString();
        System.out.printf("Usage: NautilusServer <Keyboard Client Port Number> <VR Client Port Number> [Nautilus Terminal Configuration Strings (format: %s)]\n", nautilusFormatDescriptionForTerminalConfigurationString);
      }
    } catch(Exception exception) {
      exception.printStackTrace();
    }
  }

  public static NautilusServer startAndRunNautilusServerWithKeyboardControlPortNumberAndVRPortNumberAndNautilusTerminalConfigurationStrings(
    int keyboardControlPortNumber, int vrPortNumber, ArrayList<String> nautilusTerminalConfigurationStrings) {
      return new NautilusServer(keyboardControlPortNumber, vrPortNumber, nautilusTerminalConfigurationStrings);
  }

  private NautilusServer(int keyboardControlPortNumber, int vrPortNumber, ArrayList<String> nautilusTerminalConfigurationStrings) {
    this.nautilusRoom = NautilusRoom.newEmptyNautilusRoom();

    int currentTerminalID = 0;
    for (String nautilusTerminalConfigurationString : nautilusTerminalConfigurationStrings) {
      NautilusRoomTerminal newNautilusTerminal = NautilusRoomTerminal.newNautilusTerminalFromConfigurationStringAndTerminalID(nautilusTerminalConfigurationString, currentTerminalID);
      this.nautilusRoom.addTerminalToRoom(newNautilusTerminal);
      currentTerminalID += 1;
    }

    this.nautilusKeyboardClientManager = NautilusKeyboardClientManager.createNautilusKeyboardClientManagerAndStartListeningForClientsOnPortWithNautilusKeyReceiver(keyboardControlPortNumber, this.nautilusRoom);
    this.nautilusVRClientManager = NautilusVRClientManager.createNautilusVRClientManagerAndStartListeningForClientsOnPortWithNautilusRoomObserver(vrPortNumber, this.nautilusRoom);

    this.nautilusRoom.setNautilusRoomUpdateListener(this.nautilusVRClientManager);

  }


}
