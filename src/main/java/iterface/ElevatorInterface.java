package iterface;

public interface ElevatorInterface {
    int move();

    boolean isEmpty();

    boolean isFull();

    void correctDirection();

    boolean isDirection();

    int getCurrentFloor();

    void addPassenger(int passengerFloor);

    int removePassengers();

    void setDirection(boolean direction);
}
