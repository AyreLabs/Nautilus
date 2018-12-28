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
public class NautilusKey {

    private static final int NAUTILUS_KEY_CODE_ENCODING_BASE = 1112;
    private static final int NAUTILUS_KEY_CODE_ENCODING_UPPER_BOUND = 1123;
    private static final int FUNCTION_KEY_ENCODING_BASE = 1000;
    private static final int NAUTILUS_KEY_CODE_ENTER_KEY = 10;
    private static final int NAUTILUS_KEY_CODE_DOUBLE_QUOTE_KEY = 34;
    private static final int NAUTILUS_KEY_CODE_BACKSLASH_KEY = 92;
    private static final int NAUTILUS_KEY_CODE_BACKSPACE_KEY = 8;
    private static final int NAUTILUS_KEY_CODE_ESCAPE_KEY = 1027;
    private static final int NAUTILUS_KEY_CODE_LEFT_ARROW_KEY = 1037;
    private static final int NAUTILUS_KEY_CODE_RIGHT_ARROW_KEY = 1039;

    private int nautilusKeyCode;

    public static NautilusKey nautilusKeyWithNautilusKeyStringDescription(String nautilusKeyStringDescription) {
        return new NautilusKey(nautilusKeyStringDescription);
    }

    private NautilusKey(String nautilusKeyStringDescription) {
        try {
            this.nautilusKeyCode = Integer.parseInt(nautilusKeyStringDescription);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean isANautilusRoomNumberKey() {
        return this.nautilusKeyCode >= NAUTILUS_KEY_CODE_ENCODING_BASE 
            && this.nautilusKeyCode <= NAUTILUS_KEY_CODE_ENCODING_UPPER_BOUND;
    }

    public int getSelectedNautilusRoomNumber() {
        return this.nautilusKeyCode - NAUTILUS_KEY_CODE_ENCODING_BASE;
    }

    public String getStringRepresentationOfKey() {
        if (this.nautilusKeyCode >= FUNCTION_KEY_ENCODING_BASE) {
            return "?";
        }
        return String.valueOf((char)this.nautilusKeyCode);
    }

    public boolean isEnterKey() {
        return this.nautilusKeyCode == NAUTILUS_KEY_CODE_ENTER_KEY;
    }

    public boolean isDoubleQuoteKey() {
        return this.nautilusKeyCode == NAUTILUS_KEY_CODE_DOUBLE_QUOTE_KEY;
    }

    public boolean isBackslashKey() {
        return this.nautilusKeyCode == NAUTILUS_KEY_CODE_BACKSLASH_KEY;
    }

    public boolean isBackspaceKey() {
        return this.nautilusKeyCode == NAUTILUS_KEY_CODE_BACKSPACE_KEY;
    }

    public boolean isEscapeKey() {
        return this.nautilusKeyCode == NAUTILUS_KEY_CODE_ESCAPE_KEY;
    }

    public boolean isLeftArrowKey() {
        return this.nautilusKeyCode == NAUTILUS_KEY_CODE_LEFT_ARROW_KEY;
    }

    public boolean isRightArrowKey() {
        return this.nautilusKeyCode == NAUTILUS_KEY_CODE_RIGHT_ARROW_KEY;
    }
}
