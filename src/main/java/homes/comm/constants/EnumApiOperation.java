package homes.comm.constants;

public enum EnumApiOperation {

	REQ_API_LEDGR("/getBrTitleInfo"     , "표제부정보조회"),
	REQ_API_RECAP("/getBrRecapTitleInfo", "총괄표제부정보조회"),
	REQ_API_FLROUTLINE("/getBrFlrOulnInfo", "층별개요정보조회"),
	REQ_API_PSSION("/getBrExposInfo", "전유부조회"),
	REQ_API_PSSION_AREA("/getBrExposPubuseAreaInfo", "전유공용면적")
	;
	
    private final String operation ;
    private final String opernm ;

    EnumApiOperation(String oper, String opernm ) {
    	this.operation = oper ; 
    	this.opernm = opernm ;
    }

    public String getOperation() {
    	return this.operation ; 
    }
    
    public String getOpernm() {
    	return this.opernm ; 
    }
}
