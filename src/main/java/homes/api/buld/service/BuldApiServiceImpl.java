package homes.api.buld.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import homes.api.buld.vo.BaseOutlineVo;
import homes.api.buld.vo.BuldApiReqVo;
import homes.api.buld.vo.RecapLedgrVo;
import homes.api.buld.vo.TitleLedgrVo;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuldApiServiceImpl implements BuldApiService {
	public Logger Log = LogManager.getLogger(BuldApiServiceImpl.class) ;
	private final String rType = "json" ; 
	
	/* 기본 파라미터셋 */
	private String getBaseApiParams(BuldApiReqVo reqVo, String rtype) {
		String query = "" ; 
		reqVo.setRtype(rtype);
		query += "?sigunguCd="  + reqVo.getArcd() ;
		query += "&bjdongCd="   + reqVo.getLegcd();
		query += "&bun="        + reqVo.getBunjib() ; 
		query += "&ji="         + reqVo.getBunjij() ;
		query += "&_type="      + reqVo.getRtype() ; 
		query += "&serviceKey=" + reqVo.getServiceKey() ;  
		query += "&numOfRows="  + reqVo.getNumrows();
		query += "&pageNo="     + reqVo.getPage() ;
		return query ; 
	}
	/* 기본 파라미터셋 */
	private String getBaseApiParams(BuldApiReqVo reqVo) {
		return this.getBaseApiParams(reqVo, this.rType) ; 
	}
	
	private String getApiUrl( BuldApiReqVo reqVo, String operation) {
		String apiURL = reqVo.getApiBaseUrl() + operation + this.getBaseApiParams(reqVo);
        Log.info("*** api uri: {}", apiURL) ; 
		return apiURL ;
	}
	
	/* 기본개요 API 조회 */
	@Override
	public BaseOutlineVo getBaseOutlineinfo(String operation, BuldApiReqVo reqVo) {
		String strOutline = this.get(operation, reqVo) ;
        String respCode = this.getApiRespCode(strOutline) ;
        Log.error("*** respCode: {}", respCode) ; 
        BaseOutlineVo outVo = new BaseOutlineVo() ;
    	if ( "00".equals( respCode )) {
    		JSONArray jsonItems = this.getApiDataList(strOutline) ;
    		if ( jsonItems != null && jsonItems.size() > 0) {
    			JSONObject item = (JSONObject)jsonItems.get(0) ;
    			String bregstrPk = StringUtil.getJsonString(item, "mgmBldrgstPk") ;   /* 표제부 Key       */ 
    			String tregstrPk = StringUtil.getJsonString(item, "mgmUpBldrgstPk") ; /* 총괄표제부 Key */
    			String buldid    = StringUtil.getJsonString(item, "bldgId") ;         /* 건물 id */ 
    			String buldgb    = StringUtil.getJsonString(item, "regstrGbCd") ;     /* 건축물 구분: [1]일반건물, [2]집합건물 */
    			
    			reqVo.setBregstrPk(bregstrPk);
    			reqVo.setTregstrPk(tregstrPk);
    			outVo.setBregstrPk(bregstrPk);
    			outVo.setTregstrPk(tregstrPk);
    			outVo.setBuldid(buldid) ;
    			outVo.setBuldgb(buldgb) ;
    			
    			outVo.setBuldnm((String)item.get("bldNm"));
    			outVo.setBdaddr((String)item.get("platPlc"));
    			outVo.setRdaddr((String)item.get("newPlatPlc"));
    			
//    			Log.error("BaseOutlineVo: {}", outVo.toString()) ;
    		}
    	}
		return outVo ;
	}
	/* 총괄표제부 API 조회 */
	public RecapLedgrVo getRecapLedgrinfo(String operation, BuldApiReqVo reqVo ) {
		String strOutline = this.get(operation, reqVo) ;
        String respCode = this.getApiRespCode(strOutline) ;
        Log.error("*** respCode: {}", respCode) ; 
		RecapLedgrVo recapVo = new RecapLedgrVo() ;
    	if ( "00".equals( respCode )) {
    		JSONArray jsonItems = this.getApiDataList(strOutline) ;
    		if ( jsonItems != null && jsonItems.size() > 0) {
    			JSONObject item = (JSONObject)jsonItems.get(0) ;
    			recapVo.setBuldRegstrPk(StringUtil.getJsonString(item, "mgmBldrgstPk")); /* 총괄표제부 Key */
    			recapVo.setBuldgb(StringUtil.getJsonString(item, "regstrGbCd"));     /* 건축물 구분: [1]일반건물, [2]집합건물 */
    			recapVo.setArcd(reqVo.getArcd());
    			recapVo.setLegcd(reqVo.getLegcd());
    			recapVo.setBunjib(reqVo.getBunjib());
    			recapVo.setBunjij(reqVo.getBunjij());
    			recapVo.setInco(0);
    			
    			recapVo.setBdaddr(StringUtil.getJsonString(item   , "platPlc"));        /* 건물주소(지번) */
    			recapVo.setRdaddr(StringUtil.getJsonString(item   , "newPlatPlc"));     /* 건물주소(도로명) */
    			recapVo.setBuldnm(StringUtil.getJsonString(item   , "bldNm"));          /* 건물명 */
    			recapVo.setPpscd(StringUtil.getJsonString(item    , "mainPurpsCd"));    /* 건물_용도_코드 */
    			recapVo.setPpsetcnm(StringUtil.getJsonString(item , "etcPurps"));       /* 건물_용도_기타_명 */
    			recapVo.setPlatgb(StringUtil.getJsonString(item   , "platGbCd"));       /* 대지_구분_코드 */ 
    			recapVo.setCrde(StringUtil.getJsonString(item     , "crtnDay"));        /* 생성일자 */
    			recapVo.setHshldco(StringUtil.getJsonInt(item     , "hhldCnt"));        /* 세대_수(세대) */
    			recapVo.setFmlyco(StringUtil.getJsonInt(item      , "fmlyCnt"));        /* 가구_수(가구) */
    			recapVo.setParkngco(StringUtil.getJsonInt(item    , "totPkngCnt"));     /* 총_주차_수 */
    			recapVo.setBuldco(StringUtil.getJsonInt(item      , "mainBldCnt"));     /* 주_건물수 */
    			recapVo.setBuldsubco(StringUtil.getJsonInt(item   , "atchBldCnt"));     /* 부속_건물수 */
    			recapVo.setRoomco(StringUtil.getJsonInt(item      , "hoCnt"));          /* 호실_수(호실) */ 
    			
    			/* 도로명관련 */
    			recapVo.setRdcode(StringUtil.getJsonString(item   , "naRoadCd"));   /* 도로명_코드 */ 
    			recapVo.setRdlegcd(StringUtil.getJsonString(item  , "naBjdongCd")); /* 도로명_법정동코드 */ 
    			recapVo.setUnderAt(StringUtil.getJsonString(item  , "naUgrndCd"));  /* 지상_지하_구분코드 */

    			String rdmbun = StringUtil.strLpad(StringUtil.getJsonString(item, "naMainBun"), 5, '0') ; 
    			String rdsbun = StringUtil.strLpad(StringUtil.getJsonString(item, "naSubBun"), 5, '0') ; 
    			recapVo.setRdMainBun(rdmbun); /* 도로명_본_번 */
    			recapVo.setRdSubBun(rdsbun);  /* 도로명_부_번 */
    			
    			/* 면적 */
    			Float platAr  = Float.parseFloat(StringUtil.getJsonString(item, "platArea")) ; /* 대지_면적 */
    			Float totalAr = Float.parseFloat(StringUtil.getJsonString(item, "totArea")) ;  /* 총_면적 */
    			Float buldAr  = Float.parseFloat(StringUtil.getJsonString(item, "archArea")) ; /* 건물(건축)_면적 */
    			Float bulkCalcAr = Float.parseFloat(StringUtil.getJsonString(item, "vlRatEstmTotArea")) ; /* 용적률_산정_연면적 */
    			
    			recapVo.setPlatAr(platAr);       
    			recapVo.setTotalAr(totalAr);       
    			recapVo.setBuldAr(buldAr);
    			recapVo.setBulkCalcAr(bulkCalcAr);
    			recapVo.setSupplyAr(0.0f); /* 공급_면적 */
    			
    			/* 비율 */ 
    			Float bulkRt    = Float.parseFloat(StringUtil.getJsonString(item, "vlRat")) ; /* 용적률 */
    			Float buldLndRt = Float.parseFloat(StringUtil.getJsonString(item, "bcRat")) ; /* 건폐율 */
    			recapVo.setBulkRt(bulkRt);
    			recapVo.setBuldLndRt(buldLndRt);
    			
    			recapVo.setUseYn("Y");
    			recapVo.setBatchYn("N");
    		}
    	}
		
		return recapVo ;
	}
	
	/* 표제부 API 조회 */ 
	@Override
	public TitleLedgrVo getTitleLedgrinfo(String operation, BuldApiReqVo reqVo)  {
		String strOutline = this.get(operation, reqVo) ;
        String respCode = this.getApiRespCode(strOutline) ;
        Log.error("*** respCode: {}", respCode) ; 
        TitleLedgrVo bdVo = new TitleLedgrVo() ;
    	if ( "00".equals( respCode )) {
    		JSONArray jsonItems = this.getApiDataList(strOutline) ;
    		if ( jsonItems != null && jsonItems.size() > 0) {
    			JSONObject item = (JSONObject)jsonItems.get(0) ;
    			
    			String confde = StringUtil.getJsonString(item, "useAprDay") ; /* 승인일자 */ 
    			String cfmvgb = !"".equals(confde) ? "1" : "0" ;
    			
    			bdVo.setCfmvgb(cfmvgb);
    			bdVo.setConfde(confde);
    			bdVo.setMoveinde("");
    			
    			bdVo.setTregstrPk("0");    			
    			bdVo.setArcd(reqVo.getArcd());
    			bdVo.setLegcd(reqVo.getLegcd());
    			bdVo.setBunjib(reqVo.getBunjib());
    			bdVo.setBunjij(reqVo.getBunjij());
    			bdVo.setParkngco(0); /* 표제부는 주차대수 없음 */
    			bdVo.setInco(0);

    			/* 면적 */
    			Float platAr  = Float.parseFloat(StringUtil.getJsonString(item, "platArea")) ; /* 대지_면적 */
    			Float totalAr = Float.parseFloat(StringUtil.getJsonString(item, "totArea")) ;  /* 총_면적 */
    			Float buldAr  = Float.parseFloat(StringUtil.getJsonString(item, "archArea")) ; /* 건물(건축)_면적 */
    			bdVo.setPlatAr(platAr) ;   /* 대지_면적 */
    			bdVo.setTotalAr(totalAr) ; /* 총_면적 */
    			bdVo.setBuldAr(buldAr) ;   /* 건물(건축)_면적 */
    			
    			/* 도로명관련 */
    			bdVo.setRdcode(StringUtil.getJsonString(item   , "naRoadCd"));   /* 도로명_코드 */ 
    			bdVo.setRdlegcd(StringUtil.getJsonString(item  , "naBjdongCd")); /* 도로명_법정동코드 */ 
    			bdVo.setUnderAt(StringUtil.getJsonString(item  , "naUgrndCd"));  /* 지상_지하_구분코드 */
    			String rdmbun = StringUtil.strLpad(StringUtil.getJsonString(item, "naMainBun"), 5, '0') ; 
    			String rdsbun = StringUtil.strLpad(StringUtil.getJsonString(item, "naSubBun"), 5, '0') ; 
    			bdVo.setRdMainBun(rdmbun); /* 도로명_본_번 */
    			bdVo.setRdSubBun(rdsbun);  /* 도로명_부_번 */
    			
    			bdVo.setBregstrPk(StringUtil.getJsonString(item, "mgmBldrgstPk"));   /* 표제부 Key */
    			bdVo.setBuldgb(StringUtil.getJsonString(item   , "regstrGbCd"));     /* 건축물 구분: [1]일반건물, [2]집합건물 */
    			bdVo.setBuldnm(StringUtil.getJsonString(item   , "bldNm"));          /* 건물명 */
    			bdVo.setBdaddr(StringUtil.getJsonString(item   , "platPlc"));        /* 건물주소(지번) */
    			bdVo.setRdaddr(StringUtil.getJsonString(item   , "newPlatPlc"));     /* 건물주소(도로명) */
    			bdVo.setBlocknm(StringUtil.getJsonString(item  , "newPlatPlc"));     /* 건물_동_명 */ 
    			bdVo.setPpscd(StringUtil.getJsonString(item    , "mainPurpsCd"));    /* 건물_용도_코드 */
    			bdVo.setPpsnm(StringUtil.getJsonString(item    , "mainPurpsCdNm"));  /* 건물_용도_명 */
    			bdVo.setPpsetcnm(StringUtil.getJsonString(item , "etcPurps"));       /* 건물_용도_기타_명 */

    			bdVo.setCrde(StringUtil.getJsonString(item     , "crtnDay")) ;       /* 생성_일자 */
    			bdVo.setFlgroundco(StringUtil.getJsonInt(item  , "grndFlrCnt"));     /* 지상_층수 */
    			bdVo.setFlunderco(StringUtil.getJsonInt(item   , "ugrndFlrCnt"));    /* 지하_층수 */ 
    			bdVo.setRideElvtrco(StringUtil.getJsonInt(item , "rideUseElvtCnt")); /* 승용_엘리베이터_수 */
    			bdVo.setEmgrElvtrco(StringUtil.getJsonInt(item , "emgenUseElvtCnt"));/* 비상_엘리베이터_수 */
    			bdVo.setHshldco(StringUtil.getJsonInt(item     , "hhldCnt"));        /* 세대_수(세대) */
    			bdVo.setFmlyco(StringUtil.getJsonInt(item      , "fmlyCnt"));        /* 가구_수(가구) */
    			bdVo.setPlatgb(StringUtil.getJsonString(item   , "platGbCd"));       /* 대지_구분_코드 */ 

    			bdVo.setSupplyAr(0.0f); /* 공급_면적 */
    			bdVo.setDelYn("N");
    			bdVo.setBatchYn("N");
    		}
    	}
    	Log.error("Title Ledger vo: {}", bdVo.toString()) ;
		return bdVo ;
	}
	

	/* 표제부 API 조회 */ 
	@Override
	public CommonMap getTitleLedgrList(String operation, BuldApiReqVo reqVo)  {
		String strOutline = this.get(operation, reqVo) ;
        String respCode = this.getApiRespCode(strOutline) ;
        Log.error("*** respCode: {}", respCode) ; 
        List<TitleLedgrVo> tList = new ArrayList<TitleLedgrVo>() ;
        CommonMap ledmap = new CommonMap() ; 
    	if ( "00".equals( respCode )) {
    		CommonMap pginfo = getApiPageinfo(strOutline) ;
    		JSONArray jsonItems = this.getApiDataList(strOutline) ;
    		if ( jsonItems != null && jsonItems.size() > 0) {
    			for ( int i = 0; i < jsonItems.size(); i ++ ) {
    		        TitleLedgrVo bdVo = new TitleLedgrVo() ;
	    			JSONObject item = (JSONObject)jsonItems.get(i) ;
	    			
	    			String confde = StringUtil.getJsonString(item, "useAprDay") ; /* 승인일자 */ 
	    			String cfmvgb = !"".equals(confde) ? "1" : "0" ;
	    			
	    			bdVo.setCfmvgb(cfmvgb);
	    			bdVo.setConfde(confde);
	    			bdVo.setMoveinde("");
	    			
	    			bdVo.setTregstrPk("0");    			
	    			bdVo.setArcd(reqVo.getArcd());
	    			bdVo.setLegcd(reqVo.getLegcd());
	    			bdVo.setBunjib(reqVo.getBunjib());
	    			bdVo.setBunjij(reqVo.getBunjij());
	    			bdVo.setParkngco(0); /* 표제부는 주차대수 없음 */
	    			bdVo.setInco(0);
	    			
	    			/* 면적 */
	    			Float platAr  = Float.parseFloat(StringUtil.getJsonString(item, "platArea")) ; /* 대지_면적 */
	    			Float totalAr = Float.parseFloat(StringUtil.getJsonString(item, "totArea")) ;  /* 총_면적 */
	    			Float buldAr  = Float.parseFloat(StringUtil.getJsonString(item, "archArea")) ; /* 건물(건축)_면적 */
	    			bdVo.setPlatAr(platAr) ;   /* 대지_면적 */
	    			bdVo.setTotalAr(totalAr) ; /* 총_면적 */
	    			bdVo.setBuldAr(buldAr) ;   /* 건물(건축)_면적 */

	    			bdVo.setRdcode(StringUtil.getJsonString(item   , "naRoadCd"));   /* 도로명_코드 */ 
	    			bdVo.setRdlegcd(StringUtil.getJsonString(item  , "naBjdongCd")); /* 도로명_법정동코드 */ 
	    			bdVo.setUnderAt(StringUtil.getJsonString(item  , "naUgrndCd"));  /* 지상_지하_구분코드 */
	    			String rdmbun = StringUtil.strLpad(StringUtil.getJsonString(item, "naMainBun"), 5, '0') ; 
	    			String rdsbun = StringUtil.strLpad(StringUtil.getJsonString(item, "naSubBun"), 5, '0') ; 
	    			bdVo.setRdMainBun(rdmbun); /* 도로명_본_번 */
	    			bdVo.setRdSubBun(rdsbun);  /* 도로명_부_번 */
	    			
	    			bdVo.setBregstrPk(StringUtil.getJsonString(item, "mgmBldrgstPk"));   /* 표제부 Key */
	    			bdVo.setBuldgb(StringUtil.getJsonString(item   , "regstrGbCd"));     /* 건축물 구분: [1]일반건물, [2]집합건물 */
	    			bdVo.setBuldnm(StringUtil.getJsonString(item   , "bldNm"));          /* 건물명 */
	    			bdVo.setBdaddr(StringUtil.getJsonString(item   , "platPlc"));        /* 건물주소(지번) */
	    			bdVo.setRdaddr(StringUtil.getJsonString(item   , "newPlatPlc"));     /* 건물주소(도로명) */
	    			bdVo.setBlocknm(StringUtil.getJsonString(item  , "newPlatPlc"));     /* 건물_동_명 */ 
	    			bdVo.setPpscd(StringUtil.getJsonString(item    , "mainPurpsCd"));    /* 건물_용도_코드 */
	    			bdVo.setPpsnm(StringUtil.getJsonString(item    , "mainPurpsCdNm"));  /* 건물_용도_명 */
	    			bdVo.setPpsetcnm(StringUtil.getJsonString(item , "etcPurps"));       /* 건물_용도_기타_명 */

	    			bdVo.setCrde(StringUtil.getJsonString(item     , "crtnDay")) ;       /* 생성_일자 */
	    			bdVo.setFlgroundco(StringUtil.getJsonInt(item  , "grndFlrCnt"));     /* 지상_층수 */
	    			bdVo.setFlunderco(StringUtil.getJsonInt(item   , "ugrndFlrCnt"));    /* 지하_층수 */ 
	    			bdVo.setRideElvtrco(StringUtil.getJsonInt(item , "rideUseElvtCnt")); /* 승용_엘리베이터_수 */
	    			bdVo.setEmgrElvtrco(StringUtil.getJsonInt(item , "emgenUseElvtCnt"));/* 비상_엘리베이터_수 */
	    			bdVo.setHshldco(StringUtil.getJsonInt(item     , "hhldCnt"));        /* 세대_수(세대) */
	    			bdVo.setFmlyco(StringUtil.getJsonInt(item      , "fmlyCnt"));        /* 가구_수(가구) */
	    			bdVo.setPlatgb(StringUtil.getJsonString(item   , "platGbCd"));       /* 대지_구분_코드 */ 

	    			bdVo.setSupplyAr(0.0f); /* 공급_면적 */
	    			bdVo.setDelYn("N");
	    			bdVo.setBatchYn("N");
	    			
	    			bdVo.setRnum(StringUtil.getJsonInt(item, "rnum"));
	    			bdVo.setPage(pginfo.getIntValue("page"));
	    			bdVo.setNumrows(pginfo.getIntValue("numrows"));
	    			bdVo.setTcnt(pginfo.getIntValue("tcnt"));
	    			
//	    	    	Log.error("Title Ledger vo: {}", bdVo.toString()) ;
					
	    			tList.add(bdVo) ; 
    			}
    			
    			ledmap.put("ledgrList", tList) ; 
    			ledmap.put("pginfo", pginfo) ; 
    		}
    	}
		return ledmap ;
	}
	
    private HttpURLConnection connect(String uri) {
        try {
            URL url = (new URI(uri)).toURL();
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
        	Log.error("[MalformedURLException]API URL이 잘못되었습니다.") ;
        	throw new HomesException("API URL이 잘못되었습니다") ; 
        } catch (IOException e) {
        	Log.error("[IOException]API URL이 잘못되었습니다") ;
            throw new HomesException("연결이 실패했습니다");
        } catch (URISyntaxException e) {
        	Log.error("[URISyntaxException]API URL이 잘못되었습니다") ;
        	throw new HomesException("API URL이 잘못되었습니다") ; 
		}
    }
    
    private String get(String operation, BuldApiReqVo reqVo) {
        String apiURL = this.getApiUrl(reqVo, operation) ; 
    	return this.get(apiURL) ; 
    }
    
    private String get(String apiUrl){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
            	Log.error("[" + responseCode + "]" + con.getResponseMessage()) ;
                throw new HomesException(responseCode, "[" + responseCode + "]API 응답을 읽는데 실패했습니다.");
            }
        } catch (IOException e) {
        	Log.error("[IOException]API 요청과 응답 실패") ;
            throw new HomesException(500, "API 요청과 응답 실패");
        } finally {
            con.disconnect();
        }
    }
    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);
        try (
        	BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            lineReader.close();
            return responseBody.toString();
        } catch (IOException e) {
        	Log.error("[IOException]입출력 에러발생") ;
            throw new HomesException("API 응답을 읽는데 실패했습니다.");
        }
    }
    public String getApiRespCode(String json) {
        JSONParser parser  = new JSONParser();
        String respCode = "" ; 
        try {
        	JSONObject jsonObj    = (JSONObject)parser.parse(json) ; 
        	JSONObject respJson   = (JSONObject)jsonObj.get("response") ; 
        	JSONObject headerJson = (JSONObject)respJson.get("header") ;
        	respCode = ( String )headerJson.get("resultCode") ;
        } catch (ParseException re ) {
        	Log.error("[ParseException]JSON parse failed") ;
            throw new HomesException("데이터를 분석하는데 실패하였습니다.");
		} 
    	return respCode ; 
    }

    public CommonMap getApiPageinfo(String json) {
        JSONParser parser  = new JSONParser();
        CommonMap pgmap = new CommonMap() ; 
        pgmap.put("page"   ,  0) ; 
        pgmap.put("numrows", 0) ; 
        pgmap.put("tcnt"   , 0) ; 
        try {
        	JSONObject jsonObj    = (JSONObject)parser.parse(json) ; 
        	JSONObject respJson   = (JSONObject)jsonObj.get("response") ; 
        	JSONObject headerJson = (JSONObject)respJson.get("body") ;
        	String pg   = Optional.ofNullable(( String )headerJson.get("pageNo")).orElse("0") ;
        	String rows = Optional.ofNullable(( String )headerJson.get("numOfRows")).orElse("0") ;
        	String tot  = Optional.ofNullable(( String )headerJson.get("totalCount")).orElse("0") ;
        	
            pgmap.put("page"   , Integer.parseInt(pg)) ; 
            pgmap.put("numrows", Integer.parseInt(rows)) ; 
            pgmap.put("tcnt"   , Integer.parseInt(tot)) ; 
        	
        } catch (ParseException re ) {
        	Log.error("[ParseException]JSON parse failed") ;
            throw new HomesException("데이터를 분석하는데 실패하였습니다.");
		} 
    	return pgmap ; 
    }
    
    public JSONArray getApiDataList( String json ) {
        JSONParser parser  = new JSONParser();
        JSONArray itemList = null ; 
        try {
        	JSONObject jsonObj    = (JSONObject)parser.parse(json) ; 
        	JSONObject respJson   = (JSONObject)jsonObj.get("response") ; 
        	JSONObject bodyJson   = (JSONObject)respJson.get("body") ; 
        	JSONObject itemsJson  = (JSONObject)bodyJson.get("items") ;
        	itemList   = (JSONArray)itemsJson.get("item") ;
        	
        } catch (ParseException re ) {
        	Log.error("[ParseException]JSON parse failed") ;
            throw new HomesException("데이터를 분석하는데 실패하였습니다.");
		} 
    	return itemList ; 
    }
    
}
