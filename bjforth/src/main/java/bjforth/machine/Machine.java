/*
 * Copyright 2022 Bahman Movaqar
 *
 * This file is part of bjForth.
 *
 * bjForth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * bjForth is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with bjForth. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth.machine;

import static bjforth.config.Constants.*;
import static bjforth.primitives.PrimitiveFactory.getPrimitiveContainers;
import static com.diogonunes.jcolor.Ansi.colorize;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

public class Machine {

  private MachineState state;

  public Machine(MachineState state) {
    this.state = state;
    new Bootstrap().apply(state);
  }

  public Object getMemoryAt(Integer address) {
    return state.getMemory().get(address);
  }

  public void setMemoryAt(Integer address, Object value) {
    state.getMemory().set(address, value);
  }

  public void pushToParameterStack(Object item) {
    state.getParameterStack().push(item);
  }

  public Object popFromParameterStack() {
    return state.getParameterStack().pop();
  }

  public Object peekIntoParameterStack() {
    return state.getParameterStack().peek();
  }

  public Object getItemParamaterStack(Integer pointer) {
    return state.getParameterStack().getItem(pointer);
  }

  public int getParameterStackPointer() {
    return state.getParameterStack().getPointer();
  }

  public void setParameterStackPointer(int pointer) {
    state.getParameterStack().setPointer(pointer);
  }

  public void pushToReturnStack(Object address) {
    state.getReturnStack().push(address);
  }

  public Object popFromReturnStack() {
    return state.getReturnStack().pop();
  }

  public int getReturnStackPointer() {
    return state.getReturnStack().getPointer();
  }

  public void setReturnStackPointer(int pointer) {
    state.getReturnStack().setPointer(pointer);
  }

  public Optional<DictionaryItem> getDictionaryItem(String name) {
    return state.getDictionary().get(name);
  }

  public Optional<List<DictionaryItem>> getDictionaryItems(String name) {
    return state.getDictionary().getAllForName(name);
  }

  public Optional<DictionaryItem> getDictionaryItem(Integer address) {
    return state.getDictionary().get(address);
  }

  public void createDictionaryItem(String name, DictionaryItem item) {
    state.getDictionary().put(name, item);
  }

  public void removeDictionaryItem(String name) {
    state.getDictionary().remove(name);
  }

  public Set<String> getAllDictionaryItemNames() {
    return state.getDictionary().getNames();
  }

  public Integer getInstrcutionPointer() {
    return state.getInstructionPointer();
  }

  public Integer getNextInstructionPointer() {
    return state.getNextInstructionPointer();
  }

  public void setNextInstructionPointer(Integer address) {
    state.setNextInstructionPointer(address);
  }

  public void jumpTo(Integer address) {
    state.setInstructionPointer(address);
  }

  public void DOCOL(Boolean calledByPrimitive) {
    pushToReturnStack(getNextInstructionPointer());
    if (!calledByPrimitive) {
      setNextInstructionPointer(getInstrcutionPointer() + 1);
    }
  }

  private Integer threadedCodeDepth = 0;

  public void enterThreadedCode() {
    threadedCodeDepth += 1;
  }

  public void exitThreadedCode() {
    if (threadedCodeDepth == 0) {
      throw new MachineException("Invalid threadedCode depth.");
    } else {
      threadedCodeDepth -= 1;
    }
  }

  private void applyThreadedCode(Integer IP) {
    if (threadedCodeDepth > 0) {
      setNextInstructionPointer(IP + 1);
    }
  }

  /** Executes exactly ONE memory cell and stops. */
  public void step() {
    var IP = state.getInstructionPointer();
    var content = getMemoryAt(IP);
    if (content == null) {
      throw new MachineException("Don't know how to execute *(%d)=null".formatted(IP));
    } else if (content instanceof NativeSubroutine nativeSubroutine) {
      nativeSubroutine.call(this);
    } else if (content instanceof Integer address) {
      jumpTo(address);
      applyThreadedCode(IP);
    } else if (content instanceof String s && "DOCOL".equals(s)) {
      enterThreadedCode();
      DOCOL(false);
      jumpTo(getInstrcutionPointer() + 1);
    } else {
      //      throw new MachineException("don't know how to execute *(%d)".formatted(IP));
      jumpTo(getInstrcutionPointer() + 1);
    }
  }

  /**
   * Executes exactly N memory cells and stops.
   *
   * <p>To be used for debugging/testing purposes.
   *
   * @param n N memory cells
   */
  public void step(int n) {
    IntStream.range(0, n).forEach((_i) -> step());
  }

  /** Machine's "main loop". */
  public void loop() {
    try {
      while (true) {
        try {
          step();
        } catch (MachineException e) {
          System.out.print(
              colorize(
                  "%s %s".formatted(ERROR_EMOJI, e.getMessage()),
                  FOREGROUND_COLOR,
                  BACKGROUND_COLOR));
          System.out.print(" ");
          var QUIT =
              getPrimitiveContainers().stream()
                  .filter((p) -> "QUIT".equals(p.get().getName()))
                  .findFirst()
                  .get();
          QUIT.get().execute(this);
        }
      }
    } catch (GracefulShutdown _ex) {
      var BYE =
          getPrimitiveContainers().stream()
              .filter((p) -> "BYE".equals(p.get().getName()))
              .findFirst()
              .get();
      BYE.get().execute(this);
    }
  }

  public static void main(String[] args) {
    System.out.println(
        colorize(
            "%s bjForth <https://github.com/bahmanm/bjforth>".formatted(HELLO_EMOJI),
            FOREGROUND_COLOR,
            BACKGROUND_COLOR));

    var state = new MachineState(0, 0, new Memory(), new Dictionary(), new Stack(), new Stack());
    var machine = new Machine(state); // Bootstraps the components like memory and dictionary
    var QUITaddr = machine.getDictionaryItem("QUIT").get().getAddress();
    machine.setNextInstructionPointer(machine.getDictionaryItem("INTERPRET").get().getAddress());
    machine.jumpTo(QUITaddr);
    machine.loop();
  }
}
