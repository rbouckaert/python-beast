package beast.python;

import beast.shell.ScriptedDistribution;

public class PythonDistribution extends ScriptedDistribution {

	@Override
	public void initAndValidate() {
		super.initAndValidate("python");
	}
}
