package com.kth.pattern.state.state_bean;

import com.kth.pattern.state.GumballMachine;

public class NoQuarterState implements State {
    GumballMachine gumballMachine;

    public NoQuarterState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("동전을 넣으셨습니다.");
        gumballMachine.setState(gumballMachine.getHasQuarterState());
    }
    public void ejectQuarter() {    //동전이 없는 상태에서 반환 요청을 하면 안됨
        System.out.println("동전을 넣어주세요.");
    }
    public void turnCrank() {       //동전이 없는 상태에서 손잡이를 돌릴 수 없음
        System.out.println("동전을 넣어주세요.");
    }
    public void dispense() {        //동전이 없는 상태에서 알맹이는 나오지 않음
        System.out.println("동전을 넣어주세요.");
    }
}