


package threads;

import logger.MyLogger;
import monitor.Monitor;

public class ElevatorController extends Thread implements SomeThread {

	private Monitor monitor;// ссылка на общий монитор
	private boolean flag;

    public ElevatorController(Monitor monitor) {
        this.monitor = monitor;
		flag = true;
    }

    @Override
    public void run() {
		while (flag) {
            synchronized (monitor) {
                goElevator();
                stopDispatcher();
            }
        }
    }

	// метод приостановки диспетчера
    public void stopDispatcher(){
        try {
            monitor.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	// метод для освобождения монитора лифту
    public void goElevator(){
        monitor.notify();
    }

	// метод для прерывания потока
	public void interruptThread() {
		flag = false;
		MyLogger.erroreMessage("Поток диспетчера прерван");
	}
}
