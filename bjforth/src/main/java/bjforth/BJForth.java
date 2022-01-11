package bjforth;

public class BJForth {

  private final BJForthState state = new BJForthState();

  public void mainLoop() {
    while (true) {
      var ip = state.instructionPointer();
      var content = state.memory().get(ip);
      if (content instanceof BJForthPrimitive primitive) {
        primitive.doExecute(state);
      } else if (content instanceof Integer address) {
        state.instructionPointer(address);
      } else {
        throw new BJForthException("unknown memory content");
      }
    }
  }

  public static void main(String... args) {
    System.out.println("Hello, world");
  }
}
