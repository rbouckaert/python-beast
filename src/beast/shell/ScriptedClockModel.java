package beast.shell;

import java.util.ArrayList;
import java.util.List;

import beast.core.Description;
import beast.core.Function;
import beast.core.Input;
import beast.evolution.branchratemodel.BranchRateModel.Base;
import beast.evolution.tree.Node;

@Description("Branch rate model specified using BEASTScript "+
		"getRateForBranch(node, meanrate) must be specified. "
		+ "")
public class ScriptedClockModel extends Base {
	final public Input<List<Function>> functionInputs = new Input<>("x", "function values used by the clock model", new ArrayList<>()); 
	final public Input<String> valueInput = new Input<>("value", "Script specifying the various functions required for a clock model."
			+ "Arguments specified by x-inputs in order they are specified. "
			+ "If an argument is a tree, it is passed as a JSON object. "
			+ "Should return an array with a rate for each branch."); 
	Interpreter interpreter;


	@Override
	public void initAndValidate() {}
	
	public void initAndValidate(String engineName) {
		interpreter = new Interpreter(engineName, valueInput.get(), functionInputs.get(), this.getID());
	}

	@Override
	public double getRateForBranch(Node node) {
		return interpreter.getValue(node.getNr());
	}

	
	@Override
	protected void store() {
		interpreter.isUpToDate = false;
		super.store();
	}

	@Override
	protected void restore() {
		interpreter.isUpToDate = false;
		super.restore();
	}

	@Override
	protected boolean requiresRecalculation() {
		interpreter.isUpToDate = false;
		return true;
	}
	
	
	
	
}
