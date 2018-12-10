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

  private String currentTerminalCommandResultString = "";
  private String currentTerminalEnteredCommandString = "";

  private double positionX = 0;
  private double positionY = 0;
  private double positionZ = 0;
  private double rotationX = 0;
  private double rotationY = 0;
  private double rotationZ = 0;

  private boolean inFileEditingMode = false;
  private String contentsOfCurrentFileBeingEdited = "";
  private String currentMetaDirectory = "~";
  private String fileOpenedString = "";
  private int openedFileInsertOffset = 0;


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
    if (inFileEditingMode) {
      this.pressKeyInFileEditingMode(keyThatWasPressed);
    } else {
      this.pressKeyInTerminalEditingMode(keyThatWasPressed);
    }
  }

  public void pressKeyInFileEditingMode(NautilusKey keyThatWasPressed) {
    boolean keyPressedWasSaveKey = keyThatWasPressed.isEscapeKey();
    if (keyPressedWasSaveKey) {
      this.currentTerminalCommandResultString = this.runCommandReturningResult("cd " + this.currentMetaDirectory + ";echo \""+ this.contentsOfCurrentFileBeingEdited.replace("\"", "\\\"") + "\" > " + this.fileOpenedString);
      this.inFileEditingMode = false;
    } else if (keyThatWasPressed.isLeftArrowKey()) {
      this.openedFileInsertOffset += 1;
      if (this.openedFileInsertOffset > this.contentsOfCurrentFileBeingEdited.length())
        this.openedFileInsertOffset = this.contentsOfCurrentFileBeingEdited.length();
    } else if (keyThatWasPressed.isRightArrowKey()) {
      this.openedFileInsertOffset -= 1;
      if (this.openedFileInsertOffset < 0)
        this.openedFileInsertOffset = 0;
    } else if (keyThatWasPressed.isBackspaceKey()) {
      if (this.contentsOfCurrentFileBeingEdited.length() == this.openedFileInsertOffset) {

      } else 
      if (this.contentsOfCurrentFileBeingEdited.length() > 0) {
        if (0 == this.openedFileInsertOffset) {
          if (this.contentsOfCurrentFileBeingEdited.length() > 0)
            this.contentsOfCurrentFileBeingEdited = this.contentsOfCurrentFileBeingEdited.substring(0,this.contentsOfCurrentFileBeingEdited.length()-1);
        } else {
          int midIndex = this.contentsOfCurrentFileBeingEdited.length() - this.openedFileInsertOffset;
          this.contentsOfCurrentFileBeingEdited = this.contentsOfCurrentFileBeingEdited.substring(0,midIndex) + this.contentsOfCurrentFileBeingEdited.substring(midIndex+1);
        }
      }
    } else {
      int midIndex = this.contentsOfCurrentFileBeingEdited.length() - this.openedFileInsertOffset;
      if (this.contentsOfCurrentFileBeingEdited.length() == this.openedFileInsertOffset) {
        this.contentsOfCurrentFileBeingEdited = keyThatWasPressed.getStringRepresentationOfKey() + this.contentsOfCurrentFileBeingEdited;
      } else if (0 == this.openedFileInsertOffset) {
        this.contentsOfCurrentFileBeingEdited = this.contentsOfCurrentFileBeingEdited + keyThatWasPressed.getStringRepresentationOfKey();
      } else {
        this.contentsOfCurrentFileBeingEdited = this.contentsOfCurrentFileBeingEdited.substring(0,midIndex+1) + keyThatWasPressed.getStringRepresentationOfKey() + this.contentsOfCurrentFileBeingEdited.substring(midIndex+1);
      }
    }
  }

  public void pressKeyInTerminalEditingMode(NautilusKey keyThatWasPressed) {
    if (keyThatWasPressed.isEnterKey()) {
      this.currentTerminalCommandResultString = this.runCommandReturningResult(this.currentTerminalEnteredCommandString);
      this.currentTerminalEnteredCommandString = "";
    } else {
      this.currentTerminalCommandResultString = "";
      if (keyThatWasPressed.isBackspaceKey()) {
        this.currentTerminalEnteredCommandString = this.stringByRemovingLastCharacter(this.currentTerminalEnteredCommandString);
      } else {
        this.currentTerminalEnteredCommandString = this.currentTerminalEnteredCommandString + keyThatWasPressed.getStringRepresentationOfKey();
      } 
    }
  }

  private String stringByRemovingLastCharacter(String str) {
    if (str != null && str.length() > 0) {
        str = str.substring(0, str.length() - 1);
    }
    return str;
  }


  public String constructNautilusRoomTerminalPositionStateUpdateMessage() {
    return NautilusVRProtocol.nautilusRoomTerminalPositionStateUpdateMessageWithTerminalIDPositionXYZAndRotationXYZ(this.terminalID, this.positionX, this.positionY, this.positionZ, this.rotationX, this.rotationY, this.rotationZ);
  } 

  public String constructNautilusRoomTerminalDisplayStateUpdateMessage() {
    return NautilusVRProtocol.nautilusRoomTerminalDisplayStateUpdateMessageWithTerminalIDAndDisplayString(this.terminalID, this.constructCurrentDisplayString());
  }

  private String constructCurrentDisplayString() {
    if (this.inFileEditingMode) {
      return "~~~EDIT FILE~~~\n"+this.contentsOfCurrentFileBeingEdited;
    } else {
      return this.currentTerminalCommandResultString+this.currentTerminalEnteredCommandString;
    }
  }

  public String runCommandReturningResult(String commandToRun) {
    String terminalResult = "";
    System.out.println("command to run:"+commandToRun);
    //System.out.println(commandToRun.substring(0, 2));
    System.out.println("hello there");
    boolean isAMetaCommand = commandToRun.length() >= 2 && commandToRun.substring(0, 2).equals("~!");
    if (isAMetaCommand) {
      String[] componentsOfCommand = commandToRun.split(" ");
      for(String component: componentsOfCommand) {
        System.out.println("component: "+component);
      }
      boolean fileCDCommand = componentsOfCommand[0].equals("~!cd");
      boolean fileEditCommand = componentsOfCommand[0].equals("~!of");
      if (fileCDCommand) {
        if (componentsOfCommand.length > 1) {
          this.currentMetaDirectory = componentsOfCommand[1];
        }
      }
      if (fileEditCommand) {
        System.out.println("1");
        if (componentsOfCommand.length > 1) {
          System.out.println("2");
          System.out.println(componentsOfCommand[1]);
          this.changeToFileEditModeWithFileToOpenString(componentsOfCommand[1]);
        }
      }
    } else {
      //normal terminal command
      terminalResult = this.runSystemCommandReturningResult("cd " + this.currentMetaDirectory + ";" + commandToRun);
    }

    return terminalResult;
  }


  private void changeToFileEditModeWithFileToOpenString(String fileToOpenString) {
    this.inFileEditingMode = true;
    this.openedFileInsertOffset = 0;
    this.fileOpenedString = fileToOpenString;
    System.out.println("cd " + this.currentMetaDirectory + ";cat "+ fileToOpenString);
    this.contentsOfCurrentFileBeingEdited = this.runSystemCommandReturningResult("cd " + this.currentMetaDirectory + ";cat "+ fileToOpenString);
  }

  private String runSystemCommandReturningResult(String commandToRun) {
    String resultOfSystemCommand = "";
    System.out.println("command we are running: " + commandToRun);
    try {
      String s = null;
      Process p = Runtime.getRuntime().exec(commandToRun);
            
      BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {
                resultOfSystemCommand = resultOfSystemCommand+s;
            }
            System.out.println("result of running terminal command: " + resultOfSystemCommand);
            
           /*while ((s = stdError.readLine()) != null) {
                resultOfSystemCommand = resultOfSystemCommand+s;
            }*/

      }
      catch (Exception exception) {
            exception.printStackTrace();
      }
    return resultOfSystemCommand;
  }
}



