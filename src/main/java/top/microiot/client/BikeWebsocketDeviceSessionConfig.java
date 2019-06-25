package top.microiot.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import top.microiot.api.device.HttpDeviceSession;
import top.microiot.api.device.WebsocketDeviceSession;

@Configuration
public class BikeWebsocketDeviceSessionConfig {
	@Bean("bikeWebsocketDeviceSession")
	@Scope("prototype")
	public WebsocketDeviceSession websocketDeviceSession(
			@Qualifier("bikeHttpDeviceSession") HttpDeviceSession httpDeviceSession,
			WebSocketStompClient websocketStompClient) {
		return new WebsocketDeviceSession(httpDeviceSession, websocketStompClient);
	}
}
