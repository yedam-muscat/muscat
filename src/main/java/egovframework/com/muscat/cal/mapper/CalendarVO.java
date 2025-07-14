package egovframework.com.muscat.cal.mapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

	private String shareId;
	private String authority;
	private List<String> sharedUserIds;
	
	private String calendarType;
}
