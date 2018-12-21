/*----------------------------------------------------------------------------------------
    PROJECT
    -------
    Project Nautilus

    DESCRIPTION
    -----------
    Main application file for Nautilus Keyboard Client

    AUTHOR
    ------
    Ayre Labs (2018)
----------------------------------------------------------------------------------------*/
import java.net.URI;
import java.net.URISyntaxException;

class NautilusKeyboardClientApplication {

    private URI nautilusServerURI;

    public NautilusKeyboardClientApplication(URI nautilusServerURI) { 
        this.nautilusServerURI = nautilusServerURI;
    }

    /*----------------------------------------------------------------------------------------
    Public Methods
    ----------------------------------------------------------------------------------------*/
    public void runApplication() throws URISyntaxException {
        NautilusServerConnection nautilusServerConnection = NautilusServerConnection.attemptToConnectToNautilusServerWithURI(this.nautilusServerURI);
        if (nautilusServerConnection.isConnectedToServer()) {
            NautilusKeyListener keyListenerForServer = nautilusServerConnection.listenerToSendKeyPressesToNautilusServer();
            this.createDisplayAndAddKeyListenerForServerConnection(keyListenerForServer);
        } else {
            System.out.printf("Failed to connect to Nautilus at: %s\n", nautilusServerURI.toString());
        }
    }

    /*----------------------------------------------------------------------------------------
    Private Methods
    ----------------------------------------------------------------------------------------*/
    private void createDisplayAndAddKeyListenerForServerConnection(NautilusKeyListener keyListenerInQuestion) {
        NautilusKeyboardClientDisplay nautilusKeyboardClientDisplay = new NautilusKeyboardClientDisplay();
        nautilusKeyboardClientDisplay.setNautilusKeyListener(keyListenerInQuestion);
    }

    /*----------------------------------------------------------------------------------------
    Static Public Methods
    ----------------------------------------------------------------------------------------*/
    public static void main(String[] commandLineArguments) {
        boolean hasParameterForURIConnectionToServer = commandLineArguments.length > 0;
        if (hasParameterForURIConnectionToServer) {
            NautilusKeyboardClientApplication.createApplicationWithURI(commandLineArguments[0]);
        } else {
            System.out.println("Usage: Nautilus <URI for Nautilus Server>");
        }
    }

    /*----------------------------------------------------------------------------------------
    Static Private Methods
    ----------------------------------------------------------------------------------------*/
    private static void createApplicationWithURI(String uriString) {
        try{
            NautilusKeyboardClientApplication.setupAndRunNautilusKeyboardApplicationWithURI(new URI(uriString));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
   
    private static void setupAndRunNautilusKeyboardApplicationWithURI(URI nautilusServerURI) throws Exception {
        NautilusKeyboardClientApplication nautilusKeyboardClientApplication = new NautilusKeyboardClientApplication(nautilusServerURI);
        nautilusKeyboardClientApplication.runApplication();
    }
}
