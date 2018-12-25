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
import java.io.*;
import java.net.*;

//----------------------------------------------------------------------------------------
// CLASS DEFINITION
//----------------------------------------------------------------------------------------
public class NautilusTerminalService {
	
    private static final int BASE_PORT = 6789;
    private static final String LOCALHOST = "127.0.0.1";

    private final int terminalID;
	private Socket socketConnectionToTerminalCommandService;
	private DataOutputStream outputStreamToTerminalCommandService;
	private BufferedReader inputStreamFromTerminalCommandService;
    private String resultOfCommandReceivedFromService;

	public static NautilusTerminalService startTerminalServiceForTerminalWithID(int terminalID) {
		return new NautilusTerminalService(terminalID);
	}

	private NautilusTerminalService(int terminalID) {
        this.terminalID = terminalID;
		try {
            this.initializeParameters();
        } catch(Exception exception) {
        	exception.printStackTrace();
        }
	}

    private void initializeParameters() {
        int portNumberToConnectToServiceOn = BASE_PORT + this.terminalID;
        this.resultOfCommandReceivedFromService = "";
        this.socketConnectionToTerminalCommandService = new Socket(LOCALHOST, portNumberToConnectToServiceOn);
        this.outputStreamToTerminalCommandService = new DataOutputStream(this.socketConnectionToTerminalCommandService.getOutputStream());
        this.inputStreamFromTerminalCommandService = new BufferedReader(new InputStreamReader(this.socketConnectionToTerminalCommandService.getInputStream()));
    }

	public String runNautilusTerminalServiceCommandAndReturningResult(String terminalServiceCommand) {
		return this.runNautilusTerminalServiceCommandWithInputParameterAndReturningResult(terminalServiceCommand, "");
	}

	public void runResponselessNautilusTerminalServiceCommand(String terminalServiceCommand) {
		this.runResponselessNautilusTerminalServiceCommandWithInputParameter(terminalServiceCommand, "");
	}

	public synchronized String runNautilusTerminalServiceCommandWithInputParameterAndReturningResult(String terminalServiceCommand, String inputParameter) {
        this.resultOfCommandReceivedFromService = "";
		try {
            this.sendCommandToTerminalService(terminalServiceCommand, inputParameter);
            this.getResultantOutputFromRunningCommand();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        return this.resultOfCommandReceivedFromService;
	}

    private void getResultantOutputFromRunningCommand() throws Exception {
        boolean notFinishedAddingResultLines = true;
        while (notFinishedAddingResultLines) {
            notFinishedAddingResultLines = attemptToAddResultLine();
        }
    }

    private boolean attemptToAddResultLine() throws Exception {
        String nextInputLineReceived = this.inputStreamFromTerminalCommandService.readLine();
        boolean notFinishedAddingResultLines = true;
        //System.out.printf("I: %s\n", nextInputLineReceived);
        if (nextInputLineReceived.equals("END")) {
            notFinishedAddingResultLines = false;
        } else {
            String nextLineOfInput = this.inputStreamFromTerminalCommandService.readLine();
            //System.out.printf("L: %s\n", nextLineOfInput);
            this.resultOfCommandReceivedFromService += "\n" + nextLineOfInput;
        }
        return notFinishedAddingResultLines;
    }

    private void sendCommandToTerminalService(String terminalServiceCommand, String inputParameter) throws Exception {
        String commandToSendToTerminalCommandService = String.format("./SSfN_%s.sh %d %s", terminalServiceCommand, terminalID, inputParameter);
        this.outputStreamToTerminalCommandService.writeBytes(commandToSendToTerminalCommandService + "\n");
    }

	public void runResponselessNautilusTerminalServiceCommandWithInputParameter(String terminalServiceCommand, String inputParameter) {
		try {
            String commandToSendToTerminalCommandService = String.format("./SSfN_%s.sh %d %s", terminalServiceCommand, terminalID, inputParameter);
            this.outputStreamToTerminalCommandService.writeBytes(commandToSendToTerminalCommandService + "\n");
        } catch(Exception exception) {
            exception.printStackTrace();
        }
	}
}
