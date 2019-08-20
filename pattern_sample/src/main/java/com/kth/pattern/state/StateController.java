package com.kth.pattern.state;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/state")
public class StateController {
    @RequestMapping("/g")
    public String origin() {
        //알맹이 2개만 넣고 테스트
        GumballMachineBean gumballMachine = new GumballMachineBean(2);
        gumballMachine.insertQuarter();     //동전 넣기
        gumballMachine.turnCrank();         //손잡이 돌리기

        System.out.println("------------");

        gumballMachine.insertQuarter();     //동전 넣기
        gumballMachine.ejectQuarter();      //동전 반환하기

        System.out.println("------------");

        gumballMachine.insertQuarter();     //동전 넣기
        gumballMachine.turnCrank();         //손잡이 돌리기

        return "Success";
    }
}