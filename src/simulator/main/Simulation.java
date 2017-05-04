package simulator.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Jacob Hayes on 5/3/2017.
 *
 * @author Jacob Hayes
 */
public class Simulation {

	private ArrayList<SimulatedItem> items;
	private ISimulatedItemFactory factory;

	public Simulation(ArrayList<SimulatedItem> items) { this.items = items; }
	public Simulation(int initialNumItems, ISimulatedItemFactory fctry) {
		items = new ArrayList<>(initialNumItems);
		factory = fctry;

		for (int i = 0; i < initialNumItems; i++) { items.add(factory.createSimulatedItem()); }
	}

	public void initialize(String logFolder) {
		boolean created;
		File logDir = Paths.get(logFolder).toFile();
		created = logDir.isDirectory() || logDir.mkdir();

		final Path logPath = (created ? logDir.toPath() : null);
		// Initialize each item
		items.forEach(item -> item.initialize(logPath));
	}

	public void startSimulation(int iters) {
		// Do simulation
		for (int i = 0; i < iters; i++) {
			items.forEach(SimulatedItem::simulate);
		}
		try (FileWriter fw = new FileWriter("logs/SimulationOutput.log")) {
			// Print Results to screen and a Text file
			items.stream().filter(item -> item.isCompleted() || item.isTerminated())
				.forEach(item -> {
						String res = item.generateResults();
					try { fw.append(res); } catch (IOException ioe) { System.err.println(ioe.toString()); }
					System.out.println(res);
				});
		} catch (IOException ioe) { System.err.println(ioe.toString()); }
	}

	public ArrayList<SimulatedItem> getItems() { return items; }
}
