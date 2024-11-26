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

import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MachineStateInspectionUtils.*;

import bjforth.variables.Variable;
import org.assertj.core.api.AbstractAssert;

public class MachineStateAssert extends AbstractAssert<MachineStateAssert, MachineState> {

  protected MachineStateAssert(MachineState actual, Class<MachineStateAssert> selfType) {
    super(actual, selfType);
  }

  public MachineStateAssert(MachineState actual) {
    super(actual, MachineStateAssert.class);
  }

  public MachineStateAssert hasDictionaryEqualTo(MachineState other) {
    isNotNull();
    var actualKeys = dictionaryKeys(actual);
    var otherKeys = dictionaryKeys(other);
    if (actualKeys.size() != otherKeys.size()
        || !actualKeys.stream().allMatch(otherKeys::contains)) {
      failWithMessage("Expected Dictionary to have keys <%s> but was <%s>", otherKeys, actualKeys);
    }
    for (var k : actualKeys) {
      var actualValue = actual.getDictionary().get(k);
      var otherValue = other.getDictionary().get(k);
      if (!actualValue.equals(otherValue)) {
        failWithMessage(
            "Expected Dictionary to have mapping <%s -> %s> but was <%s -> %s>",
            k, otherValue, k, actualValue);
      }
    }
    return this;
  }

  public MachineStateAssert hasDictionaryEqualTo(Dictionary other) {
    isNotNull();
    return hasDictionaryEqualTo(aMachineState().withDictionary(other).build());
  }

  public MachineStateAssert hasMemoryEqualTo(MachineState other) {
    isNotNull();
    var actualAddrs = memoryAddresses(actual);
    var otherAddrs = memoryAddresses(other);
    if (actualAddrs.size() != otherAddrs.size()
        || !actualAddrs.stream().allMatch(otherAddrs::contains)) {
      failWithMessage(
          "Expected Memory to have addresses <%s> but was <%s>", otherAddrs, actualAddrs);
    }
    for (var addr : actualAddrs) {
      var actualValue = actual.getMemory().get(addr);
      var otherValue = other.getMemory().get(addr);
      if (!actualValue.equals(otherValue)) {
        failWithMessage(
            "Expected Memory address <%s> to be <%s> but was <%s>", addr, otherValue, actualValue);
      }
    }
    return this;
  }

  public MachineStateAssert hasMemoryEqualTo(Memory other) {
    isNotNull();
    return hasMemoryEqualTo(aMachineState().withMemory(other).build());
  }

  public MachineStateAssert hasParameterStackEqualTo(MachineState other) {
    isNotNull();
    try {
      var actualPointer = actual.getParameterStack().getPointer();
      var otherPointer = other.getParameterStack().getPointer();
      if (actualPointer != otherPointer) {
        failWithMessage(
            "Expected parameter stack pointer to be <%s> but was <%s>",
            otherPointer, actualPointer);
      }
    } catch (MachineException mex) {
      if (!"Empty stack".equals(mex.getMessage())) throw mex;
    }
    var actualIterator = parameterStackAscendingIterator(actual);
    var otherIterator = parameterStackAscendingIterator(other);
    while (otherIterator.hasNext()) {
      if (!actualIterator.hasNext()) {
        failWithMessage(
            "Expected ParameterStack to contain <%s> but there were no more elements.",
            otherIterator.next());
      }
      var actualValue = actualIterator.next();
      var otherValue = otherIterator.next();
      if (otherValue != null && !actualValue.equals(otherValue)) {
        failWithMessage(
            "Expected ParameterStack to contain <%s> but was <%s>.", otherValue, actualValue);
      }
    }
    if (actualIterator.hasNext()) {
      failWithMessage(
          "Expected ParameterStack to have no more contents but was <%s>", actualIterator.next());
    }
    return this;
  }

  public MachineStateAssert hasParameterStackEqualTo(Stack other) {
    isNotNull();
    var ms = aMachineState().withParameterStack(other).build();
    return hasParameterStackEqualTo(ms);
  }

  public MachineStateAssert hasParameterStackPointerEqualTo(Integer otherPointer) {
    isNotNull();
    var actualPointer = actual.getParameterStack().getPointer();
    if (actualPointer != otherPointer) {
      failWithMessage(
          "Expected parameter stack pointer to be <%s> but was <%s>.", otherPointer, actualPointer);
    }
    return this;
  }

  public MachineStateAssert hasReturnStackEqualTo(MachineState other) {
    isNotNull();
    try {
      var actualPointer = actual.getReturnStack().getPointer();
      var otherPointer = other.getReturnStack().getPointer();
      if (actualPointer != otherPointer) {
        failWithMessage(
            "Expected return stack pointer to be <%s> but was <%s>", otherPointer, actualPointer);
      }
    } catch (MachineException mex) {
      if (!"Empty stack".equals(mex.getMessage())) throw mex;
    }
    var actualIterator = returnStackAscendingIterator(actual);
    var otherIterator = returnStackAscendingIterator(other);
    while (otherIterator.hasNext()) {
      if (!actualIterator.hasNext()) {
        failWithMessage(
            "Expected ReturnStack to contain <%s> but there were no more elements.",
            otherIterator.next());
      }
      var actualValue = actualIterator.next();
      var otherValue = otherIterator.next();
      if (!actualValue.equals(otherValue)) {
        failWithMessage(
            "Expected ReturnStack to contain <%s> but was <%s>.", otherValue, actualValue);
      }
    }
    if (actualIterator.hasNext()) {
      failWithMessage(
          "Expected ReturnStack to have no more elements but was <%s>", actualIterator.next());
    }
    return this;
  }

  public MachineStateAssert hasReturnStackEqualTo(Stack otherStack) {
    var otherMs = aMachineState().withReturnStack(otherStack).build();
    return hasReturnStackEqualTo(otherMs);
  }

  public MachineStateAssert hasReturnStackPointerEqualTo(Integer otherPointer) {
    isNotNull();
    var actualPointer = actual.getReturnStack().getPointer();
    if (actualPointer != otherPointer) {
      failWithMessage(
          "Expected return stack pointer to be <%s> but was <%s>.", otherPointer, actualPointer);
    }
    return this;
  }

  public MachineStateAssert hasInstructionPointerEqualTo(MachineState other) {
    var actualIp = actual.getInstructionPointer();
    var otherIp = other.getInstructionPointer();
    if (!actualIp.equals(otherIp)) {
      failWithMessage("Expected instructionPointer to be <%s> but was <%s>", otherIp, actualIp);
    }
    return this;
  }

  public MachineStateAssert hasInstructionPointerEqualTo(Integer otherIp) {
    var otherMs = aMachineState().withInstrcutionPointer(otherIp).build();
    return hasInstructionPointerEqualTo(otherMs);
  }

  public MachineStateAssert hasNextInstructionPointerEqualTo(MachineState other) {
    var actualNip = actual.getNextInstructionPointer();
    var otherNip = other.getNextInstructionPointer();
    if (!actualNip.equals(otherNip)) {
      failWithMessage(
          "Expected nextInstructionPointer to be <%s> but was <%s>", otherNip, actualNip);
    }
    return this;
  }

  public MachineStateAssert hasNextInstructionPointerEqualTo(Integer otherNip) {
    var otherMs = aMachineState().withNextInstructionPointer(otherNip).build();
    return hasNextInstructionPointerEqualTo(otherMs);
  }

  public MachineStateAssert isEqualTo(MachineState other) {
    return hasInstructionPointerEqualTo(other)
        .hasNextInstructionPointerEqualTo(other)
        .hasDictionaryEqualTo(other)
        .hasMemoryEqualTo(other)
        .hasReturnStackEqualTo(other)
        .hasParameterStackEqualTo(other);
  }

  public MachineStateAssert hasVariableEqualTo(Variable variable, Integer otherValue) {
    isNotNull();
    var actualValue = (Integer) actual.getMemory().get(variable.getAddress());
    if (!actualValue.equals(otherValue)) {
      failWithMessage("Expected variable to be <%s> but was <%s>", otherValue, actualValue);
    }
    return this;
  }
}
