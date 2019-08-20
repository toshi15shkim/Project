package com.kth.pattern.state.state_bean;

import com.kth.pattern.state.GumballMachine;

public class SoldState implements State {
    GumballMachine gumballMachine;

    public SoldState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {

    }
    public void ejectQuarter() {
        
    }
    public void turnCrank() {
        
    }
    public void dispense() {
        
    }
}