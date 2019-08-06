package com.kth.pattern.template;

public class RedTeaBean {

    final void prepareRecipe() {
        boilWater();
        addTeaBag();
        pourInCup();
        steepTeaBag();
        addLemon();
    }

    public void boilWater() {
        System.out.println("물 끓이는 중");
    }

    public void addTeaBag() {
        System.out.println("컵에 티백 넣는 중");
    }

    public void pourInCup() {
        System.out.println("컵에 물 따르는 중");
    }

    public void steepTeaBag() {
        System.out.println("차 우려내는 중");
    }

    public void addLemon() {
        System.out.println("레몬을 추가하는 중");
    }
}