package bjforth;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class Stack<T> {
  private final Deque<T> data = new ArrayDeque<>();

  public T pop() {
    return data.removeFirst();
  }

  public void push(T item) {
    data.addFirst(item);
  }
}
