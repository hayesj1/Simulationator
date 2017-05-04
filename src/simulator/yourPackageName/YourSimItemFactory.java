package simulator.yourPackageName;

import simulator.main.ISimulatedItemFactory;
import simulator.main.SimulatedItem;

/**
 * Created by Jacob Hayes on 5/3/2017.
 *
 * @author Jacob Hayes
 */
public class YourSimItemFactory implements ISimulatedItemFactory{

	private String[] keys;
	private Object[] values;

	public YourSimItemFactory(String[] keys, Object[] initialValues) {
		this.keys = keys;
		this.values = initialValues;
	}

	@Override
	public SimulatedItem createSimulatedItem() {
		return new YourSimItemSubClass(keys, values);
	}
}
