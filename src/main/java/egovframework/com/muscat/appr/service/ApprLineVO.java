package egovframework.com.muscat.appr.service;

import java.util.List;

import lombok.Data;

@Data
public class ApprLineVO {
	private String alineId;
	private String mberId;
	private String mberNm;

    private List<String> approvers;
    private List<String> references;
}
