package top.microiot.device;

import java.util.Date;
import java.util.Random;

import top.microiot.api.device.stomp.GetRequestSubscriber;
import top.microiot.domain.Device;
import top.microiot.domain.User;
import top.microiot.domain.attribute.Location;
import top.microiot.exception.ValueException;

public class BikeGet extends GetRequestSubscriber {

	@Override
	public Object getAttributeValue(User requester, Device device, String attribute) {
		String user = null;
		if(requester.isDevice())
			user = "device";
		else
			user = requester.getUsername();
		System.out.println(device.getString() + " " + new Date() + ": get attribute: " + attribute + " from: " + user);
		if(attribute.equals("location")) {
			Random r = new Random();
			double x = 180 * r.nextDouble();
			double y = 90 * r.nextDouble();
			Location location = new Location(x, y);
			return location;
		}else if(attribute.equals("locked")) {
			Random r = new Random();
			boolean locked = r.nextBoolean();
			return locked;
		}else {
			throw new ValueException("get unknow attribute: " + attribute);
		}
	}

}
