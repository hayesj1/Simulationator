package simulator.main;

import simulator.example.CreditRating;
import simulator.example.ExampleItemFactory;

/**
 * Created by Jacob Hayes on 4/6/2017.
 *
 * @author Jacob Hayes
 */
public class Main {
	private static final String logFolder = "/logs/";
	private static final int numIters = 10;

	private static String[] keys = { "TTL", "Name", "CreditRating", "Capital Reserves", "Profit", "Products" }; // Edit the elements to suit your needs
	private static Object[] initialValues = {numIters, "Company#", CreditRating.CCC, 0.0,  0.0, null } ; // Edit the elements to suit your needs

	public static void main(String[] args) {
		// Pass in an instance of YOUR factory if you choose to make a different simulation!!!
		Simulation simulation = new Simulation(3, new ExampleItemFactory(keys, initialValues));

		simulation.initialize(logFolder);

		simulation.startSimulation(numIters);
	}

}
