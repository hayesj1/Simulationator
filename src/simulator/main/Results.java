package simulator.main;

import java.util.Map;

/**
 * Created by Jacob Hayes on 5/3/2017.
 *
 * @author Jacob Hayes
 */
public class Results {
	private Map<String, Object> results;
	private IFormatter formatter;

	public Results() { this(new DefaultFormatter()); }

	public Results(IFormatter formatter) {
		this.formatter = formatter;
	}

	public IFormatter getFormatter() { return formatter; }
	public Map<String, Object> getRawResultSet() { return results; }
	public void setRawResultSet(Map<String, Object> results) { this.results = results; }
	public String getFormattedResults() {
		return formatter.formatResults(results.keySet().toArray(new String[results.size()]), results.values().toArray(new Object[results.size()]));
	}
}
