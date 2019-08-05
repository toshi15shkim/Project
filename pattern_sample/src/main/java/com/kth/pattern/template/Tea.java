package com.kth.pattern.template;

public class Tea extends CaffeineBeverage {
    public String brew() {
        return "차물 우려내는 중";
    }

    public String addCondiments() {
        return "레몬을 추가하는 중";
    }
}