// generated with ast extension for cup
// version 0.8
// 28/11/2024 17:59:42


package rs.ac.bg.etf.pp1.ast;

public class ConstTypeBoolean extends ConstType {

    private Boolean booleanConst;

    public ConstTypeBoolean (Boolean booleanConst) {
        this.booleanConst=booleanConst;
    }

    public Boolean getBooleanConst() {
        return booleanConst;
    }

    public void setBooleanConst(Boolean booleanConst) {
        this.booleanConst=booleanConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstTypeBoolean(\n");

        buffer.append(" "+tab+booleanConst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstTypeBoolean]");
        return buffer.toString();
    }
}
