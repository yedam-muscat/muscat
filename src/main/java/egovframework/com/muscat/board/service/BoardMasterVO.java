package egovframework.com.muscat.board.service;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardMasterVO{

	/** 게시판 ID */
	private String bbsId; 

	/** 게시판 유형 코드 */
	private String bbsTyCode;

	/** 게시판 이름 */
	private String bbsNm;

	/** 게시판 소개 */
	private String bbsIntrcn;

	/** 답글 가능 여부 (Y/N) */
	private String replyPosblAt;

	/** 파일첨부 가능 여부 (Y/N) */
	private String fileAtchPosblAt;

	/** 첨부가능 파일 수 */
	private int atchPosblFileNumber;

	/** 템플릿 ID */
	private String tmplatId;

	/** 사용 여부 (Y/N) */
	private String useAt;

	/** 커뮤니티 ID (nullable) */
	private String cmmntyId;

	/** 최초 등록자 ID */
	private String frstRegisterId;

	/** 최초 등록 시각 */
	private Date frstRegistPnttm;

}
