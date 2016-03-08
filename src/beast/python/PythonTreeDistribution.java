package beast.python;

import beast.shell.ScriptedTreeDistribution;

public class PythonTreeDistribution extends ScriptedTreeDistribution {

	@Override
	public void initAndValidate() {
		super.initAndValidate("python");
	}
}
