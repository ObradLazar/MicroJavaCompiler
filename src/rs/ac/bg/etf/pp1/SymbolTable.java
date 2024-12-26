package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class SymbolTable extends Tab {

	public static final Struct booleanType = new Struct(Struct.Bool);
	public static final Struct setType = new Struct(Struct.Enum);
	public static Obj addObj, addAllObj;
	
	public static void init() {
		Tab.init();
		currentScope.addToLocals(new Obj(Obj.Type, "bool", booleanType));
		currentScope.addToLocals(new Obj(Obj.Type, "set", setType));
		
		currentScope.addToLocals(addObj = new Obj(Obj.Meth, "add", setType , 0, 2));
		{
			openScope();
			currentScope.addToLocals(new Obj(Obj.Var, "skup", setType, 0, 1));
			currentScope.addToLocals(new Obj(Obj.Var, "broj", intType, 1, 1));
			addObj.setLocals(currentScope.getLocals());
			closeScope();
		}
		
		Struct nizIntova = new Struct(Struct.Array, intType);
		
		currentScope.addToLocals(addAllObj = new Obj(Obj.Meth, "addAll", setType, 0, 2));
		{
			openScope();
			currentScope.addToLocals(new Obj(Obj.Var, "skup", setType, 0, 1));
			currentScope.addToLocals(new Obj(Obj.Var, "nizBrojeva", nizIntova, 1, 1));
			addAllObj.setLocals(currentScope.getLocals());
			closeScope();
		}
		
	}
	
}
