package top.microiot.device;

import java.util.Date;
import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import top.microiot.api.device.stomp.SetRequestSubscriber;
import top.microiot.domain.Device;
import top.microiot.domain.User;
import top.microiot.domain.attribute.Location;
import top.microiot.exception.ValueException;

@Component
@Scope("prototype")
public class BikeSet extends SetRequestSubscriber {
	
	@Override
	public void init() {
		addType("locked", Boolean.class);
	}

	@Override
	public void setAttribute(User requester, Device device, String attribute, Object value) {
		String user = null;
		if(requester.isDevice())
			user = "device";
		else
			user = requester.getUsername();
		
		System.out.println(device.getString() + " " + new Date() + ": set attribute: " + attribute + " from: " + user);
		if(attribute.equals("locked")) {
			Boolean s = (Boolean)value;
			System.out.println(new Date() + ": locked: " + s);
			Random l = new Random();
			double x = 180 * l.nextDouble();
			double y = 90 * l.nextDouble();
			Location location = new Location(x, y);
			if(s) {
				StateChangedAlarm lock = new StateChangedAlarm(location, true);
				this.getWebsocketDeviceSession().getSession().reportAlarm("StateChangedAlarm", lock);
			}
			else {
				StateChangedAlarm lock = new StateChangedAlarm(location, false);
				this.getWebsocketDeviceSession().getSession().reportAlarm("StateChangedAlarm", lock);
			}
		} else {
			throw new ValueException("set unknonw attribute: " + attribute);
		}
	}

}
