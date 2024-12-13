// generated with ast extension for cup
// version 0.8
// 14/11/2024 0:16:39


package rs.ac.bg.etf.pp1.ast;

public class Div extends Mulop {

    public Div () {
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
        buffer.append("Div(\n");

        buffer.append(tab);
        buffer.append(") [Div]");
        return buffer.toString();
    }
}
