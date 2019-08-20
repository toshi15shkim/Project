package com.kth.pattern.state.state_bean;

import com.kth.pattern.state.GumballMachine;

public class SoldOutState implements State {
    GumballMachine gumballMachine;

    public SoldOutState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("알맹이가 모두 소진되었습니다.");
    }
    public void ejectQuarter() {    
        System.out.println("알맹이가 모두 소진되었습니다.");
    }
    public void turnCrank() {
        System.out.println("알맹이가 모두 소진되었습니다.");
    }
    public void dispense() {
        System.out.println("알맹이가 모두 소진되었습니다.");
    }
}