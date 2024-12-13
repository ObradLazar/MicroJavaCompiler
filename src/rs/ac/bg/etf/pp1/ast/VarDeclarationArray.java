// generated with ast extension for cup
// version 0.8
// 14/11/2024 0:16:39


package rs.ac.bg.etf.pp1.ast;

public class VarDeclarationArray extends VarDeclArray {

    private VarDeclArray VarDeclArray;
    private VarDecl VarDecl;

    public VarDeclarationArray (VarDeclArray VarDeclArray, VarDecl VarDecl) {
        this.VarDeclArray=VarDeclArray;
        if(VarDeclArray!=null) VarDeclArray.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public VarDeclArray getVarDeclArray() {
        return VarDeclArray;
    }

    public void setVarDeclArray(VarDeclArray VarDeclArray) {
        this.VarDeclArray=VarDeclArray;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclArray!=null) VarDeclArray.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclArray!=null) VarDeclArray.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclArray!=null) VarDeclArray.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclarationArray(\n");

        if(VarDeclArray!=null)
            buffer.append(VarDeclArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclarationArray]");
        return buffer.toString();
    }
}
