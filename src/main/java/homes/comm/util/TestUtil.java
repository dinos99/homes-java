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
        
		float fVal = StringUtil.getFloatValue("4900315") ;
		System.out.println("*** fVal: " + fVal) ;	

		String floatStr = Optional.ofNullable("4900315").orElse("") ; 
		floatStr = "".equals(floatStr) ? String.valueOf(0.00f) : floatStr ;
		System.out.println(Float.parseFloat(floatStr)) ;
		
		String sdcode = "34000" ; 
		System.out.println(sdcode.substring(0, 2)) ;
		  
 */
        // 1. 원본 JSON 문자열
        String jsonInput = "{\"mgmBldrgstPk\":1000000000000002931707, \"otherKey\":12345}, {\"mgmBldrgstPk\": 2054564878798743131345678 }";

        // 2. 정규식 패턴 설정
        // 설명: mgmBldrgstPk 뒤의 콜론(:) 다음에 오는 숫자들(\\d+)을 찾아서 그룹화합니다.
        String regex = "\"mgmBldrgstPk\"\\s*:\\s*(\\d+)";
        
        // 3. 변환 실행 ($1은 첫 번째 괄호에서 찾은 숫자 그룹을 의미함)
        String replacement = "\"mgmBldrgstPk\":\"$1\"";
        String result = jsonInput.replaceAll(regex, replacement);

        // 4. 결과 출력
        System.out.println("변환 전: " + jsonInput);
        System.out.println("변환 후: " + result);

	}
	
}
