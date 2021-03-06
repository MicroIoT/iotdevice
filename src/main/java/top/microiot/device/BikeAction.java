package top.microiot.device;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import top.microiot.api.device.stomp.ActionRequestSubscriber;
import top.microiot.domain.Device;
import top.microiot.domain.User;
import top.microiot.domain.attribute.Location;
import top.microiot.exception.ValueException;

@Component
@Scope("prototype")
public class BikeAction extends ActionRequestSubscriber {
	
	@Override
	public void init() {
		addType("getHistory", Filter.class);
	}

	@Override
	public Object action(User requester, Device device, String action, Object request) {
		String user = null;
		if(requester.isDevice())
			user = "device";
		else
			user = requester.getUsername();
		System.out.println(device.getString() + " " + new Date() + ":  action: " + action + " from: " + user);
		if(action.equals("getHistory")) {
			Filter filter = (Filter)request;
			
			List<Record> records = new ArrayList<Record>();
			for(int i = 0; i < 10; i ++)
				records.add(getRecord(filter));
			
			return records;
		}else {
			throw new ValueException("unknonw action: " + action);
		}
	}

	private Record getRecord(Filter filter) {
		Random r = new Random();
		int i = r.nextInt(10000000);
		Date start = new Date();
		start.setTime(start.getTime() + i);

		int j = r.nextInt(10000000);
		Date end = new Date();
		end.setTime(end.getTime() + j);

		Random l = new Random();
		double x = 180 * l.nextDouble();
		double y = 90 * l.nextDouble();
		Location location = new Location(x, y);
		return new Record(location, start, location, end);
	}

}
