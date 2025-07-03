package egovframework.com.muscat.cal.mapper;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class CalendarShareVO implements Serializable  {

	private String shareId;
    private String authority;
    private String calId;
    private String mberId;
    private Date shDe;
}
