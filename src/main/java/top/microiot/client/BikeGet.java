package top.microiot.client;

import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import top.microiot.api.device.stomp.GetSubscriber;
import top.microiot.domain.attribute.Location;
import top.microiot.exception.ValueException;

@Component
public class BikeGet extends GetSubscriber {

	@Override
	public Object getAttributeValue(String attribute) {
		System.out.println(new Date() + ": get attribute: " + attribute);
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
