package beast.shell;


import java.util.ArrayList;
import java.util.List;

import beast.core.Description;
import beast.core.Function;
import beast.core.Input;
import beast.core.Operator;

@Description("Operator specified using BEASTScript " +
		"double proposal() must be specified.")
public class ScriptedOperator extends Operator {
	public Input<List<Function>> functionInputs = new Input<List<Function>>("x", "function values used by the operator", new ArrayList<Function>()); 
	public Input<String> valueInput = new Input<String>("value", "script specifying the various functions required for an operator. "
			+ "Arguments specified by x-inputs in order they are specified. "
			+ "If an argument is a tree, it is passed as a JSON object. "
			+ "Should contain proposal() which returns a log of a Hastings ratio."); 

	Interpreter interpreter;

	@Override
	public void initAndValidate() {}
	
	public void initAndValidate(String engineName) {
		interpreter = new Interpreter(engineName, valueInput.get(), functionInputs.get(), this.getID());
	}

	@Override
	public double proposal() {
		return interpreter.evalFunction(functionInputs.get(), "proposal");
	}

}
