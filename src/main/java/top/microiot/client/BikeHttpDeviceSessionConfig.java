package top.microiot.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import top.microiot.api.HttpSessionProperties;
import top.microiot.api.device.HttpDeviceSession;

@Configuration
public class BikeHttpDeviceSessionConfig {
	@Bean(initMethod = "start", name="bikeHttpDeviceSession")
    @Scope("prototype")
	public HttpDeviceSession httpDeviceSession() {
		return new HttpDeviceSession(httpSessionProperties());
	}
	
	@Bean("bikeHttpProperties")
    @ConfigurationProperties(prefix = "bike.connect")
    public HttpSessionProperties httpSessionProperties(){
	    return new HttpSessionProperties();
    }
}