// generated with ast extension for cup
// version 0.8
// 24/11/2024 15:36:35


package rs.ac.bg.etf.pp1.ast;

public class MethVoid extends MethodType {

    public MethVoid () {
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
        buffer.append("MethVoid(\n");

        buffer.append(tab);
        buffer.append(") [MethVoid]");
        return buffer.toString();
    }
}
