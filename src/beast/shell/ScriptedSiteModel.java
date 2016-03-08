package beast.shell;



import java.util.ArrayList;
import java.util.List;

import beast.core.Description;
import beast.core.Function;
import beast.core.Input;
import beast.evolution.sitemodel.SiteModelInterface;
import beast.evolution.tree.Node;

@Description("Sitemodel specified using BEASTScript "+
		"getCategoryCount(), getCategoryOfSite(site, node), getRateForCategory(category, node), getCategoryRates(node), getProportionForCategory(category, node), getCategoryProportions(node)  must be specified. ")
public class ScriptedSiteModel extends SiteModelInterface.Base {
	public Input<List<Function>> functionInputs = new Input<List<Function>>("x", "function values used by the site model", new ArrayList<Function>()); 
	public Input<String> valueInput = new Input<String>("value", "script specifying the various functions required for a site model. " 
		+ "Arguments specified by x-inputs in order they are specified. "
		+ "If an argument is a tree, it is passed as a JSON object. "
		+ "Should contain getCategoryCount(), getCategoryOfSite(site, node), getRateForCategory(category, node), "
		+ "getProportionForCategory(category, node), getCategoryRates(node) and getCategoryProportions(node)."); 

	Interpreter interpreter;

	@Override
	public void initAndValidate() {}
	
	public void initAndValidate(String engineName) {
		interpreter = new Interpreter(engineName, valueInput.get(), functionInputs.get(), this.getID());
	}

	@Override
	public boolean integrateAcrossCategories() {
		return false;
	}

	@Override
	public int getCategoryCount() {
		return (int) interpreter.evalFunction(functionInputs.get(), "getCategoryCount");
	}

	@Override
	public int getCategoryOfSite(int site, Node node) {
		return (int) interpreter.evalFunction(functionInputs.get(), "getCategoryOfSite", site, node);
	}

	@Override
	public double getRateForCategory(int category, Node node) {
		return interpreter.evalFunction(functionInputs.get(), "getRateForCategory", category, node);
	}

	@Override
	public double[] getCategoryRates(Node node) {
		Object o = interpreter.evalFunction(functionInputs.get(), "getCategoryRates", node);
		if (o.getClass().isArray()) {
			return (double[]) o;
		} else if (o instanceof List) {
			double [] result = new double[((List<?>)o).size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = ((Number)((List<?>)o).get(i)).doubleValue();
			}
			return result;
		} else {
			throw new RuntimeException("getCategoryProportions failed: expected a number as result");
		}
	}
	
	@Override
	public double getProportionForCategory(int category, Node node) {
		return interpreter.evalFunction(functionInputs.get(), "getProportionForCategory", category, node);
	}

	@Override
	public double[] getCategoryProportions(Node node) {
		Object o = interpreter.evalFunction(functionInputs.get(), "getCategoryProportions", node);
		if (o.getClass().isArray()) {
			return (double[]) o;
		} else if (o instanceof List) {
			double [] result = new double[((List<?>)o).size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = ((Number)((List<?>)o).get(i)).doubleValue();
			}
			return result;
		} else {
			throw new RuntimeException("getCategoryProportions failed: expected a number as result");
		}
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
