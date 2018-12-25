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
public class NautilusServer {

    private NautilusKeyboardClientManager nautilusKeyboardClientManager;
    private NautilusVRClientManager nautilusVRClientManager;
    private NautilusRoom nautilusRoom;

    public static void main(String[] commandLineArguments) {
        try {
            NautilusServer.attemptToStartServer(commandLineArguments);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private static attemptToStartServer(String[] commandLineArguments) throws Exception {
        boolean commandLineArgumentsProvidedAreValid = commandLineArguments.length > 1;
        if (commandLineArgumentsProvidedAreValid) {
            NautilusServer.startServerForCommandLineArguements(commandLineArguments);
        } else {
            String nautilusFormatDescriptionForTerminalConfigurationString = NautilusRoomTerminal.getNautilusFormatDescriptionForTerminalConfigurationString();
            System.out.printf("Usage: NautilusServer <Keyboard Client Port Number> <VR Client Port Number> [Nautilus Terminal Configuration Strings (format: %s)]\n", nautilusFormatDescriptionForTerminalConfigurationString);
        }
    }

    private static void startServerForCommandLineArguements(String[] commandLineArguments) throws Exception {
        int keyboardControlPortNumber = Integer.parseInt(commandLineArguments[0]);
        int vrPortNumber = Integer.parseInt(commandLineArguments[1]);
        ArrayList<String> nautilusTerminalConfigurationStrings = NautilusServer.parseNautilusTerminalConfigurationStrings(commandLineArguments);
        NautilusServer nautilusServer = NautilusServer.startAndRunNautilusServerWithKeyboardControlPortNumberAndVRPortNumberAndNautilusTerminalConfigurationStrings(
                    keyboardControlPortNumber, vrPortNumber, nautilusTerminalConfigurationStrings);
    }

    private static ArrayList<String> parseNautilusTerminalConfigurationStrings(String[] commandLineArguments) {
        ArrayList<String> nautilusTerminalConfigurationStrings = new ArrayList<String>();
        for (int commandLineArgumentsIndex = 2; commandLineArgumentsIndex < commandLineArguments.length; commandLineArgumentsIndex++) {
            nautilusTerminalConfigurationStrings.add(commandLineArguments[commandLineArgumentsIndex]);
        }
        return nautilusTerminalConfigurationStrings;
    }

    public static NautilusServer startAndRunNautilusServerWithKeyboardControlPortNumberAndVRPortNumberAndNautilusTerminalConfigurationStrings(
        int keyboardControlPortNumber, int vrPortNumber, ArrayList<String> nautilusTerminalConfigurationStrings) {
        return new NautilusServer(keyboardControlPortNumber, vrPortNumber, nautilusTerminalConfigurationStrings);
    }

    private NautilusServer(int keyboardControlPortNumber, int vrPortNumber, ArrayList<String> nautilusTerminalConfigurationStrings) {
        this.setupNautilusRoomForConfiguration(nautilusTerminalConfigurationStrings);
        this.nautilusKeyboardClientManager = NautilusKeyboardClientManager.createNautilusKeyboardClientManagerAndStartListeningForClientsOnPortWithNautilusKeyReceiver(keyboardControlPortNumber, this.nautilusRoom);
        this.nautilusVRClientManager = NautilusVRClientManager.createNautilusVRClientManagerAndStartListeningForClientsOnPortWithNautilusRoomObserver(vrPortNumber, this.nautilusRoom);
        this.nautilusRoom.setNautilusRoomUpdateListener(this.nautilusVRClientManager);
    }

    private void setupNautilusRoomForConfiguration(ArrayList<String> nautilusTerminalConfigurationStrings) {
        this.nautilusRoom = NautilusRoom.newEmptyNautilusRoom();
        int currentTerminalID = 0;
        for (String nautilusTerminalConfigurationString : nautilusTerminalConfigurationStrings) {
            this.createNautilusRoomForConfigrautionStringAndID(nautilusTerminalConfigurationString, currentTerminalID);
            currentTerminalID += 1;
        }
    }

    private void createNautilusRoomForConfigrautionStringAndID() {
        NautilusRoomTerminal newNautilusTerminal = NautilusRoomTerminal.newNautilusTerminalFromConfigurationStringAndTerminalID(nautilusTerminalConfigurationString, currentTerminalID);
        this.nautilusRoom.addTerminalToRoom(newNautilusTerminal);
    }
}
