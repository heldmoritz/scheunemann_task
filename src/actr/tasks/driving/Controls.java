package actr.tasks.driving;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.util.ElementScanner14;

/**
 * This class deals with getting values from the external steering wheel &
 * accelerator/brake pedals.
 * 
 * @author Nilma Kelapanda
 */

// npk
public class Controls {

    Controller steerPedals = null;

    // function to locate the steering wheel & pedals connected to PC.
    public void startUp() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        // Find the steering wheel
        for (int i = 0; i < controllers.length && steerPedals == null; i++) {
            System.out.println(controllers[i].getType());
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

    public double getAccelerator() {
        return getValue(Component.Identifier.Axis.Y);
    }

    public double getSteering() {
        return getValue(Component.Identifier.Axis.X);
    }

    public boolean buttonXpressed() {
        return (getValue(Component.Identifier.Button._4) == 1.0);
    }

    public String getIndicator() {
        String indicator;
        if (getValue(Component.Identifier.Button._4) == 1) {
            indicator = "right";
        } else if (getValue(Component.Identifier.Button._5) == 1) {
            indicator = "left";
        } else {
            indicator = "";
        }
        return indicator;
    }

    public double getValue(Identifier identifier) {
        steerPedals.poll();
        Component[] components = steerPedals.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            Identifier ident = component.getIdentifier();
            if (ident == identifier){
                return component.getPollData();
            }
        }
        throw new java.lang.Error("These aren't the components you're looking for.");
    }

    /*
     * // method to check if an indicator button on steering wheel has been pressed.
     * public int blinkers() { // returns a number corresponding to the left or
     * right blinker, else no blinker int NOBLINKER = 0; int RIGHT = 1; int LEFT =
     * 2; double rightBlinker = 0; double leftBlinker = 0;
     * 
     * steerPedals.poll(); Component[] components = steerPedals.getComponents();
     * Component steerPedalInput = null;
     * 
     * // find the components that log the values for (int i = 0; i <
     * components.length; i++) { Component component = components[i]; Identifier
     * componentIdentifier = component.getIdentifier();
     * 
     * if (componentIdentifier == Component.Identifier.Button._4) { // right paddle
     * (R1) steerPedalInput = component; rightBlinker =
     * steerPedalInput.getPollData(); if (rightBlinker == 1) { return RIGHT; } }
     * 
     * if (componentIdentifier == Component.Identifier.Button._5) { // left paddle
     * (L1) steerPedalInput = component; leftBlinker =
     * steerPedalInput.getPollData(); if (leftBlinker == 1) { return LEFT; } } }
     * 
     * return NOBLINKER; }
     */
}
