// generated with ast extension for cup
// version 0.8
// 24/11/2024 15:36:36


package rs.ac.bg.etf.pp1.ast;

public class StatementPrintMultiple extends Statement {

    private Expr Expr;
    private Integer C2;

    public StatementPrintMultiple (Expr Expr, Integer C2) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.C2=C2;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public Integer getC2() {
        return C2;
    }

    public void setC2(Integer C2) {
        this.C2=C2;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementPrintMultiple(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+C2);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementPrintMultiple]");
        return buffer.toString();
    }
}
