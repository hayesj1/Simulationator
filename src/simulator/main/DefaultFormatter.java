package simulator.main;

import java.util.*;

/**
 * Created by Jacob Hayes on 5/3/2017.
 *
 * @author Jacob Hayes
 */
public class DefaultFormatter implements IFormatter {

	private String colSeparator;
	private String rowSeparator;
	private String lineSeparator;

	public DefaultFormatter() { this(" | ", "-", System.lineSeparator()); }
	public DefaultFormatter(String colSeparator, String rowSeparator, String lineSeparator) {
		this.colSeparator = colSeparator;
		this.rowSeparator = rowSeparator;
		this.lineSeparator = lineSeparator;
	}

	@Override
	public String formatResults(String[] keys, Object[] values) {
		StringBuilder strbldr = new StringBuilder();
		int numResults = Math.min(keys.length, values.length);

		ArrayList<Integer> keyLengths = new ArrayList<>(numResults);
		ArrayList<Integer> valueLengths = new ArrayList<>(numResults);

		for (String key : keys) {
			keyLengths.add(key.length());
		}
		for (Object val : values) {
			valueLengths.add(val.toString().length());
		}

		int colSize = Math.max(Collections.max(keyLengths), Collections.max(valueLengths));
		colSize += (colSize % 2 == 0 ? 0 : 1);

		String[] colHeaders = { "Result Name", "Result Value" };
		for (String header : colHeaders) {
			int padsize = (colSize - header.length()) / 2;
			String padding = (padsize > 0 ? " " : "" );
			if (!padding.isEmpty()) {
				for (int i = padsize ; i > 0; i--) {
					padding += " ";
				}
			}
			header = padding + header + padding;
		}
		String headerRow = colHeaders[0] + colSeparator + colHeaders[1];
		String dividerRow = "";
		for (int i = headerRow.length(); i > 0; i--) {
			dividerRow += rowSeparator;
		}

		for (int i = 0; i < numResults; i++) {
			strbldr.append(keys[i]).append(" : ");
			strbldr.append(values[i]).append(lineSeparator);
		}
		strbldr.append(lineSeparator);
		return strbldr.toString();
	}
}
