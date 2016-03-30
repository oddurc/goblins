package com.ru.usty.scheduling;

import java.util.*;
import java.lang.Math;

import com.ru.usty.scheduling.process.ProcessExecution;
import com.ru.usty.scheduling.process.ProcessInfo;
import com.ru.usty.scheduling.process.ProcessHandler;

public class Scheduler {

	public ProcessExecution processExecution;
	ProcessInfo processInfo;
	ProcessInfo runningProcessInfo;
	int runningProcessID;
	
	ProcessHandler processHandler;
	Policy policy;
	int quantum;
	//int RR_Index;
	
	ScheduleData SD_FCFS;
	
	int nextProcess;
	
	int RR_runningProcess;
	
	long schStart, schFinish;
	Timer timer;
	TimerTask timerTask;
	boolean processRunning = false;

	Queue<Integer> processQueue;
	LinkedList<Integer> processList;
	PriorityQueue<ProcessData> prioQ;

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

			//RR_Index = 0;
			processList = new LinkedList<Integer>();
			
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
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

		switch(policy) {
		case FCFS:	//First-come-first-served
			processQueue.add(processID);
			processInfo = processExecution.getProcessInfo(processID);
			if(processRunning == false){
				processExecution.switchToProcess(processID);
				processRunning = true;
			}
			break;
		case RR:	//Round robin

			processList.add(processID);
			
			RR_SwitchToProcess(processID);	

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
				processRunning = true;
			}
			else {
				ProcessInfo pi = processExecution.getProcessInfo(runningProcessID);
				long remTime = pi.totalServiceTime - pi.elapsedExecutionTime;
				
				if (remTime > processInfo.totalServiceTime) { // Ef remaining time á nýja process er minni en á núverandi process
					runningProcessInfo = processExecution.getProcessInfo(processID); // Þá skiptum við yfir á nýja
					processExecution.switchToProcess(processID);
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

		this.policy = policy;
		this.quantum = quantum;

		switch(policy) {
		case FCFS:	//First-come-first-served
				
			processQueue.remove();
			
			//processHandler = new ProcessHandler();
			//processInfo = processExecution.getProcessInfo(lastProcess); 
			
			SD_FCFS.WT = SD_FCFS.WT + processInfo.elapsedWaitingTime;
			SD_FCFS.TAT = SD_FCFS.TAT + processInfo.elapsedExecutionTime;
			SD_FCFS.processCount++;
//			System.out.println(processInfo.);
			
			processRunning = false;
			
			if (!processQueue.isEmpty()){
				System.out.println(processInfo.elapsedWaitingTime);
				processExecution.switchToProcess(processQueue.element()); 
				processRunning = true;
			}
			
			break;
		case RR:	//Round robin
			processList.remove(processID);
			
			RR_runningProcess = processList.lastIndexOf(processID+1);
			
			processRunning = false;
			
			if(!processList.isEmpty()) {
				RR_SwitchToProcess(RR_runningProcess);
			}
			
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
				processRunning = true;
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
				processRunning = true;
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
				processRunning = true;
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
								
						if(RR_runningProcess == processList.indexOf(processList.getLast())){
							processRunning=true;
							RR_runningProcess = processList.indexOf(processList.getFirst());
						    processExecution.switchToProcess(RR_runningProcess);
						    Thread.sleep(quantum);
						}
						else{
							processRunning=true;
							processExecution.switchToProcess(RR_runningProcess);
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
}

