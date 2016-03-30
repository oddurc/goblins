package com.ru.usty.scheduling;

import java.util.Comparator;

public class ProcessResponseRatioComparator implements Comparator<ProcessData> {
	@Override
	public int compare(ProcessData x, ProcessData y) {
		// TODO Auto-generated method stub
		
		if (x.responseRatio > y.responseRatio) {
			return -1;
		}
		else if (x.responseRatio < y.responseRatio) {
			return 1;
		}	
		return 0;
	}
}
