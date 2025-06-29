package egovframework.com.muscat.group.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import egovframework.com.muscat.group.service.GroupTreeService;

@Service("groupTreeService")
public class GroupTreeServiceImpl implements GroupTreeService {

    @Override
    public List<Map<String, Object>> getTreeData(String id) {
        List<Map<String, Object>> treeData = new ArrayList<>();

        if (id == null || id.isEmpty()) {
            Map<String, Object> root = new HashMap<>();
            root.put("id", "dept-001");
            root.put("text", "회사");
            root.put("children", true);
            treeData.add(root);
        } else {
            Map<String, Object> child = new HashMap<>();
            child.put("id", id + "-child");
            child.put("text", "하위 부서");
            child.put("children", false);
            treeData.add(child);
        }

        return treeData;
    }

	@Override
	public String grouptree() {
		// TODO Auto-generated method stub
		return null;
	}
}


