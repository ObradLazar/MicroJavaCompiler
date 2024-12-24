package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class SymbolTable extends Tab {

	public static final Struct booleanType = new Struct(Struct.Bool);
	public static final Struct setType = new Struct(Struct.Enum);
	
	public static void init() {
		Tab.init();
		currentScope.addToLocals(new Obj(Obj.Type, "bool", booleanType));
		currentScope.addToLocals(new Obj(Obj.Type, "set", setType));
	}
	
}
