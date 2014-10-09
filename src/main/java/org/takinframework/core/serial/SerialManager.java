package org.takinframework.core.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import org.takinframework.core.util.LogUtil;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * 串口通信管理器
 * @author twg
 *
 */
@SuppressWarnings("restriction")
public class SerialManager implements SerialPortEventListener {
	private static final String SERIAL_NAME = "_DEFULT_SERIAL_";
	private static final int READ_TIME_OUT = 200;//2秒
	private static final int BPS = 9600;
	private static String PortName = "COM1";
	private static Enumeration<?> portList;//枚举类
	//检测系统中可用的通讯端口类
	private CommPortIdentifier portId;
	//RS-232的串行口
	private SerialPort serialPort;
	//输入输出流
	private OutputStream out;
	private InputStream in;
	//接收数据的缓冲区
	private SerialBuffer SB = new SerialBuffer();
	//读取数据的程序
	private ReadSerial RT = new ReadSerial();
	
	
	public SerialManager() {}
	/**
	 * 本函数构造一个指向特定串口，该串口由参数portId所指定。
	 * portId = 1 表示COM1，portId = 2 表示COM2，由此类推,默认为COM1
	 * @param portId
	 */
	public SerialManager(int portId) {
		PortName = "COM" + portId;
	}
	/**
	 * 本函数初始化所指定的串口。
	 * 初始化的结果是该串口其参数被设置为9600, N, 8, 1。
	 * @return
	 */
	public void init() {
		LogUtil.info("串口通信配置开始初始化....");
			portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				portId = (CommPortIdentifier) portList.nextElement();
				/*getPortType方法返回端口类型*/
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					/*找Windows系统下的第一个串口*/
					if (portId.getName().equals(PortName)) {
						try {
							portId = CommPortIdentifier.getPortIdentifier(PortName);
							serialPort = (SerialPort) portId.open(SERIAL_NAME, 2000);
							serialPort.enableReceiveTimeout(READ_TIME_OUT);
							if (serialPort instanceof SerialPort) {
								serialPort.setSerialPortParams(BPS, SerialPort.DATABITS_8,
										SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
								serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
								in = serialPort.getInputStream();
								out = serialPort.getOutputStream();
							} else {
								LogUtil.error("系统异常: 只能和串口RS-232通信");
							}
							//设置串口监听  
				            try {  
				                serialPort.addEventListener((SerialPortEventListener) this);  
				            } catch (TooManyListenersException e) {
				            	String errorMsg = "系统异常: 串口["+PortName+"]监听者过多";
				    			LogUtil.error(errorMsg, e);
				            }  
				              
				            serialPort.notifyOnDataAvailable(true);
							
						} catch (NoSuchPortException e) {
							String errorMsg = "系统异常: 没有检测到串口[" + PortName + "]";
							LogUtil.error(errorMsg, e);
						} catch (PortInUseException e) {
							String errorMsg = "系统异常: 串口[" + PortName + "]被占用";
							LogUtil.error(errorMsg, e);
						} catch (UnsupportedCommOperationException e) {
							String errorMsg = "系统异常: 串口操作不支持";
							LogUtil.error(errorMsg, e);
						} catch (IOException e) {
							String errorMsg = "系统异常: 串口打开异常";
							LogUtil.error(errorMsg, e);
						}
					}
				}
			}
			
	}
	/**
	 * 本函数从串口(缓冲区)中读取指定长度的一个字符串。参数Length指定所返回字符串的长度。
	 * @param Length
	 * @return
	 */
	public String ReadPort(int Length) {
		if(null != SB){return SB.GetMsg(Length);}
		return "";
	}
	/**
	 * 本函数从串口(缓冲区)中读取字符串
	 * @return
	 */
	public String ReadPort() {
		if(null != SB){return SB.GetMsg();}
		return "";
	}
	/**
	 * 本函数向串口发送一个字符串。参数Msg是需要发送的字符串。
	 * @param Msg
	 */
	public boolean WritePort(String Msg) {
		if(null == out){
			return false;
		}
		for (int i = 0; i < Msg.length(); i++){
			try {
				out.write(Msg.charAt(i));
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 本函数停止串口检测进程并关闭串口。
	 */
	@SuppressWarnings("deprecation")
	public void ClosePort() {
		if(null != RT){RT.stop();}
		serialPort.close();
	}
	
	public void serialEvent(SerialPortEvent event) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		switch(event.getEventType()) {
			case SerialPortEvent.BI:/*Break interrupt,通讯中断*/
			case SerialPortEvent.OE:/*Overrun error，溢位错误*/
			case SerialPortEvent.FE:/*Framing error，传帧错误*/
			case SerialPortEvent.PE:/*Parity error，校验错误*/
			case SerialPortEvent.CD:/*Carrier detect，载波检测*/
			case SerialPortEvent.CTS:/*Clear to send，清除发送*/
			case SerialPortEvent.DSR:/*Data set ready，数据设备就绪*/
			case SerialPortEvent.RI:/*Ring indicator，响铃指示*/
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/
			break;
			case SerialPortEvent.DATA_AVAILABLE:/*Data available at the serial port，端口有可用数据。读到缓冲数组，输出到终端*/
			try {
				while (in.available() > 0) {
					RT.init(SB, in);
					RT.start();
				}
			} catch (IOException e) {}
			break;
		}
	}

}
