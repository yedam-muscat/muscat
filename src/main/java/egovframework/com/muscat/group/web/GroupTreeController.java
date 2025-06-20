package egovframework.com.muscat.group.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/org")
public class GroupTreeController {

    @GetMapping("/treeData.do")
    @ResponseBody
    public List<Map<String, Object>> getTreeData(@RequestParam(required = false) String id) {
        List<Map<String, Object>> treeData = new ArrayList<>();

        if (id == null || id.isEmpty()) {
            // 루트 노드 반환
            Map<String, Object> root = new HashMap<>();
            root.put("id", "dept-001");
            root.put("text", "회사");
            root.put("children", true);
            treeData.add(root);
        } else {
            // 예시: 하위 노드 반환
            Map<String, Object> child = new HashMap<>();
            child.put("id", id + "-child");
            child.put("text", "하위 부서");
            child.put("children", false);
            treeData.add(child);
        }

        return treeData;
    }

}
