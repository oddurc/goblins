package com.ru.usty.scheduling;

import java.util.*;
import java.lang.Math;

import com.ru.usty.scheduling.process.ProcessExecution;
import com.ru.usty.scheduling.process.ProcessInfo;
import com.ru.usty.scheduling.process.ProcessHandler;

public class Scheduler implements Runnable {

	ProcessExecution processExecution;
	ProcessInfo processInfo;
	ProcessInfo runningProcessInfo;
	int runningProcessID;
	int compareID_1;
	int compareID_2;
	
	boolean incIndex;
	
	ProcessHandler processHandler;
	Policy policy;
	int quantum;
	int quantumTime;
	int RR_Index;
	int currentRunningIndex;
	
	ScheduleData SD_FCFS;
	
	int nextProcess;
	
	int RR_runningProcess = 0;
	
	long schStart, schFinish;
	Timer timer;
	TimerTask timerTask;
	boolean processRunning = false;

	Queue<Integer> processQueue;
	LinkedList<Integer> processList;
	PriorityQueue<ProcessData> prioQ;
	
	long[] arriving = new long[15];
	long[] finished = new long[15];
	long[] starting = new long[15];
	int processCount = 0;
	long sumResponseTime = 0;
	long sumTurnaroundTime= 0;
	long avgResponseTime = 0;
	long avgTurnaroundTime = 0;
	
	
	//------------------------------------------------------------
	boolean oneThreadRR = true;
	Thread threadRR;
	long startedProcess; 
	boolean noProcessRunning = true;
	int procID;


	/**
	 * Add any objects and variables here (if needed)
	 */


	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public Scheduler(ProcessExecution processExecution) {
		this.processExecution = processExecution;

		/**
		 * Add general initialization code here (if needed)
		 */
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void startScheduling(Policy policy, int quantum) {

		this.policy = policy;
		this.quantum = quantum;
		
		// Initializing for calculating purposes
		for(int i = 0; i <= 14; i++){
			arriving[i] = 0;
			finished[i] = 0;
			starting[i] = 0;	
		}
		
		processCount = 0;
		sumResponseTime = 0;
		sumTurnaroundTime= 0;
		avgResponseTime = 0;
		avgTurnaroundTime = 0;
		
		//this.schStart = System.currentTimeMillis(); //sets the start time of this scheduler

		switch(policy) {
		case FCFS:	//First-come-first-served
			processQueue = new LinkedList<Integer>();
			SD_FCFS = new ScheduleData();
		
			System.out.println("Starting new scheduling task: First-come-first-served");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case RR:	//Round robin
			//System.out.println("WT: " + (SD_FCFS.WT/SD_FCFS.processCount) + " - TAT: " + (SD_FCFS.TAT/SD_FCFS.processCount) );
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
//			RR_Index = 0;
//			processList = new LinkedList<Integer>();
//			this.quantumTime = quantum;
//			processRunning = false;
//			compareID_2 = -1;
//			incIndex = true;
			
//			if(oneThreadRR){
//				threadRR = new Thread(this);
//				threadRR.start();
//			}
//			oneThreadRR = false;

			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SPN:	//Shortest process next
			System.out.println("Starting new scheduling task: Shortest process next");
			
			Comparator<ProcessData> comparatorRunTime = new ProcessRunTimeComparator();
			prioQ = new PriorityQueue<ProcessData>(comparatorRunTime);
			processRunning = false;
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			
			Comparator<ProcessData> comparatorRemTime = new ProcessRemainingTimeComparator();
			prioQ = new PriorityQueue<ProcessData>(comparatorRemTime);
			processRunning = false;
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			
			Comparator<ProcessData> comparatorResponseRatio = new ProcessResponseRatioComparator();
			prioQ = new PriorityQueue<ProcessData>(comparatorResponseRatio);
			processRunning = false;
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case FB:	//Feedback
			System.out.println("Starting new scheduling task: Feedback, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}

		/**
		 * Add general scheduling or initialization code here (if needed)
		 */
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processAdded(int processID) {

		this.policy = policy;
		this.quantum = quantum;
		
		arriving[processID] = System.currentTimeMillis();

		switch(policy) {
		case FCFS:	//First-come-first-served
			processQueue.add(processID);
			processInfo = processExecution.getProcessInfo(processID);
			if(processRunning == false){
				processExecution.switchToProcess(processID);
				starting[processID] = System.currentTimeMillis();
				processRunning = true;
			}
			break;
		case RR:	//Round robin
//			processList.add(processID);
//			
//			if (incIndex) {
//				if ((RR_Index + 1) >= processList.size() ) {
//					RR_Index = 0;
//				}
//				else {
//					RR_Index++;
//				}
//			}
//			runProcess(RR_Index);
//			
//			processList.add(processID);
//
//			if(noProcessRunning == true){
//				
//				//procID = processList.remove();
//				procID = processList.getFirst();
//				startedProcess = System.currentTimeMillis();
//				processExecution.switchToProcess(procID); 
//				noProcessRunning = false;
//				
////				if(starting[processID] == 0){
////					starting[processID] = System.currentTimeMillis();
////				}
//			}
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SPN:	//Shortest process next
			processInfo = processExecution.getProcessInfo(processID);
			prioQ.add(new ProcessData(processID, processInfo.totalServiceTime, (processInfo.totalServiceTime-processInfo.elapsedExecutionTime), processInfo.totalServiceTime));
			if (!processRunning) {
				int ID = prioQ.element().processID;
				runningProcessInfo = processExecution.getProcessInfo(ID);
				processExecution.switchToProcess(ID);
				starting[processID] = System.currentTimeMillis();
				processRunning = true;
			}
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			processInfo = processExecution.getProcessInfo(processID);
			prioQ.add(new ProcessData(processID, processInfo.totalServiceTime, (processInfo.totalServiceTime-processInfo.elapsedExecutionTime), processInfo.totalServiceTime));
			if (!processRunning) {
				int ID = prioQ.element().processID;
				runningProcessInfo = processExecution.getProcessInfo(ID);
				runningProcessID = ID;			
				processExecution.switchToProcess(ID);
				if (starting[ID] == 0) {
					starting[ID] = System.currentTimeMillis();
				}
				processRunning = true;
			}
			else {
				ProcessInfo pi = processExecution.getProcessInfo(runningProcessID);
				long remTime = pi.totalServiceTime - pi.elapsedExecutionTime;
				
				if (remTime > processInfo.totalServiceTime) { // Ef remaining time á nýja process er minni en á núverandi process
					runningProcessInfo = processExecution.getProcessInfo(processID); // Þá skiptum við yfir á nýja process-inn
					processExecution.switchToProcess(processID);
					if (starting[processID] == 0) {
						starting[processID] = System.currentTimeMillis();
					}
					processRunning = true;
					
					// Vil þá taka gamla processinn úr queue og setja hann aftur inn með annan runtime (þeas núverandi remaining time)
					prioQ.remove(new ProcessData(runningProcessID, runningProcessInfo.totalServiceTime, remTime, remTime)); // remTime hér hefur engin áhrif á remove virknina
					prioQ.add(new ProcessData(runningProcessID, runningProcessInfo.totalServiceTime, remTime, remTime));
					
					runningProcessID = processID;
				}
			}
						
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			LinkedList<Integer> temp = new LinkedList<Integer>();
			while (!prioQ.isEmpty()) {
				int id = prioQ.element().processID;
				temp.add(id);
				processInfo = processExecution.getProcessInfo(id);
				prioQ.remove(new ProcessData(id, processInfo.totalServiceTime, processInfo.elapsedWaitingTime, processInfo.totalServiceTime));
			}
			
			while (!temp.isEmpty()) {
				int id = temp.element();
				processInfo = processExecution.getProcessInfo(id);
				long rRatio = ((processInfo.elapsedWaitingTime-processInfo.totalServiceTime)/processInfo.totalServiceTime);
				prioQ.add(new ProcessData(id, processInfo.totalServiceTime, (processInfo.totalServiceTime-processInfo.elapsedExecutionTime), rRatio));
				temp.remove(0);
			}
			
			processInfo = processExecution.getProcessInfo(processID);
			long rRatio = ((processInfo.elapsedWaitingTime-processInfo.totalServiceTime)/processInfo.totalServiceTime);
			prioQ.add(new ProcessData(processID, processInfo.totalServiceTime, (processInfo.totalServiceTime-processInfo.elapsedExecutionTime), rRatio));
			
			int ID = prioQ.element().processID;
			runningProcessInfo = processExecution.getProcessInfo(ID);
			runningProcessID = ID;			
			processExecution.switchToProcess(ID);
			if (starting[ID] == 0) {
				starting[ID] = System.currentTimeMillis();
			}
			processRunning = true;	
			
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case FB:	//Feedback
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {
		
		processCount++;
		finished[processID] = System.currentTimeMillis();

		this.policy = policy;
		this.quantum = quantum;

		switch(policy) {
		case FCFS:	//First-come-first-served
				
			processQueue.remove();
			
			//processHandler = new ProcessHandler();
			//processInfo = processExecution.getProcessInfo(lastProcess); 
			
//			SD_FCFS.WT = SD_FCFS.WT + processInfo.elapsedWaitingTime;
//			SD_FCFS.TAT = SD_FCFS.TAT + processInfo.elapsedExecutionTime;
//			SD_FCFS.processCount++;
//			System.out.println(processInfo.);
			
			processRunning = false;
			
			if (!processQueue.isEmpty()){
				//System.out.println(processInfo.elapsedWaitingTime);
				int ID = processQueue.element();
				processExecution.switchToProcess(ID);
				starting[ID] = System.currentTimeMillis();
				processRunning = true;
			}
			
			if(processCount == 15){
				statistics();
			}
			
			break;
		case RR:	//Round robin
//			processList.remove(runningProcessID);
//			processRunning = false;
//			
//			if(!processList.isEmpty()) {
//				if (RR_Index >= processList.size() ) {
//					RR_Index = 0;
//				}
//				
//				runProcess(RR_Index);
//			}
//			
			
//			if(!processList.isEmpty()){
//				
//				procID = processList.remove();
//				startedProcess = System.currentTimeMillis();
//				processExecution.switchToProcess(procID);
				
//				if(starting[procID] == 0){
//					starting[procID] = System.currentTimeMillis();
//				}
//			}
//			else{
//				noProcessRunning = true;
//			}
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SPN:	//Shortest process next
			processRunning = false;
			//processInfo = processExecution.getProcessInfo(processID);
			prioQ.remove(new ProcessData(processID, runningProcessInfo.totalServiceTime, runningProcessInfo.elapsedWaitingTime, runningProcessInfo.totalServiceTime));
			
			if (!prioQ.isEmpty()) {
				int ID = prioQ.element().processID;
				runningProcessInfo = processExecution.getProcessInfo(ID);
				runningProcessID = ID;
				processExecution.switchToProcess(ID);
				starting[ID] = System.currentTimeMillis();
				processRunning = true;
			}
			
			if(processCount == 15){
				statistics();
			}
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			processRunning = false;
			prioQ.remove(new ProcessData(processID, runningProcessInfo.totalServiceTime, runningProcessInfo.elapsedWaitingTime, runningProcessInfo.totalServiceTime));
			if (!prioQ.isEmpty()) {
				int ID = prioQ.element().processID;
				runningProcessInfo = processExecution.getProcessInfo(ID);
				runningProcessID = ID;
				processExecution.switchToProcess(ID);
				if (starting[ID] == 0) {
					starting[ID] = System.currentTimeMillis();
				}
				processRunning = true;
			}
			
			if(processCount == 15){
				statistics();
			}
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			processRunning = false;
			prioQ.remove(new ProcessData(processID, runningProcessInfo.totalServiceTime, runningProcessInfo.elapsedWaitingTime, runningProcessInfo.totalServiceTime));
			if (!prioQ.isEmpty()) {
				int ID = prioQ.element().processID;
				runningProcessInfo = processExecution.getProcessInfo(ID);
				runningProcessID = ID;
				processExecution.switchToProcess(ID);
				if (starting[ID] == 0) {
					starting[ID] = System.currentTimeMillis();
				}
				processRunning = true;
			}
			
			if(processCount == 15){
				statistics();
			}
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case FB:	//Feedback
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}
	}

	public void RR_SwitchToProcess(int processID){
		RR_runningProcess = processList.indexOf(processID);
		
		Thread t = new Thread(){
			public void run() {
				while(true) {
					try {
						if(!processRunning){
							processExecution.switchToProcess(RR_runningProcess);
							processRunning = true;
							Thread.sleep(quantum);
						}
								
						if(processList.indexOf(RR_runningProcess) == processList.getLast()){
							RR_runningProcess = 0;
						    processExecution.switchToProcess(RR_runningProcess);
						    processRunning=true;
						    Thread.sleep(quantum);
						}
						else{
							RR_runningProcess++;
							processExecution.switchToProcess(processList.indexOf(RR_runningProcess));
							processRunning=true;
							Thread.sleep(quantum);
						}
					} 
					catch (InterruptedException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			 }
		};
		t.start();		
	}
	
	public void runProcess(int index) {
		if (!processList.isEmpty()) {
			
			//long startTime = System.currentTimeMillis();
			runningProcessID = processList.get(RR_Index);
			compareID_1 = processList.get(RR_Index);
			
			if (compareID_1 != compareID_2) {
				processExecution.switchToProcess(runningProcessID);
			}
//			else {
//				runForQuantumTime();
//			}
			currentRunningIndex = RR_Index;
			processRunning = true;
			
			runForQuantumTime();
			
//			while ((System.currentTimeMillis() - startTime) <= quantumTime) {
//				// Let process run for quantum milliseconds
//			}
			
			// Geri svo stuff þegar tíminn er liðinn
			
			
//			
//			if ((RR_Index + 1) >= processList.size() ) {
//				RR_Index = 0;
//			}
//			else {
//				RR_Index++;
//			}
			
			//if (currentRunningIndex != RR_Index) {
			compareID_2 = processList.get(RR_Index);
			//runProcess(RR_Index);
			//}
		}
	}
	public void runForQuantumTime() {
		long startTime = System.currentTimeMillis();
		long lidinnTimi = System.currentTimeMillis() - startTime;
		while (lidinnTimi <= quantumTime) {
			lidinnTimi = System.currentTimeMillis() - startTime;
			// Let process run for quantum milliseconds
		}
		System.out.println("Millisekúndur sem liðu eru: " + lidinnTimi);
		if ((RR_Index + 1) >= processList.size() ) {
			RR_Index = 0;
		}
		else {
			RR_Index++;
		}
		incIndex = false;
	}

	@Override
	public void run() {
		switch(this.policy) {
		case RR:
			
			while(true){

				try {
					Thread.sleep(quantum);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//Sleep again if another process has been started
				while(System.currentTimeMillis() - startedProcess <= quantum){

					try{
						Thread.sleep(System.currentTimeMillis() - startedProcess);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				processList.add(procID);

				if(!processList.isEmpty()){
					
					procID = processList.remove();
					startedProcess = System.currentTimeMillis();
					processExecution.switchToProcess(procID);
					
//					if(starting[procID] == 0){
//						starting[procID] = System.currentTimeMillis();
//					}
				}
				else{
					noProcessRunning = true;
				}

				if(this.policy != Policy.RR){
					return; 
				}
			}
		case FB:
		}
	}
	public void statistics(){
		for(int i = 0; i < this.processCount ; i++){		
			this.sumResponseTime += (this.starting[i] - this.arriving[i]);
			this.sumTurnaroundTime += (this.finished[i] - this.arriving[i]);
		}
		
		this.avgResponseTime = (this.sumResponseTime/this.processCount);
		this.avgTurnaroundTime = (this.sumTurnaroundTime/this.processCount);
		
		System.out.println("Average Response Time for " + this.policy + ": " + this.avgResponseTime + " milliseconds");
		System.out.println("Average Turnaround Time for " + this.policy + ": " + this.avgTurnaroundTime + " milliseconds");	
	}
}

