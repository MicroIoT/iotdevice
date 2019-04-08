package com.leanit.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.leaniot.api.device.WebsocketDeviceSession;
import com.leaniot.domain.attribute.Location;

@SpringBootApplication
public class IotdeviceApplication implements CommandLineRunner {
	@Autowired
	private WebsocketDeviceSession wsession;
	@Autowired
	private BikeGet myGet;
	@Autowired
	private BikeSet mySet;
	@Autowired
	private BikeAction myAction;
	
	public static void main(String[] args) {
		SpringApplication.run(IotdeviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		wsession.subscribe(myGet);
		
		Map<String, Class<?>> attType = new HashMap<String, Class<?>>();
		attType.put("locked", Boolean.class);
		mySet.setAttType(attType);
		wsession.subscribe(mySet);
		
		Map<String, Class<?>> actionType = new HashMap<String, Class<?>>();
		actionType.put("getHistory", Filter.class);
		myAction.setActionType(actionType);
		wsession.subscribe(myAction);
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true) {
			try {
				String line = scanner.nextLine();
				if(line.equals("exit")) {
					wsession.stop();
					System.exit(0);
				}
				else if(line.equals("lock")) {
					StateChangedAlarm lock = new StateChangedAlarm(Long.toString(new Date().getTime()), true);
					wsession.getSession().reportAlarm("StateChangedAlarm", lock);
				}
				else if(line.equals("unlock")) {
					StateChangedAlarm unlock = new StateChangedAlarm(Long.toString(new Date().getTime()), false);
					wsession.getSession().reportAlarm("StateChangedAlarm", unlock);
				}
				else if(line.equals("location")) {
					Map<String, Object> events = new HashMap<String, Object>();
					Random r = new Random();
					double x = 180 * r.nextDouble();
					double y = 90 * r.nextDouble();
					events.put("location", new Location(x, y));
					wsession.getSession().reportEvents(events);
				}
			}
			catch(Throwable e) {
				e.printStackTrace(new java.io.PrintStream(System.out));
			}
		}
	}

}
