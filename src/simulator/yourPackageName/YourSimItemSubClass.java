package simulator.yourPackageName;

import simulator.main.SimulatedItem;

/**
 * Created by Jacob Hayes on 5/3/2017.
 *
 * @author Jacob Hayes
 */
public class YourSimItemSubClass extends SimulatedItem {

	// add a field for each initial value

	public YourSimItemSubClass(String[] keys, Object[] initialVals) {
		super(keys, initialVals);
		//Initialize your fields with the initial values
	}

	@Override
	public void simulate() {
		// Do each simulation step
	}

	@Override
	public String generateResults() {
		// Create a nice representation of your fields and their values
		return "Results here!";
	}
}
