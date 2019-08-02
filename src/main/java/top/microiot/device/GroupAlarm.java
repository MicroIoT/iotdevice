package top.microiot.device;

import java.util.Date;
import java.util.List;

import javax.tools.DocumentationTool.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import top.microiot.api.client.stomp.AlarmSubscriber;
import top.microiot.api.device.WebsocketDeviceSession;
import top.microiot.domain.Device;
import top.microiot.domain.NotifyObject;

@Component
public class GroupAlarm extends AlarmSubscriber {
	@Autowired
	private MyGet myGet;
	
	@Override
	public void init() {
		addType("StateChangedAlarm", StateChangedAlarm.class);
	}

	@Override
	public void onAlarm(NotifyObject notifyObject, String alarmType, Object alarmInfo, Date reportTime, Date receiveTime) {
		if (alarmType.equals("StateChangedAlarm")) {
			StateChangedAlarm info = (StateChangedAlarm) alarmInfo;

			System.out.println("bike group received StateChangedAlarm: sessionid is " + info.getSessionid() + " locked: " + info.isLocked() + " from child device " + notifyObject.getString() );
			myGet.setLocked(info.isLocked());
			WebsocketDeviceSession s = (WebsocketDeviceSession) this.getWebsocketClientSession();
			List<Device> devices = s.getSession().getMyChildren();
			
			for(Device device : devices) {
				if(device.getDeviceName().equals("002单车"))
					s.getAsync(device.getId(), "location", Location.class, myGet);
			}
			
		} else
			System.out.println(alarmType + " device: " + notifyObject.getString());
	}

}
