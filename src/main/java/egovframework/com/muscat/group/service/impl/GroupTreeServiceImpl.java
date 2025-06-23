package egovframework.com.muscat.group.service.impl;

import org.springframework.stereotype.Service;

import egovframework.com.muscat.group.service.GroupTreeService;

@Service
public class GroupTreeServiceImpl  implements GroupTreeService {
	
	@Override
	public java.lang.String grouptree() {
		
		return "grouptree";
	}
}
