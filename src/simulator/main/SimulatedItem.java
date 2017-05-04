package simulator.main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * An Item to simulate in the simulation. You must subclass this class to use in any simulation.
 * Use {@link ISimulatedItemFactory#createSimulatedItem()} to instantiate
 * your subclass when preparing the simulation.
 *
 * @see ISimulatedItemFactory
 * @see Simulation
 *
 * Created by Jacob Hayes on 4/7/2017.
 *
 * @author Jacob Hayes
 */
public abstract class SimulatedItem {

	static int numItems = 0;
	/** Provided for efficient access to randomness for subclasses */
	protected final Random random = new Random();

	/** The initial values for the simulation */
	private Map<String, Object> initialValues;
	/** The Results; Will be populated when {@link #isCompleted()} is true */
	private String results;
	/** The path to the log file, if any */
	private Path logFile = null;
	/** Identifier for this item*/
	private int itemID;
	/** Time to live countdown */
	protected int ttl;

	/** True if this item is ready to simulate; false otherwise */
	private boolean initialized;
	/** True if this item is active (participating) in the simulation; false otherwise*/
	private boolean steppable;
	/** True if this item has run the course of the simulation and has readied its results; false otherwise */
	private boolean completed;
	/** True if this item has received a request to complete and terminate early; false otherwise */
	private boolean willTerminate;
	/** True if this item has terminated early; false otherwise */
	private boolean terminated;


	public SimulatedItem(String[] keys, Object[] initialVals) {
		initialized = false;
		steppable = true;
		completed = false;
		willTerminate = false;
		terminated = false;

		initialValues = new HashMap<>();
		results = null;
		itemID = ++numItems;
		ttl = -1;

		for (int i = 0; i < keys.length; i++) {
			initialValues.put(keys[i], initialVals[i]);
		}
	}

	/** Perform additional setup prior to simulation */
	public void initialize() { initialize(""); }
	public void initialize(String logFilePath) { this.initialize(logFilePath != null && !logFilePath.isEmpty() ? Paths.get(logFilePath) : null); }
	public void initialize(Path logFile) {
		if (logFile != null) { this.logFile = logFile; }
	}

	/** Stop simulating this item and generate its results */
	public void terminate() {
		willTerminate = true;
		steppable = false;

		results = generateResults();

		terminated = true;
		return;
	}

	/** Simulate one iteration */
	public abstract void simulate();

	/** Called when this item has completed, to generate and return a Results object*/
	public abstract String generateResults();

	/** Optionally called by each call to {@link #simulate()} to log info. Must be overriden to provide any functionality. */
	public void log() {}

	public Map<String, Object> getInitialValues() { return initialValues; }
	public String getResults() { return results; }
	public int getItemID() { return this.itemID; }
	public void setItemID(int id) { this.itemID = id; }

	public Path getLogFile() { return logFile; }
	public void setLogFile(Path logFilePath) { logFile = logFilePath; }
	public void setLogFile(String logFilePath) { setLogFile(logFilePath, (String[]) null); }
	public void setLogFile(String rootDir, String... otherPathElements) { logFile = Paths.get(rootDir, otherPathElements); }

	public boolean isInitialized() { return initialized; }
	public boolean isActive() { return steppable; }
	public boolean isDisabled() {return !steppable; }
	public boolean isCompleted() { return completed; }
	public boolean willTerminate() { return willTerminate; }
	public boolean isTerminated() { return terminated; }
}
