package panels;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logger.MyLogger;

public class MainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image imageLift;
	private Image passenger;
	private int x;
	private int y;
	private int xPas;
	private int yPas;
	private int flag;
	private ArrayList<Integer> coordinates;

	public MainPanel() {
		x = 245;
		y = 361;
		flag=1;
		try {
			imageLift = ImageIO.read(new File("src/main/resources/static/lift.jpg"));
			passenger = ImageIO.read(new File("src/main/resources/static/passager.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
			MyLogger.erroreMessage("Ошибка при загрузке изображений");

		}

	}

	// метод прорисовки панели
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < coordinates.size(); i++) {
			g2.drawLine(10, coordinates.get(i), 700, coordinates.get(i));
		}
		g2.drawLine(10, 361, 700, 361);

		g2.drawImage(imageLift, x, y, null);
		if(flag==2){
			g2.drawImage(passenger, xPas, yPas, null);
		}
	}

	// прорисовка движения лифта вниз
	public void moveDown() {
		flag=1;
		for (int i = 0; i < 70; i++) {
			y = y + 1;
			repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				MyLogger.erroreMessage("Поток лифта прерван");
			}

		}
	}

	// прорисовка движения лифта вверх
	public void moveUp() {
		flag=1;
		for (int i = 0; i < 70; i++) {
			y = y - 1;
			repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				MyLogger.erroreMessage("Поток лифта прерван");
			}

		}
	}

	// прорисовка вхождения чеовека в лифт
	public void enter(int floor) {
		flag = 2;
		yPas = coordinateNow(floor);
		xPas = 10;
		for (int i = 10; i < 270; i++) {
			repaint();
			xPas = xPas + 1;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				MyLogger.erroreMessage("Поток пассажира прерван");
			}
		}

	}

	// прорисовка выхода человека из лифта
	public void exit(int floor) {
		flag=2;
		xPas = 270;
		yPas = coordinateNow(floor);
		for (int i = 270; i < 540; i++) {
			repaint();
			xPas = xPas + 1;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				MyLogger.erroreMessage("Поток пассажира прерван");
			}
		}

	}

	// установка координат этажей
	public void setCoordinates(ArrayList<Integer> coordinates) {
		this.coordinates = coordinates;

	}

	// получение координат по заданному этажу
	private int coordinateNow(int floor) {
		return (coordinates.get(floor));

	}
}
