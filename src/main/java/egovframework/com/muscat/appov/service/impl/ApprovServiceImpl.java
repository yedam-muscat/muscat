package egovframework.com.muscat.appov.service.impl;

import org.springframework.stereotype.Service;

import egovframework.com.muscat.appov.service.ApprovService;

@Service
public class ApprovServiceImpl implements ApprovService {

	@Override
	public String test() {
		return "test";
	}

}
