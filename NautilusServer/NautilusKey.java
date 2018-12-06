
public class NautilusKey {
  private int nautilusKeyCode = 0;
  private static final int nautilusRoomKeyCodeBase = 112;
  private static final int nautilusRoomKeyCodeUpperBound = 123;

  public static NautilusKey nautilusKeyWithNautilusKeyStringDescription(String nautilusKeyStringDescription) {
    return new NautilusKey(nautilusKeyStringDescription);
  }

  private NautilusKey(String nautilusKeyStringDescription) {
    try {
      nautilusKeyStringDescription = Integer.parseInt(nautilusKeyStringDescription);
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
    return String.valueOf((char)this.nautilusKeyCode);
  }

}
