package tdt4140.gr1809.app.client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.DataPoint.DataType;

public class DataGeneratorTemperature {
	public UUID userId;
	private LocalDateTime time;
	private DataType dataType;
	
	public DataGeneratorTemperature(UUID userID, LocalDateTime datetime, DataType type) {
		this.userId = userID;
		this.time = datetime;
		this.dataType = type;
	}
	
	public void generateRandomTemperature() {
		int n = 0;
		
		Random rand = new Random();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		numbers.add(100);
		
		for (int i = 1;i<30;i++) {
			int upOrDown = rand.nextInt(2);
			int delta = rand.nextInt(1);
			if (upOrDown == 1) {
				int temp = numbers.get(i-1);
				numbers.add(temp + delta);
			}
			else {
				int temp = numbers.get(i-1);
				numbers.add(temp-delta);
				
			}
		}
		System.out.println(numbers);
		int minutes = rand.nextInt(15);
		for (Integer i: numbers) {
			DataPoint datapoint = DataPoint.builder()
					.userId(userId)
					.time(time.now().minusMinutes(minutes))
					.value(i)
					.dataType(dataType)
					.build();
		}
	}
}
