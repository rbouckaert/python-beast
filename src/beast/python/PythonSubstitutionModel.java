package beast.python;

import beast.shell.ScriptedSubstitutionModel;

public class PythonSubstitutionModel extends ScriptedSubstitutionModel {

	@Override
	public void initAndValidate() {
		super.initAndValidate("python");
	}
}
