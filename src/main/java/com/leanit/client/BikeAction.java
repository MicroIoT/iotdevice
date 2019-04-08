package com.leanit.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.leaniot.api.device.stomp.ActionSubscriber;
import com.leaniot.exception.ValueException;

@Component
public class BikeAction extends ActionSubscriber {
	@Override
	public Object action(String action, Object request) {
		System.out.println(new Date() + ":  action: " + action);
		if(action.equals("getHistory")) {
			Filter filter = (Filter)request;
			
			List<Record> records = new ArrayList<Record>();
			for(int i = 0; i < 10; i ++)
				records.add(getRecord(filter));
			
			return new Records(records);
		}else {
			throw new ValueException("unknonw action: " + action);
		}
	}

	private Record getRecord(Filter filter) {
		Random r = new Random();
		int i = r.nextInt(10);
		Date start;
		if(filter.getStartDate() == null)
			start = new Date();
		else
			start = filter.getStartDate();
		start.setTime(start.getTime()-(i * 1000000));
		
		Date end;
		if(filter.getEndDate() == null)
			end = new Date();
		else
			end = filter.getEndDate();
		end.setTime(end.getTime()-(i * 1000000));
		
		return new Record(Long.toString(start.getTime()), start, end);
	}

}
