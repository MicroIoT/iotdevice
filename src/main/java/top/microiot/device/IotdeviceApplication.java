package top.microiot.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import top.microiot.api.device.IoTDevice;
import top.microiot.api.device.WebsocketDeviceSession;
import top.microiot.domain.Device;
import top.microiot.domain.DeviceGroup;
import top.microiot.domain.attribute.Location;

@SpringBootApplication(exclude={MongoAutoConfiguration.class})
public class IotdeviceApplication implements CommandLineRunner {
	@Autowired
	@Qualifier("websocketDeviceSession")
	private WebsocketDeviceSession wsession;
	@Autowired
	@Qualifier("bikeWebsocketDeviceSession")
	private WebsocketDeviceSession wsession1;
	@Autowired
	@Qualifier("bikeGroupWebsocketDeviceSession")
	private WebsocketDeviceSession wsession2;
	
	@Autowired
	private BikeGet myGet;
	@Autowired
	private BikeSet mySet;
	@Autowired
	private BikeAction myAction;
	@Autowired
	private BikeGet myGet1;
	@Autowired
	private BikeSet mySet1;
	@Autowired
	private BikeAction myAction1;
	
	@Autowired
	private GroupAlarm groupAlarm;
	
	public static void main(String[] args) {
		SpringApplication.run(IotdeviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		myGet.setDurable(true);
		mySet.setDurable(true);
		myAction.setDurable(true);
		IoTDevice bike = new IoTDevice(wsession, myGet, mySet, myAction);
		IoTDevice bike1 = new IoTDevice(wsession1, myGet1, mySet1, myAction1);
		IoTDevice group = new IoTDevice(wsession2, null, null, null);
		
		List<DeviceGroup> groups = group.getDeviceGroup();
		for(DeviceGroup g : groups) {
			if(g.getName().equals("设备组")) {
				for(Device device : g.getDevices()) {
					if(device.getName().equals("001"))
						group.subscribeAlarm(device.getId(), groupAlarm);
					System.out.println("设备组：" + device.getString());
				}
			}
		}
		
		Device device = group.getDevice();
		System.out.println("device group: " + device.getName());
		
		System.out.println("请输入命令：");
		command();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true) {
			try {
				String line = scanner.nextLine();
				if(line.equals("exit")) {
					bike.stop();
					bike1.stop();
					group.stop();
					System.exit(0);
				}
				else if(line.equals("lock 1")) {
					reportAlarm(bike, true);
				}
				else if(line.equals("unlock 1")) {
					reportAlarm(bike, false);
				}
				else if(line.equals("lock 2")) {
					reportAlarm(bike1, true);
				}
				else if(line.equals("unlock 2")) {
					reportAlarm(bike1, false);
				}
				else if(line.equals("location 1")) {
					reportLocation(bike);
				}
				else if(line.equals("location 2")) {
					reportLocation(bike1);
				}
				else {
					command();
				}
			}
			catch(Throwable e) {
				e.printStackTrace(new java.io.PrintStream(System.out));
			}
		}
	}

	private void reportAlarm(IoTDevice bike, boolean lock) {
		Random r = new Random();
		double x = 180 * r.nextDouble();
		double y = 90 * r.nextDouble();
		Location location = new Location(x, y);
		StateChangedAlarm alarm = new StateChangedAlarm(location, lock);
		bike.reportAlarm("StateChangedAlarm", alarm);
	}

	private void reportLocation(IoTDevice bike) {
		Map<String, Object> events = new HashMap<String, Object>();
		Random r = new Random();
		double x = 180 * r.nextDouble();
		double y = 90 * r.nextDouble();
		events.put("location", new Location(x, y));
		bike.reportEvent(events);
	}

	private void command() {
		System.out.println("上报lock告警：lock 1; lock 2");
		System.out.println("上报unlock告警：unlock 1; unlock 2;");
		System.out.println("上报location事件：location 1; location 2;");
		System.out.println("退出：exit");
	}
}
