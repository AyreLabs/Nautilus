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
		this.runNautilusTerminalServiceCommandWithInputParameterAndReturningResult(terminalServiceCommand, "");
	}

	public synchronized String runNautilusTerminalServiceCommandWithInputParameterAndReturningResult(String terminalServiceCommand, String inputParameter) {
		String commandToSendToTerminalCommandService = String.format("./SSfN_%s.sh %d %s", terminalServiceCommand, terminalID, inputParameter);
		this.outputStreamToTerminalCommandService.writeBytes(commandToSendToTerminalCommandService + "\n");
		String resultOfCommandReceivedFromService = "";

		while (true) {
			String nextInputLineReceived = this.inputStreamFromTerminalCommandService.readLine();
			if (nextInputLineReceived.equals("END")) {
				break;
			} else {
				resultOfCommandReceivedFromService += this.inputStreamFromTerminalCommandService.readLine();
			}
		}

		return resultOfCommandReceivedFromService;
	}


	//clientSocket.close();


}


