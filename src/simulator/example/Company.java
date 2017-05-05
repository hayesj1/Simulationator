package simulator.example;

import simulator.main.Results;
import simulator.main.SimulatedItem;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jacob Hayes on 5/3/2017.
 *
 * @author Jacob Hayes
 */
public class Company extends SimulatedItem {

	private static int numCompanies = 0;
	private static ArrayList<String> productNames = new ArrayList<>(30);
	private static Results results = new Results();

	private String name;
	private CreditRating creditRating;
	private double capitalReserves;
	private double profit;
	private HashMap<String, Double> products;

	private int strictlyProfitableForIters;
	private int ratedForIters;

	public Company(String[] keys, Object[] initialValues) {
		super(keys, initialValues);
		ttl = (Integer) initialValues[0];
		name = initialValues[1] + String.valueOf(++numCompanies);
		creditRating = (CreditRating) initialValues[2];
		capitalReserves = (Double) initialValues[3];
		profit = (Double) initialValues[4];
		products = new HashMap<>();

		strictlyProfitableForIters = 0;
		ratedForIters = 0;
	}

	static {
		productNames.add("Bob's Building Blocks");
		productNames.add("Fred's Fishing Kit");
		productNames.add("Anakin's Younging-Slayer");
		productNames.add("Yoda's Slimesaber");
		productNames.add("Vader's Bloody Laser of Blood");
		productNames.add("New-Age Tonka Truck: Now with Annoying Audio!");
		productNames.add("Old-School Tonka Truck: Now with More Manlyness!");
		productNames.add("Bao Bing Jing's REAL Viking Axe");
		productNames.add("Copper Infused Spatula & Laddle Set");
		productNames.add("Non-Stick Supreme Professional Frying Pan");
		productNames.add("Genuine Acme A-Bomb: Guaranteed to Annihilate Pesky RoadRunners!");
		productNames.add("Genuine Acme H-Bomb: For Coyotes Ages 10 and Up!");
		productNames.add("Sing-Along Farm Books with Animal Soundboard");
		productNames.add("Thomas & Friends: Plutona La Radia -- Sodor's First Nuclear Locomotive");
		productNames.add("Crayola Crayons: 250 Crayon Set");
		productNames.add("Hamma Time Hammer!");
		productNames.add("Rainbow Flex-Seal");
		productNames.add("Semi-Automatic Cap Gun");
		productNames.add("Lawn Darts 2.0");
		productNames.add("Frisbee: 3 Pack");
		productNames.add("Regulation Football Ball - Signed by a 'REAL' FIFA Boardmember");
		productNames.add("NFL Grade Football - Signed by 'Tom Brady'");
		productNames.add("Mrs. Builder Housewife Doll!");
		productNames.add("Bob's Laundry *Requires Mrs. Builder Doll for optimal fun*");
		productNames.add("Bob's Dishes *Requires Mrs. Builder Doll for optimal fun*");
		productNames.add("Mrs. Builder's Kitchen *Requires Mrs. Builder Doll for optimal fun*");
		productNames.add("Bob's Construction Site *Requires Bob's Building Blocks for optimal fun*");
	}

	@Override
	public void initialize(String logFilePath) {
		super.initialize(Paths.get(logFilePath, "Item" + this.getItemID() + ".log"));
		this.initialize();
	}

	@Override
	public void initialize(Path logFile) {
		super.initialize(Paths.get(logFile.toString(), "item" + this.getItemID() + ".log"));
		this.initialize();
	}

	@Override
	public void initialize() {
		capitalReserves += random.nextDouble() * 100000 - random.nextDouble() * 10;
		profit += random.nextDouble() * 100;

		strictlyProfitableForIters = (int) Math.max(0.0, Math.floor(random.nextDouble() * 10) - 4);
		ratedForIters = (int) Math.max(0.0, Math.floor(random.nextDouble() * 10) - 4);
	}

	@Override
	public void simulate() {
		double rand = Math.floor(random.nextDouble() * 10);
		double aggProfit;
		double prevProfit = this.profit;
		CreditRating prevRating = this.creditRating;

		if (rand > 6) { createNewProduct(rand); }
		aggProfit = computeAggregateProductProfit();

		computeProductProfit(rand);
		computeProfit(prevProfit, aggProfit, rand);
		computeCreditRating(prevRating, prevProfit, rand);

		this.capitalReserves += this.profit;
		this.ttl--;

		if (this.ttl == 0) {
			this.completed = true;
			this.terminate();
		}
	}

	private void computeProductProfit(double rand) {
		for (Double profit : products.values()) {
			int randSign =((int) rand < 5 ? -1 : 1);
			profit *= rand * randSign;
		}
	}

	private void computeProfit(double prevProfit, double aggregateProductProfit, double rand) {
		long prevProfitMag = (long) magnitude(prevProfit);
		double randomness = rand * Math.pow(10, Math.floor(prevProfitMag - (prevProfitMag * 0.333)));
		this.profit = aggregateProductProfit + randomness;
	}

	private void computeCreditRating(CreditRating prevRating, double prevProfit, double rand) {
		double profitDelta = this.profit - prevProfit;
		double magDelta = magnitude(this.profit) - magnitude(prevProfit);
		int profitDeltaSgn = (int) Math.signum(profitDelta);
		int magDeltaSgn = (int) Math.signum(magDelta);
		double magRatio = magnitude(this.profit) / magDelta;
		double minMagRatio = computeMinMagnitudeRatio(prevRating, magDeltaSgn);

		if (profitDeltaSgn < 0) { // Profits decreasing
			if (magRatio < minMagRatio && ratedForIters > 4 && rand > 3) { this.creditRating = this.creditRating.decrementRating(); }
		} else if (profitDeltaSgn > 0) { // Profits increasing
			if (magRatio > minMagRatio && ratedForIters > 3 && rand > 4) { this.creditRating = this.creditRating.incrementRating(); }
		} else { // Profits unchanging
			if (magRatio < minMagRatio && ratedForIters > 4 && rand > 5) { this.creditRating = this.creditRating.decrementRating(); }
		}

		if (this.creditRating.equalTo(prevRating)) { this.ratedForIters++; }
		else { this.ratedForIters = 0; }

		if (profitDeltaSgn >= 0) { this.strictlyProfitableForIters++; }
		else { strictlyProfitableForIters = 0; }
	}

	private void createNewProduct(double rand) {
		String name = productNames.get(random.nextInt(productNames.size()-1));
		products.put(name, (rand > 6 ? rand*10 : 0.0));
		productNames.remove(name);
	}

	private double computeAggregateProductProfit() {
		double accum = 0.0;
		for (Double profit : products.values()) { accum += profit; }

		return accum;
	}

	private double computeMinMagnitudeRatio(CreditRating prevRating, double magDeltaSgn) {
		double ret;

		if (prevRating.equalTo(CreditRating.getFailureBenchMark())) {
			ret = 1.000;
		} else if (prevRating.equalTo(CreditRating.getPerfectBenchMark())) {
			ret = 0.000;
		} else if (prevRating.lessThan(CreditRating.getPoorBenchMark())){
			ret = 0.250;
		} else if (prevRating.lessThan(CreditRating.getGoodBenchMark())) {
			ret = 0.333;
		} else if (prevRating.lessThan(CreditRating.getExcellentBenchMark())) {
			ret = 0.405;
		} else {
			ret = 0.550;
		}

		return ret * ((magDeltaSgn < 0) ? 1.050 : (magDeltaSgn > 0 ? 1.005 : 1.010));
	}

	private double magnitude(double value) {
		if (value < 10 && value > -10) {
			return 1;
		}
		return Math.floor(Math.log10(value));
	}

	@Override
	public String generateResults() {
		NumberFormat frmt = NumberFormat.getCurrencyInstance();

		HashMap<String, Object> state = new HashMap<>(this.getInitialValues());

		state.replace("Name", name);
		state.replace("CreditRating", creditRating);
		state.replace("Capital Reserves", frmt.format(capitalReserves));
		state.replace("Profit", frmt.format(profit));
		state.replace("TTL", ttl);
		StringBuilder productStr = new StringBuilder();
		for (Map.Entry<String, Double> val : products.entrySet()) {
			productStr.append(val.getKey()).append(" = ").append(frmt.format(val.getValue())).append(" || ");
		}
		state.replace("Products", productStr.toString());

		results.setRawResultSet(state);
		return results.getFormattedResults();
	}
}
