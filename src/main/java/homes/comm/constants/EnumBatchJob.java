package homes.comm.constants;

public enum EnumBatchJob {
	BJT000("BJT000", "건축물대장-파일분할"),
	BJT001("BJT001", "건축물대장-단지정보등록"),
	BJT002("BJT002", "건축물대장-기본개요-시도별분할"),
	BJT003("BJT003", "건축물대장-기본개요-시군구별분할"),
	
	BLD001("BLD001", "건축물대장-기본개요"),
	BLD002("BLD002", "건축물대장-총괄표제부"),
	BLD003("BLD003", "건축물대장-표제부"),
	BLD005("BLD005", "건축물대장-부속지번"),
	BLD009("BLD009", "건축물대장-전유부"),
	
	BTS000("BTS000", "작업대기"),
	BTS001("BTS001", "작업처리중"),
	BTS002("BTS002", "작업완료"),
	BTS999("BTS999", "작업에러"),
	
	SPLIT_BASE_SUMMRY_RAWDATA("BDT000", "기본개요-RAWDATA분할"),
	INSERT_BASE_SUMMRY_RAWDATA("BDT001", "기본개요-RAWDATA등록"),
	;

    private final String jobcd ;
    private final String jobnm ;
    
    EnumBatchJob(String jobcd, String jobnm ) {
    	this.jobcd = jobcd ; 
    	this.jobnm = jobnm ;
    }

    public String getCode() {
    	return this.jobcd ; 
    }
    
    public String getName() {
    	return this.jobnm ; 
    }

}
