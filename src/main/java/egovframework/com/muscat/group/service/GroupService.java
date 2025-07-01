package egovframework.com.muscat.group.service;

import java.util.List;
import java.util.Map;

public interface GroupService {

	// 이거 로그인 쪽에서 쓰고 있음
	List<GroupVO> getGroupChartData(); // 부서
	
	List<GroupwithVO> getGroupData(); // 부서 + 사원
	
	List<GroupVO> getselectGroupUser(); // 부서 + 사원
}
