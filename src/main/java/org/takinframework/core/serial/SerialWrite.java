package org.takinframework.core.serial;

public class SerialWrite implements Runnable {

	public void run() {
		try {
			System.out.println("SerialWrite.run()");
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] arg){
		for (int i = 0; i < 10; i++) {
			SerialWrite serialWrite = new SerialWrite();
			Thread thread = new Thread(serialWrite,"client_"+i);
			thread.start();
		}
		
	}

}
