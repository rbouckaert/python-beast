package beast.python;

import beast.shell.ScriptedClockModel;

public class PythonClockModel extends ScriptedClockModel {

	@Override
	public void initAndValidate() {
		super.initAndValidate("python");
	}
}
