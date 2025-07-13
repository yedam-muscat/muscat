package egovframework.com.muscat.appr.service;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApprHistoryVO extends ApprLineDetailVO {
	private String ahistoryId;
	private String ahistoryComment;
	private Date frstRegistPnttm;
	private Date lastUpdtPnttm;
	private String adocId;
}
