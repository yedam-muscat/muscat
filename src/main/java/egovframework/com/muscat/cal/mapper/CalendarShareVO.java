package egovframework.com.muscat.cal.mapper;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class CalendarShareVO implements Serializable  {

	private String shareId;
    private String authority;
    private String mberId;
    private Date shDe;
    private String shColor;
    private String shereNm;
    private String ownerId;
}
