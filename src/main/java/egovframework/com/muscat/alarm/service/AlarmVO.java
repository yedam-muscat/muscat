package egovframework.com.muscat.alarm.service;

import java.util.Date;

import lombok.Data;
@Data
public class AlarmVO {
	int ntcnNo;					//알림번호
	String ntcnSj;				//알림제목
	String ntcnCn;				//알림내용
	String ntcnTm;				//알림시간
	String bhNtcnIntrvl;		//사전알림간격
	Date frstRegistPnttm;		//최초등록시점
	Date lastUpdtPnttm;			//최초수정시점
	String frstRegisterId;		//최초등록자ID
	String lastUpdusrId;		//최초수정자ID
	String reciConfirmation;	//수신확인여부
	String ntcnTy;				//알림구분
	long nttId;					//연결번호
	String bbsId;				//연결ID
	String ntcnChContect;		//채팅연결번호
	String ntcnChId;			//채팅룸ID
	String mberId;				//받는사람
	String readYn;             // 'N': 안 읽음, 'Y': 읽음
}
