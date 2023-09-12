package simulator.model;

public abstract class NewRoadEvent extends Event {

	protected String id, srcJun, destJunc;
	protected int length, co2Limit, maxSpeed;
	protected Weather weather;
	protected Junction src, dest;

	public NewRoadEvent() {
		super();
	};

	public NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time);
		this.id = id;
		this.srcJun = srcJun;
		this.destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}

	void execute(RoadMap map) {
		src = map.getJunction(srcJun);
		dest = map.getJunction(destJunc);
		map.addRoad(createRoadObject());
	}

	abstract Road createRoadObject();

}
