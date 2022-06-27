package actr.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import actr.tasks.driving.Driver;

/**
 * This class creates all the required data for the different trials and
 * represents it as an easy-to-use List.
 * Currently each element contains <nBack_level (0-4), construction
 * (true/false)> with a total of 10 elements,
 * conditions are randomized in such a way that the same n-back level isn't
 * driven twice in a row.
 * 
 * @author Milan de Mooij
 */
public class Trials {
    List<Integer> nbackList = new ArrayList<>();
    List<Boolean> roadList = new ArrayList<>();

    
    public void generateTrials() {
        int rng = (int)Math.round(Math.random());

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i <= 4; i++) {
                nbackList.add(i);
                Boolean drivingDiff = ((rng+i) % 2 == 0) ? true : false;
                roadList.add(drivingDiff);
            }
        }
        goodShuffle();
        // Add first 10 trials in reverse order.
        for (int i = 9; i >= 0; i--) {
            nbackList.add(nbackList.get(i));
            roadList.add(roadList.get(i));
        }
    }

    public List<Integer> getNback() {
        return nbackList;
    }

    public List<Boolean> getDriving() {
        return roadList;
    }

    // Till randomization condition is satisfied, continue shuffling.
    public void goodShuffle() {
        Collections.shuffle(nbackList);

        while(!checkNback(nbackList)) 
            Collections.shuffle(nbackList);

    }

    // This method checks if the randomization for n-back is satisfactory.
    public boolean checkNback(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) == list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

}