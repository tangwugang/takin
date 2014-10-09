package org.takinframework.core.serial;

/**
 * 用来保存从串口所接收数据的缓冲区
 * @author twg
 *
 */
public class SerialBuffer {
	private String Content = "";//内容
	private String CurrentMsg, TempContent;
	private boolean available = false;//标识消息是否可用
	private int LengthNeeded = 1;//统计收到的消息字节长度
	/**
	 * 获取指定长度的内容
	 * @param Length
	 * @return
	 */
	public synchronized String GetMsg(int Length) {
		LengthNeeded = Length;
		notifyAll();
		if (LengthNeeded > Content.length()) {
			available = false;
			while (available == false) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}
		CurrentMsg = Content.substring(0, LengthNeeded);
		TempContent = Content.substring(LengthNeeded);
		Content = TempContent;
		LengthNeeded = 1;
		notifyAll();
		return CurrentMsg;
	}
	/**
	 * 获取全部内容
	 * @return
	 */
	public synchronized String GetMsg() {
		notifyAll();
		return Content;
	}
	/**
	 * 从输入流中获取，生成内容
	 * @param c
	 */
	public synchronized void PutChar(int c) {
		Character d = new Character((char) c);
		Content = Content.concat(d.toString());
		if (LengthNeeded < Content.length()) {
			available = true;
		}
		notifyAll();
	}
	/**
	 * 获取输入流时设置的缓存
	 * @return
	 */
	public byte[] GetBuffer(){
		return new byte[1024];
	}

}
