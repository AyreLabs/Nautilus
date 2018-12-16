import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class NautilusRoomTerminal {
  //private boolean isAValidTerminal = false;

  private int terminalID = 0;

  private String currentTerminalWindowDisplayString = "";

  private double positionX = 0;
  private double positionY = 0;
  private double positionZ = 0;
  private double rotationX = 0;
  private double rotationY = 0;
  private double rotationZ = 0;
  private double spatialTerminalWidth = 0;

  private boolean terminalDisplayNeedsToBeUpdated = true;
  private long systemTimeThatTerminalDisplayWasLastUpdated = 0;
  private final static long MS_DELAY_BETWEEN_REGULAR_TERMINAL_DISPLAY_UPDATES = 1500;


  public static String getNautilusFormatDescriptionForTerminalConfigurationString() {
    return "terminal_initilisation_string~position-x~position-y~position-z~rotation-x~rotation-y~rotation-z~spatialTerminalWidth";
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
        this.spatialTerminalWidth = Double.valueOf(terminalConfigurationStringComponents[7]);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
  }

  public static NautilusRoomTerminal newNautilusTerminalFromConfigurationStringAndTerminalID(String terminalConfigurationString, int terminalID) {
    return new NautilusRoomTerminal(terminalConfigurationString, terminalID);
  }

  public void pressKeyOnTerminal(NautilusKey keyThatWasPressed) {
    if (keyThatWasPressed.isBackspaceKey()) {
        this.runNautilusScreenServiceOnTerminalWithIDAndInputParameterReturningResult("InjectBackspaceIntoSTDINForTerminalWithID", Integer.toString(terminalID), "");
    } else if (keyThatWasPressed.isEnterKey()) {
        this.runNautilusScreenServiceOnTerminalWithIDAndInputParameterReturningResult("InjectReturnIntoSTDINForTerminalWithID", Integer.toString(terminalID), "");
    } else if (keyThatWasPressed.isEscapeKey()) {
        this.runNautilusScreenServiceOnTerminalWithIDAndInputParameterReturningResult("InjectEscapeIntoSTDINForTerminalWithID", Integer.toString(terminalID), "");
    } else {
        this.runNautilusScreenServiceOnTerminalWithIDAndInputParameterReturningResult("InjectSTDINForTerminalWithIDAndInjectedInput", Integer.toString(terminalID), keyThatWasPressed.getStringRepresentationOfKey());
    }
    this.terminalDisplayNeedsToBeUpdated = true;
  }



    private String runNautilusScreenServiceOnTerminalWithIDAndInputParameterReturningResult(String serviceToRun, String terminalID, String inputParameter) {

        String resultOfSystemCommand = "";

    	try {
      String s = null;//./filewrite.sh ~ hello test
       //Process p = Runtime.getRuntime().exec("./filewrite.sh ");
       Process p = Runtime.getRuntime().exec(new String[] { "./screen_service_for_Nautilus/SSfN_"+serviceToRun+".sh", terminalID, inputParameter});
            
      BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {
                resultOfSystemCommand = resultOfSystemCommand+s+"\n";
            }

           while ((s = stdError.readLine()) != null) {
                resultOfSystemCommand = resultOfSystemCommand+s+"\n";
            }

      }
      catch (Exception exception) {
            exception.printStackTrace();
      }

      return resultOfSystemCommand;
    }

  private String stringByRemovingLastCharacter(String str) {
    if (str != null && str.length() > 0) {
        str = str.substring(0, str.length() - 1);
    }
    return str;
  }


  public String constructNautilusRoomTerminalPositionStateUpdateMessage() {
    return NautilusVRProtocol.nautilusRoomTerminalPositionStateUpdateMessageWithTerminalIDPositionXYZAndRotationXYZAndSpatialTerminalWidth(this.terminalID, this.positionX, this.positionY, this.positionZ, this.rotationX, this.rotationY, this.rotationZ, this.spatialTerminalWidth);
  } 

  public String constructNautilusRoomTerminalDisplayStateUpdateMessage() {
    String currentTerminalDisplay = this.constructCurrentDisplayString();
    //System.out.printf("AAAAAAAAAAA:%s\n", currentTerminalDisplay);
    return NautilusVRProtocol.nautilusRoomTerminalDisplayStateUpdateMessageWithTerminalIDAndDisplayString(this.terminalID, currentTerminalDisplay);
  }

  private String constructCurrentDisplayString() {
      return this.currentTerminalWindowDisplayString;
  }

  public boolean terminalHasDirtyDisplay() {
    return false;
    //return this.terminalDisplayNeedsToBeUpdated 
      //|| (System.currentTimeMillis() - this.systemTimeThatTerminalDisplayWasLastUpdated) > MS_DELAY_BETWEEN_REGULAR_TERMINAL_DISPLAY_UPDATES;
  }

  public void updateDirtyTerminalDisplay() {
    this.updateRoomTerminalWithNewTerminalDisplayInformation();
    this.terminalDisplayNeedsToBeUpdated = false;
    this.systemTimeThatTerminalDisplayWasLastUpdated = System.currentTimeMillis();
  }


  private void updateRoomTerminalWithNewTerminalDisplayInformation() {
    String terminalDisplayInfoDump = this.runNautilusScreenServiceOnTerminalWithIDAndInputParameterReturningResult("PullDisplayForTerminalWithID", Integer.toString(terminalID), "");
    String[] terminalDisplayInfoDumpComponents = terminalDisplayInfoDump.split("\n", -1);
    //System.out.printf("AAA: %s\n", terminalDisplayInfoDumpComponents[0]);
    String viewportHeightString = (terminalDisplayInfoDumpComponents[0].split(","))[2];
    int viewportHeight = Integer.parseInt(viewportHeightString);
    //String terminalStringBuffer = terminalDisplayInfoDumpComponents[1];
    String[] terminalStringBufferLines = terminalDisplayInfoDumpComponents;//terminalStringBuffer.split("\n");
    String terminalDisplayString = "";
    for (int terminalStringBufferViewportLineIndex = 0; terminalStringBufferViewportLineIndex< viewportHeight; terminalStringBufferViewportLineIndex++) {
  //System.out.printf("QQQ:linesLength:%d,height:%d,index:%d\n", terminalStringBufferLines.length, viewportHeight, terminalStringBufferViewportLineIndex);
        terminalDisplayString += terminalStringBufferLines[terminalStringBufferViewportLineIndex+(terminalStringBufferLines.length-viewportHeight-1)] + "\n";
    }
    this.currentTerminalWindowDisplayString = terminalDisplayString;
  }

}



