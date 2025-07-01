package egovframework.com.muscat.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.group.mapper.GroupMapper;
import egovframework.com.muscat.group.service.GroupService;
import egovframework.com.muscat.group.service.GroupVO;
import egovframework.com.muscat.group.service.GroupwithVO;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupMapper groupMapper;

	@Override
	public List<GroupVO> getGroupChartData() {
		return groupMapper.selectGroupChart(); // 부서
	}
	
	@Override
	public List<GroupwithVO> getGroupData() {
		return groupMapper.selectgetGroupData(); // 부서 + 사원
	}

	@Override
	public List<GroupVO> getselectGroupUser() {
		List<GroupVO> dept = groupMapper.selectGroupChart();
		List<GroupVO> user = groupMapper.selectGroupUser(); // 부서
		dept.addAll(user);
		return dept;
	}

}
