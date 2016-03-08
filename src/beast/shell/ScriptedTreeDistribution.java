package beast.shell;



import java.util.ArrayList;
import java.util.List;

import beast.core.Description;
import beast.core.Function;
import beast.core.Input;
import beast.evolution.tree.TreeDistribution;

@Description("Tree distribution model specified using Python "+
		"calculateLogP(tree, intervals) must be specified")
public class ScriptedTreeDistribution extends TreeDistribution {
	public Input<List<Function>> functionInputs = new Input<List<Function>>("x", "function values used by the tree distribution", new ArrayList<Function>()); 
	public Input<String> valueInput = new Input<String>("value", "script specifying the various functions required for a tree distribution" 
		+ "Arguments specified by x-inputs in order they are specified. "
		+ "If an argument is a tree, it is passed as a JSON object. "
		+ "Should contain calculateLogP(tree, treeIntervals)."); 
	
	Interpreter interpreter;
	
	@Override
	public void initAndValidate() {}
	
	public void initAndValidate(String engineName) {
		interpreter = new Interpreter(engineName, valueInput.get(), functionInputs.get(), this.getID());
	}

	@Override
	public double calculateLogP() {
		logP = 0;
		logP = interpreter.evalFunction(functionInputs.get(), "calculateLogP", treeInput.get(), treeIntervalsInput.get());
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
}
