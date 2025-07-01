package egovframework.com.muscat.cal.mapper;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReservationVO  implements Serializable{

	private String resveId;    
    private String mtgrumId;
    private String rsvctmId;      
    private String resveDe;        
    private String resveBeginTm;   
    private String resveEndTm;     
    private String frstRegisterId; 
    private String lastUpdusrId;   
}
