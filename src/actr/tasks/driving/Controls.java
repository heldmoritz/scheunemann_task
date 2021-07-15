package actr.tasks.driving;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;

import java.util.ArrayList;
import java.util.List;


/**
 * This class deals with getting values from the external steering wheel & accelerator/brake pedals.
 * 
 * @author Nilma Kelapanda
 */

//npk
public class Controls {

    Controller steerPedals = null;

    //function to locate the steering wheel & pedals connected to PC.
    public void startUp() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        // Find the steering wheel
        for (int i = 0; i < controllers.length && steerPedals == null; i++) {
            // System.out.println(controllers[i].getType());
            if (controllers[i].getType() == Controller.Type.STICK) {
                steerPedals = controllers[i];
                break;
            }
        }

        if (steerPedals == null) {
            System.out.println("Found no steering wheel!");
            System.exit(0);
        }
    }

    // function that returns the steer axis, accelerator axis, & brake axis as a list of Doubles.
    public List<Double> getControls() {
        startUp();

        double steer = 9;
        double accel = 9;
        double brake = 9;

        List<Double> controls = new ArrayList<Double>(3);
        
        steerPedals.poll();
        Component[] components = steerPedals.getComponents();
        Component steerPedalInput = null;

        // find the components that log the values
        for (int i = 0; i < components.length && !controlsSet(steer, accel, brake); i++) {
            Component component = components[i];
            Identifier componentIdentifier = component.getIdentifier();
            // System.out.println(component + "\t" + componentIdentifier);

            //steering
            if (componentIdentifier == Component.Identifier.Axis.X) {
                steerPedalInput = component;
                steer = steerPedalInput.getPollData();
            }

            //accelerator & brake values are under one component
            if (componentIdentifier == Component.Identifier.Axis.Y) {
                steerPedalInput = component;

                //negative values correspond to accelerator
                if (steerPedalInput.getPollData() < 0) {
                    accel = steerPedalInput.getPollData();
                    accel = convertAccel(accel);
                    brake = 0;
                } else { //positive values correspond to brake
                    brake  = steerPedalInput.getPollData();
                    brake = convertBrake(brake);
                    accel = 0;
                }
            } 
        }

        //add values to list 
        controls.add(0, steer);
        controls.add(1, accel);
        controls.add(2, brake);
        return controls;
    }

    //converts accelerator range from (0, 0.83) to (0, 1) 
    private double convertAccel(double x) {
        double accel =  - (1 / 0.83) * x;
        return accel <= 1 ? accel : 1;
    }
    
    //converts accelerator range from (0, 0.76) to (0, 1) 
    private double convertBrake(double x) {
        double brake = (1 / 0.76) * x;
        return brake <= 1 ? brake : 1;
    }

    //checks if the steer/accel/brake values have been read from external input
    private boolean controlsSet(double x, double y, double z) {
        if (x == 9 || y == 9 || z == 9) {
            return false;
        } else {
            return true;
        }
    }

    //returns true if button X is pressed
    public boolean buttonXpressed() {
        startUp();
        float buttonX= 0;

        steerPedals.poll();
        Component[] components = steerPedals.getComponents();
        Component steerPedalInput = null;

        // find the components that log the values
        for (int i = 0; i < components.length && buttonX != 9; i++) {
            Component component = components[i];
            Identifier componentIdentifier = component.getIdentifier();
            if (componentIdentifier == Component.Identifier.Button._0) {
                steerPedalInput = component;
                buttonX = steerPedalInput.getPollData();
            }
        }

        return true;//(buttonX == 1.0 ? true : false);
    }

    //method to check if an indicator button on steering wheel has been pressed.
    public int blinkers() {
        //returns a number corresponding to the left or right blinker, else no blinker
        int NOBLINKER = 0;
        int RIGHT = 1;
        int LEFT = 2;
        double rightBlinker = 0;
        double leftBlinker = 0;
 
        startUp();
 
        steerPedals.poll();
        Component[] components = steerPedals.getComponents();
        Component steerPedalInput = null;
 
        // find the components that log the values
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            Identifier componentIdentifier = component.getIdentifier();
            
            if (componentIdentifier == Component.Identifier.Button._4) { //right paddle (R1)
                steerPedalInput = component;
                rightBlinker = steerPedalInput.getPollData();
                if (rightBlinker == 1) {
                    return RIGHT;
                }
            } 

            if (componentIdentifier == Component.Identifier.Button._5) { //left paddle (L1)
                steerPedalInput = component;
                leftBlinker = steerPedalInput.getPollData();
                if (leftBlinker == 1) {
                    return LEFT;
                }           
            }
        }

        return NOBLINKER;
    }

}

