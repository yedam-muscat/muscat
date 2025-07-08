package egovframework.com.muscat.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StompMessage {
	public enum Type {
		ENTER, TALK, LEAVE
	}

	private Type type; // 입장, 채팅, 퇴장 구분
	private String roomId; // 방 번호
	private String sender; // 보낸 사람
	private String content; // 메시지
}