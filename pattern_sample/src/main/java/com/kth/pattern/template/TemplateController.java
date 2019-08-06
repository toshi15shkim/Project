package com.kth.pattern.template;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/template")
public class TemplateController {

    @RequestMapping("/r")
    public String orderRedTea() {
        //홍차를 만들기 위해선
        //물을 끓이고, 컵에 티백을 넣고, 물을 붓습니다.
        //마지막으로 레몬을 추가합니다.
        //RedTea tea = new RedTea();
        RedTeaBean tea = new RedTeaBean();
        tea.prepareRecipe();

        return "차 주문 완료";
    }

    @RequestMapping("/c")
    public String orderCoffee() {
        //커피를 만들기 위해선
        //물을 끓이고, 커피 두 스푼 덜고, 물을 붓습니다.
        //마지막으로 설탕을 추가합니다.
        Coffee coffee = new Coffee();
        coffee.prepareRecipe();
        
        return "커피 주문 완료";
    }
}