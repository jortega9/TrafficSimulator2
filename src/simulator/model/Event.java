package simulator.model;

public abstract class Event implements Comparable<Event> {

	protected int _time;

	Event() {
	};

	Event(int time) throws IllegalArgumentException {
		if (time < 1)
			throw new IllegalArgumentException("Time must be positive (" + time + ")");
		_time = time;
	}

	public int getTime() {
		return _time;
	}

	public int compareTo(Event o) {
		if (o._time < _time) {
			return 1;
		} else if (o._time > _time) {
			return -1;
		} else {
			return 0;
		}
	}

	abstract void execute(RoadMap map);
}
