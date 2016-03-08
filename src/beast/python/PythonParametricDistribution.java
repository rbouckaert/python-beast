package beast.python;

import beast.shell.ScriptedParametricDistribution;

public class PythonParametricDistribution extends ScriptedParametricDistribution {

	@Override
	public void initAndValidate() {
		super.initAndValidate("python");
	}
}
