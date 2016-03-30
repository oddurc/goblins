package com.ru.usty.scheduling;

public class ProcessData {
	int processID;
	long runTime;
	
	public ProcessData(int processID, long runTime) {
		this.processID = processID;
		this.runTime = runTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProcessData)) return false;
		
		ProcessData pc = (ProcessData) o;
		
		if (this.processID != pc.processID) return false;
		
		return true;
	}
}
