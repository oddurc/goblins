package com.ru.usty.scheduling;

import java.util.*;

public class ProcessRunTimeComparator implements Comparator<ProcessData> {
	@Override
	public int compare(ProcessData x, ProcessData y) {
		// TODO Auto-generated method stub
		
		if (x.runTime < y.runTime) {
			return -1;
		}
		else if (x.runTime > y.runTime) {
			return 1;
		}	
		return 0;
	}
}
