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

//----------------------------------------------------------------------------------------
// CLASS DEFINITION
//----------------------------------------------------------------------------------------
private class NautilusKeyboardClient {
        
    private int currentlySelectedNautilusRoomNumber = 0;
        
    public NautilusKeyboardClient() { /* Nothing */ }
        
    public void setCurrentlySelectedNautilusRoomNumber(int currentlySelectedNautilusRoomNumber) {
        this.currentlySelectedNautilusRoomNumber = currentlySelectedNautilusRoomNumber;
    }
    
    public int getCurrentlySelectedNautilusRoomNumber() {
        return this.currentlySelectedNautilusRoomNumber;
    }
}