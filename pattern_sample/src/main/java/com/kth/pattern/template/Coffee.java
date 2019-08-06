package com.kth.pattern.template;

public class Coffee extends CaffeineBeverage {

    @Override
    public void material() {
        System.out.println("커피 두 스푼 넣는 중");
    }

    @Override
    public void brew() {
        System.out.println("필터로 커피를 우려내는 중");
    }

    @Override
    public void addCondiments() {
        System.out.println("설탕을 추가하는 중");
    }
}