package com.ru.usty.scheduling;

import java.util.*;

public class ProcessRemainingTimeComparator implements Comparator<ProcessData> {
	@Override
	public int compare(ProcessData x, ProcessData y) {
		// TODO Auto-generated method stub
		
		if (x.remainingTime < y.remainingTime) {
			return -1;
		}
		else if (x.remainingTime > y.remainingTime) {
			return 1;
		}	
		return 0;
	}
}
