


package monitor;


public class Monitor {

	private int countPerson;
	private int capacityElevator;
	private int all;

	private int floor;

	public Monitor(int capacityElevator, int all) {
		this.capacityElevator = capacityElevator;
		this.all = all;
	}

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

	public int getCapacityElevator() {
		return capacityElevator;
	}

	public int getCountPerson() {
		return countPerson;
	}

	public void setCountPerson(int countPerson) {
		this.countPerson = countPerson;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}
}
