package egovframework.com.muscat.cal.mapper;

import java.io.Serializable;

import lombok.Data;

@Data
public class ScudVO implements Serializable {
	
	/** 리더 아이디 */
	private String leaderId;
	
	private String shareId;
	
	private String schdulId;
	
	private String roomId; 
	
	private String roomName;
	
	/** 캘린더 아이디 */
	private String calId;

	/** 일정구분(회의/교육/세미나/강의 기타) */
	private String schdulSe;

	/** 일정부서ID */
	private String schdulDeptId;

	/** 일정종류(부서일정/개인일정) */
	private String schdulKindCode;

	/** 일정시작일자 */
	private String schdulBgnde;

	/** 일정종료일자 */
	private String schdulEndde;

	/** 일정명 */
	private String schdulNm;

	/** 일정내용 */
	private String schdulCn;

	/** 일정장소 */
	private String schdulPlace;

	/** 일정중요도코드 */
	private String schdulIpcrCode;

	/** 일정담담자ID */
	private String schdulChargerId;

	/** 첨부파일ID */
	private String atchFileId;

	/** 반복구분(반복, 연속, 요일반복) */
	private String reptitSeCode;

	/** 최초등록시점 */
	private String frstRegisterPnttm = "";

	/** 최초등록자ID */
	private String frstRegisterId = "";

	/** 최종수정시점 */
	private String lastUpdusrPnttm = "";

	/** 최종수정ID */
	private String lastUpdusrId = "";

	/** 일정시작일자(시간) */
	private String schdulBgndeHH = "";

	/** 일정시작일자(분) */
	private String schdulBgndeMM = "";

	/** 일정종료일자(시간) */
	private String schdulEnddeHH = "";

	/** 일정종료일자(분) */
	private String schdulEnddeMM = "";

	/** 일정시작일자(Year/Month/Day) */
	private String schdulBgndeYYYMMDD = "";

	/** 일정종료일자(Year/Month/Day) */
	private String schdulEnddeYYYMMDD = "";

	/** 담당부서 */
	private String schdulDeptName = "";

	/** 담당자명 */
	private String schdulChargerName = "";
	
	private String reservedRoom;

}
