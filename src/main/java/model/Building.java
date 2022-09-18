package model;


import iterface.ElevatorInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unchecked")
public class Building {
    private int floors;
    private ElevatorInterface elevator;
    private List<Integer>[] passengersInFloor;
    private static Random random = new Random();
    private static final int STEPS_TO_VIEW = 10; // i will show you 10 steps of work cycle


    public Building(int floors) {
        this.floors = floors;
        elevator = new Elevator(floors);
        passengersInFloor = new List[floors];
        fillRandomPassengers();
    }

    /**
     * This method shows how the elevator work
     */
    public void startCycle() {
        for (int i = 1; i <= STEPS_TO_VIEW; i++) {
            int removedPassengers = this.removePassengersFromLift();
            if (elevator.isEmpty()) //if elevator is empty, recalculate direction
                elevator.setDirection(this.getElevatorDirectionByMajorPartOfPeople());

            int addedPassengers = this.addPassengersToElevator();

            if (removedPassengers == 0 && addedPassengers == 0) i--;
            else {
                createRandomPassengers(removedPassengers); // recreate passengers who leave the elevator (this passengers can`t enter to the lift, because they are busy
                this.showInformation(i, removedPassengers, addedPassengers);
            }
            elevator.move();
        }
    }


    private int addPassengersToElevator() {
        elevator.correctDirection();

        ArrayList<Integer> indexesToDelete = new ArrayList<>();
        for (int i = 0; i < passengersInFloor[elevator.getCurrentFloor() - 1].size() && !elevator.isFull(); i++) {
            if (elevator.isDirection()) { //if lift goes up
                if (passengersInFloor[elevator.getCurrentFloor() - 1].get(i) > elevator.getCurrentFloor()) {//DRY :(
                    indexesToDelete.add(i);
                    elevator.addPassenger(
                            passengersInFloor[elevator.getCurrentFloor() - 1].get(i));
                }
            } else {
                if (passengersInFloor[elevator.getCurrentFloor() - 1].get(i) < elevator.getCurrentFloor()) {
                    indexesToDelete.add(i);
                    elevator.addPassenger(
                            passengersInFloor[elevator.getCurrentFloor() - 1].get(i));
                }
            }
        }
        //delete passengers from queue
        if (indexesToDelete.size() > 0) {
            passengersInFloor[elevator.getCurrentFloor() - 1].subList(0, indexesToDelete.size()).clear();
        }

        return indexesToDelete.size();
    }


    private int removePassengersFromLift() {
        return elevator.removePassengers();
    }


    private void fillRandomPassengers() {
        for (int i = 0; i < floors; i++) {
            passengersInFloor[i] = fillFloor(i + 1);
        }
    }

    private List<Integer> fillFloor(int currentFloor) {
        ArrayList<Integer> floor = new ArrayList<>();
        int passInTheFloor = random.nextInt(11); //0...10
        for (int j = 1; j < passInTheFloor; j++) {
            floor.add(createRandomPassenger(currentFloor));
        }
        return floor;
    }

    private int createRandomPassenger(int currentFloor) {
        int passengerTargetFloor = currentFloor;
        while (passengerTargetFloor == currentFloor)
            passengerTargetFloor = random.nextInt(floors) + 1;

        return passengerTargetFloor;
    }

    private void createRandomPassengers(int count) {
        for (int j = 0; j < count; j++)
            this.passengersInFloor[elevator.getCurrentFloor() - 1].add(
                    createRandomPassenger(elevator.getCurrentFloor()));
    }

    private boolean getElevatorDirectionByMajorPartOfPeople() {
        if (elevator.getCurrentFloor() == 1) return true;
        else if (elevator.getCurrentFloor() == floors) return false;
        else {
            int peoplesWhoWantUp = 0;
            for (int i = 0; i < passengersInFloor[elevator.getCurrentFloor() - 1].size(); i++)
                if (passengersInFloor[elevator.getCurrentFloor() - 1].get(i) > elevator.getCurrentFloor())
                    peoplesWhoWantUp++;

            return passengersInFloor[elevator.getCurrentFloor() - 1].size() - peoplesWhoWantUp < peoplesWhoWantUp;
            //if peoples who want to go up less then another peoples set direction false(down)
            // true otherwise
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = floors - 1; i >= 0; i--) {
            if (elevator.getCurrentFloor() != i + 1)
                result.append("" + (i + 1) + " floor: " + passengersInFloor[i].toString() + "\n");
            else
                result.append("" + (i + 1) + " floor: " + passengersInFloor[i].toString() + " Lift:{" + elevator + "}\n");
        }
        return result.toString();
    }

    private void showInformation(int step, int removedPassengers, int addedPassengers) {
        System.out.println("----------- Step " + step + " -----------");
        System.out.print(this);
        System.out.println("Leave: " + removedPassengers + " Entry: " + addedPassengers + "\n");
    }
}
