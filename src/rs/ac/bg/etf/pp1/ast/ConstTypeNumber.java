// generated with ast extension for cup
// version 0.8
// 28/11/2024 17:59:42


package rs.ac.bg.etf.pp1.ast;

public class ConstTypeNumber extends ConstType {

    private Integer numberConst;

    public ConstTypeNumber (Integer numberConst) {
        this.numberConst=numberConst;
    }

    public Integer getNumberConst() {
        return numberConst;
    }

    public void setNumberConst(Integer numberConst) {
        this.numberConst=numberConst;
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
        buffer.append("ConstTypeNumber(\n");

        buffer.append(" "+tab+numberConst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstTypeNumber]");
        return buffer.toString();
    }
}
