package com.kth.pattern.template;

public class CoffeeBean {

    final void prepareRecipe() {
        boilWater();
        addCoffeeSpoon();
        pourInCup();
        brew();
        addSugar();
    }

    public void boilWater() {
        System.out.println("물 끓이는 중");
    }

    public void addCoffeeSpoon() {
        System.out.println("커피 두 스푼 넣는 중");
    }

    public void pourInCup() {
        System.out.println("컵에 물 따르는 중");
    }

    public void brew() {
        System.out.println("필터로 커피를 우려내는 중");
    }

    public void addSugar() {
        System.out.println("설탕을 추가하는 중");
    }
}