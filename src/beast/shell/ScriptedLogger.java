package beast.shell;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import beast.core.BEASTObject;
import beast.core.Description;
import beast.core.Function;
import beast.core.Input;
import beast.core.Loggable;

@Description("Logger specified using BEASTScript "+
		"init(out), log(sample, out), close(out) must be specified. ")
public class ScriptedLogger extends BEASTObject implements Loggable {
	public Input<List<Function>> functionInputs = new Input<List<Function>>("x", "function values used by the distirbution", new ArrayList<Function>()); 
	public Input<String> valueInput = new Input<String>("value", "script specifying the various functions required for logging."
			+ "Arguments specified by x-inputs in order they are specified. "
			+ "If an argument is a tree, it is passed as a JSON object. "
			+ "Should contain init(out), log(sample, out) and close(out)."); 

	Interpreter interpreter;

	@Override
	public void initAndValidate() {}
	
	public void initAndValidate(String engineName) {
		interpreter = new Interpreter(engineName, valueInput.get(), functionInputs.get(), this.getID());
	}
	
	@Override
	public void init(PrintStream out) {
		interpreter.evalVoidFunction(functionInputs.get(), "init", out);

	}

	@Override
	public void log(int nSample, PrintStream out) {
		interpreter.evalVoidFunction(functionInputs.get(), "log", nSample, out);
	}

	@Override
	public void close(PrintStream out) {
		interpreter.evalVoidFunction(functionInputs.get(), "close", out);
	}

}
