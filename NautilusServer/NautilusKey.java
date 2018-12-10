/*----------------------------------------------------------------------------------------
    PROJECT
    -------
    Project Nautilus

    DESCRIPTION
    -----------
    ....

    AUTHOR
    ------
    Ayre Labs (2018)
----------------------------------------------------------------------------------------*/

public class NautilusKey {
  private int nautilusKeyCode = 0;
  private static final int nautilusRoomKeyCodeBase = 1112;
  private static final int nautilusRoomKeyCodeUpperBound = 1123;

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
    return this.nautilusKeyCode >= nautilusRoomKeyCodeBase && this.nautilusKeyCode <= nautilusRoomKeyCodeUpperBound;
  }

  public int getSelectedNautilusRoomNumber() {
    return this.nautilusKeyCode - nautilusRoomKeyCodeBase;
  }

  public String getStringRepresentationOfKey() {
    if (this.nautilusKeyCode >= 1000) {
      return "?";
    }
    return String.valueOf((char)this.nautilusKeyCode);
  }

  public boolean isEnterKey() {
    return this.nautilusKeyCode == 10;
  }

  public boolean isBackspaceKey() {
    return this.nautilusKeyCode == 8;
  }

  public boolean isEscapeKey() {
    return this.nautilusKeyCode == 1027;
  }

  public boolean isLeftArrowKey() {
    return this.nautilusKeyCode == 1037;
  }

  public boolean isRightArrowKey() {
    return this.nautilusKeyCode == 1039;
  }

}
