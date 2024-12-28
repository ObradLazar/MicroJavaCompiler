package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;

	private Obj currentMethod = null;
	private int numberOfFormalParamsInMethod = 0;
	private int numberOfVariablesInMethod = 0;
	
	public int getMainPc() {
		return mainPc;
	}

	// METHOD

	public void visit(MethodName methodName) {

		currentMethod = methodName.obj;

		if ("main".equalsIgnoreCase(methodName.getMetodName())) {
			mainPc = Code.pc;
		}
		methodName.obj.setAdr(Code.pc);
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

	//	DESIGNATOR
	
	public void visit(DesignatorIdent designatorIdent) {}
	
	public void visit(DesignatorIdentExpr designatorIdentExpr) {}
	
	//	FACTOR

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
		if(typeOfConstant.equals(SymbolTable.booleanType)) {
			ConstTypeBoolean booleanConstValue = (ConstTypeBoolean)factorConst.getConstType();
			if(booleanConstValue.getBooleanConst()){
				Code.loadConst(1);
			}else{
				Code.loadConst(0);
			}
		}else if(typeOfConstant.equals(SymbolTable.charType)) {
			ConstTypeCharacter charConstValue = (ConstTypeCharacter)factorConst.getConstType();
			Code.loadConst((int)(charConstValue.getCharConst()));
		}else {	// SymbolTable.intType
			ConstTypeNumber integerConstValue = (ConstTypeNumber)factorConst.getConstType();
			Code.loadConst(integerConstValue.getNumberConst());
		}
	}

	//	TERM
	
	public void visit(MultipleTerm multipleTerm) {
		if(multipleTerm.getMulop() instanceof Mul) {
			Code.put(Code.mul);
		}else if(multipleTerm.getMulop() instanceof Div) {
			Code.put(Code.div);
		}else { // MOD
			Code.put(Code.rem);
		}
	}
	
	//	EXPR
	
	public void visit(ExprAddop exprAddop) {
		if(exprAddop.getAddop() instanceof Plus) {
			Code.put(Code.add);
		}else if(exprAddop.getAddop() instanceof Minus) {
			Code.put(Code.sub);
		}
		return;
	}
	
	public void visit(ExprNegative exprNegative) {
		Code.put(Code.neg);
	}
	
	// DESIGNATOR STATEMENT

	public void visit(DesignatorEqualExpr designatorEqualExpr) {
		Code.store(designatorEqualExpr.getDesignator().obj);
	}
	
	public void visit(DesignatorActPars designatorActPars) {
		Obj methodWithArgs = designatorActPars.getDesignator().obj;
		int offset = methodWithArgs.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);

		// u koliko ima povratnu vrednost
		if (methodWithArgs.getType().equals(SymbolTable.noType)) {
			Code.put(Code.pop);
		}
	}
	
	public void visit(DesignatorNoActPars designatorNoActPars) {
		Obj methodWithArgs = designatorNoActPars.getDesignator().obj;
		int offset = methodWithArgs.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);

		// u koliko ima povratnu vrednost
		if (methodWithArgs.getType().equals(SymbolTable.noType)) {
			Code.put(Code.pop);
		}
	}
	
	public void visit(DesignatorIncrement designatorIncrement) {
		Obj designatorToIncrement = designatorIncrement.getDesignator().obj;
		
		//ako je niz
		if(designatorToIncrement.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		
		Code.load(designatorToIncrement);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designatorToIncrement);
		
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
		
		if(typeOfExprToPrint.equals(SymbolTable.charType)) {
			Code.loadConst(charWidth);
			Code.put(Code.bprint);
		}else if(typeOfExprToPrint.equals(SymbolTable.intType) 
				|| typeOfExprToPrint.equals(SymbolTable.booleanType)) {
			Code.loadConst(intAndBoolWidth);
			Code.put(Code.print);
		}
		
	}
	
	public void visit(StatementPrintMultiple statementPrintMultiple) {
		int constArg = statementPrintMultiple.getC2();
		Code.loadConst(constArg);
		
		Struct typeOfDesignator = statementPrintMultiple.getExpr().struct;
		if(typeOfDesignator.equals(SymbolTable.charType)) {
			Code.put(Code.bprint);
		}else {
			Code.put(Code.print);
		}
	}
	
	public void visit(StatementRead statementRead) {
		Struct designatorType = statementRead.getDesignator().obj.getType();
		if(designatorType.equals(SymbolTable.charType)) {
			Code.put(Code.bread);
		}else{
			Code.put(Code.read);
		}
		Code.store(statementRead.getDesignator().obj);
	}
	

}
