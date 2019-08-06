package com.kth.pattern.template;

public class Coffee extends CaffeineBeverage {

    @Override
    public String material() {
        return "커피 두 스푼 넣는 중";
    }

    @Override
    public String brew() {
        return "필터로 커피를 우려내는 중";
    }

    @Override
    public String addCondiments() {
        return "설탕을 추가하는 중";
    }
}