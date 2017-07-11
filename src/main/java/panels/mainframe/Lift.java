package panels.mainframe;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logger.MyLogger;
import monitor.Monitor;
import panels.MainPanel;
import threads.Elevator;
import threads.ElevatorController;
import threads.Passenger;
import threads.SomeThread;

public class Lift extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static MainPanel contentPane;// основная панель фрейма
	private JTextArea textArea;// область для логирования
	private ActionListener buttonStart;// слушатель для кнопки старта
										// приложениЦя
	private JTextField textField;
	private JTextField textField_1;
	private static ArrayList<Thread> allThreads;// все работающие потоки
	private static JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		startApplication();
	}

	/**
	 * Create the frame.
	 */
	public Lift() {
		allThreads = new ArrayList<Thread>();
		ArrayList<Integer> coordinates = new ArrayList<Integer>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 622, 560);
		contentPane = new MainPanel();
		contentPane.setBackground(new Color(240, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(310, 456, 286, 55);
		contentPane.add(textArea);
		coordinates.add(291);
		coordinates.add(221);
		coordinates.add(151);
		coordinates.add(81);
		coordinates.add(11);
		contentPane.setCoordinates(coordinates);
		
		JLabel label = new JLabel("Вход");
		label.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label.setBounds(8, 372, 91, 46);
		contentPane.add(label);

		JLabel lblNewLabel = new JLabel("Выход");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(540, 372, 86, 46);
		contentPane.add(lblNewLabel);

		this.textField = new JTextField();
		textField.setText("10");
		textField.setBounds(13, 490, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Количество людей");
		lblNewLabel_1.setBounds(109, 490, 97, 14);
		contentPane.add(lblNewLabel_1);

		this.textField_1 = new JTextField();
		textField_1.setText("5");
		textField_1.setBounds(13, 459, 86, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Вместимость лифта");
		lblNewLabel_2.setBounds(107, 463, 123, 14);
		contentPane.add(lblNewLabel_2);
		btnNewButton = new JButton("Старт");
		buttonStart = new StartListener(textField, textField_1);
		btnNewButton.addActionListener(buttonStart);
		btnNewButton.setBounds(8, 429, 89, 23);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Стоп");
		btnNewButton_1.addActionListener(new ActionListener() {
			// слушатель для кнопки остановки приложения
			public void actionPerformed(ActionEvent e) {
				// остановка всех потоков
				for (int i = 0; i < allThreads.size(); i++) {
					SomeThread someThread = (SomeThread) allThreads.get(i);
					someThread.interruptThread();
				}
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				// закрытие фрейма
				dispose();
				// запуск нового фрейма
				startApplication();
			}
		});
		btnNewButton_1.setBounds(107, 429, 89, 23);
		contentPane.add(btnNewButton_1);


	}

	// метод для запуска потока
	public static void startThread(Thread thread) {
		allThreads.add(thread);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
		try {
			Thread.sleep(100);// чтобы лифт стартовал первым
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// метод для запуска списка потоков
	public static void startThreads(ArrayList<Thread> threads) {
		for (Thread thread : threads) {
			allThreads.add(thread);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}
	}

	// заполнение списка пассажиров
	public static ArrayList<Thread> fillListPeople(int countPersons, int countFloors, Monitor monitor) {
		ArrayList<Thread> people = new ArrayList<Thread>();
		int firstFloor = 0;
		int lastFloor = 5;
		int comeInFloor;
		int comeOutFloor;
		for (int i = 0; i < countPersons; i++) {
			do {
				comeInFloor = firstFloor + (int) (Math.random() * lastFloor);
				comeOutFloor = firstFloor + (int) (Math.random() * lastFloor);
			} while (comeInFloor == comeOutFloor);
			people.add(new Passenger(comeInFloor, comeOutFloor, monitor, contentPane));
		}
		return people;
	}

	public static void sendMessages(ArrayList<Thread> threads) {
		for (Thread thread : threads) {
			MyLogger.sendMessage(thread.toString());
		}
	}

	// метод запуска фрейма
	private static void startApplication() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Lift frame = new Lift();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	// класс слушателя для кнопки запуска приложения
	public class StartListener implements ActionListener {
		private JTextField count;
		private JTextField all;

		public StartListener(JTextField count, JTextField all) {
			this.count = count;
			this.all = all;

		}

		public void actionPerformed(ActionEvent e) {
			int countPeople;
			int maxPeople;
			MyLogger.setTextArea(textArea);
			try {
				maxPeople = Integer.parseInt(count.getText());
				countPeople = Integer.parseInt(all.getText());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
				MyLogger.erroreMessage("Ошибка ввода данных");
				return;
			}
			int countFloors = 4;
			Monitor monitor = new Monitor(maxPeople, countPeople);
			ArrayList<Thread> people = fillListPeople(countPeople, countFloors, monitor);
			sendMessages(people);
			startThread(new ElevatorController(monitor));
			startThread(new Elevator(monitor, countFloors, contentPane));
			startThreads(people);
		}
	}
}
