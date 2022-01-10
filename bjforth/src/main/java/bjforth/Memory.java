package bjforth;

import java.util.HashMap;
import java.util.Map;

public class Memory {
  private final Map<Integer, Object> cells;

  public Memory() {
    cells = new HashMap<>();
  }

  public void set(Integer address, Object value) {
    cells.put(address, value);
  }

  public Object get(Integer address) {
    return cells.get(address);
  }
}
