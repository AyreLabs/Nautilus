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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

//----------------------------------------------------------------------------------------
// CLASS DEFINITION
//----------------------------------------------------------------------------------------
public class NautilusRoomTerminal {

    private int terminalID;
    private String currentTerminalWindowDisplayString;
    private NautilusRoomTerminalDimensions roomDimensions;
    private boolean terminalDisplayNeedsToBeUpdated;
    private long systemTimeThatTerminalDisplayWasLastUpdated;
    private NautilusTerminalService terminalServiceForThisNautilusTerminal;

    private final static long MS_DELAY_BETWEEN_REGULAR_TERMINAL_DISPLAY_UPDATES = 3000;

    public static String getNautilusFormatDescriptionForTerminalConfigurationString() {
        return "terminal_initilisation_string~position-x~position-y~position-z~rotation-x~rotation-y~rotation-z~spatialTerminalWidth";
    }

    private NautilusRoomTerminal(String terminalConfigurationString, int terminalID) {
        this.setupRoomStaticFeatures(terminalConfigurationString, terminalID);
        this.setupRoomDynamicFeatures(terminalID);
    }

    private void setupRoomStaticFeatures(String terminalConfigurationString, int terminalID) {
        this.currentTerminalWindowDisplayString = "";
        this.terminalID = terminalID;
        this.roomDimensions = NautilusRoomTerminalDimensions();
        this.setupRoomDimensionsFromTerminalConfigurationString(terminalConfigurationString);        
    }

    private void setupRoomDynamicFeatures(int terminalID) {
        this.terminalServiceForThisNautilusTerminal = NautilusTerminalService.startTerminalServiceForTerminalWithID(terminalID);
        this.terminalDisplayNeedsToBeUpdated = true;
        this.systemTimeThatTerminalDisplayWasLastUpdated = 0;
    }

    private void setupRoomDimensionsFromTerminalConfigurationString() {
        String[] terminalConfigurationStringComponents = terminalConfigurationString.split("~");
        boolean terminalConfigurationStringIsValid = terminalConfigurationStringComponents.length >= 7;
        if (terminalConfigurationStringIsValid) {
            this.attemptToSetRoomDimensionsValuesFromTerminalConfigurationStringComponents(terminalConfigurationStringComponents);
        }
    }

    private void attemptToSetRoomDimensionsValuesFromTerminalConfigurationStringComponents(String[] terminalConfigurationStringComponents) {
        try {
            this.setRoomDimensionsValuesFromTerminalConfigurationStringComponents(terminalConfigurationStringComponents);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setRoomDimensionsValuesFromTerminalConfigurationStringComponents(String[] terminalConfigurationStringComponents) {
        this.setRoomPositionFromTerminalConfigurationStringComponents(terminalConfigurationStringComponents);
        this.setRoomRotationFromTerminalConfigurationStringComponents(terminalConfigurationStringComponents);
    }

    private void setRoomPositionFromTerminalConfigurationStringComponents(String[] terminalConfigurationStringComponents) {
        this.roomDimensions.positionX = Double.valueOf(terminalConfigurationStringComponents[1]);
        this.roomDimensions.positionY = Double.valueOf(terminalConfigurationStringComponents[2]);
        this.roomDimensions.positionZ = Double.valueOf(terminalConfigurationStringComponents[3]);
        this.roomDimensions.spatialTerminalWidth = Double.valueOf(terminalConfigurationStringComponents[7]);
    }

    private void setRoomRotationFromTerminalConfigurationStringComponents(String[] terminalConfigurationStringComponents) {
        this.roomDimensions.rotationX = Double.valueOf(terminalConfigurationStringComponents[4]);
        this.roomDimensions.rotationY = Double.valueOf(terminalConfigurationStringComponents[5]);
        this.roomDimensions.rotationZ = Double.valueOf(terminalConfigurationStringComponents[6]); 
    }

    public static NautilusRoomTerminal newNautilusTerminalFromConfigurationStringAndTerminalID(String terminalConfigurationString, int terminalID) {
        return new NautilusRoomTerminal(terminalConfigurationString, terminalID);
    }

    public void pressKeyOnTerminal(NautilusKey keyThatWasPressed) {
        if (keyThatWasPressed.isBackspaceKey()) {
            this.terminalServiceForThisNautilusTerminal.runResponselessNautilusTerminalServiceCommand("InjectBackspaceIntoSTDINForTerminalWithID");
        } else if (keyThatWasPressed.isEnterKey()) {
            this.terminalServiceForThisNautilusTerminal.runResponselessNautilusTerminalServiceCommand("InjectReturnIntoSTDINForTerminalWithID");
        } else if (keyThatWasPressed.isEscapeKey()) {
            this.terminalServiceForThisNautilusTerminal.runResponselessNautilusTerminalServiceCommand("InjectEscapeIntoSTDINForTerminalWithID");
        } else {
            this.terminalServiceForThisNautilusTerminal.runResponselessNautilusTerminalServiceCommandWithInputParameter("InjectSTDINForTerminalWithIDAndInjectedInput", "\"" + keyThatWasPressed.getStringRepresentationOfKey() + "\"");
        } 
        this.terminalDisplayNeedsToBeUpdated = true;
    }

    private String stringByRemovingLastCharacter(String stringInQuestion) {
        if (stringInQuestion != null && stringInQuestion.length() > 0) {
            stringInQuestion = stringInQuestion.substring(0, stringInQuestion.length() - 1);
        }
        return stringInQuestion;
    }

    public String constructNautilusRoomTerminalPositionStateUpdateMessage() {
        return NautilusVRProtocol.nautilusRoomTerminalPositionStateUpdateMessageWithTerminalIDPositionXYZAndRotationXYZAndSpatialTerminalWidth(this.terminalID, this.roomDimensions.positionX, this.roomDimensions.positionY, this.roomDimensions.positionZ, this.roomDimensions.rotationX, this.roomDimensions.rotationY, this.roomDimensions.rotationZ, this.roomDimensions.spatialTerminalWidth);
    } 

    public String constructNautilusRoomTerminalDisplayStateUpdateMessage() {
        String currentTerminalDisplay = this.constructCurrentDisplayString();
        System.out.printf("AAAAAAAAAAA:%s\n", currentTerminalDisplay);
        return NautilusVRProtocol.nautilusRoomTerminalDisplayStateUpdateMessageWithTerminalIDAndDisplayString(this.terminalID, currentTerminalDisplay);
    }

    private String constructCurrentDisplayString() {
        return this.currentTerminalWindowDisplayString;
    }

    public boolean terminalHasDirtyDisplay() {
        //return false;
        return this.terminalDisplayNeedsToBeUpdated 
            || (System.currentTimeMillis() - this.systemTimeThatTerminalDisplayWasLastUpdated) > MS_DELAY_BETWEEN_REGULAR_TERMINAL_DISPLAY_UPDATES;
    }

    public void updateDirtyTerminalDisplay() {
        this.updateRoomTerminalWithNewTerminalDisplayInformation();
        this.terminalDisplayNeedsToBeUpdated = false;
        this.systemTimeThatTerminalDisplayWasLastUpdated = System.currentTimeMillis();
    }

    private void updateRoomTerminalWithNewTerminalDisplayInformation() {
         String terminalDisplayInfoDump = this.terminalServiceForThisNautilusTerminal.runNautilusTerminalServiceCommandAndReturningResult("PullDisplayForTerminalWithID");
        this.currentTerminalWindowDisplayString = terminalDisplayInfoDump;
    }
}
