package top.microiot.device;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import top.microiot.api.HttpSessionProperties;
import top.microiot.api.device.HttpDeviceSession;

@Configuration
public class BikeGroupHttpDeviceSessionConfig {
	@Bean(initMethod = "start", name="bikeGroupHttpDeviceSession")
    @Scope("prototype")
	public HttpDeviceSession httpDeviceSession() {
		return new HttpDeviceSession(httpSessionProperties());
	}
	
	@Bean("bikeGroupHttpProperties")
    @ConfigurationProperties(prefix = "bikegroup.connect")
    public HttpSessionProperties httpSessionProperties(){
	    return new HttpSessionProperties();
    }
}
