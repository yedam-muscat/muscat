package egovframework.com.muscat.chat.service;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class RoomVO {
private String roomId;
private String roomName;
private Date mgDate;
private String roomPw;
private List<UserVO> usered;
}
