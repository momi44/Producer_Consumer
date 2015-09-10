import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class PC
{
	
	/*main*/
	public static void main(String[] args)
	{
		for (int i=0; i<100; i++){
			 Thread p = new Producer();
			 p.start(); 
			 Thread c = new Consumer();
			 c.start();
		}
	}
	/***Array that is used to store the random numbers***/
	public static int[] item = new int[10];
	public static int currentSize = 0;
	private static Lock threadLock = new ReentrantLock();;
	private static Condition thresholdCondition = threadLock.newCondition();
	
	/***The producer class that add the random number to the array***/
	 public static class Producer extends Thread
	 {
		Producer(){};
		public void run()
		{
			threadLock.lock();
			try{
				if (currentSize<10)
				{	
					add();
					thresholdCondition.signalAll();
				}
				else
				{
					System.out.println("The array is full and no new item can be added");
				}
			}finally{
				threadLock.unlock();
			}
		}
	}
	
	 /***The consumer class that remove the random number from the array***/
	public static class Consumer extends Thread{
		Consumer(){};
		public void run()
		{
			threadLock.lock();
			try {
				while (currentSize<1){
					thresholdCondition.await();
				}
				remove();
			   
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				threadLock.unlock();
			}
		}
	}
	
	/***This method produce random number***/
	public static int randGenerator(){
		int x = (int) (Math.random()*100);
		return x;
	}
	
	/***This method is to add rand Num to array item***/
	public static void add()
	{
		if (currentSize < 10)
		{
			int random = randGenerator();
			item[currentSize] = random;
			currentSize++;
			System.out.println("Number " + random + " has been added");
		}
	}
	
	/***This method is to remove the random number from the array***/
	public static void remove()
	{
		if (currentSize > 0)
		{
			int deletedNumber = item[currentSize-1];
			item[currentSize-1]=0;
			currentSize--;	
			System.out.println("Number " + deletedNumber + " has been Removed");
		}
	}
}