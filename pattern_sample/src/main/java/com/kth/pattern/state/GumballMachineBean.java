package com.kth.pattern.state;

public class GumballMachineBean {
    final static int SOLD_OUT = 0;      //알맹이 매진
    final static int NO_QUARTER = 1;    //동전 없음
    final static int HAS_QUARTER = 2;   //동전 있음
    final static int SOLD = 3;          //알맹이 판매
    
    //현재 상태를 저장하기 위한 인스턴스 변수
    //초기값은 알맹이 매진 상태
    int state = SOLD_OUT;
    int count = 0;

    public GumballMachineBean(int count) {
        this.count = count;
        if(count > 0) {
            state = NO_QUARTER;
        }
    }

    //동전 투입 행동 구현
    public void insertQuarter() {
        if(state == HAS_QUARTER) {  //동전이 이미 투입되어 있는 경우
            System.out.println("동전은 한 개만 넣어주세요.");
        } else if(state == NO_QUARTER) {    //그렇지 않다면 동전을 받고, HAS_QUARTER 상태로 넘어감
            state = HAS_QUARTER;
            System.out.println("동전을 넣으셨습니다.");
        } else if(state == SOLD_OUT) {      //매진 상태에서는 동전을 반환
            System.out.println("매진되었습니다. 다음 기회를 이용해주세요."); 
        } else if(state == SOLD) {
            System.out.println("잠깐만 기다려주세요. 알맹이가 나가고 있습니다.");
        }
    }

    //동전 반환 행동 구현
    public void ejectQuarter() {
        if(state == HAS_QUARTER) {  //동전이 있으면 반환하고 NO_QUARTER 상태로 전환
            System.out.println("동전이 반환됩니다.");
            state = NO_QUARTER;
        } else if(state == NO_QUARTER) {    //동전이 없다면 돌려줄 필요가 없음
            System.out.println("동전을 넣어주세요.");
        } else if(state == SOLD) {          //손잡이를 돌렸다면 동전을 돌려줄 수 없음
            System.out.println("이미 알맹이를 뽑으셨습니다.");
        } else if(state == SOLD_OUT) {      //매진 상태에서는 동전 투입이 불가하므로 반환 없음
            System.out.println("동전을 넣지 않으셨습니다. 동전이 반환되지 않습니다.");
        }
    }

    //손잡이 돌림 행동 구현
    public void turnCrank() {
        if(state == SOLD) {
            System.out.println("손잡이는 한 번만 돌려주세요.");
        } else if(state == NO_QUARTER) {        //동전을 먼저 넣어야 함
            System.out.println("동전을 넣어주세요.");
        } else if(state == SOLD_OUT) {          //알맹이가 없음
            System.out.println("매진되었습니다.");
        } else if(state == HAS_QUARTER) {       //성공
            System.out.println("손잡이를 돌리셨습니다.");   //상태를 SOLD로 변경하고 dispense 메소드 호출
            state = SOLD;
            dispense();
        }
    }

    //알맹이 내보냄 행동 구현
    public void dispense() {
        if(state == SOLD) {
            System.out.println("알맹이가 나가고 있습니다.");
            count = count - 1;
            if(count == 0) {
                System.out.println("더 이상 알맹이가 없습니다.");
                state = SOLD_OUT;
            } else {
                state = NO_QUARTER;
            }
        }
    }
}