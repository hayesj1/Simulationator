package simulator.example;

/**
 * Created by Jacob Hayes on 5/3/2017.
 *
 * @author Jacob Hayes
 */
public enum CreditRating {
	F, C, CC, CCC, B, BB, BBB, A, AA, AAA;

	public static CreditRating getFailureBenchMark() { return F; }
	public static CreditRating getPoorBenchMark() { return CC; }
	public static CreditRating getGoodBenchMark() { return BB; }
	public static CreditRating getExcellentBenchMark() { return AA; }
	public static CreditRating getPerfectBenchMark() { return AAA; }

	public boolean lessThan(CreditRating other) { return this.ordinal() < other.ordinal(); }
	public boolean greaterThan(CreditRating other) { return this.ordinal() > other.ordinal(); }
	public boolean equalTo(CreditRating other) { return this.ordinal() == other.ordinal(); }

	public CreditRating decrementRating() {
		int idx = this.ordinal();
		return CreditRating.values()[idx + deltaWithBoundsCheck(false, idx)];
	}

	public CreditRating incrementRating() {
		int idx = this.ordinal();
		return CreditRating.values()[idx + deltaWithBoundsCheck(true, idx)];
	}

	private int deltaWithBoundsCheck(boolean upperBound, int val) {
		if (upperBound) {
			return (val < CreditRating.values().length-1 ? 1 : 0);
		} else {
			return (val > 0 ? -1 : 0);
		}
	}
}
