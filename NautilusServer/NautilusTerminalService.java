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


public class NautilusTerminalService {
	private final int terminalID;
	private static final int BASE_PORT = 6789;
	private static final String LOCALHOST = "127.0.0.1";
	private Socket socketConnectionToTerminalCommandService = null;
	private DataOutputStream outputStreamToTerminalCommandService = null;
	private BufferedReader inputStreamFromTerminalCommandService = null;

	public static NautilusTerminalService startTerminalServiceForTerminalWithID(int terminalID) {
		return new NautilusTerminalService(terminalID);
	}

	private NautilusTerminalService(int terminalID) {
		this.terminalID = terminalID;

		try {

			int portNumberToConnectToServiceOn = BASE_PORT + terminalID;
      		this.socketConnectionToTerminalCommandService = new Socket(LOCALHOST, portNumberToConnectToServiceOn);
      		this.outputStreamToTerminalCommandService = new DataOutputStream(this.socketConnectionToTerminalCommandService.getOutputStream());
        	this.inputStreamFromTerminalCommandService = new BufferedReader(new InputStreamReader(this.socketConnectionToTerminalCommandService.getInputStream()));

        } catch(Exception exception) {
        	exception.printStackTrace();
        }
	}


	public String runNautilusTerminalServiceCommandAndReturningResult(String terminalServiceCommand) {
		return this.runNautilusTerminalServiceCommandWithInputParameterAndReturningResult(terminalServiceCommand, "");
	}

	public void runResponselessNautilusTerminalServiceCommand(String terminalServiceCommand) {
		this.runResponselessNautilusTerminalServiceCommandWithInputParameter(terminalServiceCommand, "");
	}

	public synchronized String runNautilusTerminalServiceCommandWithInputParameterAndReturningResult(String terminalServiceCommand, String inputParameter) {
        String resultOfCommandReceivedFromService = "";
		try {
            String commandToSendToTerminalCommandService = String.format("./SSfN_%s.sh %d %s", terminalServiceCommand, terminalID, inputParameter);
            this.outputStreamToTerminalCommandService.writeBytes(commandToSendToTerminalCommandService + "\n");

            String seperator = "";
            while (true) {
                String nextInputLineReceived = this.inputStreamFromTerminalCommandService.readLine();
                //System.out.printf("I: %s\n", nextInputLineReceived);
                if (nextInputLineReceived.equals("END")) {
                    break;
                } else {
                    String nextLineOfInput = this.inputStreamFromTerminalCommandService.readLine();
                    //System.out.printf("L: %s\n", nextLineOfInput);
                    resultOfCommandReceivedFromService += seperator + nextLineOfInput;
                    seperator = "\n";
                }
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        return resultOfCommandReceivedFromService;
	}

	public void runResponselessNautilusTerminalServiceCommandWithInputParameter(String terminalServiceCommand, String inputParameter) {
		try {
            String commandToSendToTerminalCommandService = String.format("./SSfN_%s.sh %d %s", terminalServiceCommand, terminalID, inputParameter);
            this.outputStreamToTerminalCommandService.writeBytes(commandToSendToTerminalCommandService + "\n");

        } catch(Exception exception) {
            exception.printStackTrace();
        }
	}

	//clientSocket.close();


}


