package top.microiot.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateChangedAlarm {
	private String sessionid;
	private boolean locked;
}