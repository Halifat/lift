
package threads;

import logger.MyLogger;
import monitor.Monitor;
import panels.MainPanel;

public class Elevator extends Thread implements SomeThread {

	private Monitor monitor;// ссылка на общий монитор
	private int countFloors;// общее количество жтажей
	private int numberFirstFloor;// номер первого этажа
	private MainPanel panel;// ссылка на графическую панель для отрисовки
							// движения лифта

	public Elevator(Monitor monitor, int countFloors, MainPanel panel) {
        this.monitor = monitor;
		this.countFloors = countFloors;
		numberFirstFloor = 0;
		this.panel = panel;

    }

    @Override
    public void run() {
		while (check()) {

			synchronized (monitor) {
				// некоторый костыль для корректного перехода от спуска к
				// подъему лифта
				if (monitor.getFloor() <= numberFirstFloor) {
					if (monitor.getFloor() < 0) {
						monitor.setFloor(1);
					}
					// движение лифта ввверх
					for (int i = monitor.getFloor(); i <= countFloors; i++) {
						panel.moveUp();
						MyLogger.sendMessage("Вверх, остановка на " + monitor.getFloor() + " этаже");
						 goDispatcher();
		                 stopElevator();
						monitor.setFloor(monitor.getFloor() + 1);
					}
				}
				// движение лифта вниз
				else if (monitor.getFloor() == (countFloors + 1)) {
					monitor.setFloor(monitor.getFloor() - 2);
					for (int i = countFloors; i > numberFirstFloor; i--) {
						panel.moveDown();
						MyLogger.sendMessage("Вниз, остановка на " + monitor.getFloor() + " этаже");
                        goDispatcher();
                        stopElevator();
						monitor.setFloor(monitor.getFloor() - 1);
                    }
					monitor.setFloor(monitor.getFloor() - 1);
                }
            }
		}
		MyLogger.sendMessage("Все пассажиры доставлены");
    }

	// приостановка лифта для работы других потоков
    public void stopElevator(){
        try {
            monitor.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	// возобновление работы лифта
    public void goDispatcher(){
        monitor.notify();
    }

	// проверка наличия ожидающих/пассажиров
	private boolean check() {
		if (monitor.getAll() != 0) {
			return true;
		} else {
			return false;
		}
	}

	// метод для прерывание потока
	public void interruptThread() {
		monitor.setAll(0);
		MyLogger.erroreMessage("Поток лифта прерван");
	}
}
