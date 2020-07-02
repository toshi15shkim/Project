package com.roytuts.spring.activiti.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.roytuts.spring.activiti.integration.service.OnboardingService;
import com.roytuts.spring.activiti.integration.vo.User;

@RestController
public class OnboardingController {

	@Autowired
	private OnboardingService onboardingService;

	@PostMapping("/onboard")
	public String startOnboarding(@RequestBody final User user) throws ParseException {
		onboardingService.onboard(user);
		return "Onboarding completed successfully";
	}

}
