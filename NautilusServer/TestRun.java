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

public class TestRun {
  //private boolean isAValidTerminal = false;

  private static Process commandProxyProcess = null;

  public static void main( String[] commandLineArguments ) throws Exception {



String sentence;
  String modifiedSentence;
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      Socket clientSocket = new Socket("127.0.0.1", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
          BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //sentence = inFromUser.readLine();
              for (int i=0;i<10;++i)
                  //outToServer.writeBytes("./screen -S \"Nautilus0\" -X stuff \"Q\"\n");
                  outToServer.writeBytes("ps aux\n");
                    //modifiedSentence = inFromServer.readLine();
                      //System.out.println("FROM SERVER: " + modifiedSentence);
                        clientSocket.close();



/*
        try{commandProxyProcess = new ProcessBuilder("./SystemCommandProxy").start();
BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(commandProxyProcess.getOutputStream()));
        for (int i=0;i<1;++i) {
writer.write("./screen -S Nautilus0 -X stuff L\n./screen -S Nautilus0 -X stuff L\n./screen -S Nautilus0 -X stuff L\n./screen -S Nautilus0 -X stuff L\n");
           // TestRun.runNautilusScreenServiceOnTerminalWithIDAndInputParameterReturningResult("InjectSTDINForTerminalWithIDAndInjectedInput", "0", "O");
  //./screen -S Nautilus0 -X stuff Q
  }
        writer.flush();
        commandProxyProcess.waitFor();//destroyForcibly();
        }catch(Exception e){e.printStackTrace();}
  */}





    private static String runNautilusScreenServiceOnTerminalWithIDAndInputParameterReturningResult(String serviceToRun, String terminalID, String inputParameter) {





        String resultOfSystemCommand = "";




















  //./screen -S Nautilus0 -X stuff Q

    	try {


BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(commandProxyProcess.getOutputStream()));
writer.write("./screen -S Nautilus0 -X stuff L\n");
        //writer.flush();
                //writer.close();


            /*
ProcessBuilder pb = new ProcessBuilder("./screen", "-S", "Nautilus0", "-X", "stuff", "Q");

    pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

                Process process = pb.start();
                        int errCode = process.waitFor();

*/


/*
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
*/
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



}



