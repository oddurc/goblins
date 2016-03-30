package com.ru.usty.scheduling;

public class ProcessData {
	int processID;
	long runTime;
	long remainingTime;
	long responseRatio;
	
	public ProcessData(int processID, long runTime, long remTime, long rr) {
		this.processID = processID;
		this.runTime = runTime;
		this.remainingTime = remTime;
		this.responseRatio = rr;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProcessData)) return false;
		
		ProcessData pc = (ProcessData) o;
		
		if (this.processID == pc.processID) return true;
		
		return false;
	}
}
