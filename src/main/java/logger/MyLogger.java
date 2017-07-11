


package logger;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;

public class MyLogger {

	private static Logger logger = Logger.getLogger(MyLogger.class);
	private static JTextArea textArea;// сылка на компонент панели, для
										// лоигирования в нее
	private static String infoMessage = "\n(n;m) первая цифра этаж входа,\n вторая цифра необходимый этаж";
    private MyLogger() {
    }

	// логирование сообщения о действии
    public static void sendMessage(String message){

		logger.info(message);
		textArea.setText(message + infoMessage);
    }

	public static void setTextArea(JTextArea area) {
    	textArea=area;
    }
	public static void erroreMessage(String message) {
		logger.error(message);
	}

}
