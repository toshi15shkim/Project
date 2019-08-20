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

    //생성자에서는 알맹이의 총 개수를 인자로 받음
    public GumballMachine(int numberGumballs) {
        //인스턴스 하나씩 생성
        soldOutState = new SoldOutState(this);
        noQuarterState = new NoQuarterState(this);
        hasQuarterState = new HasQuarterState(this);
        soldState = new SoldState(this);

        //알맹이의 개수가 0보다 많으면 noQuarterState 상태로 설정
        this.count = numberGumballs;
        if(numberGumballs > 0) {
            state = noQuarterState;
        }
    }

    public void insertQuarter() {   //동전 투입
        state.insertQuarter();
    }
 
    public void ejectQuarter() {    //동전 반환
        state.ejectQuarter();
    }

    public void turnCrank() {       //손잡이 돌리기
        state.turnCrank();
        state.dispense();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void releaseBall() {
        System.out.println("알맹이가 나왔습니다.");
        if(count != 0) {
            --count;
        }
    }

    public int getCount() {
        return this.count;
    }

    public State getNoQuarterState() {
        return this.noQuarterState;
    }

    public State getHasQuarterState() {
        return this.hasQuarterState;
    }

    public State getSoldOutState() {
        return this.soldOutState;
    }

    public State getSoldState() {
        return this.soldState;
    }
}