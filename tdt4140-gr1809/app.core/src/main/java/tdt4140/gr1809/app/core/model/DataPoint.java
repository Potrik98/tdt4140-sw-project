package tdt4140.gr1809.app.core.model;

import java.time.LocalDateTime;

public class DataPoint {
	public final LocalDateTime time;
	public final int[] values;

	public DataPoint(LocalDateTime time, int[] values) {
		this.time = time;
		this.values = values;
	}
}