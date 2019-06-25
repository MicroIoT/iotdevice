package top.microiot.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import top.microiot.api.device.WebsocketDeviceSession;
import top.microiot.domain.attribute.Location;

@SpringBootApplication
public class IotdeviceApplication implements CommandLineRunner {
	@Autowired
	private WebsocketDeviceSession wsession;
	@Autowired
	@Qualifier("bikeWebsocketDeviceSession")
	private WebsocketDeviceSession wsession1;
	
	private WebsocketDeviceSession session;
	
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
	
	public static void main(String[] args) {
		SpringApplication.run(IotdeviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		session = wsession;
		
		wsession.subscribe(myGet);
		wsession.subscribe(mySet);
		wsession.subscribe(myAction);
		
		wsession1.subscribe(myGet1);
		wsession1.subscribe(mySet1);
		wsession1.subscribe(myAction1);
		
		System.out.println("请输入命令：");
		command();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true) {
			try {
				String line = scanner.nextLine();
				if(line.equals("exit")) {
					session.stop();
					System.exit(0);
				}
				else if(line.equals("lock 1")) {
					session = wsession;
					reportLock();
				}
				else if(line.equals("unlock 1")) {
					session = wsession;
					reportUnlock();
				}
				else if(line.equals("location 1")) {
					session = wsession;
					reportLocation();
				}
				else if(line.equals("lock 2")) {
					session = wsession1;
					reportLock();
				}
				else if(line.equals("unlock 2")) {
					session = wsession1;
					reportUnlock();
				}
				else if(line.equals("location 2")) {
					session = wsession1;
					reportLocation();
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

	private void reportLocation() {
		Map<String, Object> events = new HashMap<String, Object>();
		Random r = new Random();
		double x = 180 * r.nextDouble();
		double y = 90 * r.nextDouble();
		events.put("location", new Location(x, y));
		session.getSession().reportEvents(events);
	}

	private void reportUnlock() {
		StateChangedAlarm unlock = new StateChangedAlarm(Long.toString(new Date().getTime()), false);
		session.getSession().reportAlarm("StateChangedAlarm", unlock);
	}

	private void reportLock() {
		StateChangedAlarm lock = new StateChangedAlarm(Long.toString(new Date().getTime()), true);
		session.getSession().reportAlarm("StateChangedAlarm", lock);
	}

	private void command() {
		System.out.println("上报lock告警：lock 1; lock 2");
		System.out.println("上报unlock告警：unlock 1; unlock 2;");
		System.out.println("上报location事件：location 1; location 2;");
		System.out.println("退出：exit");
	}
}
