package com.kth.pattern.template;

public abstract class CaffeineBeverage {

    final void prepareRecipe() {
        boilWater();
        material();
        pourInCup();
        brew();
        addCondiments();
    }

    void boilWater() {
        System.out.println("물 끓이는 중");
    }

    abstract void material(); //재료 준비

    void pourInCup() {
        System.out.println("컵에 물 따르는 중");
    }

    abstract void brew();   //우려내기

    abstract void addCondiments();  //첨가물 추가
}