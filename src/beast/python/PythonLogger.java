package beast.python;

import beast.shell.ScriptedLogger;

public class PythonLogger extends ScriptedLogger {

	@Override
	public void initAndValidate() {
		super.initAndValidate("python");
	}
}
