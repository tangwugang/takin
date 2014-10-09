package org.takinframework.core.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.takinframework.core.util.LogUtil;

/**
 * Java串口通信
 * @author twg
 *
 */
@SuppressWarnings("rawtypes")
public class SerialRead implements Runnable, SerialPortEventListener {
	
	private static CommPortIdentifier portId;
	private static Enumeration portList;//枚举类
	private static SerialPort commPort = null;
	private static InputStream in = null;
	private static OutputStream out = null;
	private static final String PORT_NAME="COM2";
	private static final int BPS = 9600;
	Thread readThread;

	private static final int READ_TIME_OUT = 30000;
	private static final String SERIAL_NAME_READ = "_DEFULT_SERIAL_READ";
	private static final String SERIAL_NAME_WRITE = "_DEFULT_SERIAL_WRITE";
	
	
	public SerialRead(){
		read();
	}
	
	public void read(){
		System.out.println("SerialRead.read()"+this.readThread.currentThread());
		try {
			/* open方法打开通讯端口，获得一个CommPort对象。
			 * 它使程序独占端口。如果端口正被其他应用程序占用，
			 * 将使用 CommPortOwnershipListener事件机制，
			 * 传递一个PORT_OWNERSHIP_REQUESTED事件。
			 * 每个端口都关联一个 InputStream 和一个OutputStream。
			 * 如果端口是用open方法打开的，那么任何的getInputStream都将返回相同的数据流对象，
			 * 除非有close 被调用。有两个参数，第一个为应用程序名；第二个参数是在端口打开时阻塞等待的毫秒数。*/
			commPort = (SerialPort) portId.open(SERIAL_NAME_READ, 2000);
		} catch (PortInUseException e) {
        	String errorMsg = "系统异常: 串口["+portId.getName()+"]被占用";
        	LogUtil.error(errorMsg, e);
		}
		/*获取端口的输入流对象*/
		try {
			in = commPort.getInputStream();
		} catch (IOException e) {
			LogUtil.error("SerialRead获取输入流异常", e);
		}
		/*注册一个SerialPortEventListener事件来监听串口事件*/
		try {
			commPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			commPort.close();
			String errorMsg = "系统异常: 串口["+portId.getName()+"]监听者过多";
			LogUtil.error(errorMsg, e);
		}
		commPort.notifyOnDataAvailable(true);/*数据可用*/
		if ( commPort instanceof SerialPort ) {
		    SerialPort serialPort = (SerialPort) commPort;
		    try {
				serialPort.setSerialPortParams(BPS,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				String errorMsg = "系统异常: 串口["+portId.getName()+"]操作命令不支持";
				LogUtil.error(errorMsg, e);
			}
		} else {
			LogUtil.debug("Error: Only serial ports are handled.");
		}
		readThread = new Thread(this);
		readThread.start();
		
	}
	
	public void read(String portName, int bps){
		try {
			/* open方法打开通讯端口，获得一个CommPort对象。
			 * 它使程序独占端口。如果端口正被其他应用程序占用，
			 * 将使用 CommPortOwnershipListener事件机制，
			 * 传递一个PORT_OWNERSHIP_REQUESTED事件。
			 * 每个端口都关联一个 InputStream 和一个OutputStream。
			 * 如果端口是用open方法打开的，那么任何的getInputStream都将返回相同的数据流对象，
			 * 除非有close 被调用。有两个参数，第一个为应用程序名；第二个参数是在端口打开时阻塞等待的毫秒数。*/
			commPort = (SerialPort) portId.open(SERIAL_NAME_READ, 2000);
		} catch (PortInUseException e) {}
		/*获取端口的输入流对象*/
		try {
			in = commPort.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*注册一个SerialPortEventListener事件来监听串口事件*/
		try {
			commPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		commPort.notifyOnDataAvailable(true);/*数据可用*/
		try {
			if ( commPort instanceof SerialPort ) {
		        SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(bps,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	        } else {
            	LogUtil.debug("Error: Only serial ports are handled.");
	        }
			
		} catch (Exception e) {
		}
		readThread = new Thread(this);
		readThread.start();
		
	}
	
	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void write(String message){
		try {
			commPort = (SerialPort) portId.open(SERIAL_NAME_WRITE,2000);
			commPort.enableReceiveTimeout(READ_TIME_OUT);
			if ( commPort instanceof SerialPort ) {
		        SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(BPS,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
				out = serialPort.getOutputStream();
				out.write(message.getBytes());
				out.flush();out.close();
	        } else {
	            LogUtil.debug("Error: Only serial ports are handled.");
	        }
		}catch (PortInUseException e) {
			String errorMsg = "系统异常: 串口["+portId.getName()+"]写被占用";
        	LogUtil.error(errorMsg, e);
		}catch (UnsupportedCommOperationException e) {
			String errorMsg = "系统异常: 串口["+portId.getName()+"]写操作命令不支持";
			LogUtil.error(errorMsg, e);
		}catch (IOException e){
			String errorMsg = "系统异常: 串口["+portId.getName()+"]写打开异常";
			LogUtil.error(errorMsg, e);
		}finally{
			close();
		}
	}
	
	public static void connect ( String portName, int bps){
        CommPortIdentifier portIdentifier;
	try {
	    portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() ) {
		    String errorMsg = "系统异常|SYSRROR: 串口|SERIAL["+portName+"]被占用|OCCUPIED";
		    LogUtil.error(errorMsg);
		} else {
		    commPort = (SerialPort) portIdentifier.open(SERIAL_NAME_WRITE,2000);
		    commPort.enableReceiveTimeout(READ_TIME_OUT);
		    if ( commPort instanceof SerialPort ) {
		        SerialPort serialPort = (SerialPort) commPort;
	                serialPort.setSerialPortParams(bps,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
	                in = serialPort.getInputStream();
	                out = serialPort.getOutputStream();
	            } else {
	            	LogUtil.debug("Error: Only serial ports are handled.");
	            }
		    }
        } catch (NoSuchPortException e) {
		    String errorMsg = "系统异常|SYSRROR: 没有检测到串口|NOT FOUND SERIAL["+portName+"]";
		    LogUtil.error(errorMsg,e);
        } catch (PortInUseException e) {
        	String errorMsg = "系统异常: 串口["+portName+"]被占用";
        	LogUtil.error(errorMsg, e);
		} catch (UnsupportedCommOperationException e) {
		    String errorMsg = "系统异常: 串口操作不支持";
		    LogUtil.error(errorMsg, e);
		} catch (IOException e) {
		    String errorMsg = "系统异常: 串口打开异常";
		    LogUtil.error(errorMsg, e);
	    }
    }
	
	
	public static void connect ( String portName, int bps,byte[] data){
        CommPortIdentifier portIdentifier;
	try {
	    portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() ) {
		    String errorMsg = "系统异常|SYSRROR: 串口|SERIAL["+portName+"]被占用|OCCUPIED";
		    LogUtil.error(errorMsg);
		} else {
		    commPort = (SerialPort) portIdentifier.open(SERIAL_NAME_WRITE,2000);
		    commPort.enableReceiveTimeout(READ_TIME_OUT);
		    if ( commPort instanceof SerialPort ) {
		        SerialPort serialPort = (SerialPort) commPort;
	                serialPort.setSerialPortParams(bps,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
	                in = serialPort.getInputStream();
	                out = serialPort.getOutputStream();
	                if(data != null){
	                	out.write(data);
	                	LogUtil.info(data);
	                }else {
	                	in.read();
	                	LogUtil.info(data);
	                }
	            } else {
	            	LogUtil.debug("Error: Only serial ports are handled.");
	            }
		    }
        } catch (NoSuchPortException e) {
		    String errorMsg = "系统异常|SYSRROR: 没有检测到串口|NOT FOUND SERIAL["+portName+"]";
		    LogUtil.error(errorMsg,e);
        } catch (PortInUseException e) {
        	String errorMsg = "系统异常: 串口["+portName+"]被占用";
        	LogUtil.error(errorMsg, e);
		} catch (UnsupportedCommOperationException e) {
		    String errorMsg = "系统异常: 串口操作不支持";
		    LogUtil.error(errorMsg, e);
		} catch (IOException e) {
		    String errorMsg = "系统异常: 串口打开异常";
		    LogUtil.error(errorMsg, e);
	    }finally{
	    	close();
	    }
    }
	
	public static void write(byte[] d){
		System.out.println("SerialConn.write()"+out);
		try {
			if(d != null){
            	out.write(d);
            	LogUtil.info(d);
            }
		} catch (IOException e) {
		}finally{
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}
    
	/** 关闭串口 */
	public static void close() {
	    commPort.close();
	}
	
	public static InputStream getIn() {
		return in;
	}
	
	public static OutputStream getOut() {
		return out;
	}
	
	
	public static void main (String[] arg){
		//byte[] data = "tangwugang".getBytes();
		//SerialRead.connect("COM2", 9600);
		//SerialRead.write(data);
		/*不带参数的getPortIdentifiers方法获得一个枚举对象，
		 * 该对象又包含了系统中管理每个端口的CommPortIdentifier对象。
		 * 注意这里的端口不仅仅是指串口，也包括并口。这个方法还可以带参数。
		 * getPortIdentifiers(CommPort)获得与已经被应用程序打开的端口相对应的CommPortIdentifier对象。
		 * getPortIdentifier(String portName)获取指定端口名（比如“COM1”）的CommPortIdentifier对象。*/
		
		
		Executor executor = Executors.newFixedThreadPool(10);  
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if(portId.isCurrentlyOwned()){
				String errorMsg = "系统异常|SYSRROR: 串口|SERIAL["+portId.getName()+"]被占用|OCCUPIED";
			    LogUtil.error(errorMsg);
			}else{
				/*getPortType方法返回端口类型*/
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					/*找Windows系统下的第一个串口*/
					if (portId.getName().equals(PORT_NAME)) {
						SerialRead reader = new SerialRead();
						executor.execute(reader);
						
						reader.write("holle word");
						//Thread thread = new Thread(reader); 
						 //thread.start();
					}
				}
			}
		}
	}


	public void serialEvent(SerialPortEvent event) {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
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
			byte[] readBuffer = new byte[20];
			try {
			while (in.available() > 0) {
				int numBytes = in.read(readBuffer);
			}
			System.out.print(new String(readBuffer));
			} catch (IOException e) {}
			break;
		}
		
		
	}

	

}
