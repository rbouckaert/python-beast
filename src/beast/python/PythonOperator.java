package beast.python;

import beast.shell.ScriptedOperator;

public class PythonOperator extends ScriptedOperator {

	@Override
	public void initAndValidate() {
		super.initAndValidate("python");
	}
}
