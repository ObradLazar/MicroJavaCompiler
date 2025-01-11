// generated with ast extension for cup
// version 0.8
// 10/0/2025 16:47:31


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Designator Designator);
    public void visit(MethodDecl MethodDecl);
    public void visit(MethodType MethodType);
    public void visit(Factor Factor);
    public void visit(Mulop Mulop);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(ConstType ConstType);
    public void visit(Declaration Declaration);
    public void visit(Expr Expr);
    public void visit(FormPars FormPars);
    public void visit(VarDeclList VarDeclList);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(Addop Addop);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(DeclList DeclList);
    public void visit(Variable Variable);
    public void visit(Statement Statement);
    public void visit(FormParsList FormParsList);
    public void visit(FormParsExist FormParsExist);
    public void visit(Setop Setop);
    public void visit(VarDeclArray VarDeclArray);
    public void visit(Term Term);
    public void visit(ActPars ActPars);
    public void visit(StatementList StatementList);
    public void visit(Type Type);
    public void visit(FactorDesignatorCallFuncNoPars FactorDesignatorCallFuncNoPars);
    public void visit(FactorDesignatorCallFunc FactorDesignatorCallFunc);
    public void visit(FactorDesignator FactorDesignator);
    public void visit(FactorExpr FactorExpr);
    public void visit(FactorNewExprArray FactorNewExprArray);
    public void visit(FactorConst FactorConst);
    public void visit(SingleActPar SingleActPar);
    public void visit(MultipleActPars MultipleActPars);
    public void visit(SignleTerm SignleTerm);
    public void visit(MultipleTerm MultipleTerm);
    public void visit(Mod Mod);
    public void visit(Div Div);
    public void visit(Mul Mul);
    public void visit(Minus Minus);
    public void visit(Plus Plus);
    public void visit(Union Union);
    public void visit(ExprAddop ExprAddop);
    public void visit(ExprNegative ExprNegative);
    public void visit(ExprPositive ExprPositive);
    public void visit(DesignatorArrayName DesignatorArrayName);
    public void visit(DesignatorIdentExpr DesignatorIdentExpr);
    public void visit(DesignatorIdent DesignatorIdent);
    public void visit(DesignatorNoActPars DesignatorNoActPars);
    public void visit(DesignatorActPars DesignatorActPars);
    public void visit(DesignatorDecrement DesignatorDecrement);
    public void visit(DesignatorIncrement DesignatorIncrement);
    public void visit(DesignatorUnion DesignatorUnion);
    public void visit(DesignatorEqualExpr DesignatorEqualExpr);
    public void visit(StatementReturnSomething StatementReturnSomething);
    public void visit(StatementReturnEmpty StatementReturnEmpty);
    public void visit(StatementPrintMultiple StatementPrintMultiple);
    public void visit(StatementPrint StatementPrint);
    public void visit(StatementRead StatementRead);
    public void visit(StatementDesignator StatementDesignator);
    public void visit(NoStatementList NoStatementList);
    public void visit(StatementListMultiple StatementListMultiple);
    public void visit(FormParArray FormParArray);
    public void visit(FormPar FormPar);
    public void visit(FormParsListSignle FormParsListSignle);
    public void visit(FormParsListMultiple FormParsListMultiple);
    public void visit(NoFormPars NoFormPars);
    public void visit(FormParsDoExist FormParsDoExist);
    public void visit(ConstTypeCharacter ConstTypeCharacter);
    public void visit(ConstTypeBoolean ConstTypeBoolean);
    public void visit(ConstTypeNumber ConstTypeNumber);
    public void visit(NoVarDeclarationArray NoVarDeclarationArray);
    public void visit(VarDeclarationArray VarDeclarationArray);
    public void visit(MethodName MethodName);
    public void visit(MethodDeclaration MethodDeclaration);
    public void visit(MethVoid MethVoid);
    public void visit(MethType MethType);
    public void visit(VariableArray VariableArray);
    public void visit(VariableIdent VariableIdent);
    public void visit(VarDeclListSingle VarDeclListSingle);
    public void visit(VarDeclListMultiple VarDeclListMultiple);
    public void visit(VarDecl VarDecl);
    public void visit(ConstDeclaration ConstDeclaration);
    public void visit(ConstDeclListSignle ConstDeclListSignle);
    public void visit(ConstDeclListMultiple ConstDeclListMultiple);
    public void visit(ConstDecl ConstDecl);
    public void visit(NoMethodDeclList NoMethodDeclList);
    public void visit(MethodDeclarationListMultiple MethodDeclarationListMultiple);
    public void visit(DeclarationVariable DeclarationVariable);
    public void visit(DeclarationConstant DeclarationConstant);
    public void visit(NoDeclarationList NoDeclarationList);
    public void visit(DeclarationList DeclarationList);
    public void visit(ProgramName ProgramName);
    public void visit(Program Program);

}
