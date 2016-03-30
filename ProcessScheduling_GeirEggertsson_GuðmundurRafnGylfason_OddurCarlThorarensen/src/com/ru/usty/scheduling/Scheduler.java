package com.ru.usty.scheduling;

import java.util.*;

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
	int RR_Index;
	
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

			RR_Index = 0;
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
			prioQ.add(new ProcessData(processID, processInfo.totalServiceTime, (processInfo.totalServiceTime-processInfo.elapsedExecutionTime)));
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
			prioQ.add(new ProcessData(processID, processInfo.totalServiceTime, (processInfo.totalServiceTime-processInfo.elapsedExecutionTime)));
			if (!processRunning) {
				int ID = prioQ.element().processID;
				runningProcessInfo = processExecution.getProcessInfo(ID);
				runningProcessID = ID;
				
				// B�ta vi� timer sem er starta� h�r, tekur t�mann hversu lengi �essi process keyrir
				
				processExecution.switchToProcess(ID);
				processRunning = true;
			}
			else {
				ProcessInfo pi = processExecution.getProcessInfo(runningProcessID);
				long remTime = pi.totalServiceTime - pi.elapsedExecutionTime;
				
				if (remTime > processInfo.totalServiceTime) { // Ef remaining time � n�ja process er minni en � n�verandi process
					runningProcessInfo = processExecution.getProcessInfo(processID); // �� skiptum vi� yfir � n�ja
					processExecution.switchToProcess(processID);
					processRunning = true;
					
					// Vil �� taka gamla processinn �r queue og setja hann aftur inn me� annan runtime (�eas n�verandi remaining time)
					prioQ.remove(new ProcessData(runningProcessID, runningProcessInfo.totalServiceTime, remTime)); // remTime h�r hefur engin �hrif � remove virknina
					prioQ.add(new ProcessData(runningProcessID, runningProcessInfo.totalServiceTime, remTime));
					
					runningProcessID = processID;
				}
			}
						
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			
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
			processList.remove(processList.indexOf(processID));
			
			processRunning = false;
			
			if(!processList.isEmpty()) {
				RR_SwitchToProcess(processID);
			}
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SPN:	//Shortest process next
			processRunning = false;
			//processInfo = processExecution.getProcessInfo(processID);
			prioQ.remove(new ProcessData(processID, runningProcessInfo.totalServiceTime, runningProcessInfo.elapsedWaitingTime));
			
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
			prioQ.remove(new ProcessData(processID, runningProcessInfo.totalServiceTime, runningProcessInfo.elapsedWaitingTime));
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
}

