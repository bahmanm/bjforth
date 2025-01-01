/*
 * Copyright 2023 Bahman Movaqar
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
package bjforth.primitives;

import java.util.List;
import java.util.function.Supplier;

public class PrimitiveFactory {

  private static PrimitiveContainer containerADD = new PrimitiveContainer(ADD::new);

  private static PrimitiveContainer containerADDSTORE = new PrimitiveContainer(ADDSTORE::new);

  private static PrimitiveContainer containerAND = new PrimitiveContainer(AND::new);

  private static PrimitiveContainer containerATLANGLE = new PrimitiveContainer(ATLANGLE::new);

  private static PrimitiveContainer containerBASE = new PrimitiveContainer(BASE::new);

  private static PrimitiveContainer containerBASESTORE = new PrimitiveContainer(BASESTORE::new);

  private static PrimitiveContainer containerBRANCH = new PrimitiveContainer(BRANCH::new);

  private static PrimitiveContainer containerBYE = new PrimitiveContainer(BYE::new);

  private static PrimitiveContainer containerCFA = new PrimitiveContainer(CFA::new);

  private static PrimitiveContainer containerCHAR = new PrimitiveContainer(CHAR::new);

  private static PrimitiveContainer containerCOLON = new PrimitiveContainer(COLON::new);

  private static PrimitiveContainer containerCOPY = new PrimitiveContainer(COPY::new);

  private static PrimitiveContainer containerCREATE = new PrimitiveContainer(CREATE::new);

  static Primitive CREATE() {
    return containerCREATE.get();
  }

  private static PrimitiveContainer containerCOMMA = new PrimitiveContainer(COMMA::new);

  private static PrimitiveContainer containerCOMMALANGLE = new PrimitiveContainer(COMMALANGLE::new);

  private static PrimitiveContainer containerDECR = new PrimitiveContainer(DECR::new);

  private static PrimitiveContainer containerDECR4 = new PrimitiveContainer(DECR4::new);

  private static PrimitiveContainer containerDFA = new PrimitiveContainer(DFA::new);

  private static PrimitiveContainer containerDIV = new PrimitiveContainer(DIV::new);

  private static PrimitiveContainer containerDOTDOUBLEQUOTE =
      new PrimitiveContainer(DOTDOUBLEQUOTE::new);

  private static PrimitiveContainer containerDOTLANGLE = new PrimitiveContainer(DOTLANGLE::new);

  private static PrimitiveContainer containerDOTSTACK = new PrimitiveContainer(DOTSTACK::new);

  private static PrimitiveContainer containerDROP = new PrimitiveContainer(DROP::new);

  static Primitive DROP() {
    return containerDROP.get();
  }

  private static PrimitiveContainer containerDSPFETCH = new PrimitiveContainer(DSPFETCH::new);

  private static PrimitiveContainer containerDSPFETCHFETCH =
      new PrimitiveContainer(DSPFETCHFETCH::new);

  private static PrimitiveContainer containerDSPSTORE = new PrimitiveContainer(DSPSTORE::new);

  private static PrimitiveContainer containerDUMP = new PrimitiveContainer(DUMP::new);

  private static PrimitiveContainer containerDUP = new PrimitiveContainer(DUP::new);

  private static PrimitiveContainer containerEMIT = new PrimitiveContainer(EMIT::new);

  private static PrimitiveContainer containerEQU = new PrimitiveContainer(EQU::new);

  private static PrimitiveContainer containerEXECUTE = new PrimitiveContainer(EXECUTE::new);

  private static PrimitiveContainer containerEXIT = new PrimitiveContainer(EXIT::new);

  static Primitive EXIT() {
    return containerEXIT.get();
  }

  private static PrimitiveContainer containerFETCH = new PrimitiveContainer(FETCH::new);

  private static PrimitiveContainer containerFIND = new PrimitiveContainer(FIND::new);

  static Primitive FIND() {
    return containerFIND.get();
  }

  private static PrimitiveContainer containerFORGET = new PrimitiveContainer(FORGET::new);

  private static PrimitiveContainer containerFROMR = new PrimitiveContainer(FROMR::new);

  private static PrimitiveContainer containerGE = new PrimitiveContainer(GE::new);

  private static PrimitiveContainer containerGT = new PrimitiveContainer(GT::new);

  private static PrimitiveContainer containerHERE = new PrimitiveContainer(HERE::new);

  static Primitive HERE() {
    return containerHERE.get();
  }

  private static PrimitiveContainer containerHIDDEN = new PrimitiveContainer(HIDDEN::new);

  static Primitive HIDDEN() {
    return containerHIDDEN.get();
  }

  private static PrimitiveContainer containerHIDE = new PrimitiveContainer(HIDE::new);

  private static PrimitiveContainer containerIDDOT = new PrimitiveContainer(IDDOT::new);

  private static PrimitiveContainer containerIMMEDIATE = new PrimitiveContainer(IMMEDIATE::new);

  static Primitive IMMEDIATE() {
    return containerIMMEDIATE.get();
  }

  private static PrimitiveContainer containerINTERPRET = new PrimitiveContainer(INTERPRET::new);

  private static PrimitiveContainer containerINCR = new PrimitiveContainer(INCR::new);

  private static PrimitiveContainer containerINCR4 = new PrimitiveContainer(INCR4::new);

  private static PrimitiveContainer containerKEY = new PrimitiveContainer(KEY::new);

  static Primitive KEY() {
    return containerKEY.get();
  }

  private static PrimitiveContainer containerLATEST = new PrimitiveContainer(LATEST::new);

  private static PrimitiveContainer containerLBRAC = new PrimitiveContainer(LBRAC::new);

  static Primitive LBRAC() {
    return containerLBRAC.get();
  }

  private static PrimitiveContainer containerLIT = new PrimitiveContainer(LIT::new);

  private static PrimitiveContainer containerLITSTRING = new PrimitiveContainer(LITSTRING::new);

  private static PrimitiveContainer containerLT = new PrimitiveContainer(LT::new);

  private static PrimitiveContainer containerLTE = new PrimitiveContainer(LTE::new);

  private static PrimitiveContainer containerMOD = new PrimitiveContainer(MOD::new);

  private static PrimitiveContainer containerMOVE = new PrimitiveContainer(MOVE::new);

  private static PrimitiveContainer containerMUL = new PrimitiveContainer(MUL::new);

  private static PrimitiveContainer containerNEQU = new PrimitiveContainer(NEQU::new);

  private static PrimitiveContainer containerNROT = new PrimitiveContainer(NROT::new);

  private static PrimitiveContainer containerNUMBER = new PrimitiveContainer(NUMBER::new);

  static Primitive NUMBER() {
    return containerNUMBER.get();
  }

  private static PrimitiveContainer containerNULL = new PrimitiveContainer(NULL::new);

  private static PrimitiveContainer containerOR = new PrimitiveContainer(OR::new);

  private static PrimitiveContainer containerOVER = new PrimitiveContainer(OVER::new);

  private static PrimitiveContainer containerPRINT = new PrimitiveContainer(PRINT::new);

  private static PrimitiveContainer containerPRINTLN = new PrimitiveContainer(PRINTLN::new);

  static Primitive PRINTLN() {
    return containerPRINTLN.get();
  }

  private static PrimitiveContainer containerQDUP = new PrimitiveContainer(QDUP::new);

  private static PrimitiveContainer containerQNULL = new PrimitiveContainer(QNULL::new);

  private static PrimitiveContainer containerQFALSE = new PrimitiveContainer(QFALSE::new);

  private static PrimitiveContainer containerQTRUE = new PrimitiveContainer(QTRUE::new);

  private static PrimitiveContainer containerQUIT = new PrimitiveContainer(QUIT::new);

  private static PrimitiveContainer containerRANGLEAT = new PrimitiveContainer(RANGLEAT::new);

  private static PrimitiveContainer containerRANGLECOMMA = new PrimitiveContainer(RANGLECOMMA::new);

  private static PrimitiveContainer containerRANGLEDOT = new PrimitiveContainer(RANGLEDOT::new);

  private static PrimitiveContainer containerRBRAC = new PrimitiveContainer(RBRAC::new);

  static Primitive RBRAC() {
    return containerRBRAC.get();
  }

  private static PrimitiveContainer containerRDROP = new PrimitiveContainer(RDROP::new);

  private static PrimitiveContainer containerROT = new PrimitiveContainer(ROT::new);

  private static PrimitiveContainer containerRSPFETCH = new PrimitiveContainer(RSPFETCH::new);

  private static PrimitiveContainer containerRSPSTORE = new PrimitiveContainer(RSPSTORE::new);

  private static PrimitiveContainer containerSEE = new PrimitiveContainer(SEE::new);

  private static PrimitiveContainer containerSEMICOLON = new PrimitiveContainer(SEMICOLON::new);

  private static PrimitiveContainer containerSTORE = new PrimitiveContainer(STORE::new);

  private static PrimitiveContainer containerSTOREBASE = new PrimitiveContainer(STOREBASE::new);

  private static PrimitiveContainer containerSTOREHERE = new PrimitiveContainer(STOREHERE::new);

  private static PrimitiveContainer containerSUB = new PrimitiveContainer(SUB::new);

  private static PrimitiveContainer containerSUBSTORE = new PrimitiveContainer(SUBSTORE::new);

  private static PrimitiveContainer containerSWAP = new PrimitiveContainer(SWAP::new);

  private static PrimitiveContainer containerTELL = new PrimitiveContainer(TELL::new);

  private static PrimitiveContainer containerTICK = new PrimitiveContainer(TICK::new);

  private static PrimitiveContainer containerTOR = new PrimitiveContainer(TOR::new);

  private static PrimitiveContainer containerTWODROP = new PrimitiveContainer(TWODROP::new);

  private static PrimitiveContainer containerTWODUP = new PrimitiveContainer(TWODUP::new);

  private static PrimitiveContainer containerTWOSWAP = new PrimitiveContainer(TWOSWAP::new);

  private static PrimitiveContainer containerWORD = new PrimitiveContainer(WORD::new);

  static Primitive WORD() {
    return containerWORD.get();
  }

  private static PrimitiveContainer containerZBRANCH = new PrimitiveContainer(ZBRANCH::new);

  private static PrimitiveContainer containerZEQU = new PrimitiveContainer(ZEQU::new);

  private static PrimitiveContainer containerZGE = new PrimitiveContainer(ZGE::new);

  private static PrimitiveContainer containerZGT = new PrimitiveContainer(ZGT::new);

  private static PrimitiveContainer containerZLE = new PrimitiveContainer(ZLE::new);

  private static PrimitiveContainer containerZLT = new PrimitiveContainer(ZLT::new);

  private static PrimitiveContainer containerZNEQU = new PrimitiveContainer(ZNEQU::new);

  private static List<PrimitiveContainer> primitiveContainers =
      List.of(
          containerADD,
          containerADDSTORE,
          containerAND,
          containerATLANGLE,
          containerBASE,
          containerBASESTORE,
          containerBRANCH,
          containerBYE,
          containerCFA,
          containerCHAR,
          containerCOLON,
          containerCOPY,
          containerCREATE,
          containerCOMMA,
          containerCOMMALANGLE,
          containerDECR,
          containerDECR4,
          containerDFA,
          containerDIV,
          containerDOTDOUBLEQUOTE,
          containerDOTLANGLE,
          containerDOTSTACK,
          containerDROP,
          containerDSPFETCH,
          containerDSPFETCHFETCH,
          containerDSPSTORE,
          containerDUMP,
          containerDUP,
          containerEMIT,
          containerEQU,
          containerEXECUTE,
          containerEXIT,
          containerFETCH,
          containerFIND,
          containerFORGET,
          containerFROMR,
          containerGE,
          containerGT,
          containerHERE,
          containerHIDDEN,
          containerHIDE,
          containerIDDOT,
          containerIMMEDIATE,
          containerINTERPRET,
          containerINCR,
          containerINCR4,
          containerKEY,
          containerLATEST,
          containerLBRAC,
          containerLIT,
          containerLITSTRING,
          containerLT,
          containerLTE,
          containerMOD,
          containerMOVE,
          containerMUL,
          containerNEQU,
          containerNROT,
          containerNUMBER,
          containerNULL,
          containerOR,
          containerOVER,
          containerPRINT,
          containerPRINTLN,
          containerQDUP,
          containerQNULL,
          containerQFALSE,
          containerQTRUE,
          containerQUIT,
          containerRANGLEAT,
          containerRANGLECOMMA,
          containerRANGLEDOT,
          containerRBRAC,
          containerRDROP,
          containerROT,
          containerRSPFETCH,
          containerRSPSTORE,
          containerSEE,
          containerSEMICOLON,
          containerSTORE,
          containerSTOREBASE,
          containerSTOREHERE,
          containerSUB,
          containerSUBSTORE,
          containerSWAP,
          containerTELL,
          containerTICK,
          containerTOR,
          containerTWODROP,
          containerTWODUP,
          containerTWOSWAP,
          containerWORD,
          containerZBRANCH,
          containerZEQU,
          containerZGE,
          containerZGT,
          containerZLE,
          containerZLT,
          containerZNEQU);

  public static List<PrimitiveContainer> getPrimitiveContainers() {
    return primitiveContainers;
  }

  public static class PrimitiveContainer {

    private Primitive instance;
    private Supplier<Primitive> supplier;

    PrimitiveContainer(Supplier<Primitive> supplier) {
      this.instance = null;
      this.supplier = supplier;
    }

    public synchronized Primitive get() {
      if (instance == null) {
        instance = supplier.get();
      }
      return instance;
    }
  }
}
