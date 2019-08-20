package com.kth.pattern.state.state_bean;

import com.kth.pattern.state.GumballMachine;

public class HasQuarterState implements State {
    GumballMachine gumballMachine;

    public HasQuarterState(GumballMachine gumballMachine) {
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