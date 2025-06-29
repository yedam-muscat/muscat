package egovframework.com.muscat.group.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.muscat.group.service.GroupTreeService;

@Controller
@RequestMapping("/group")
public class GroupTreeController {

    @Resource(name = "groupTreeService")
    private GroupTreeService groupTreeService;

    @GetMapping("/treeData.do")
    @ResponseBody
    public List<Map<String, Object>> getTreeData(@RequestParam(required = false) String id) {
        return groupTreeService.getTreeData(id);
    }
}



