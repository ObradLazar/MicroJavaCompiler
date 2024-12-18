// generated with ast extension for cup
// version 0.8
// 14/11/2024 15:27:47


package rs.ac.bg.etf.pp1.ast;

public class MethodSignatureVoid extends MethodSignature {

    private String methodName;
    private FormParsList FormParsList;

    public MethodSignatureVoid (String methodName, FormParsList FormParsList) {
        this.methodName=methodName;
        this.FormParsList=FormParsList;
        if(FormParsList!=null) FormParsList.setParent(this);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName=methodName;
    }

    public FormParsList getFormParsList() {
        return FormParsList;
    }

    public void setFormParsList(FormParsList FormParsList) {
        this.FormParsList=FormParsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsList!=null) FormParsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsList!=null) FormParsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsList!=null) FormParsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodSignatureVoid(\n");

        buffer.append(" "+tab+methodName);
        buffer.append("\n");

        if(FormParsList!=null)
            buffer.append(FormParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodSignatureVoid]");
        return buffer.toString();
    }
}
