package bjforth;

public class BJForthState {

  private Integer instructionPointer = 0;
  private final Memory memory = new Memory();
  private final Dictionary dictionary = new Dictionary();

  public Dictionary dictionary() {
    return dictionary;
  }

  public Memory memory() {
    return memory;
  }

  public Integer instructionPointer() {
    return instructionPointer;
  }

  public void instructionPointer(Integer address) {
    instructionPointer = address;
  }
}
