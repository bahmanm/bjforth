package bjforth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Dictionary {

  private final Map<String, List<Integer>> items;

  public Dictionary() {
    items = new HashMap<>();
  }

  public void put(String name, Integer address) {
    items.merge(name, List.of(address), (currentValue, newValue) -> {
      currentValue.addAll(newValue);
      return currentValue;
    });
  }

  public Optional<Integer> get(String name) {
    return Optional.ofNullable(items.get(name))
        .map(addresses -> addresses.get(addresses.size() - 1));
  }
}
