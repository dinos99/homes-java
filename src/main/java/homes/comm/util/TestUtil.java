package homes.comm.util;

import java.util.Optional;

public class TestUtil {

	public static String[] getHeader() {
		String[] headers = {
			"buldRegstrPk"       , "regstrGbCd"   , "regstrGbNm"    , "regstrKdCd"   , "regstrKdNm"     , "plotLoc"       , "rdnmPloLoc"   , "buldNm"         , "arcd"           , "legCd", 
			"plotGbCd"           , "bun"         , "ji"           , "spLndNm"      , "block"          , "lot"           , "outLotCo"     , "nwAddrOadCd"    , "nwAddrEgCd"     , "nwAddrNdCd", 
			"nwAddrStB"          , "nwAddrUbB"   , "blockNm"      , "mainSubGbCd"  , "mainSubGbNm"    , "plotAr"        , "buldAr"       , "buldLndRt"      , "totalAr"        , "bulkCalcTotAr", 
			"bulkRt"             , "strctCd"     , "strctCdNm"    , "etcStrct"     , "mainPpsCd"      , "mainPpsCdNm"   , "etcPps"       , "rfCd"           , "rfCdNm"         , "etcRf",
			"hshldCo"            , "fmlyCo"      , "height"       , "grndFloorCo"  , "undrGrndFloorCo", "rdngElvtrCo"   , "emgncElvtrCo" , "subBuldCo"      , "subBuldAr"      , "totBlockAr", 
			"inMechaCo"          , "inMechaAr"   , "outMechaCo"   , "outMechaAr"   , "inIndpntCo"     , "inIndpntAr"    , "outIndpntCo"  , "outIndpntAr"    , "prmissDe"       , "stwkDe", 
			"occupancyApprovalDe", "prmissNoYyyy", "prmissNoOrgCd", "prmissNoOrgNm", "prmissNoGbCd"   , "prmissNoGbNm"  , "unitCo"       , "energyEfcnyGrad", "energyRedcnRt"  , "energyEpiScr", 
			"ecoBuldGrad"        , "ecoBuldScr"  , "brinBuldGrad" , "brinBuldScr"  , "creatDe"        , "erdsgnApplcYn" , "rserthqkAblty", "fstUploadDt"    , "fstUploadUserNo", "lstChngDt", 
			"lstChngUserNo"      , "batchYn"     , "createUserNo" , "creatDt"      , "updtUserNo"     , "updtDt"
		} ; 
		return headers ; 
	}
	
	public static void main( String[] args ) {
/*
		String txt = "arCodeList1";
		boolean result1 = txt.matches("[A-Z0-9]+"); // 숫자로 이루어져 있는지
		System.out.println(result1);
		
		Long timestamp = System.currentTimeMillis() ;
        Date date = new Date();
        date.setTime(timestamp);
        String format = "yyyy년 MM월 dd일 HH시 mm분 ss초" ;
        System.out.println(new SimpleDateFormat(format, Locale.KOREA).format(date)) ;
        System.out.println(new SimpleDateFormat(format, Locale.KOREAN).format(date)) ;
		  
 */
		float fVal = StringUtil.getFloatValue("4900315") ;
		System.out.println("*** fVal: " + fVal) ;	

		String floatStr = Optional.ofNullable("4900315").orElse("") ; 
		floatStr = "".equals(floatStr) ? String.valueOf(0.00f) : floatStr ;
		System.out.println(Float.parseFloat(floatStr)) ;
		
		String sdcode = "34000" ; 
		System.out.println(sdcode.substring(0, 2)) ;

	}
	
}
