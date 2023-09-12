package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
	
	private Factory<LightSwitchingStrategy> _lssFactory;
	private Factory<DequeuingStrategy> _dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		_lssFactory = lssFactory;
		_dqsFactory = dqsFactory;
	}


	protected Event createTheInstance(JSONObject data) {
		int time, x, y;
		JSONArray coor;
		String id;
		
		LightSwitchingStrategy ls = _lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dq = _dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
		
		time = data.getInt("time");
		id = data.getString("id");
		coor = data.getJSONArray("coor"); x = coor.getInt(0); y = coor.getInt(1);
		
		NewJunctionEvent je = new NewJunctionEvent(time, id, ls, dq, x, y);
		return je;
	}

}
