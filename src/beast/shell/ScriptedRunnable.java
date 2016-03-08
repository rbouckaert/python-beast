package beast.shell;

import java.util.ArrayList;
import java.util.List;

import beast.core.Description;
import beast.core.Function;
import beast.core.Input;
import beast.core.Runnable;

@Description("Runnable specified using BEASTScript "+
		"run() must be specified. ")
public class ScriptedRunnable extends Runnable {
	public Input<List<Function>> functionInputs = new Input<List<Function>>("x", "function values used by the substitution model", new ArrayList<Function>()); 
	public Input<String> valueInput = new Input<String>("value", "script specifying the various functions required for a substitution model" 
		+ "Arguments specified by x-inputs in order they are specified. "
		+ "If an argument is a tree, it is passed as a JSON object. "
		+ "Should contain run()."); 

	Interpreter interpreter;
	
	@Override
	public void initAndValidate() {}
	
	public void initAndValidate(String engineName) {
		interpreter = new Interpreter(engineName, valueInput.get(), functionInputs.get(), this.getID());
	}

	@Override
	public void run() throws Exception {
		interpreter.evalFunction(functionInputs.get(), "run");
	}

}
