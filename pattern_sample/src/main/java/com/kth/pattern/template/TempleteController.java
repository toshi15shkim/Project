package com.kth.pattern.template;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/templete")
public class TempleteController {

    @RequestMapping("/t")
    public String orderTea() {
        Tea tea = new Tea();
        tea.prepareRecipe();

        return "차 주문 완료";
    }

    @RequestMapping("/c")
    public String orderCoffee() {
        Coffee coffee = new Coffee();
        coffee.prepareRecipe();
        
        return "커피 주문 완료";
    }
}