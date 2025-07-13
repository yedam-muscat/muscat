package egovframework.com.muscat.appr.service;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApprDocVO extends ApprLineVO{
	private String adocId;
	private String mberId;
	private Map<String, Object> adocContent;
	private String apprStatus;
	private String alineId;
	private String dformId;
	private String adocTitle;
	
}
