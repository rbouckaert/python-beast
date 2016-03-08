package beast.shell;



import java.util.ArrayList;
import java.util.List;

import beast.core.Description;
import beast.core.Function;
import beast.core.Input;
import beast.evolution.datatype.DataType;
import beast.evolution.substitutionmodel.EigenDecomposition;
import beast.evolution.substitutionmodel.SubstitutionModel;
import beast.evolution.tree.Node;


@Description("Substitution model specified using BEASTScript "+
		"getTransitionProbabilities(node, fStartTime, fEndTime, fRate, matrix) must be specified. ")
public class ScriptedSubstitutionModel extends SubstitutionModel.Base {
	public Input<List<Function>> functionInputs = new Input<List<Function>>("x", "function values used by the substitution model", new ArrayList<Function>()); 
	public Input<String> valueInput = new Input<String>("value", "script specifying the various functions required for a substitution model" 
		+ "Arguments specified by x-inputs in order they are specified. "
		+ "If an argument is a tree, it is passed as a JSON object. "
		+ "Should contain getTransitionProbabilities(node, startTime, endTime, rate)."); 

	Interpreter interpreter;
	
	@Override
	public void initAndValidate() {}
	
	public void initAndValidate(String engineName) {
		interpreter = new Interpreter(engineName, valueInput.get(), functionInputs.get(), this.getID());
	}

	@Override
	public void getTransitionProbabilities(Node node, double startTime, double endTime, double rate, double[] matrix) {
		double [] result = interpreter.evalFunctionToArray(functionInputs.get(), "getTransitionProbabilities", node, startTime, endTime, rate);
		System.arraycopy(result, 0, matrix, 0, matrix.length);
	}

	@Override
	public EigenDecomposition getEigenDecomposition(Node node) {
		return null;
	}

	@Override
	public boolean canHandleDataType(DataType dataType) {
		return true;
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
