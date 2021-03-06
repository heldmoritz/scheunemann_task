package actr.tasks.driving;

import java.awt.*;

/**
 * 
 * The head-Up Display (HUD) is a source of visual information for the driver.
 * It is to provide two pieces of information to the driver:
 * The speedlimit but only if the driver deviates too much from it
 * The level of automation
 * 
 */

public class HUD {

    Simcar car;
    private int displayedAutomation; // 0: no assistance, 1: semi-autonomous, 2:autonomous
    String automationText;

    public HUD(Simcar simcar) {
        this.car = simcar;
    }

    int dashHeight, hudWidth, hudHeight, hudX, hudY;

    private int nBlinks = 7; // start and end with a blank
    private int blinkDuration = 20; // control the time of the blink n*0.050s
    private int generalBlinkCounter = 0;
    private int instanceBlinkCounter = 0;

    Color hudColor = new Color(204, 204, 204, 80); // semi-transparent Color.lightgray

    void draw(Graphics g, Env env) {
        dashHeight = Env.envHeight / 3; // default: 80, from Simcar.draw()

        hudWidth = Env.envWidth / 6;
        hudHeight = hudWidth / 4;
        hudX = Env.envWidth / 4;
        hudY = Env.envHeight - dashHeight - hudHeight;

        // Head-Up Display (HUD)
        g.setColor(hudColor);
        g.fillRoundRect(hudX, hudY, hudWidth, hudHeight, 5, 5);

        int speed = (int) Utilities.mph2kph(Utilities.mps2mph(car.speed));
        int speedLimit = Integer.parseInt(env.speedsign.speedlimit);

        if (Math.abs(speed - speedLimit) > 0.1 * speedLimit) { // deviation from limit by more than 10%
            drawSpeedWarning(g, env);
        }

        drawAutomationLevel(g, env);
    }

    private void drawSpeedWarning(Graphics g, Env env) {
        // location and sizing
        int r = (int) Math.round((hudHeight * 0.75) / 2); // red circle radius
        int wr = (int) Math.round(0.8 * r); // white circle radius
        int cx = (int) Math.round(hudX + hudWidth - ((float) hudHeight / 2));
        int cy = (int) Math.round(hudY + ((float) hudHeight / 2));

        // red circle
        int x = cx - r;
        int y = cy - r;
        g.setColor(Color.red);
        g.fillOval(x, y, r * 2, r * 2);

        // white circle
        x = cx - wr;
        y = cy - wr;
        g.setColor(Color.white);
        g.fillOval(x, y, wr * 2, wr * 2);

        // text
        String speed = env.speedsign.speedlimit; // This value is dependent on the speed lmit as perceived by the model
        Font myFont = new Font("Helvetica", Font.BOLD, 14);
        g.setFont(myFont);
        g.setColor(Color.black);
        drawCenteredText(cx, cy, g, speed);
    }

    private void drawAutomationLevel(Graphics g, Env env) {
        int cx = Math.round(hudX + (float) (hudWidth - hudHeight) / 2);
        int cy1 = Math.round(hudY + (float) hudHeight / 3);
        int cy2 = cy1 + Math.round((float) hudHeight / 3);
        if (displayedAutomation == 0) {
            automationText = "DRIVER\nCONTROL";
        } else if (displayedAutomation == 1) {
            automationText = "CRUISE\nCONTROL";
        } else if (displayedAutomation == 2) {
            automationText = "PASSENGER\nMODE";
        } else {
            System.out.println("displayedAutomation has the wrong value");
        }



        String[] levelText = automationText.split("\n");
        String levelText1 = levelText[0];
        String levelText2 = levelText[1];

        Font myFont = new Font("Helvetica", Font.PLAIN, 20);
        g.setFont(myFont);
        g.setColor(Color.black);
        drawCenteredText(cx, cy1, g, levelText1);
        drawCenteredText(cx, cy2, g, levelText2);
    }

    private void drawCenteredText(int cx, int cy, Graphics g, String text) {
        FontMetrics fm = g.getFontMetrics();
        int x = cx - fm.stringWidth(text) / 2;
        int y = cy + (fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(text, x, y);
    }

    public void increaseDisplayedAutomation() {
        if (displayedAutomation < 2)
            displayedAutomation += 1;
        else
            System.out.println("Automation level is already at max!");
    }

    public void decreaseDisplayedAutomation() {
        if (displayedAutomation != 0)
            displayedAutomation -= 1;
        else
            System.out.println("Automation level is already at min!");
    }

    public void setDisplayedAaLevel(int displayedAutomation) {
        this.displayedAutomation = displayedAutomation;
    }

}
