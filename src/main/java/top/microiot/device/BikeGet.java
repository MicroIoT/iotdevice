package top.microiot.device;

import java.util.Date;
import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import top.microiot.api.device.stomp.GetRequestSubscriber;
import top.microiot.domain.Device;
import top.microiot.domain.attribute.Location;
import top.microiot.exception.ValueException;

@Component
@Scope("prototype")
public class BikeGet extends GetRequestSubscriber {

	@Override
	public Object getAttributeValue(Device device, String attribute) {
		System.out.println(device.getString() + " " + new Date() + ": get attribute: " + attribute);
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
