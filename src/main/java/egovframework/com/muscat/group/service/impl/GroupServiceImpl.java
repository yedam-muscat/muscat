package egovframework.com.muscat.group.service.impl;

import org.springframework.stereotype.Service;

import egovframework.com.muscat.group.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService{
	@Override
	public String group() {
		
		return "group";
	}
}
