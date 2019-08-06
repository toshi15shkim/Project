package com.kth.pattern.template;

public class RedTea extends CaffeineBeverage {

    @Override
    public void material() {
        System.out.println("컵에 티백 넣는 중");
    }

    @Override
    public void brew() {
        System.out.println("차 우려내는 중");
    }

    @Override
    public void addCondiments() {
        System.out.println("레몬을 추가하는 중");
    }
}