package com.kth.pattern.state.state_bean;

import com.kth.pattern.state.GumballMachine;

public class SoldOutState implements State {
    GumballMachine gumballMachine;

    public SoldOutState(GumballMachine gumballMachine) {
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