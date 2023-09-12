package simulator.model;

import java.util.Collections;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	public MoveAllStrategy() {
	}

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		return Collections.unmodifiableList(q);
	}

}