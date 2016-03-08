package beast.python;

import beast.shell.ScriptedRunnable;

public class PythonRunnable extends ScriptedRunnable {

	@Override
	public void initAndValidate() {
		super.initAndValidate("python");
	}
}
