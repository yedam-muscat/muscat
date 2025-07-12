package egovframework.com.muscat.appr.service;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ApprDocVO {
	private String adocId;
	private String mberId;
	private Map<String, Object> adocContent;
	private String apprStatus;
	private String alineId;
	private String dformId;
	private String adocTitle;
	
    private List<String> approvers;
    private List<String> references;
}
