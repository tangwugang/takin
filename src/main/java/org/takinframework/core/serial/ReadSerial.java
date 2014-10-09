package org.takinframework.core.serial;

import java.io.IOException;
import java.io.InputStream;
/**
 * 从串口读取数据的程序
 * @author twg
 *
 */
public class ReadSerial extends Thread {
	private SerialBuffer ComBuffer;
	private InputStream ComPort;

	public void init(SerialBuffer SB, InputStream Port) {
		ComBuffer = SB;
		ComPort = Port;
	}
	
	public void run() {
		int c;
		try {
			while (true) {
				c = ComPort.read(ComBuffer.GetBuffer());
				ComBuffer.PutChar(c);
			}
		} catch (IOException e) {
		}
	}

}
