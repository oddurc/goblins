package com.ru.usty.scheduling;

public class ProcessData {
	int processID;
	long runTime;
	long remainingTime;
	
	public ProcessData(int processID, long runTime) {
		this.processID = processID;
		this.runTime = runTime;
		this.remainingTime = runTime;
	}

	@Override
	public boolean equals(Object o) { // Define how to compare ProcessData instances
		if (this == o) return true;
		if (!(o instanceof ProcessData)) return false;
		ProcessData pc = (ProcessData) o;
		if (this.processID == pc.processID) return true;
		return false;
	}
}
