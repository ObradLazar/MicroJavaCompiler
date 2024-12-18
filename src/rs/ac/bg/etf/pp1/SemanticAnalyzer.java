package rs.ac.bg.etf.pp1;

import java_cup.runtime.*;
import org.apache.log4j.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.Obj;

public class SemanticAnalyzer extends VisitorAdaptor{
	
	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	/*
	 * ALL METHODS
	public void visit(Designator Designator) { }
    public void visit(MethodDecl MethodDecl) { }
    public void visit(Factor Factor) { }
    public void visit(AddopTermList AddopTermList) { }
    public void visit(Mulop Mulop) { }
    public void visit(DesignatorStatement DesignatorStatement) { }
    public void visit(ConstType ConstType) { }
    public void visit(Declaration Declaration) { }
    public void visit(Expr Expr) { }
    public void visit(Type Type) { }
    public void visit(FormPars FormPars) { }
    public void visit(VarDeclList VarDeclList) { }
    public void visit(MethodSignature MethodSignature) { }
    public void visit(ConstDeclList ConstDeclList) { }
    public void visit(Addop Addop) { }
    public void visit(MethodDeclList MethodDeclList) { }
    public void visit(DeclList DeclList) { }
    public void visit(Variable Variable) { }
    public void visit(Statement Statement) { }
    public void visit(FormParsList FormParsList) { }
    public void visit(Setop Setop) { }
    public void visit(VarDeclArray VarDeclArray) { }
    public void visit(Term Term) { }
    public void visit(ActPars ActPars) { }
    public void visit(StatementList StatementList) { }
    public void visit(TypeName TypeName) { visit(); }
    public void visit(FactorDesignatorCallFuncNoPars FactorDesignatorCallFuncNoPars) { visit(); }
    public void visit(FactorDesignatorCallFunc FactorDesignatorCallFunc) { visit(); }
    public void visit(FactorDesignator FactorDesignator) { visit(); }
    public void visit(FactorExpr FactorExpr) { visit(); }
    public void visit(FactorNewExprArray FactorNewExprArray) { visit(); }
    public void visit(FactorConst FactorConst) { visit(); }
    public void visit(SingleActPar SingleActPar) { visit(); }
    public void visit(MultipleActPars MultipleActPars) { visit(); }
    public void visit(SignleTerm SignleTerm) { visit(); }
    public void visit(MultipleTerm MultipleTerm) { visit(); }
    public void visit(Mod Mod) { visit(); }
    public void visit(Div Div) { visit(); }
    public void visit(Mul Mul) { visit(); }
    public void visit(Minus Minus) { visit(); }
    public void visit(Plus Plus) { visit(); }
    public void visit(Union Union) { visit(); }
    public void visit(NoTermAddopList NoTermAddopList) { visit(); }
    public void visit(MultipleTermAddopList MultipleTermAddopList) { visit(); }
    public void visit(NegativeExpr NegativeExpr) { visit(); }
    public void visit(PositiveExpr PositiveExpr) { visit(); }
    public void visit(DesignatorIdentExpr DesignatorIdentExpr) { visit(); }
    public void visit(DesignatorIdent DesignatorIdent) { visit(); }
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
    public void visit(NoStatementList NoStatementList) { visit(); }
    public void visit(StatementListMultiple StatementListMultiple) { visit(); }
    public void visit(FormParArray FormParArray) { visit(); }
    public void visit(FormPar FormPar) { visit(); }
    public void visit(FormParsListSignle FormParsListSignle) { visit(); }
    public void visit(FormParsListMultiple FormParsListMultiple) { visit(); }
    public void visit(ConstTypeCharacter ConstTypeCharacter) { visit(); }
    public void visit(ConstTypeBoolean ConstTypeBoolean) { visit(); }
    public void visit(ConstTypeNumber ConstTypeNumber) { visit(); }
    public void visit(NoVarDeclarationArray NoVarDeclarationArray) { visit(); }
    public void visit(VarDeclarationArray VarDeclarationArray) { visit(); }
    public void visit(MethodDeclaration MethodDeclaration) { visit(); }
    public void visit(MethodSignatureTypeNoPars MethodSignatureTypeNoPars) { visit(); }
    public void visit(MethodSignatureVoidNoPars MethodSignatureVoidNoPars) { visit(); }
    public void visit(MethodSignatureVoid MethodSignatureVoid) { visit(); }
    public void visit(MethodSignatureType MethodSignatureType) { visit(); }
    public void visit(VariableArray VariableArray) { visit(); }
    public void visit(VariableIdent VariableIdent) { visit(); }
    public void visit(VarDeclListSignle VarDeclListSignle) { visit(); }
    public void visit(VarDeclListMultiple VarDeclListMultiple) { visit(); }
    public void visit(VarDecl VarDecl) { visit(); }
    public void visit(ConstDeclaration ConstDeclaration) { visit(); }
    public void visit(ConstDeclListSignle ConstDeclListSignle) { visit(); }
    public void visit(ConstDeclListMultiple ConstDeclListMultiple) { visit(); }
    public void visit(ConstDecl ConstDecl) { visit(); }
    public void visit(NoMethodDeclList NoMethodDeclList) { visit(); }
    public void visit(MethodDeclarationListMultiple MethodDeclarationListMultiple) { visit(); }
    public void visit(DeclarationVariable DeclarationVariable) { visit(); }
    public void visit(DeclarationConstant DeclarationConstant) { visit(); }
    public void visit(NoDeclarationList NoDeclarationList) { visit(); }
    public void visit(DeclarationList DeclarationList) { visit(); }
    public void visit(ProgramName ProgramName) { visit(); }
    public void visit(Program Program) { visit(); }
	 * */
	
	
	
}
