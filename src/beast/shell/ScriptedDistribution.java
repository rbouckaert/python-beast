package beast.shell;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import beast.core.Description;
import beast.core.Distribution;
import beast.core.Function;
import beast.core.Input;
import beast.core.State;

@Description("BEAST distribution specified using Python script "+
		"calculateLogP() must be specified. ")
public class ScriptedDistribution extends Distribution {
	public Input<List<Function>> functionInputs = new Input<List<Function>>("x", "function values used by the distribution", new ArrayList<Function>()); 
	public Input<String> valueInput = new Input<String>("value", "script specifying a density functions f(args) "
			+ "Arguments specified by x-inputs in order they are specified. "
			+ "If an argument is a tree, it is passed as a JSON object. "
			+ "Should return a density for the arguments provided."); 

	Interpreter interpreter;

	@Override
	public void initAndValidate() {}
	
	public void initAndValidate(String engineName) {
		interpreter = new Interpreter(engineName, valueInput.get(), functionInputs.get(), this.getID());
	}
	
    public double calculateLogP() {
        logP = 0;
        logP = interpreter.getArrayValue();
        return logP;
    }

	@Override
	public void store() {
		interpreter.isUpToDate = false;
		super.store();
	}

	@Override
	public void restore() {
		interpreter.isUpToDate = false;
		super.restore();
	}

	@Override
	protected boolean requiresRecalculation() {
		interpreter.isUpToDate = false;
		return true;
	}
	
	@Override
	public List<String> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sample(State state, Random random) {
		// TODO Auto-generated method stub

	}

}
