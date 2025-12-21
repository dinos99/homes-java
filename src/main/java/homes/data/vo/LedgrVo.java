package homes.data.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LedgrVo {

	  private int ridngElvtrCo;
	  private int emgrElvtco;
	  private int groundco;
	  private int underco;
	  private int hshldco;
	  private int fmlyco;
	  private int hosilco;
	  
	  private String htbdno; 
	  private String hbdno; 
	  private String buldRegstrPk;
	  private String buldgb;
	  private String arcd;
	  private String legcd;
	  private String bunjib;
	  private String bunjij;
	  private String buldnm;
	  private String dongnm;
	  private String bdaddr;
	  private String rdaddr;
	  private String ppscd;
	  private String ppsetcnm;
	  private String estcd;
	  private String cfmvgb;
	  private String confde;
	  private String moveinde;
	  
	  private String rdcode;
	  private String rdlegcd;
	  private String underAt;
	  private String rdMainBun;
	  private String rdSubBun;

	  private Float totalAr;
	  private Float supplyAr ; 
	  private Float buldAr;
	  private Float platAr;
	  private Float nvCordLngX;
	  private Float nvCordLatY;
	  private Float koCordLngY;
	  private Float koCordLatX;
	  private Float cordLngX;
	  private Float cordLatY;

	  private String crde;
	  private String batchde;
	  private String batchAt;
	  private String bfBatchAt ; 
	  private String afBatchAt ; 
	  private String useYn;
	  
	  private Long mngrno;

}
