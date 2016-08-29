/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Obj;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author adrian.stoicescu
 */
public class Light implements Callable<State> {

    private String location;
    private Boolean isOn;
    private int action;//1-turn off;2-turn on

    public Light(String loc, Boolean state, int act) {
        this.location = loc;
        this.isOn = state;
        this.action = act;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getState() {
        return isOn;
    }

    public int getAction() {
        return action;
    }

    @Override
    public State call() throws Exception {
        //added just for simulation delays
        State state = new State();
        Long duration = (long) (Math.random() * 3);
        TimeUnit.SECONDS.sleep(duration);
        state.setField(location);
        state.setState(isOn);
        if (action == 1) {
            if (isOn) {
                state.setMessage("Switching light from : " + location);
                state.setState(!isOn);
            } else {
                state.setMessage("Light from " + location + " was off");
            }
        } else {
            if (isOn) {
                state.setMessage("Light from " + location + " was on");
            } else {                
                state.setMessage("Switching light from : " + location);
                state.setState(!isOn);
            }
        }

        return state;
    }

}
