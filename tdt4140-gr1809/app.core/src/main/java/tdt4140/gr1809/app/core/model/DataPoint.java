import java.time.LocalDateTime;

public class DataPoint {
	public LocalDateTime time;
	public int[] values;

	public DataPoint(LocalDateTime time, int[] values) {
		this.time = time;
		this.values = values;
	}
}