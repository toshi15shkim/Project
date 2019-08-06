package com.kth.pattern.template;

public class RedTea extends CaffeineBeverage {

    @Override
    public String material() {
        return "컵에 티백 넣는 중";
    }

    @Override
    public String brew() {
        return "차물 우려내는 중";
    }

    @Override
    public String addCondiments() {
        return "레몬을 추가하는 중";
    }
}