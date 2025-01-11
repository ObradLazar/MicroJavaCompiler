package rs.ac.bg.etf.pp1;

import org.apache.log4j.*;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	Obj currentMethod = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	boolean mainMethodExists = false;
	Struct currentType = null;
	int currentConstIntegerValue = 0;
	int numberOfGlobalVariables = 0;
	int variableCounter = 0;
	boolean voidMethod = false;
	int argumentCounter = 0;
	int methodParamCounter = 0;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public void visit(ProgramName ProgramName) {
		ProgramName.obj = SymbolTable.insert(Obj.Prog, ProgramName.getName(), SymbolTable.noType);
		SymbolTable.openScope();
	}

	public void visit(Program Program) {
		if (!mainMethodExists) {
			report_error("Main metoda nije pronadjena, ", Program);
		}

		SymbolTable.chainLocalSymbols(Program.getProgramName().obj);
		SymbolTable.closeScope();
	}

	public void visit(Type Type) {
		String typeName = Type.getTypeName();

		Obj typeExist = SymbolTable.find(typeName);
		if (typeExist.equals(SymbolTable.noObj)) {
			report_error("Tip nije difinisan,", Type);
			return;
		}

		if (typeExist.getKind() != Obj.Type) {
			report_error("Greska pri imenovanja tipa", Type);
			return;
		}

		currentType = typeExist.getType();
		Type.struct = typeExist.getType();

		return;
	}

	// VARIABLES

	public void visit(VariableIdent VariableIdent) {
		Obj var = SymbolTable.insert(Obj.Var, VariableIdent.getVarName(), currentType);
		if (var.equals(SymbolTable.noObj)) {
			report_error("Simbol vec postoji u tabeli,", VariableIdent);
			return;
		}

		if (currentMethod == null) {
			numberOfGlobalVariables++;
		}

		var.setAdr(variableCounter++);
	}

	public void visit(VariableArray VariableArray) {
		Struct arrayType = new Struct(Struct.Array, currentType);

		Obj var = SymbolTable.insert(Obj.Var, VariableArray.getVarName(), arrayType);
		if (var.equals(SymbolTable.noObj)) {
			report_error("Simbol vec postoji u tabeli,", VariableArray);
			return;
		}

		if (currentMethod == null) {
			numberOfGlobalVariables++;
		}

		var.setAdr(variableCounter++);
	}

	// CONSTANTS

	public void visit(ConstTypeCharacter ConstTypeCharacter) {
		Character constChar = ConstTypeCharacter.getCharConst();
		currentConstIntegerValue = (int) constChar;
		ConstTypeCharacter.struct = SymbolTable.charType;
	}

	public void visit(ConstTypeBoolean ConstTypeBoolean) {
		Boolean boolConst = ConstTypeBoolean.getBooleanConst();
		if (boolConst) {
			currentConstIntegerValue = 1;
		} else {
			currentConstIntegerValue = 0;
		}
		ConstTypeBoolean.struct = SymbolTable.booleanType;
	}

	public void visit(ConstTypeNumber ConstTypeNumber) {
		currentConstIntegerValue = ConstTypeNumber.getNumberConst();
		ConstTypeNumber.struct = SymbolTable.intType;
	}

	public void visit(ConstDeclaration ConstDeclaration) {
		if (!currentType.equals(ConstDeclaration.getConstType().struct)) {
			report_error("Deklarisani tip i tip konstante se ne poklapaju!", ConstDeclaration);
			return;
		}
		Obj constant = SymbolTable.insert(Obj.Con, ConstDeclaration.getName(), ConstDeclaration.getConstType().struct);
		if (constant.equals(SymbolTable.noObj)) {
			report_error("Konstanta sa ovim imenom vec postoji!", ConstDeclaration);
			return;
		}
		constant.setAdr(currentConstIntegerValue);
	}

	// METHOD

	public void visit(MethodName MethodName) {
		variableCounter = 0;
		returnFound = false;
		Struct typeOfMethod = SymbolTable.noType;
		if (!voidMethod) {
			typeOfMethod = currentType;
		}
		String methodName = MethodName.getMetodName();
		if (methodName.equals("main")) {
			mainMethodExists = true;
			if (typeOfMethod != SymbolTable.noType) {
				report_error("Main metoda mora biti tipa void.", MethodName);
				return;
			}
		}
		Obj newMethod = SymbolTable.insert(Obj.Meth, MethodName.getMetodName(), typeOfMethod);
		if (newMethod.equals(SymbolTable.noObj)) {
			report_error("Greska pri pravljenju metode.", MethodName);
			return;
		}
		currentMethod = newMethod;
		MethodName.obj = newMethod;
		SymbolTable.openScope();
	}

	public void visit(MethodDeclaration MethodDecl) {

		if (currentMethod.getType() != SymbolTable.noType && !returnFound) {
			report_error("Metoda mora imati povratnu vrednost.", MethodDecl);
			return;
		}

		returnFound = false;

		currentMethod.setLevel(argumentCounter);

		SymbolTable.chainLocalSymbols(currentMethod);
		SymbolTable.closeScope();

		argumentCounter = 0;
		currentMethod = null;
	}

	public void visit(MethVoid MethVoid) {
		voidMethod = true;
	}

	// FORM PARS

	public void visit(FormParsList FormParsList) {
		if (currentMethod.getName().equals("main")) {
			report_error("Main metoda ne sme imati argumente.", FormParsList);
			return;
		}
	}

	public void visit(FormParArray FormParArray) {
		Obj parameter = SymbolTable.currentScope.findSymbol(FormParArray.getParName());
		if (parameter != SymbolTable.noObj) {
			report_error("Simbol sa ovim imenom vec postoji.", FormParArray);
			return;
		}
		Struct arrayType = new Struct(Struct.Array, currentType);
		parameter = SymbolTable.insert(Obj.Var, FormParArray.getParName(), arrayType);
		if (parameter == SymbolTable.noObj) {
			report_error("Greska pri ubacivanju argumenta u tabelu simbola.", FormParArray);
			return;
		}
		parameter.setAdr(variableCounter++);
		parameter.setFpPos(argumentCounter++);
	}

	public void visit(FormPar FormPar) {
		Obj parameter = SymbolTable.currentScope.findSymbol(FormPar.getParName());
		if (parameter != null) {
			report_error("Simbol sa ovim imenom vec postoji.", FormPar);
			return;
		}
		parameter = SymbolTable.insert(Obj.Var, FormPar.getParName(), FormPar.getType().struct);
		if (parameter == SymbolTable.noObj) {
			report_error("Greska pri ubacivanju argumenta u tabelu simbola.", FormPar);
			return;
		}
		parameter.setAdr(variableCounter++);
		parameter.setFpPos(argumentCounter++);
	}

	// STATEMENTS

	public void visit(StatementReturnEmpty StatementReturnEmpty) {
		if (currentMethod == null) {
			report_error("Return se mora nalaziti unutar neke metode.", StatementReturnEmpty);
			return;
		}
		if (!voidMethod) {
			report_error("Metoda mora imati povratnu vrednost.", StatementReturnEmpty);
			return;
		}
		returnFound = false;
	}

	public void visit(StatementReturnSomething StatementReturnSomething) {
		if (currentMethod == null) {
			report_error("Return se mora nalaziti unutar neke metode.", StatementReturnSomething);
			return;
		}
		if (voidMethod) {
			report_error("Void metoda ne sme imati povratnu vrednost.", StatementReturnSomething);
			return;
		}
		if (!StatementReturnSomething.getExpr().struct.equals(currentMethod.getType())) {
			report_error("Ne poklapa se tip metode sa tipom povratne vrednosti.", StatementReturnSomething);
			return;
		}

		returnFound = true;

	}

	public void visit(StatementPrintMultiple StatementPrintMultiple) {
		if (!StatementPrintMultiple.getExpr().struct.equals(SymbolTable.intType)
				&& !StatementPrintMultiple.getExpr().struct.equals(SymbolTable.booleanType)
				&& !StatementPrintMultiple.getExpr().struct.equals(SymbolTable.charType)
				&& !StatementPrintMultiple.getExpr().struct.equals(SymbolTable.setType)) {
			report_error("Tip mora biti int, bool, char ili set.", StatementPrintMultiple);
			return;
		}
	}

	public void visit(StatementPrint StatementPrint) {
		if (!StatementPrint.getExpr().struct.equals(SymbolTable.intType)
				&& !StatementPrint.getExpr().struct.equals(SymbolTable.booleanType)
				&& !StatementPrint.getExpr().struct.equals(SymbolTable.charType)
				&& !StatementPrint.getExpr().struct.equals(SymbolTable.setType)) {
			report_error("Tip mora biti int, bool, char ili set.", StatementPrint);
			return;
		}
	}

	public void visit(StatementRead StatementRead) {
		if (StatementRead.getDesignator().obj.getKind() != Obj.Var
				&& !(StatementRead.getDesignator().obj.getKind() == Obj.Elem)) {
			report_error("Vrednost u zagradama mora biti promenljiva.", StatementRead);
			return;
		}
		if (!StatementRead.getDesignator().obj.getType().equals(SymbolTable.intType)
				&& !StatementRead.getDesignator().obj.getType().equals(SymbolTable.booleanType)
				&& !StatementRead.getDesignator().obj.getType().equals(SymbolTable.charType)) {
			report_error("Podatak mora biti tipa bool, int ili char.", StatementRead);
			return;
		}
	}

	// DESIGNATOR

	public void visit(DesignatorIdent DesignatorIdent) {
		Obj newDesignator = SymbolTable.find(DesignatorIdent.getDesignatorName());
		if (newDesignator == SymbolTable.noObj) {
			report_error("Simbol sa ovim imenom ne postoji.", DesignatorIdent);
			return;
		}
		if ((newDesignator.getKind() != Obj.Con) && (newDesignator.getKind() != Obj.Var)
				&& (newDesignator.getKind() != Obj.Meth) && (newDesignator.getKind() != Obj.Elem)) {
			report_error("Simbol nije pravilno deklarisan.", DesignatorIdent);
			return;
		}
		DesignatorIdent.obj = newDesignator;
	}

	public void visit(DesignatorIdentExpr DesignatorIdentExpr) {
		String designatorName = DesignatorIdentExpr.getDesignatorArrayName().getDesignatorName();
		
		Obj newDesignator = SymbolTable.find(designatorName);
		if (newDesignator.equals(SymbolTable.noObj)) {
			report_error("Simbol sa ovim imenom ne postoji.", DesignatorIdentExpr);
			return;
		}
		if ((newDesignator.getKind() != Obj.Var) && (newDesignator.getType().getKind() != Struct.Array)) {
			report_error("Simbol nije pravilno deklarisan.", DesignatorIdentExpr);
			return;
		}
		if (!DesignatorIdentExpr.getExpr().struct.equals(SymbolTable.intType)) {
			report_error("Izraz u [ ] mora biti tipa int.", DesignatorIdentExpr);
			return;
		}

		Obj arrayObject = new Obj(Obj.Elem, designatorName,
				newDesignator.getType().getElemType());
		arrayObject.setLevel(newDesignator.getLevel());
		DesignatorIdentExpr.obj = arrayObject;
	}
	
	public void visit(DesignatorArrayName designatorArrayName) {
		String designatorName = designatorArrayName.getDesignatorName();
		
		Obj newDesignator = SymbolTable.find(designatorName);
		
		designatorArrayName.obj = newDesignator;
	}
	
	// FACTOR

	public void visit(FactorConst FactorConst) {
		FactorConst.struct = FactorConst.getConstType().struct;
	}

	public void visit(FactorDesignatorCallFuncNoPars FactorDesignatorCallFuncNoPars) {
		Obj methodFactor = SymbolTable.find(FactorDesignatorCallFuncNoPars.getDesignator().obj.getName());
		if (methodFactor.equals(SymbolTable.noObj)) {
			report_error("Ova metoda ne postoji u tabeli simbola.", FactorDesignatorCallFuncNoPars);
			return;
		}
		if (methodFactor.getKind() != Obj.Meth) {
			report_error("Simbol nije deklarisan kao metoda.", FactorDesignatorCallFuncNoPars);
			return;
		}

		FactorDesignatorCallFuncNoPars.struct = FactorDesignatorCallFuncNoPars.getDesignator().obj.getType();
	}

	public void visit(FactorDesignatorCallFunc FactorDesignatorCallFunc) {
		if (!(FactorDesignatorCallFunc.getDesignator().obj.getKind() == Obj.Meth)) {
			report_error("Simbol nije deklarisan kao metoda.", FactorDesignatorCallFunc);
			return;
		}
		Obj methodToCall = SymbolTable.find(FactorDesignatorCallFunc.getDesignator().obj.getName());
		if (methodToCall.getLevel() != methodParamCounter) {
			report_error("Nije isti broj argumenata i prosledjenih parametara metode.", FactorDesignatorCallFunc);
			return;
		}
		methodParamCounter = 0;
		FactorDesignatorCallFunc.struct = FactorDesignatorCallFunc.getDesignator().obj.getType();
	}

	public void visit(FactorNewExprArray FactorNewExprArray) {
		if (!FactorNewExprArray.getExpr().struct.equals(SymbolTable.intType)) {
			report_error("Izraz u [ ] zagradama mora biti tipa int.", FactorNewExprArray);
			return;
		}
		String setOrArray = FactorNewExprArray.getType().getTypeName();
		if (setOrArray.equals("set")) {
			FactorNewExprArray.struct = SymbolTable.setType;
		} else {
			FactorNewExprArray.struct = new Struct(Struct.Array, currentType);
		}
	}

	public void visit(FactorExpr FactorExpr) {
		FactorExpr.struct = FactorExpr.getExpr().struct;
	}

	public void visit(FactorDesignator FactorDesignator) {
		FactorDesignator.struct = FactorDesignator.getDesignator().obj.getType();
	}

	// TERM

	public void visit(SignleTerm SignleTerm) {
		SignleTerm.struct = SignleTerm.getFactor().struct;
	}

	public void visit(MultipleTerm MultipleTerm) {
		if (!(MultipleTerm.getTerm().struct.equals(SymbolTable.intType))
				|| !(MultipleTerm.getFactor().struct.equals(SymbolTable.intType))) {
			report_error("Potrebno je da obe strane u izrazu budu tipa int", MultipleTerm);
			return;
		}

		MultipleTerm.struct = SymbolTable.intType;
	}

	// EXPR

	public void visit(ExprAddop ExprAddop) {
		if (!(ExprAddop.getExpr().struct.equals(SymbolTable.intType))
				|| !(ExprAddop.getTerm().struct.equals(SymbolTable.intType))) {
			report_error("Potrebno je da obe strane u izrazu budu tipa int", ExprAddop);
			return;
		}

		ExprAddop.struct = SymbolTable.intType;
	}

	public void visit(ExprNegative ExprNegative) {
		if (!ExprNegative.getTerm().struct.equals(SymbolTable.intType)) {
			report_error("Izraz mora biti tipa int.", ExprNegative);
		}

		ExprNegative.struct = SymbolTable.intType;
	}

	public void visit(ExprPositive ExprPositive) {
		ExprPositive.struct = ExprPositive.getTerm().struct;
	}

	// DESIGNATOR STATEMENT

	public void visit(DesignatorNoActPars DesignatorNoActPars) {
		Obj designatorMethod = SymbolTable.find(DesignatorNoActPars.getDesignator().obj.getName());
		if (designatorMethod.getKind() != Obj.Meth) {
			report_error("Simbol nije deklarisan kao metoda.", DesignatorNoActPars);
			return;
		}
	}

	public void visit(DesignatorActPars DesignatorActPars) {
		if (!(DesignatorActPars.getDesignator().obj.getKind() == Obj.Meth)) {
			report_error("Simbol nije deklarisan kao metoda.", DesignatorActPars);
			return;
		}
		Obj methodToCall = SymbolTable.find(DesignatorActPars.getDesignator().obj.getName());
		if (methodToCall.getLevel() != methodParamCounter) {
			report_error("Nije isti broj argumenata i prosledjenih parametara metode.", DesignatorActPars);
			return;
		}
		methodParamCounter = 0;
	}

	public void visit(DesignatorDecrement DesignatorDecrement) {
		if (!DesignatorDecrement.getDesignator().obj.getType().equals(SymbolTable.intType)
				&& !(DesignatorDecrement.getDesignator().obj.getKind() == Obj.Elem)) {
			report_error("Podatak mora biti tipa int da bi se radila operacija ++/--.", DesignatorDecrement);
			return;
		}
		if (!(DesignatorDecrement.getDesignator().obj.getKind() == Obj.Var)) {
			report_error("Podatak mora biti promenljiva ili clan niza.", DesignatorDecrement);
			return;
		}
	}

	public void visit(DesignatorIncrement DesignatorIncrement) {
		if (!DesignatorIncrement.getDesignator().obj.getType().equals(SymbolTable.intType)) {
			report_error("Podatak mora biti tipa int da bi se radila operacija ++/--.", DesignatorIncrement);
			return;
		}
		if (!(DesignatorIncrement.getDesignator().obj.getKind() == Obj.Var)
				&& !(DesignatorIncrement.getDesignator().obj.getKind() == Obj.Elem)) {
			report_error("Podatak mora biti promenljiva ili clan niza.", DesignatorIncrement);
			return;
		}
	}

	public void visit(DesignatorUnion DesignatorUnion) {
		if (!DesignatorUnion.getDesignator1().obj.getType().equals(SymbolTable.setType)
				|| !DesignatorUnion.getDesignator2().obj.getType().equals(SymbolTable.setType)
				|| !DesignatorUnion.getDesignator().obj.getType().equals(SymbolTable.setType)) {
			report_error("Unija se moze koristiti samo nad podacima tipa skup (set).", DesignatorUnion);
			return;
		}
	}

	public void visit(DesignatorEqualExpr DesignatorEqualExpr) {
		if (DesignatorEqualExpr.getDesignator().obj.getKind() != Obj.Var
				&& DesignatorEqualExpr.getDesignator().obj.getKind() != Obj.Elem) {
			report_error("Tip mora biti int ili element niza.", DesignatorEqualExpr);
			return;
		}

		if (!DesignatorEqualExpr.getExpr().struct.assignableTo(DesignatorEqualExpr.getDesignator().obj.getType())) {
			report_error("Tip deklarisane promenljive se mora poklapati sa tipom podatka.", DesignatorEqualExpr);
			return;
		}
	}

	// ACT PARS
	public void visit(SingleActPar SingleActPar) {
		methodParamCounter++;
	}

	public void visit(MultipleActPars MultipleActPars) {
		methodParamCounter++;
	}

}
