package top.microiot.device;

import java.util.Date;
import java.util.List;

import javax.tools.DocumentationTool.Location;

import org.springframework.stereotype.Component;

import top.microiot.api.client.stomp.AlarmSubscriber;
import top.microiot.api.device.WebsocketDeviceSession;
import top.microiot.domain.Device;
import top.microiot.domain.DeviceGroup;
import top.microiot.domain.ManagedObject;

@Component
public class GroupAlarm extends AlarmSubscriber {
	@Override
	public void init() {
		addType("StateChangedAlarm", StateChangedAlarm.class);
	}

	@Override
	public void onAlarm(ManagedObject notifyObject, String alarmType, Object alarmInfo, Date reportTime, Date receiveTime) {
		if (alarmType.equals("StateChangedAlarm")) {
			StateChangedAlarm info = (StateChangedAlarm) alarmInfo;

			System.out.println("bike group received StateChangedAlarm:  locked: " + info.isLocked() + " from child device " + notifyObject.getString() );
			MyGet myGet = new MyGet();
			myGet.setLocked(info.isLocked());
			WebsocketDeviceSession s = (WebsocketDeviceSession) this.getWebsocketClientSession();
			List<DeviceGroup> groups = s.getSession().getMyDeviceGroup();
			
			for(DeviceGroup group : groups) {
				if(group.getName().equals("设备组")) {
					for(Device device : group.getDevices()) {
						if(device.getName().equals("002"))
							s.getAsync(device.getId(), "location", Location.class, myGet);
					}
				}
					
			}
			
		} else
			System.out.println(alarmType + " device: " + notifyObject.getString());
	}

}
