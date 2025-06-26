package egovframework.com.muscat.cal.mapper;

import java.io.Serializable;

import lombok.Data;

@Data
public class CalendarVO implements Serializable {
	private String calId;
	private String calName;
	private String color;
	private String note;
	private String addDe;
	private String postDe;
	private String ownerId;

}
