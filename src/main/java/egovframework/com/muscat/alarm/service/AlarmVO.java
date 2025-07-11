package egovframework.com.muscat.alarm.service;

import java.util.Date;

import lombok.Data;
@Data
public class AlarmVO {
	int ntcnNo;
	String ntcnSj;
	String ntcnCn;
	String ntcnTm;
	String bhNtcnIntrvl;
	Date frstRegistPnttm;
	Date lastUpdtPnttm;
	String frstRegisterId;
	String lastUpdusrId;
	String reciConfirmation;
	String ntcnTy;
	int ntcnContect;
	int ntcnId;
	String ntcnChContect;
	String ntcnChId;
	String mberId;
}
