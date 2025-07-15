package egovframework.com.muscat.appr.service;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApprDocVO extends ApprLineVO {
	private String adocId;
	private String adocContent;
	private String apprStatus;
	private String alineId;
	private String dformId;
	private String dformName;
	private String dformContent;
	private String adocTitle;
	private Date regDate;
	private Date updDate;
	
}
