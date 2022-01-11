package bjforth;

public interface BJForthPrimitive {
  void execute(BJForthState state);

  default void doExecute(BJForthState state) {
    execute(state);
    state.instructionPointer(state.instructionPointer() + 1);
  }
}
