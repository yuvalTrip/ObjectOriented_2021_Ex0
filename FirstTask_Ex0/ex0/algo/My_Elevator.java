package ex0.algo;

import ex0.CallForElevator;
import ex0.Elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class My_Elevator {
    public ElevatorStatus Elvstatus;
    public CallStatus Callstatus;
    public Elevator elevator;
    public ArrayList<Integer> CurrentUp;//initilize 4 arraylist
    public ArrayList<Integer> NextUp;
    public ArrayList<Integer> CurrentDown;
    public ArrayList<Integer> NextDown;
    public int direction;//define direction variable
    public static final int UP = 1, DOWN = -1, LEVEL = 0;


    public enum ElevatorStatus {
        UP, DOWN, LEVEL, EROR;
    }

    public enum CallStatus {
        INIT, GOING2SRC, GOIND2DEST, DONE;
    }

    public My_Elevator(Elevator e) {
        this.elevator = e;
        this.CurrentDown = new ArrayList<Integer>();
        this.CurrentUp = new ArrayList<Integer>();
        this.NextDown = new ArrayList<Integer>();
        this.NextUp = new ArrayList<Integer>();
        this.direction = LEVEL;

    }


    public boolean isNoadditionalStops() {//if the elevator dont move , than the status id LEVEL
        return (CurrentUp.size() == 0 && NextUp.size() == 0 && CurrentDown.size() == 0 && NextDown.size() == 0);

    }

    //function put every new call into the relevant array(fro the 4 arrays we already define)
    public Elevator PutCallsIntoArray(Elevator e, CallForElevator newCall) {
        int BuildingHeight = e.getMaxFloor() - e.getMinFloor();//building height
        if ((e.getState() == UP || e.getState() == Elevator.LEVEL) &&//if the elevator move up or in leve status, and the call is also to direction up
                newCall.getType() == UP) {
            if (e.getPos() < newCall.getSrc()) {//check if the elevator is in lower floor from the call
                if (CurrentUp.contains(newCall.getSrc()) == false) {//if the call is not already in the array
                    CurrentUp.add(newCall.getSrc());//than we will add it
                }
                if (CurrentUp.contains(newCall.getDest()) == false) {//if the destination of the call is not already in the array
                    CurrentUp.add(newCall.getDest());//than we will add the destination
                }
                Collections.sort(CurrentUp);//after we add the new call (source and the destination) we will sort the array
                return e;
            } else {
                if (NextUp.contains(newCall.getSrc()) == false) {//if the elvator is in higher floor than the new call, and not appear in the NextUp array
                    NextUp.add(newCall.getSrc());//than we will add it to the NextUp array
                }

                if (NextUp.contains(newCall.getDest()) == false) {//same for the destination floor of the new call
                    NextUp.add(newCall.getDest());
                }
                Collections.sort(NextUp);//after we add the new call (source and the destination) we will sort the array
                return e;
            }

        }
        if ((e.getState() == Elevator.DOWN || e.getState() == Elevator.LEVEL) &&
                newCall.getType() == Elevator.DOWN) {//if the elevator move down or in leve status, and the call is also to direction down
            if (e.getPos() > newCall.getSrc()) {//check if the elevator is in higher floor from the call
                if (CurrentDown.contains(newCall.getSrc()) == false) {//if the call is not already in the array
                    CurrentDown.add(newCall.getSrc());//than we will add it
                }
                if (CurrentDown.contains(newCall.getDest()) == false) {//if the destination of the call is not already in the array
                    CurrentDown.add(newCall.getDest());//than we will add the destination
                }
                Collections.sort(CurrentDown);//after we add the new calls we will sort the array
                return e;
            } else {
                if (NextDown.contains(newCall.getSrc()) == false) {//if the source of the call is not already in the array
                    NextDown.add(newCall.getSrc());//we will add the source floor of the call
                }

                if (NextDown.contains(newCall.getDest()) == false) {//if the destination of the call is not already in the array
                    NextDown.add(newCall.getDest());//than we will add the destination
                }
                Collections.sort(NextDown);//after we add the new call (source and the destination) we will sort the array
                return e;
            }
        }

        if (e.getState() != newCall.getType()) {//if the direction of the new call and the elvators are different
            if ((e.getState() == Elevator.UP || e.getState() == Elevator.LEVEL)
                    && newCall.getType() == Elevator.DOWN) {
                if (CurrentDown.contains(newCall.getSrc()) == false) {//if the source of the call is not already in the array
                    CurrentDown.add(newCall.getSrc());//we will add the source floor of the call
                }
                if (CurrentDown.contains(newCall.getDest()) == false) {//if the destination of the call is not already in the array
                    CurrentDown.add(newCall.getDest());//than we will add the destination
                }
                Collections.sort(CurrentDown);//after we add the new call (source and the destination) we will sort the array
                return e;
            }

        } else {
            if ((e.getState() == Elevator.DOWN || e.getState() == Elevator.LEVEL)
                    && newCall.getType() == Elevator.UP) {
                if (CurrentUp.contains(newCall.getSrc()) == false) {//if the source of the call is not already in the array
                    CurrentUp.add(newCall.getSrc());//we will add the source floor of the call
                }
                if (CurrentUp.contains(newCall.getDest()) == false) {//if the destination of the call is not already in the array
                    CurrentUp.add(newCall.getDest());//than we will add the destination
                }
            }
            Collections.sort(CurrentUp);//after we add the new call (source and the destination) we will sort the array
            return e;
        }
        return e;
    }

}