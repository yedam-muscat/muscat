package egovframework.com.muscat.appr.service;

import java.util.Date;

import lombok.Data;

@Data
public class DocFormVO {
	private String dformId;
	private String dformName;
	private String dformContent;
	private Date frstRegistPnttm;
	private Date lastUpdtPnttm;
	private String dformDesc;
	private String useYn;
}
