package com.kth.pattern.state;

import com.kth.pattern.state.state_bean.State;
import com.kth.pattern.state.state_bean.HasQuarterState;
import com.kth.pattern.state.state_bean.NoQuarterState;
import com.kth.pattern.state.state_bean.SoldOutState;
import com.kth.pattern.state.state_bean.SoldState;

public class GumballMachine {
    //모든 상태 객체 선언 (State 형식)
    State soldOutState;
    State noQuarterState;
    State hasQuarterState;
    State soldState;

    State state = soldOutState;
    int count = 0;

    public GumballMachine(int numberGumballs) {
        soldOutState = new SoldOutState(this);
        noQuarterState = new NoQuarterState(this);
        hasQuarterState = new HasQuarterState(this);
        soldState = new SoldState(this);
        this.count = numberGumballs;
        if(numberGumballs > 0) {
            state = noQuarterState;
        }
    }

    public void insertQuarter() {
        state.insertQuarter();
    }
 
    public void ejectQuarter() {
        state.ejectQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
        state.dispense();
    }

    void setState(State state) {
        this.state = state;
    }

    void releaseBall() {
        System.out.println("알맹이가 나왔습니다.");
        if(count != 0) {
            --count;
        }
    }
}