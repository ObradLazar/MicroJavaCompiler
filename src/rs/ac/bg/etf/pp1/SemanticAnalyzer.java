package rs.ac.bg.etf.pp1;

import org.apache.log4j.*;
import rs.ac.bg.etf.pp1.ast.*;

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
		if (typeExist == SymbolTable.noObj) {
			report_error("Tip nije difinisan,", Type);
			return;
		}

		if (typeExist.getKind() != Obj.Type) {
			report_error("Greska pri imenovanja tipa", Type);
			return;
		}

		// ovde cuvam trenutni tip za deklaraciju const i var
		currentType = typeExist.getType();
		Type.struct = typeExist.getType();

		return;
	}

	public void visit(VariableIdent VariableIdent) {
		Obj var = SymbolTable.insert(Obj.Var, VariableIdent.getVarName(), currentType);
		if(var == SymbolTable.noObj) {
			report_error("Simbol vec postoji u tabeli,", VariableIdent);
			return;
		}
		
		if(currentMethod == null) {
			numberOfGlobalVariables++;
		}
		
		var.setAdr(variableCounter++);
	}

	public void visit(VariableArray VariableArray) { 
		Struct arrayType = new Struct(Struct.Array, currentType);
		
		Obj var = SymbolTable.insert(Obj.Var, VariableArray.getVarName(), arrayType);
		if(var == SymbolTable.noObj) {
			report_error("Simbol vec postoji u tabeli,", VariableArray);
			return;
		}
		
		if(currentMethod == null) {
			numberOfGlobalVariables++;
		}
		
		var.setAdr(variableCounter++);
	}
	
	public void visit(ConstTypeCharacter ConstTypeCharacter) { 
		Character constChar = ConstTypeCharacter.getCharConst();
		currentConstIntegerValue = (int)constChar;
		ConstTypeCharacter.struct = SymbolTable.charType;
	}
	
    public void visit(ConstTypeBoolean ConstTypeBoolean) { 
    	Boolean boolConst = ConstTypeBoolean.getBooleanConst();
    	if(boolConst) {
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
    	if(!currentType.equals(ConstDeclaration.getConstType().struct)) {
    		report_error("Deklarisani tip i tip konstante se ne poklapaju!", ConstDeclaration);
    		return;
    	}
    	Obj constant = SymbolTable.insert(Obj.Con, ConstDeclaration.getName(), ConstDeclaration.getConstType().struct);
    	if(constant == SymbolTable.noObj) {
    		report_error("Konstanta sa ovim imenom vec postoji!", ConstDeclaration);
    		return;
    	}  	
    	constant.setAdr(currentConstIntegerValue);
    }
    
    public void visit(MethodName MethodName) {
    	returnFound = false;
    	Struct typeOfMethod = SymbolTable.noType;
    	if(!voidMethod) {
    		typeOfMethod = currentType;
    	}
    	String methodName = MethodName.getMetodName();
    	if(methodName.equals("main")) {
    		mainMethodExists = true;
    		if(typeOfMethod != SymbolTable.noType) {
    			report_error("Main metoda mora biti tipa void.", MethodName);
    			return;
    		}
    	}
    	Obj newMethod = SymbolTable.insert(Obj.Meth, MethodName.getMetodName(), typeOfMethod);
    	if(newMethod == SymbolTable.noObj) {
    		report_error("Greska pri pravljenju metode.", MethodName);
    		return;
    	}
    	currentMethod = newMethod;
    	SymbolTable.openScope();
    }
    
    public void visit(MethVoid MethVoid) { 
    	voidMethod = true;
    }
    
    public void visit(FormParsList FormParsList) {
    	if(currentMethod.getName().equals("main")) {
    		report_error("Main metoda ne sme imati argumente.", FormParsList);
    		return;
    	}
    }
    
    public void visit(StatementReturnEmpty StatementReturnEmpty) { 
    	if(!voidMethod) {
    		report_error("Metoda mora imati povratnu vrednost.", StatementReturnEmpty);
    		return;
    	}
    	returnFound = true;
    }
    
    public void visit(DesignatorIdent DesignatorIdent) { 
    	Obj newDesignator = SymbolTable.find(DesignatorIdent.getDesignatorName());
    	if(newDesignator == SymbolTable.noObj) {
    		report_error("Simbol sa ovim imenom ne postoji.", DesignatorIdent);
    		return;
    	}
    	if((newDesignator.getKind() != Obj.Con) && (newDesignator.getKind() != Obj.Var)) {
    		report_error("Simbol nije pravilno deklarisan.", DesignatorIdent);
    		return;
    	}
    	//potencijalno neko povezivanje sa obj
    }
    
    public void visit(DesignatorIdentExpr DesignatorIdentExpr) { 
    	Obj newDesignator = SymbolTable.find(DesignatorIdentExpr.getDesignatorName());
    	if(newDesignator == SymbolTable.noObj) {
    		report_error("Simbol sa ovim imenom ne postoji.", DesignatorIdentExpr);
    		return;
    	}
    	if((newDesignator.getKind() != Obj.Var) || (newDesignator.getType().getKind() != Struct.Array)) {
    		report_error("Simbol nije pravilno deklarisan.", DesignatorIdentExpr);
    		return;
    	}
    	//potencijalno neko povezivanje sa obj + provera expr
    }
    
    public void visit(FactorConst FactorConst) { 
    	FactorConst.struct = FactorConst.getConstType().struct;
    }
	/*
	 * ALL METHODS 
	public void visit(Designator Designator) { }
    public void visit(MethodDecl MethodDecl) { }
    public void visit(Factor Factor) { }
    public void visit(DesignatorStatement DesignatorStatement) { }    
    public void visit(Declaration Declaration) { }
    public void visit(Expr Expr) { }
    public void visit(DeclList DeclList) { }
    public void visit(Statement Statement) { }
    public void visit(VarDeclArray VarDeclArray) { }
    public void visit(Term Term) { }
    public void visit(ActPars ActPars) { }
    public void visit(StatementList StatementList) { }
    public void visit(FactorDesignatorCallFuncNoPars FactorDesignatorCallFuncNoPars) { visit(); }
    public void visit(FactorDesignatorCallFunc FactorDesignatorCallFunc) { visit(); }
    public void visit(FactorDesignator FactorDesignator) { visit(); }
    public void visit(FactorExpr FactorExpr) { visit(); }
    public void visit(FactorNewExprArray FactorNewExprArray) { visit(); }
    
    public void visit(SingleActPar SingleActPar) { visit(); }
    public void visit(MultipleActPars MultipleActPars) { visit(); }
    public void visit(SignleTerm SignleTerm) { visit(); }
    public void visit(MultipleTerm MultipleTerm) { visit(); }
    public void visit(ExprAddop ExprAddop) { visit(); }
    public void visit(ExprNegative ExprNegative) { visit(); }
    public void visit(ExprPositive ExprPositive) { visit(); }
    public void visit(DesignatorIdentExpr DesignatorIdentExpr) { visit(); }
    
    public void visit(DesignatorNoActPars DesignatorNoActPars) { visit(); }
    public void visit(DesignatorActPars DesignatorActPars) { visit(); }
    public void visit(DesignatorDecrement DesignatorDecrement) { visit(); }
    public void visit(DesignatorIncrement DesignatorIncrement) { visit(); }
    public void visit(DesignatorUnion DesignatorUnion) { visit(); }
    public void visit(DesignatorEqualExpr DesignatorEqualExpr) { visit(); }
    public void visit(StatementReturnSomething StatementReturnSomething) { visit(); }
    public void visit(StatementReturnEmpty StatementReturnEmpty) { visit(); }
    public void visit(StatementPrintMultiple StatementPrintMultiple) { visit(); }
    public void visit(StatementPrint StatementPrint) { visit(); }
    public void visit(StatementRead StatementRead) { visit(); }
    public void visit(StatementDesignator StatementDesignator) { visit(); }
    public void visit(StatementListMultiple StatementListMultiple) { visit(); }
    public void visit(MethodDeclaration MethodDeclaration) { visit(); }
    public void visit(MethVoid MethVoid) { visit(); }
    public void visit(MethType MethType) { visit(); }
    public void visit(MethodDeclarationListMultiple MethodDeclarationListMultiple) { visit(); }
    public void visit(DeclarationVariable DeclarationVariable) { visit(); }
    public void visit(DeclarationConstant DeclarationConstant) { visit(); }
    public void visit(DeclarationList DeclarationList) { visit(); }
    
    	MOZDA NE TREBA
   	public void visit(FormPar FormPar) { visit(); }
    public void visit(FormParsListSignle FormParsListSignle) { visit(); }
    public void visit(FormParsListMultiple FormParsListMultiple) { visit(); }
	public void visit(FormParArray FormParArray) { visit(); }
	 */

}
