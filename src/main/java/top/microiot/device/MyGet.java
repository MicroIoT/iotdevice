package top.microiot.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import top.microiot.api.client.stomp.GetResponseSubscriber;
import top.microiot.api.device.WebsocketDeviceSession;
import top.microiot.domain.Device;
import top.microiot.domain.attribute.Location;

@Component
public class MyGet extends GetResponseSubscriber {
	@Autowired
	private MySet mySet;
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
			List<Device> devices = s.getSession().getMyChildren();
			
			for(Device d : devices) {
				if(d.getName().equals("002单车"))
				s.setAsync(d.getId(), "locked", locked, mySet);
			}
			
		}
		else if(attribute.equals("locked")) {
			boolean locked = (boolean)value;
			System.out.println("locked: " + locked );
		}
		
	}
}
