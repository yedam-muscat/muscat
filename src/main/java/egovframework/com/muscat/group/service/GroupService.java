package egovframework.com.muscat.group.service;

import java.util.List;
import java.util.Map;

public interface GroupService {

	List<GroupVO> getGroupChartData(); // 부서
	
	List<GroupwithVO> getGroupData(); // 부서 + 사원
}
