/*
 * Copyright 2023 Bahman Movaqar
 *
 * This file is part of BJForth.
 *
 * BJForth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BJForth is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BJForth. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth.primitives;

import java.util.function.Supplier;

public class PrimitiveFactory {

  private static PrimitiveContainer containerADD = new PrimitiveContainer(ADD::new);

  static Primitive ADD() {
    return containerADD.get();
  }

  private static PrimitiveContainer containerADDSTORE = new PrimitiveContainer(ADDSTORE::new);

  static Primitive ADDSTORE() {
    return containerADDSTORE.get();
  }

  private static PrimitiveContainer containerBASE = new PrimitiveContainer(BASE::new);

  static Primitive BASE() {
    return containerBASE.get();
  }

  private static PrimitiveContainer containerCFA = new PrimitiveContainer(CFA::new);

  static Primitive CFA() {
    return containerCFA.get();
  }

  private static PrimitiveContainer containerCOPY = new PrimitiveContainer(COPY::new);

  static Primitive COPY() {
    return containerCOPY.get();
  }

  private static PrimitiveContainer containerCREATE = new PrimitiveContainer(CREATE::new);

  static Primitive CREATE() {
    return containerCREATE.get();
  }

  private static PrimitiveContainer containerCOMMA = new PrimitiveContainer(COMMA::new);

  static Primitive COMMA() {
    return containerCOMMA.get();
  }

  private static PrimitiveContainer containerDECR = new PrimitiveContainer(DECR::new);

  static Primitive DECR() {
    return containerDECR.get();
  }

  private static PrimitiveContainer containerDECR4 = new PrimitiveContainer(DECR4::new);

  static Primitive DECR4() {
    return containerDECR4.get();
  }

  private static PrimitiveContainer containerDFA = new PrimitiveContainer(DFA::new);

  static Primitive DFA() {
    return containerDFA.get();
  }

  private static PrimitiveContainer containerDIV = new PrimitiveContainer(DIV::new);

  static Primitive DIV() {
    return containerDIV.get();
  }

  private static PrimitiveContainer containerDOCOL = new PrimitiveContainer(DOCOL::new);

  static Primitive DOCOL() {
    return containerDOCOL.get();
  }

  private static PrimitiveContainer containerDROP = new PrimitiveContainer(DROP::new);

  static Primitive DROP() {
    return containerDROP.get();
  }

  private static PrimitiveContainer containerDSPFETCH = new PrimitiveContainer(DSPFETCH::new);

  static Primitive DSPFETCH() {
    return containerDSPFETCH.get();
  }

  private static PrimitiveContainer containerDSPSTORE = new PrimitiveContainer(DSPSTORE::new);

  static Primitive DSPSTORE() {
    return containerDSPSTORE.get();
  }

  private static PrimitiveContainer containerDUP = new PrimitiveContainer(DUP::new);

  static Primitive DUP() {
    return containerDUP.get();
  }

  private static PrimitiveContainer containerEMIT = new PrimitiveContainer(EMIT::new);

  static Primitive EMIT() {
    return containerEMIT.get();
  }

  private static PrimitiveContainer containerEQU = new PrimitiveContainer(EQU::new);

  static Primitive EQU() {
    return containerEQU.get();
  }

  private static PrimitiveContainer containerFETCH = new PrimitiveContainer(FETCH::new);

  static Primitive FETCH() {
    return containerFETCH.get();
  }

  private static PrimitiveContainer containerFIND = new PrimitiveContainer(FIND::new);

  static Primitive FIND() {
    return containerFIND.get();
  }

  private static PrimitiveContainer containerFROMR = new PrimitiveContainer(FROMR::new);

  static Primitive FROMR() {
    return containerFROMR.get();
  }

  private static PrimitiveContainer containerGE = new PrimitiveContainer(GE::new);

  static Primitive GE() {
    return containerGE.get();
  }

  private static PrimitiveContainer containerGT = new PrimitiveContainer(GT::new);

  static Primitive GT() {
    return containerGT.get();
  }

  private static PrimitiveContainer containerHERE = new PrimitiveContainer(HERE::new);

  static Primitive HERE() {
    return containerHERE.get();
  }

  private static PrimitiveContainer containerINCR = new PrimitiveContainer(INCR::new);

  static Primitive INCR() {
    return containerINCR.get();
  }

  private static PrimitiveContainer containerINCR4 = new PrimitiveContainer(INCR4::new);

  static Primitive INCR4() {
    return containerINCR4.get();
  }

  private static PrimitiveContainer containerKEY = new PrimitiveContainer(KEY::new);

  static Primitive KEY() {
    return containerKEY.get();
  }

  private static PrimitiveContainer containerLATEST = new PrimitiveContainer(LATEST::new);

  static Primitive LATEST() {
    return containerLATEST.get();
  }

  private static PrimitiveContainer containerLBRAC = new PrimitiveContainer(LBRAC::new);

  static Primitive LBRAC() {
    return containerLBRAC.get();
  }

  private static PrimitiveContainer containerLIT = new PrimitiveContainer(LIT::new);

  static Primitive LIT() {
    return containerLIT.get();
  }

  private static PrimitiveContainer containerLT = new PrimitiveContainer(LT::new);

  static Primitive LT() {
    return containerLT.get();
  }

  private static PrimitiveContainer containerLTE = new PrimitiveContainer(LTE::new);

  static Primitive LTE() {
    return containerLTE.get();
  }

  private static PrimitiveContainer containerMOD = new PrimitiveContainer(MOD::new);

  static Primitive MOD() {
    return containerMOD.get();
  }

  private static PrimitiveContainer containerMOVE = new PrimitiveContainer(MOVE::new);

  static Primitive MOVE() {
    return containerMOVE.get();
  }

  private static PrimitiveContainer containerMUL = new PrimitiveContainer(MUL::new);

  static Primitive MUL() {
    return containerMUL.get();
  }

  private static PrimitiveContainer containerNEQU = new PrimitiveContainer(NEQU::new);

  static Primitive NEQU() {
    return containerNEQU.get();
  }

  private static PrimitiveContainer containerNROT = new PrimitiveContainer(NROT::new);

  static Primitive NROT() {
    return containerNROT.get();
  }

  private static PrimitiveContainer containerOVER = new PrimitiveContainer(OVER::new);

  static Primitive OVER() {
    return containerOVER.get();
  }

  private static PrimitiveContainer containerQDUP = new PrimitiveContainer(QDUP::new);

  static Primitive QDUP() {
    return containerQDUP.get();
  }

  private static PrimitiveContainer containerRBRAC = new PrimitiveContainer(RBRAC::new);

  static Primitive RBRAC() {
    return containerRBRAC.get();
  }

  private static PrimitiveContainer containerRDROP = new PrimitiveContainer(RDROP::new);

  static Primitive RDROP() {
    return containerRDROP.get();
  }

  private static PrimitiveContainer containerROT = new PrimitiveContainer(ROT::new);

  static Primitive ROT() {
    return containerROT.get();
  }

  private static PrimitiveContainer containerRSPFETCH = new PrimitiveContainer(RSPFETCH::new);

  static Primitive RSPFETCH() {
    return containerRSPFETCH.get();
  }

  private static PrimitiveContainer containerRSPSTORE = new PrimitiveContainer(RSPSTORE::new);

  static Primitive RSPSTORE() {
    return containerRSPSTORE.get();
  }

  private static PrimitiveContainer containerSTORE = new PrimitiveContainer(STORE::new);

  static Primitive STORE() {
    return containerSTORE.get();
  }

  private static PrimitiveContainer containerSUB = new PrimitiveContainer(SUB::new);

  static Primitive SUB() {
    return containerSUB.get();
  }

  private static PrimitiveContainer containerSUBSTORE = new PrimitiveContainer(SUBSTORE::new);

  static Primitive SUBSTORE() {
    return containerSUBSTORE.get();
  }

  private static PrimitiveContainer containerSWAP = new PrimitiveContainer(SWAP::new);

  static Primitive SWAP() {
    return containerSWAP.get();
  }

  private static PrimitiveContainer containerTOR = new PrimitiveContainer(TOR::new);

  static Primitive TOR() {
    return containerTOR.get();
  }

  private static PrimitiveContainer containerTWODROP = new PrimitiveContainer(TWODROP::new);

  static Primitive TWODROP() {
    return containerTWODROP.get();
  }

  private static PrimitiveContainer containerTWODUP = new PrimitiveContainer(TWODUP::new);

  static Primitive TWODUP() {
    return containerTWODUP.get();
  }

  private static PrimitiveContainer containerTWOSWAP = new PrimitiveContainer(TWOSWAP::new);

  static Primitive TWOSWAP() {
    return containerTWOSWAP.get();
  }

  private static PrimitiveContainer containerWORD = new PrimitiveContainer(WORD::new);

  static Primitive WORD() {
    return containerWORD.get();
  }

  private static PrimitiveContainer containerZEQU = new PrimitiveContainer(ZEQU::new);

  static Primitive ZEQU() {
    return containerZEQU.get();
  }

  private static PrimitiveContainer containerZGE = new PrimitiveContainer(ZGE::new);

  static Primitive ZGE() {
    return containerZGE.get();
  }

  private static PrimitiveContainer containerZGT = new PrimitiveContainer(ZGT::new);

  static Primitive ZGT() {
    return containerZGT.get();
  }

  private static PrimitiveContainer containerZLE = new PrimitiveContainer(ZLE::new);

  static Primitive ZLE() {
    return containerZLE.get();
  }

  private static PrimitiveContainer containerZLT = new PrimitiveContainer(ZLT::new);

  static Primitive ZLT() {
    return containerZLT.get();
  }

  private static PrimitiveContainer containerZNEQU = new PrimitiveContainer(ZNEQU::new);

  static Primitive ZNEQU() {
    return containerZNEQU.get();
  }

  private static PrimitiveContainer containerNUMBER = new PrimitiveContainer(NUMBER::new);

  static Primitive NUMBER() {
    return containerNUMBER.get();
  }

  private static class PrimitiveContainer {

    private Primitive instance;
    private Supplier<Primitive> supplier;

    PrimitiveContainer(Supplier<Primitive> supplier) {
      this.instance = null;
      this.supplier = supplier;
    }

    private synchronized Primitive get() {
      if (instance == null) {
        instance = supplier.get();
      }
      return instance;
    }
  }
}
