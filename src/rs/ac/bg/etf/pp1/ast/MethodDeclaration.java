// generated with ast extension for cup
// version 0.8
// 19/11/2024 19:40:54


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclaration extends MethodDecl {

    private MethodType MethodType;
    private String methodName;
    private FormParsExist FormParsExist;
    private VarDeclArray VarDeclArray;
    private StatementList StatementList;

    public MethodDeclaration (MethodType MethodType, String methodName, FormParsExist FormParsExist, VarDeclArray VarDeclArray, StatementList StatementList) {
        this.MethodType=MethodType;
        if(MethodType!=null) MethodType.setParent(this);
        this.methodName=methodName;
        this.FormParsExist=FormParsExist;
        if(FormParsExist!=null) FormParsExist.setParent(this);
        this.VarDeclArray=VarDeclArray;
        if(VarDeclArray!=null) VarDeclArray.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethodType getMethodType() {
        return MethodType;
    }

    public void setMethodType(MethodType MethodType) {
        this.MethodType=MethodType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName=methodName;
    }

    public FormParsExist getFormParsExist() {
        return FormParsExist;
    }

    public void setFormParsExist(FormParsExist FormParsExist) {
        this.FormParsExist=FormParsExist;
    }

    public VarDeclArray getVarDeclArray() {
        return VarDeclArray;
    }

    public void setVarDeclArray(VarDeclArray VarDeclArray) {
        this.VarDeclArray=VarDeclArray;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodType!=null) MethodType.accept(visitor);
        if(FormParsExist!=null) FormParsExist.accept(visitor);
        if(VarDeclArray!=null) VarDeclArray.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodType!=null) MethodType.traverseTopDown(visitor);
        if(FormParsExist!=null) FormParsExist.traverseTopDown(visitor);
        if(VarDeclArray!=null) VarDeclArray.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodType!=null) MethodType.traverseBottomUp(visitor);
        if(FormParsExist!=null) FormParsExist.traverseBottomUp(visitor);
        if(VarDeclArray!=null) VarDeclArray.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclaration(\n");

        if(MethodType!=null)
            buffer.append(MethodType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+methodName);
        buffer.append("\n");

        if(FormParsExist!=null)
            buffer.append(FormParsExist.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclArray!=null)
            buffer.append(VarDeclArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclaration]");
        return buffer.toString();
    }
}
