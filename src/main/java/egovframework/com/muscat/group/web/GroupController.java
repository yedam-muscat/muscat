package egovframework.com.muscat.group.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.muscat.group.service.GroupService;
@Controller
public class GroupController {
	
 @Autowired GroupService groupService;
 
 @RequestMapping("/group.do")
 public String group() {
	 return "group/" + groupService.group();
 }
}
