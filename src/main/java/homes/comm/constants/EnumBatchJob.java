package homes.comm.constants;

public enum EnumBatchJob {
	TRUNCATE_TOTAL_LEDGR("TRC001", "총괄표제부 초기화 "),
	TRUNCATE_TITLE_LEDGR("TRC002", "표제부 초기화 "),
	
	MSG_NOT_EXIST_JOB_FILE_READY("MSG001", "[대기]디렉토리에 작업파일이 존재하지 않습니다."),
	
	CONV_LEGCD_NULL_TO_CODE_ARC000("ARC000", "표제부-법정동코드변환"),
	
	BJT000("BJT000", "건축물대장-파일분할"),
	BJT001("BJT001", "건축물대장-단지정보등록"),
	BJT002("BJT002", "건축물대장-기본개요-시도별분할"),
	BJT003("BJT003", "건축물대장-기본개요-시군구별분할"),
	
	BLD001("BLD001", "건축물대장-기본개요"),
	BLD002("BLD002", "건축물대장-총괄표제부"),
	BLD003("BLD003", "건축물대장-표제부"),
	BLD005("BLD005", "건축물대장-부속지번"),
	BLD009("BLD009", "건축물대장-전유부"),
	
	BTS_READY("BTS000", "작업대기"),
	BTS_PROC("BTS001" , "작업처리중"),
	BTS_DONE("BTS002" , "작업완료"),
	BTS_ERROR("BTS999", "작업에러"),
	
	SPLIT_BASE_SUMMRY("BDT100" , "기본개요-파일분할"),

	SPLIT_TITLE_LEDGER("BDT300" , "표제부-파일분할"),
	INSERT_TITLE_LEDGER("BDT310", "건축물대장-표제부등록"),	
	
	INSERT_TOTAL_LEDGER("BDT021", "건축물대장-총괄표제부등록"),
	SPLIT_PSSION_LEDGER("BDT040", "표제부-파일분할"),
	INSERT_PSSION_LEDGER("BDT041", "건축물대장-표제부등록"),

	SPLIT_PSSION_AREA("BDT060" , "[06]전유공용면적-지역별파일분할"),
	
	MNG_BASE_SUMMRY("HBT000", "홈즈관리대장 기본개요관리"),
	MNG_LEDGR_MSTR ("HBT001", "홈즈관리대장 총괄표제부관리"),
	MNG_LEDGR      ("HBT002", "홈즈관리대장 표제부관리"),
	
	BATCH_AT_000("0", "작업대기"), 
	BATCH_AT_010("1", "기본개요등록완료"),
	BATCH_AT_020("2", "총괄표제부등록완료"),
	BATCH_AT_030("3", "표제부등록완료")
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
