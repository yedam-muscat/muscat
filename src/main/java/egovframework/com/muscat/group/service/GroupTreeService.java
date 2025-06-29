package egovframework.com.muscat.group.service;

import java.util.List;
import java.util.Map;

public interface GroupTreeService {
		String grouptree();

		List<Map<String, Object>> getTreeData(String id);
		
}
