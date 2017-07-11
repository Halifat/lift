

package threads;

import logger.MyLogger;
import monitor.Monitor;
import panels.MainPanel;

public class Passenger extends Thread implements SomeThread {
	private int comeOutFloor;// необходимый этаж
	private int comeInFloor;// этаж, на котором находится(ожидает) пассажир
	private Monitor monitor;// ссылка на общий монитор
	private boolean isInElevator;
	private boolean flag;
	private MainPanel panel;// ссылка на графическую панель, для отрисовки
							// пассажира

	public Passenger(int comeInFloor, int comeOutFloor, Monitor monitor, MainPanel panel) {
        this.comeOutFloor = comeOutFloor;
        this.comeInFloor = comeInFloor;
        this.monitor = monitor;
        isInElevator = false;
		this.panel = panel;
		flag = true;
    }

    @Override
    public void run() {
		while (flag) {
            synchronized (monitor) {
                int floor = monitor.getFloor();
				int countPerson = monitor.getCountPerson();
				// вход пассажира в лифт
                if(!isInElevator && (comeInFloor == floor) &&
						(countPerson < monitor.getCapacityElevator())) {
                    isInElevator = true;
					monitor.setCountPerson(countPerson + 1);
					MyLogger.sendMessage("Пассажир (" + comeInFloor + ";" + comeOutFloor + ") зашел на "
                            + floor + " этаже");
					panel.enter(floor);
                }
				// выход пассажира их лифта
                else if (isInElevator && comeOutFloor == floor) {

					monitor.setCountPerson(countPerson - 1);
					MyLogger.sendMessage("Пассажир (" + comeInFloor + ";" + comeOutFloor + ") вышел на "
                            + floor + " этаже");
					panel.exit(floor);
					monitor.setAll(monitor.getAll() - 1);
					flag = false;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "comeInFloor=" + comeInFloor +
                ", comeOutFloor=" + comeOutFloor +
                '}';
    }

	// прерывание потока
	public void interruptThread() {
		flag = false;
		MyLogger.erroreMessage("Поток пассажира прерван");
	}
}
