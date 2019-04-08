package com.leanit.client;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.leaniot.api.device.stomp.SetSubscriber;
import com.leaniot.exception.ValueException;

@Component
public class BikeSet extends SetSubscriber {
	@Override
	public void setAttribute(String attribute, Object value) {
		System.out.println(new Date() + ": set attribute: " + attribute);
		if(attribute.equals("locked")) {
			Boolean s = (Boolean)value;
			System.out.println(new Date() + ": locked: " + s);
			if(s) {
				StateChangedAlarm lock = new StateChangedAlarm(Long.toString(new Date().getTime()), true);
				this.getWebsocketDeviceSession().getSession().reportAlarm("StateChangedAlarm", lock);
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
