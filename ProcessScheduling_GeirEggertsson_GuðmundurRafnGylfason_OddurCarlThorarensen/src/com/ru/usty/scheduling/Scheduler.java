package com.ru.usty.scheduling;

import java.util.LinkedList;
import java.util.Queue;

import com.ru.usty.scheduling.process.ProcessExecution;
import com.ru.usty.scheduling.process.ProcessInfo;
import com.ru.usty.scheduling.process.ProcessHandler;

public class Scheduler {

	ProcessExecution processExecution;
	ProcessInfo processInfo;
	ProcessHandler processHandler;
	Policy policy;
	int quantum;
	
	ScheduleData SD_FCFS;
	
	int nextProcess;
	
	long schStart, schFinish;
	
	boolean processRunning = false;

	Queue<Integer> processQueue;

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
			System.out.println("WT: " + (SD_FCFS.WT/SD_FCFS.processCount) + " - TAT: " + (SD_FCFS.TAT/SD_FCFS.processCount) );
			processQueue = new LinkedList<Integer>();
			
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SPN:	//Shortest process next
			System.out.println("Starting new scheduling task: Shortest process next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
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
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case RR:	//Round robin
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SPN:	//Shortest process next
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			
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
			//processInfo = processExecution.getProcessInfo(processID); 
			
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
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SPN:	//Shortest process next
			
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			
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
}
