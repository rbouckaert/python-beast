package beast.python;

import beast.shell.ScriptedSiteModel;

public class PythonSiteModel extends ScriptedSiteModel {

	@Override
	public void initAndValidate() {
		super.initAndValidate("python");
	}
}
