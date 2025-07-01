package egovframework.com.muscat.group.service;

import lombok.Data;

@Data
public class GroupVO {
	private String id;
    private String parent;
    private String text;
    private String type;
    UserVO data;
}

