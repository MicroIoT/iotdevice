package top.microiot.client;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import top.microiot.api.device.HttpDeviceSession;
import top.microiot.api.device.WebsocketDeviceSession;
import top.microiot.api.device.stomp.SetRequestSubscriber;
import top.microiot.domain.Device;
import top.microiot.exception.ValueException;

@Component
@Scope("prototype")
public class BikeSet extends SetRequestSubscriber {
	
	@Override
	public void init() {
		addType("locked", Boolean.class);
	}

	@Override
	public void setAttribute(Device device, String attribute, Object value) {
		System.out.println(device.getString() + " " + new Date() + ": set attribute: " + attribute);
		if(attribute.equals("locked")) {
			Boolean s = (Boolean)value;
			System.out.println(new Date() + ": locked: " + s);
			if(s) {
				StateChangedAlarm lock = new StateChangedAlarm(Long.toString(new Date().getTime()), true);
				WebsocketDeviceSession wsession = this.getWebsocketDeviceSession();
				HttpDeviceSession session = wsession.getSession();
				session.reportAlarm("StateChangedAlarm", lock);
			}
			else {
				StateChangedAlarm lock = new StateChangedAlarm(Long.toString(new Date().getTime()), false);
				this.getWebsocketDeviceSession().getSession().reportAlarm("StateChangedAlarm", lock);
			}
		} else {
			throw new ValueException("set unknonw attribute: " + attribute);
		}
	}

}
