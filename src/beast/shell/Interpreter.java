package beast.shell;




import java.io.PrintStream;
import java.util.List;

import javax.script.*;

import beast.core.*;
import beast.core.util.*;
import beast.evolution.tree.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

@Description("Interpreter for Python-BEAST interoperation")
public class Interpreter implements Loggable {

    ScriptEngine engine;
    Invocable inv;

    boolean isUpToDate = false;
    boolean isScript = true; // otherwise it is an expression
    double [] value;
    String stringValue;
    
    String id;
    List<Function> arguments;
    
    public Interpreter(String engineName, String script, List<Function> arguments, String id) {
    	this.id = id;
    	this.arguments = arguments;
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a script engine
        engine = factory.getEngineByName(engineName);
        value = new double[1];
        value[0] = Double.NaN;

        try {
        	engine.eval(script);
		} catch (ScriptException e) {
			throw new IllegalArgumentException(e);
		}
        //initEngine();
        inv = (Invocable) engine;
        
    }

    private void calc(List<Function> arguments, String functionName, Object ... moreArgs) {
        //Bindings bind = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        Object [] args = new Object[arguments.size() + moreArgs.length];
        int k = 0;
        for (beast.core.Function f : arguments) {
            //String name = var.nameInput.get();
            Object _value = null;
            //beast.core.Function f = var.functionInput.get();
            if (f instanceof Tree) {
                Tree tree = (Tree) f;
                String treeString = toJSON(tree.getRoot());
                _value = "{" + treeString + "}";
            } else if (f.getDimension() > 1) {
                Double [] a = new Double[f.getDimension()];
                for (int i = 0;i < f.getDimension(); i++) {
                    a[i] = f.getArrayValue(i);
                }
                _value = a;
            } else {
                _value = f.getArrayValue();
            }
            args[k++] = _value;
            //bind.put(name, _value);
        }
        for (Object o : moreArgs) {
            Object _value = null;
            if (o instanceof Tree) {
                Tree tree = (Tree) o;
                String treeString = toJSON(tree.getRoot());
                _value = "{" + treeString + "}";
            } else if (o instanceof Function) {
            	Function f = (Function)o;
            	if (f.getDimension() > 1) {
                    Double [] a = new Double[f.getDimension()];
                    for (int i = 0;i < f.getDimension(); i++) {
                        a[i] = f.getArrayValue(i);
                    }
                    _value = a;
                } else {
                    _value = f.getArrayValue();
                }
            } else {
            	_value = o;
            }
            args[k++] = _value;
        }
        
        Object o = null;
    	this.stringValue = null;
        try {
       		o = inv.invokeFunction(functionName, args);
       		if (o != null) {
       			if (o instanceof ScriptObjectMirror) {
       				ScriptObjectMirror m = (ScriptObjectMirror) o;
       				if (value.length != m.values().size()) {
       					value = new double[m.values().size()];
       				}
       				int i = 0;
       				for (Object o2 : m.values()) {
           				this.value[i++] = Double.parseDouble(o2.toString());       					
       				}
       			} else {
	       			try {
	       				this.value[0] = Double.parseDouble(o.toString());
	       			} catch (NumberFormatException e) {
	       	            this.value[0] = Double.NaN;
	       			}
	   	            this.stringValue = o.toString();
       			}
       		}
        } catch (NoSuchMethodException | ScriptException e) {
            this.value[0] = Double.NaN;
        	this.stringValue = null;
            Log.err.println(e.getMessage());
        }
        isUpToDate = true;
    }

    private String toJSON(Node node) {
        StringBuilder bf = new StringBuilder();
        bf.append("height: " + node.getHeight());
        bf.append(",nr:" + node.getNr());
        if (!node.isLeaf()) {
            bf.append(",children:{");
            for (int i = 0; i < node.getChildCount(); i++) {
                bf.append(toJSON(node.getChild(i)));
                if (i < node.getChildCount() - 1) {
                    bf.append(",");
                }
            }
            bf.append("}");
        } else {
            //bf.append(",id:" + node.getID());
        }
        return bf.toString();
    }

    public double getValue(Object ...moreArgs) {
        if (!isUpToDate) {
            calc(arguments, "f", moreArgs);
        }
        return value[0];
    }

    public double getArrayValue(int iDim, Object ... moreArgs) {
        if (!isUpToDate) {
            calc(arguments, "f", moreArgs);
        }
        return value[iDim];
    }

    public int getDimension() {return value.length;}


    protected void store() {
        isUpToDate = false;
    }

    protected void restore() {
        isUpToDate = false;
    }

    protected boolean requiresRecalculation() {
        // we only get here if at least one input has changed, so always recalculate
        isUpToDate = false;
        return true;
    }


    // Loggable implementation
    public void init(final PrintStream out) {
        if (value.length == 1)
            out.print(this.id + "\t");
        else
            for (int i = 0; i < value.length; i++)
                out.print(this.id + "_" + i + "\t");
    }

    public void log(final int nSample, final PrintStream out) {
    	isUpToDate = false;
        for (int i = 0; i < value.length; i++)
            out.print(getArrayValue(i) + "\t");
    }

    public void close(final PrintStream out) {
        // nothing to do
    }




	public double evalFunction(List<Function> list, String functionName, Object ... args) {
		calc(list, functionName, args);
		return value[0];
	}
    
	public double [] evalFunctionToArray(List<Function> list, String functionName, Object ... args) {
		calc(list, functionName, args);
        isUpToDate = true;
		return value.clone();
	}

	public String evalStringFunction(List<Function> list, String functionName, Object ... args) {
		calc(list, functionName, args);
		return stringValue;
	}
	
	public void evalVoidFunction(List<Function> list, String functionName, Object ... args) {
		calc(list, functionName, args);
	}
}
