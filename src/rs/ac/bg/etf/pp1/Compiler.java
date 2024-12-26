package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.util.Log4JUtils;

public class Compiler {
	
	public static void main(String[] args) {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
		Logger logger = Logger.getLogger(Compiler.class);

		//if (args.length < 2) {
		//	logger.error("Not enough arguments supplied! Usage: Compiler <source-file> <obj-file>");
		//	return;
		//}

		//File sourceCodeFile = new File(args[0]);
		File sourceCodeFile = new File("./test/test301.mj");
		//File sourceCodeFile = new File("./test/program.mj");
		if (!sourceCodeFile.exists()) {
			logger.error("Source file " + sourceCodeFile.getAbsolutePath() + " not found!");
			return;
		}

		int numberOfDelimiterCharacters = 90;
		char delimiterCharacter = '=';

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < numberOfDelimiterCharacters; i++)
			builder.append(delimiterCharacter);

		String delimiter = builder.toString();

		logger.info(delimiter);
		logger.info("Compiling source file " + sourceCodeFile.getAbsolutePath());
		logger.info(delimiter);

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceCodeFile))) {
			Yylex lexer = new Yylex(bufferedReader);
			MJParser parser = new MJParser(lexer);

			logger.info("Parsing source file");
			logger.info(delimiter);

			Symbol symbol = parser.parse();
			SyntaxNode root = (SyntaxNode) symbol.value;

			if (parser.errorDetected) {
				logger.info(delimiter);
				logger.error("Parsing failed");
				logger.info(delimiter);
				return;
			}

			logger.info(delimiter);
			logger.info("Parsing successful");
			logger.info(delimiter);
			logger.info("Abstract syntax tree\n" + ((Program) root).toString(" "));
			logger.info(delimiter);

			logger.info("Checking program semantics");
			logger.info(delimiter);

			SymbolTable.init();
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
			root.traverseBottomUp(semanticAnalyzer);
/*
			if (semanticAnalyzer.errorDetected) {
				logger.info(delimiter);
				logger.error("Semantic check failed");
				logger.info(delimiter);
				return;
			}
*/
			logger.info(delimiter);
			logger.info("Semantic check passed");
			logger.info(delimiter);
			SymbolTable.dump();
/*
			File bytecodeFile = new File(args[1]);
			if (bytecodeFile.exists())
				bytecodeFile.delete();

			CodeGenerator codeGenerator = new CodeGenerator();
			root.traverseBottomUp(codeGenerator);
			Code.dataSize = semanticAnalyzer.numberOfGlobalVars;
			Code.mainPc = codeGenerator.mainPc;
			Code.write(new FileOutputStream(bytecodeFile));

			logger.info(delimiter);
			logger.info("Bytecode written to " + bytecodeFile.getAbsolutePath());
			logger.info(delimiter);
*/
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
