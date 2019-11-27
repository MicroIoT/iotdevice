package top.microiot.device;

import java.util.List;

import top.microiot.api.client.stomp.GetResponseSubscriber;
import top.microiot.api.device.WebsocketDeviceSession;
import top.microiot.domain.Device;
import top.microiot.domain.DeviceGroup;
import top.microiot.domain.attribute.Location;

public class MyGet extends GetResponseSubscriber {
	private boolean locked;
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public void onGetError(Device device, String attribute, String error) {
		System.out.println(device.getString() + " attribute[" + attribute + "] error:" + error);
	}

	@Override
	public void onGetResult(Device device, String attribute, Object value) {
		System.out.println(device.getString() + " attribute[" + attribute + "]:");
		if(attribute.equals("location")) {
			Location location = (Location)value;
			System.out.println(location.getLongitude() + ":" + location.getLatitude());

			WebsocketDeviceSession s = (WebsocketDeviceSession) this.getWebsocketClientSession();
			
			List<DeviceGroup> groups = s.getSession().getMyDeviceGroup();
			
			for(DeviceGroup group : groups) {
				if(group.getName().equals("设备组")) {
					for(Device d : group.getDevices()) {
						if(d.getName().equals("002"))
							s.setAsync(d.getId(), "locked", locked, new MySet());
					}
				}
					
			}
		}
		else if(attribute.equals("locked")) {
			boolean locked = (boolean)value;
			System.out.println("locked: " + locked );
		}
		
	}
}
