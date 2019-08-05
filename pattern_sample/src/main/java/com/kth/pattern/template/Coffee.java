package com.kth.pattern.template;

public class Coffee extends CaffeineBeverage {

    @Override
    public String brew() {
        return "필터로 커피를 우려내는 중";
    }

    @Override
    public String addCondiments() {
        return "설탕과 커피를 추가하는 중";
    }
}