package simulator.example;

import simulator.main.SimulatedItem;
import simulator.main.ISimulatedItemFactory;

/**
 * Created by Jacob Hayes on 5/3/2017.
 *
 * @author Jacob Hayes
 */
public class ExampleItemFactory implements ISimulatedItemFactory {

	private String[] keys;
	private Object[] values;

	public ExampleItemFactory(String[] keys, Object[] initialValues) {
		this.keys = keys;
		this.values = initialValues;
	}

	@Override
	public SimulatedItem createSimulatedItem() {
		return new Company(keys, values);
	}
}
