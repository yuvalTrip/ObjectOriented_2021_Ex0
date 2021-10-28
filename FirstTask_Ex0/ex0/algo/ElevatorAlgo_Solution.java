package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ElevatorAlgo_Solution implements ElevatorAlgo {
    public static final int UP = 1, DOWN = -1, LEVEL=0;
    private int direction;
    private Building building;
    private Elevator elevator;
    private My_Elevator[] MyelvArrays;
    private long total1Time;
    private long total2Time;


    public ElevatorAlgo_Solution(Building b) { //building constructor
        this.MyelvArrays = new My_Elevator[b.numberOfElevetors()];
        this.building = b;
        this.direction = UP;
        /*
        an array of My_Elevator (a class we created) with all the elevator in the building.
        initialization the array by new Elevator.
        */
        for (int i = 0; i < b.numberOfElevetors(); i++) {
            MyelvArrays[i] = new My_Elevator(b.getElevetor(i));
        }
    }

    @Override
    public Building getBuilding() {
        return building;
    }

    @Override
    public String algoName() {
        return "Best Elevator";
    }

    @Override
    //get  new call and match the elevator with the min time
    public int allocateAnElevator(CallForElevator c) {
        My_Elevator [] tempMyElevator= MyelvArrays.clone();//save a copy of MyelvArrays array.
        int temMinElv = 0;// define min index
        MyelvArrays[temMinElv].PutCallsIntoArray(building.getElevetor(temMinElv), c);//we will add the new calls into the elevator with the shortest time
        double minTime = CalculateRouteTime(temMinElv, c); // default minTime- first elevator
        int numberOfElevetors1= building.numberOfElevetors();
        for (int i = 1; i < numberOfElevetors1; i++) { // go all over the elevator in the building
            MyelvArrays[i].PutCallsIntoArray(building.getElevetor(i), c);//we will add the new calls into every elevator in the building
            double tempValue = CalculateRouteTime(i, c); // if there is elevator with shorter time , we will define it as the new minElevator
            if (tempValue < minTime) {
                minTime = tempValue;
                temMinElv = i;
            }
        }
        MyelvArrays= tempMyElevator.clone();//return to the original array
        My_Elevator curr = MyelvArrays[temMinElv]; //the elevator with the min time
        curr.PutCallsIntoArray(building.getElevetor(temMinElv), c);//we will add the new calls into the elevator with the shortest time
        return temMinElv; //return the index of the min elevator
    }

    //function get index of elevator and compute how long it will take to do all the route
    public double CalculateRouteTime (int ElveIndex, CallForElevator c){
        ArrayList<Integer> CurrentPath = new ArrayList <Integer>();//create one array for all route (with all calls)
        My_Elevator curr = MyelvArrays[ElveIndex]; // get the My_Elevator with the index that given
        Elevator e = curr.elevator; // get the Elevator from this elevator
        CurrentPath.add(e.getPos());//add to the CurrentPath list the current position of the elevator
        if((curr.direction==UP || curr.direction==LEVEL) && c.getType() == UP){ //check direction of the new call and the elevator
            CurrentPath.addAll(curr.CurrentUp); //we will set the route by this order
            CurrentPath.addAll(curr.CurrentDown);
            CurrentPath.addAll(curr.NextUp);
            CurrentPath.addAll(curr.NextDown);
        }

        else if ((curr.direction == DOWN || curr.direction ==LEVEL)&&
                c.getType() == Elevator.DOWN)
        {
            CurrentPath.addAll(curr.CurrentDown);//we will set the route by this order
            CurrentPath.addAll(curr.CurrentUp);
            CurrentPath.addAll(curr.NextDown);
            CurrentPath.addAll(curr.NextUp);
        }
        double FinalRouteTime=0;
        for (int i=0;i<CurrentPath.size()-1; i++){//we will run above each 2 close index (means each source and destination in the route)
            FinalRouteTime=FinalRouteTime+Src_To_Dst(CurrentPath.get(0), CurrentPath.get(1),ElveIndex); // calculate the total time between 2 floors
            CurrentPath.remove(0);
        }
        return FinalRouteTime;
    }

    @Override
    public void cmdElevator(int tempMinElv) {
        My_Elevator curr = MyelvArrays[tempMinElv]; // get the elevator with tempMinElv index from the arrray
        int dir = curr.direction;//define new variable of direction we will change during the function
        if (curr.elevator.getState() == Elevator.LEVEL) { //if the elevator is in LEVEL status
            if (dir == UP || dir==LEVEL){//if the direction is up or level
                if (curr.CurrentUp.size() != 0){//if the array is not empty
                    curr.elevator.goTo(curr.CurrentUp.get(0));//we will send the elevator to the first element in the array
                    curr.CurrentUp.remove(0);//we will remove the first element from the array
                    return;//we will exit from the function,because we finished the deal with this case
                }
                if (curr.CurrentUp.size() == 0) {//if the array is empty
                    curr.CurrentUp.addAll(curr.NextUp);//we will add all NextUp array to CurrentUp array
                    curr.NextUp.clear();//after we 'add all', we will clear all the NextUp array
                    curr.direction=DOWN;//we will define new direction to the elevator because we finished to deal with the current direction
                    if(curr.isNoadditionalStops()){//if there are no more stops than we will define the elevator status as LEVEL
                        curr.direction=LEVEL;
                    }
                    return;//we will exit from the function
                }
            }
            if (dir == DOWN|| dir==LEVEL) {
                if (curr.CurrentDown.size() != 0) {//if the array is not empty
                    curr.elevator.goTo(curr.CurrentDown.get(curr.CurrentDown.size()-1));//we will send the elevator to the last element in the array
                    curr.CurrentDown.remove(curr.CurrentDown.size()-1);//we will remove the last element from the array
                    return;//we will exit from the function
                }
                if (curr.CurrentDown.size() == 0) {//if the array is empty
                    curr.CurrentDown.addAll(curr.NextDown);
                    curr.NextDown.clear();//after we 'add all', we will clear all the NextUp array
                    curr.direction=UP;//we will define new direction to the elevator because we finished to deal with the current direction
                    if(curr.isNoadditionalStops()){
                        curr.direction=LEVEL;//if there are no more stops than we will define the elevator status as LEVEL
                    }
                    return;//we will exit from the function
                }
            }
        }
    }

    public double Src_To_Dst(int src, int dst,int index){ //function compute the time from one floor to another
        My_Elevator curr = MyelvArrays[index];
        Elevator e = curr.elevator;
        int DistBetFloors=Math.abs(src-dst);
        //we will compute the time by using formula x=vt --> t=x/v
        double time = (DistBetFloors / e.getSpeed() )+(e.getStartTime()+e.getStopTime()+e.getTimeForClose()+e.getTimeForOpen());

        return time;
    }
}