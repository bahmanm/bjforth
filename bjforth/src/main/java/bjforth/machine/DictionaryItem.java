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

public class DictionaryItem {
  private String name;
  private Integer address;
  private Boolean isImmediate;
  private Boolean isHidden;
  private Integer length = 1;

  public DictionaryItem(String name, Integer address, Boolean isImmediate, Boolean isHidden) {
    this.name = name;
    this.address = address;
    this.isImmediate = isImmediate;
    this.isHidden = isHidden;
  }

  public DictionaryItem(DictionaryItem other) {
    this.name = other.name;
    this.address = other.address;
    this.isImmediate = other.isImmediate;
    this.isHidden = other.isHidden;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAddress() {
    return address;
  }

  public void setAddress(Integer address) {
    this.address = address;
  }

  public Boolean getIsImmediate() {
    return isImmediate;
  }

  public void setIsImmediate(Boolean isImmediate) {
    this.isImmediate = isImmediate;
  }

  public Boolean getIsHidden() {
    return isHidden;
  }

  public void setIsHidden(Boolean isHidden) {
    this.isHidden = isHidden;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  @Override
  public String toString() {
    return "DictionaryItem(%s, %d, %s, %s)".formatted(name, address, isImmediate, isHidden);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((address == null) ? 0 : address.hashCode());
    result = prime * result + ((isHidden == null) ? 0 : isHidden.hashCode());
    result = prime * result + ((isImmediate == null) ? 0 : isImmediate.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  // GENERATED
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    DictionaryItem other = (DictionaryItem) obj;
    if (address == null) {
      if (other.address != null) return false;
    } else if (!address.equals(other.address)) return false;
    if (isHidden == null) {
      if (other.isHidden != null) return false;
    } else if (!isHidden.equals(other.isHidden)) return false;
    if (isImmediate == null) {
      if (other.isImmediate != null) return false;
    } else if (!isImmediate.equals(other.isImmediate)) return false;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return true;
  }
}
