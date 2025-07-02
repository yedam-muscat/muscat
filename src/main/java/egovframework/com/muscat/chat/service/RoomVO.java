package egovframework.com.muscat.chat.service;

import java.util.Date;

import lombok.Data;

@Data
public class RoomVO {
private String roomId;
private String roomName;
private Date mgDate;
private String roomPw;

}
