package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {

	private TrafficSimulator ts;

	private Factory<Event> ef;

	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if (sim.equals(null))
			throw new IllegalArgumentException("Traffic Simulator with null value in controller");
		if (eventsFactory.equals(null))
			throw new IllegalArgumentException("Events Factory with null value in controller");
		ts = sim;
		ef = eventsFactory;
	}

	public void reset() {
		ts.reset();
	}

	public void loadEvents(InputStream in) throws IOException {
		try {

			JSONObject jo = new JSONObject(new JSONTokener(in));

			if (!jo.has("events"))
				throw new IllegalArgumentException("Incorrect Input Stream");

			JSONArray events = jo.getJSONArray("events");

			for (int i = 0; i < events.length(); i++) {
				JSONObject e = events.getJSONObject(i);
				ts.addEvent(ef.createInstance(e));
			}

			in.close();
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("Error in the in JSON File: " + e.getMessage());
		}
	}

	public void run(int n, OutputStream out) {

		if (out == null) {
			out = new OutputStream() {
				public void write(int b) throws IOException {
				}
			};
		}

		PrintStream print = new PrintStream(out);
		print.println("{");
		print.println("  " + "\"states\": [");

		for (int i = 0; i < n; i++) {
			ts.advance();
			print.print(ts.report());
			if (i < n - 1)
				print.println(",");

		}

		print.println("\n]");
		print.println("}");

	}

	public void run(int n) {
		for (int i = 0; i < n; i++) {
			ts.advance();
		}

	}

	public void addObserver(TrafficSimObserver o) {
		ts.addObserver(o);
	}

	public void removeObserver(TrafficSimObserver o) {
		ts.removeObserver(o);
	}

	public void addEvent(Event e) {
		ts.addEvent(e);
	}

	public void addUsersEvent(Event e) {
		ts.addEvent(e);
	}

}
