package egovframework.com.muscat.group.mapper;

import java.util.List;

import egovframework.com.muscat.group.service.GroupVO;
import egovframework.com.muscat.group.service.GroupwithVO;

public interface GroupMapper {
	
	List<GroupVO> selectGroupChart();
	List<GroupwithVO> selectgetGroupData();
	List<GroupVO> selectGroupUser();
	}

