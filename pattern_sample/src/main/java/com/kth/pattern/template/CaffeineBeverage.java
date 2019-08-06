package com.kth.pattern.template;

public abstract class CaffeineBeverage {
    
    final void prepareRecipe() {
        StringBuffer sb = new StringBuffer();
        sb.append(boilWater()).append("\r\n");
        sb.append(material()).append("\r\n");
        sb.append(pourInCup()).append("\r\n");
        sb.append(brew()).append("\r\n");
        sb.append(addCondiments()).append("\r\n");

        System.out.println(sb);
    }

    String boilWater() {
        return "물 끓이는 중";
    }

    abstract String material(); //재료 준비

    abstract String brew();   //우려내기

    String pourInCup() {
        return "컵에 물 따르는 중";
    }

    abstract String addCondiments();  //첨가물 추가
}