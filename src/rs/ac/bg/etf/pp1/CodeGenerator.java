package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	int mainPc;

	private Obj currentMethod = null;
	private int numberOfFormalParamsInMethod = 0;
	private int numberOfVariablesInMethod = 0;

	public int getMainPc() {
		return mainPc;
	}

	private int addressAdd;

	// PROGRAM

	public void visit(ProgramName programName) {

		// ORD (char c)
		Obj ordObj = SymbolTable.find("ord");
		int argumentsOrd = ordObj.getLevel();
		int variablesOrd = ordObj.getLocalSymbols().size();
		int addressOrd = Code.pc;
		ordObj.setAdr(addressOrd);

		Code.put(Code.enter);
		Code.put(argumentsOrd);
		// Code.put(argumentsOrd + variablesOrd);
		Code.put(variablesOrd);

		Code.put(Code.load_n);

		Code.put(Code.exit);
		Code.put(Code.return_);

		// CHR (int i)
		Obj chrObj = SymbolTable.find("chr");
		int argumentsChr = chrObj.getLevel();
		int variablesChr = chrObj.getLocalSymbols().size();
		int addressChr = Code.pc;
		chrObj.setAdr(addressChr);

		Code.put(Code.enter);
		Code.put(argumentsChr);
		// Code.put(argumentsChr + variablesChr);
		Code.put(variablesChr);

		Code.put(Code.load_n);

		Code.put(Code.exit);
		Code.put(Code.return_);

		// LEN (array arr)
		Obj lenObj = SymbolTable.find("len");
		int argumentsLen = lenObj.getLevel();
		int variablesLen = 0;
		int addresLen = Code.pc;
		lenObj.setAdr(addresLen);

		Code.put(Code.enter);
		Code.put(argumentsLen);
		Code.put(argumentsLen + variablesLen);

		Code.put(Code.load_n);
		Code.put(Code.arraylength);

		Code.put(Code.exit);
		Code.put(Code.return_);

		// ADD (set a, int b)

		/*
		 * 0 - set 1 - elem 2 - found 3 - iterator 4 - capacity
		 */

		Obj addObj = SymbolTable.find("add");
		int argumentsAdd = 2;
		int variablesAdd = 3;
		addressAdd = Code.pc;
		addObj.setAdr(addressAdd);

		Code.put(Code.enter);
		Code.put(argumentsAdd);
		Code.put(variablesAdd + argumentsAdd);

		// load iterator
		Code.loadConst(1);
		Code.put(Code.store_3);

		// load found
		Code.loadConst(0);
		Code.put(Code.store_2);

		// load cap
		Code.put(Code.load_n);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.put(Code.store);
		Code.put(4);

		// for
		int beginFor = Code.pc;

		Code.put(Code.load_3);
		Code.put(Code.load);
		Code.put(4);
		Code.putFalseJump(Code.le, 0);
		int jumpFor = Code.pc - 2;

		Code.put(Code.load_n);
		Code.put(Code.load_3);
		Code.put(Code.aload);

		Code.put(Code.load_1);
		Code.putFalseJump(Code.eq, 0);
		int beginIfEqualElem = Code.pc - 2;

		Code.loadConst(1);
		Code.put(Code.store_2);
		Code.putJump(0);
		int elemFound = Code.pc - 2;

		Code.fixup(beginIfEqualElem);

		Code.put(Code.load_3);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.store_3);

		Code.putJump(beginFor);

		Code.fixup(jumpFor);

		Code.fixup(elemFound);

		// check found
		Code.put(Code.load_2);
		Code.loadConst(1);
		Code.putFalseJump(Code.ne, 0);
		int exitAddres = Code.pc - 2;

		// inc capacity
		Code.put(Code.load);
		Code.put(4);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.store);
		Code.put(4);

		// upis elementa u set
		Code.put(Code.load_n);
		Code.put(Code.load);
		Code.put(4);
		Code.put(Code.load_1);
		Code.put(Code.astore);

		// upis capacity u set
		Code.put(Code.load_n);
		Code.loadConst(0);
		Code.put(Code.load);
		Code.put(4);
		Code.put(Code.astore);

		Code.fixup(exitAddres);

		Code.put(Code.exit);
		Code.put(Code.return_);

		// ADDALL

		Obj addAllObj = SymbolTable.find("addAll");
		int argumentsAddAll = 2;
		int variablesAddAll = 2;
		int addressAddAll = Code.pc;
		addAllObj.setAdr(addressAddAll);

		Code.put(Code.enter);
		Code.put(argumentsAddAll);
		Code.put(argumentsAddAll + variablesAddAll);

		/*
		 * 0 - set 1 - array 2 - arraySize 3 - iter
		 */

		// load arraySize
		Code.put(Code.load_1);
		Code.put(Code.arraylength);
		Code.put(Code.store_2);

		// load iter
		Code.loadConst(0);
		Code.put(Code.store_3);

		int addressBeginOfFor = Code.pc;

		// add args
		Code.put(Code.load_n);
		Code.put(Code.load_1);
		Code.put(Code.load_3);
		Code.put(Code.aload);

		// call add
		Code.put(Code.call);
		Code.put2(addressAdd - Code.pc + 1);

		// iter++
		Code.put(Code.load_3);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.store_3);

		// check if end
		Code.put(Code.load_2);
		Code.put(Code.load_3);
		Code.putFalseJump(Code.le, addressBeginOfFor);

		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	// METHOD

	public void visit(MethodName methodName) {

		currentMethod = methodName.obj;

		if ("main".equalsIgnoreCase(methodName.getMetodName())) {
			mainPc = Code.pc;
		}
		// pamti se adresa metode
		currentMethod.setAdr(Code.pc);
		numberOfFormalParamsInMethod = currentMethod.getLevel();
		numberOfVariablesInMethod = currentMethod.getLocalSymbols().size();

		Code.put(Code.enter);
		Code.put(numberOfFormalParamsInMethod);
		Code.put(numberOfFormalParamsInMethod + numberOfVariablesInMethod);

	}

	public void visit(MethodDeclaration methodDeclaration) {
		currentMethod = null;
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	// DESIGNATOR
	/*
	 * public void visit(DesignatorIdentExpr DesignatorIdentExpr) { Obj
	 * designatorArray = DesignatorIdentExpr.obj;
	 * 
	 * Code.load(designatorArray); Code.put(Code.dup_x1); Code.put(Code.pop); }
	 */
	public void visit(DesignatorArrayName designatorArrayName) {
		Code.load(designatorArrayName.obj);
	}

	// FACTOR

	public void visit(FactorDesignator FactorDesignator) {
		Code.load(FactorDesignator.getDesignator().obj);
	}

	// Ovo se ne radi zato sto rezultat ostaje na exprStack implicitno
	// public void visit(FactorExpr FactorExpr) {}

	public void visit(FactorNewExprArray FactorNewExprArray) {
		// val == 0 (char) val != 0 (int, bool) + set

		if (FactorNewExprArray.getType().struct.equals(SymbolTable.setType)) {
			// nije bitno koji broj se stavi!
			Code.loadConst(1);
			Code.put(Code.add);
			Code.put(Code.newarray);
			Code.put(1);
		} else if (FactorNewExprArray.getType().struct.equals(SymbolTable.charType)) {
			Code.put(Code.newarray);
			Code.put(0);
		} else {
			Code.put(Code.newarray);
			Code.put(1);
		}
	}

	public void visit(FactorDesignatorCallFunc designatorCallFunc) {
		Obj factorFuncCall = designatorCallFunc.getDesignator().obj;

		int offset = factorFuncCall.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	public void visit(FactorDesignatorCallFuncNoPars factorDesignatorCallFuncNoPars) {
		Obj factorFuncCall = factorDesignatorCallFuncNoPars.getDesignator().obj;
		int offset = factorFuncCall.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	public void visit(FactorConst factorConst) {
		Struct typeOfConstant = factorConst.struct;
		if (typeOfConstant.equals(SymbolTable.booleanType)) {
			ConstTypeBoolean booleanConstValue = (ConstTypeBoolean) factorConst.getConstType();
			if (booleanConstValue.getBooleanConst()) {
				Code.loadConst(1);
			} else {
				Code.loadConst(0);
			}
		} else if (typeOfConstant.equals(SymbolTable.charType)) {
			ConstTypeCharacter charConstValue = (ConstTypeCharacter) factorConst.getConstType();
			Code.loadConst((int) (charConstValue.getCharConst()));
		} else { // SymbolTable.intType
			ConstTypeNumber integerConstValue = (ConstTypeNumber) factorConst.getConstType();
			Code.loadConst(integerConstValue.getNumberConst());
		}
	}

	// TERM

	public void visit(MultipleTerm multipleTerm) {
		if (multipleTerm.getMulop() instanceof Mul) {
			Code.put(Code.mul);
		} else if (multipleTerm.getMulop() instanceof Div) {
			Code.put(Code.div);
		} else { // MOD
			Code.put(Code.rem);
		}
	}

	// EXPR

	public void visit(ExprAddop exprAddop) {
		if (exprAddop.getAddop() instanceof Plus) {
			Code.put(Code.add);
		} else if (exprAddop.getAddop() instanceof Minus) {
			Code.put(Code.sub);
		}
		return;
	}

	public void visit(ExprNegative exprNegative) {
		Code.put(Code.neg);
	}

	// DESIGNATOR STATEMENT

	public void visit(DesignatorUnion DesignatorUnion) {
		Obj resultSet = DesignatorUnion.getDesignator().obj;
		Obj set1 = DesignatorUnion.getDesignator1().obj;
		Obj set2 = DesignatorUnion.getDesignator2().obj;

		/*
		 * 0 - result 1 - op1 2 - op2 3 - setSize 4 - iterator
		 */

		Code.load(resultSet);
		Code.load(set1);
		Code.load(set2);

		Code.put(Code.enter);
		Code.put(3);
		Code.put(6);

		// dodaj prvi set

		// dohvati velicinu seta
		Code.put(Code.load_1);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.put(Code.store_3);
	
		// iterator
		Code.loadConst(1);
		Code.put(Code.store);
		Code.put(4);

		// for
		int addressBeginOfForFirst = Code.pc;

		// add args
		Code.put(Code.load_n);
		Code.put(Code.load_1);
		Code.put(Code.load);
		Code.put(4);
		Code.put(Code.aload);

		// call add
		Code.put(Code.call);
		Code.put2(addressAdd - Code.pc + 1);

		// iter++
		Code.put(Code.load);
		Code.put(4);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.store);
		Code.put(4);

		// check if end
		Code.put(Code.load_3);
		Code.put(Code.load);
		Code.put(4);
		Code.putFalseJump(Code.lt, addressBeginOfForFirst);

		// dodaj drugi set
		// dohvati velicinu seta
		Code.put(Code.load_2);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.put(Code.store_3);
		
		// iterator
		Code.loadConst(1);
		Code.put(Code.store);
		Code.put(5);
		
		// for
		int addressBeginOfForSecond = Code.pc;

		// add args
		Code.put(Code.load_n);
		Code.put(Code.load_2);
		Code.put(Code.load);
		Code.put(5);
		Code.put(Code.aload);

		// call add
		Code.put(Code.call);
		Code.put2(addressAdd - Code.pc + 1);

		// iter++
		Code.put(Code.load);
		Code.put(5);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.store);
		Code.put(5);

		// check if end
		Code.put(Code.load_3);
		Code.put(Code.load);
		Code.put(5);
		Code.putFalseJump(Code.lt, addressBeginOfForSecond);

		Code.put(Code.exit);
	}

	public void visit(DesignatorEqualExpr designatorEqualExpr) {
		Code.store(designatorEqualExpr.getDesignator().obj);
	}

	public void visit(DesignatorActPars designatorActPars) {
		Obj methodWithArgs = designatorActPars.getDesignator().obj;

		int offset = methodWithArgs.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);

		// u koliko ima povratnu vrednost
		if (!methodWithArgs.getType().equals(SymbolTable.noType)) {
			Code.put(Code.pop);
		}
	}

	public void visit(DesignatorNoActPars designatorNoActPars) {
		Obj methodWithArgs = designatorNoActPars.getDesignator().obj;

		int offset = methodWithArgs.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);

		// u koliko ima povratnu vrednost
		if (!methodWithArgs.getType().equals(SymbolTable.noType)) {
			Code.put(Code.pop);
		}
	}

	public void visit(DesignatorIncrement designatorIncrement) {
		Obj designatorToIncrement = designatorIncrement.getDesignator().obj;

		// ako je niz
		if (designatorToIncrement.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}

		Code.load(designatorToIncrement);
		Code.loadConst(1);
		Code.put(Code.add);

		Code.store(designatorToIncrement);
	}

	public void visit(DesignatorDecrement DesignatorDecrement) {
		Obj designatorToDecrement = DesignatorDecrement.getDesignator().obj;

		// ako je niz
		if (designatorToDecrement.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}

		Code.load(designatorToDecrement);
		Code.loadConst(1);
		Code.put(Code.sub);

		Code.store(designatorToDecrement);
	}

	// STATEMENT

	public void visit(StatementReturnEmpty statementReturnEmpty) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(StatementReturnSomething returnSomething) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(StatementPrint statementPrint) {
		int intAndBoolWidth = 5;
		int charWidth = 1;

		Struct typeOfExprToPrint = statementPrint.getExpr().struct;

		if (typeOfExprToPrint.equals(SymbolTable.charType)) {
			Code.loadConst(charWidth);
			Code.put(Code.bprint);
		} else if (typeOfExprToPrint.equals(SymbolTable.intType) || typeOfExprToPrint.equals(SymbolTable.booleanType)) {
			Code.loadConst(intAndBoolWidth);
			Code.put(Code.print);
		} else if (typeOfExprToPrint.equals(SymbolTable.setType)) {
			Code.put(Code.enter);
			Code.put(0);
			Code.put(3); // 0-set ; 1-iter ; 2-capacity

			// load set
			Code.put(Code.store_n);

			// load iter
			Code.loadConst(1);
			Code.put(Code.store_1);

			// load capacity
			Code.put(Code.load_n);
			Code.loadConst(0);
			Code.put(Code.aload);
			Code.put(Code.store_2);

			// for
			int beginForAddres = Code.pc;

			Code.put(Code.load_1);
			Code.put(Code.load_2);
			Code.putFalseJump(Code.le, 0);
			int pcPrePrinta = Code.pc - 2;

			// print single
			Code.put(Code.load_n);
			Code.put(Code.load_1);
			Code.put(Code.aload);
			Code.loadConst(intAndBoolWidth);
			Code.put(Code.print);

			// iter++
			Code.put(Code.load_1);
			Code.loadConst(1);
			Code.put(Code.add);
			Code.put(Code.store_1);

			Code.putJump(beginForAddres);

			Code.fixup(pcPrePrinta);

			Code.put(Code.exit);
		}

	}

	public void visit(StatementPrintMultiple statementPrintMultiple) {
		int constArg = statementPrintMultiple.getC2();

		Code.loadConst(constArg);
		Struct typeOfDesignator = statementPrintMultiple.getExpr().struct;

		if (typeOfDesignator.equals(SymbolTable.charType)) {
			Code.put(Code.bprint);
		} else if (typeOfDesignator.equals(SymbolTable.intType) || typeOfDesignator.equals(SymbolTable.booleanType)) {
			Code.put(Code.print);
		} else if (typeOfDesignator.equals(SymbolTable.setType)) {
			// ispisati set broj po broj odvojen blanko znakovima

		}
	}

	public void visit(StatementRead statementRead) {
		Struct designatorType = statementRead.getDesignator().obj.getType();

		if (designatorType.equals(SymbolTable.charType)) {
			Code.put(Code.bread);
		} else {
			Code.put(Code.read);
		}

		Code.store(statementRead.getDesignator().obj);
	}

}
