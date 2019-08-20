package com.kth.pattern.state.state_bean;

import com.kth.pattern.state.GumballMachine;

public class SoldState implements State {
    GumballMachine gumballMachine;

    public SoldState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("잠깐만 기다려 주세요. 알맹이가 나가고 있습니다.");
    }
    public void ejectQuarter() {    
        System.out.println("이미 알맹이를 뽑으셨습니다.");
    }
    public void turnCrank() {
        System.out.println("손잡이는 한 번만 돌려주세요.");
    }
    public void dispense() {
        gumballMachine.releaseBall();
        if(gumballMachine.getCount() > 0) {     //알맹이가 남아있으면 다시 처음으로
            gumballMachine.setState(gumballMachine.getNoQuarterState());
        } else {        //알맹이 모두 소진
            System.out.println("알맹이가 모두 소진되었습니다.");
            gumballMachine.setState(gumballMachine.getSoldOutState());
        }
    }
}