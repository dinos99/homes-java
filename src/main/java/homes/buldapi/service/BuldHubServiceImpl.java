package homes.buldapi.service;

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
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import homes.api.buld.vo.BuldApiReqVo;
import homes.buldapi.mapper.BuldHubMapper;
import homes.buldapi.vo.BuldHubReqVo;
import homes.buldapi.vo.BuldHubResVo;
import homes.comm.constants.EnumApiOperation;
import homes.comm.util.JsonUtil;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuldHubServiceImpl implements BuldHubService {
	public Logger Log = LogManager.getLogger(BuldHubServiceImpl.class) ; 
	private final Environment env ; 
	private final BuldHubMapper mapper ;
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

    private String get(String operation, BuldApiReqVo reqVo) {
        String apiURL = this.getApiUrl(reqVo, operation) ; 
    	return this.get(apiURL) ; 
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
    
    @SuppressWarnings("unchecked")
	public String getApiRespCode(String json) {
    	/* **********************************************************************
    	 * simple-json이 바보같아서 Gson으로 변경 
    	 * **********************************************************************/
    	/* Gson 객체 생성 */
    	Gson gson = new Gson();
        String respCode = "" ; 
        /*
        JSONParser parser  = new JSONParser();
        Object jsonObj        = (JSONObject)parser.parse(json);
    	JSONObject respJson   = (JSONObject)jsonObj.get("response") ; 
    	JSONObject headerJson = (JSONObject)respJson.get("header") ;
    	respCode = ( String )headerJson.get("resultCode") ;
    	*/
		Map<Object, Object> jsonObj  = gson.fromJson(json, Map.class);
		Map<Object, Object> respJson = ( Map<Object, Object> )jsonObj.get("response") ;
    	Map<Object, Object> headerJson = ( Map<Object, Object> )respJson.get("header") ;  
    	respCode = ( String )headerJson.get("resultCode") ;
		return respCode ; 
    }

    @SuppressWarnings("unchecked")
    public CommonMap getApiPageinfo(String respData) {
    	/* **********************************************************************
    	 * simple-json이 바보같아서 Gson으로 변경 
    	 * **********************************************************************/
    	/* Gson 객체 생성 */
    	Gson gson = new Gson();
        CommonMap pgmap = new CommonMap() ; 
        pgmap.put("page"   ,  0) ; 
        pgmap.put("numrows", 0) ; 
        pgmap.put("tcnt"   , 0) ; 
        Map<Object, Object> jsonObj  = gson.fromJson(respData, Map.class);
		Map<Object, Object> respJson = ( Map<Object, Object> )jsonObj.get("response") ;
		Map<Object, Object> bodyJson = ( Map<Object, Object> )respJson.get("body") ;  

		Log.info("*** bodyJson: {}", bodyJson) ;
		
		String pg   = Optional.ofNullable(( String )bodyJson.get("pageNo")).orElse("0") ;
		String rows = Optional.ofNullable(( String )bodyJson.get("numOfRows")).orElse("0") ;
		String tot  = Optional.ofNullable(( String )bodyJson.get("totalCount")).orElse("0") ;
		
		
		pgmap.put("page"   , Integer.parseInt(pg)) ; 
		pgmap.put("numrows", Integer.parseInt(rows)) ; 
		pgmap.put("tcnt"   , Integer.parseInt(tot)) ; 
		
    	return pgmap ; 
    }
    
    /* *******************************************************************************
     * 전체페이지 데이터 목록조회
     * *******************************************************************************/
	public int getApiDataList(String operation, BuldApiReqVo reqVo, BuldHubReqVo paramVo ) {
		List<CommonMap> dataList = new ArrayList<CommonMap>() ;

        /* 1 page 조회 */ // operation
		String respData = this.get(operation, reqVo) ;
        String respCode = this.getApiRespCode(respData) ;
        CommonMap pgmap = this.getApiPageinfo(respData) ;

		int t_cnt = pgmap.getIntValue("tcnt") ; 
		int r_cnt = pgmap.getIntValue("numrows") ; 
		int pgLast = Math.ceilDiv(t_cnt, r_cnt) ;
		
		int ins_co = 0 ; 
		/* 응답성공 && 전체건수 > 0 && 1 page 이상 */ 
        if ( "00".equals(respCode) && t_cnt > 0 && pgLast > 1 ) {
        	/* 다시 1page부터 조회 */  
//    		List<Map<Object, Object>> apiList = this.getApiDataList(respData) ;
        	for ( int i = 0 ; i < pgLast ; i ++ ) {
        		reqVo.setPage(i + 1 );
        		respData = this.get(operation, reqVo) ;
        		List<Map<Object, Object>> apiList = this.getApiDataList(respData) ;
        		if ( apiList != null && apiList.size() > 0) {
        			for ( int row = 0; row < apiList.size(); row ++ ) {
            			Map<Object, Object> apiData = ( Map<Object, Object> )apiList.get(row) ; 
            			CommonMap apimap = null ;
            			if (operation.equals(EnumApiOperation.REQ_API_FLROUTLINE.getOperation())) {
            				apimap = getFlrOutLineData( paramVo, apiData ) ;
            				ins_co += mapper.insertTempLedgrFloor(apimap) ; 
            				dataList.add(apimap) ; 
            			}
        			}
        		}
        	}
        }
		return ins_co ; 
	}
	
    @SuppressWarnings("unchecked")
	public List<Map<Object, Object>> getApiDataList( String json ) {
    	/* **********************************************************************
    	 * simple-json이 바보같아서 Gson으로 변경 
    	 * **********************************************************************/
    	/* Gson 객체 생성 */
    	Gson gson = new Gson();
    	/* **********************************************************************
    	 * Gson도 바보다 .... Double 형식을 자연상수로 변경해 버림 
    	 * **********************************************************************/
    	String json_str = json ; 
    	json_str = JsonUtil.convString("mgmBldrgstPk", json_str) ; 
//		Log.info("str_respJson: {}", json_str) ;
		
    	Map<Object, Object> jsonObj   = gson.fromJson(json_str, Map.class);
    	Map<Object, Object> respJson  = (Map<Object, Object>)jsonObj.get("response") ;
    	Map<Object, Object> bodyJson  = (Map<Object, Object>)respJson.get("body") ;  
    	Map<Object, Object> itemsJson = (Map<Object, Object>)bodyJson.get("items") ;
		List<Map<Object, Object>> itemList = (List<Map<Object, Object>>)itemsJson.get("item") ; 

    	return itemList ; 
    }
	
	@Override
	@Transactional( readOnly = true )
	public CommResponseVo getRdRelateJibun( BuldHubReqVo paramVo ) {
		paramVo.setPage();  
		Long t_cnt = mapper.selectRdRelateJibunCount(paramVo) ;
		List<CommonMap> dataList = mapper.selectRdRelateJibun(paramVo) ;
		Log.info(paramVo);
		return new CommResponseVo(t_cnt, paramVo.getNumrows(), paramVo.getPgno(), null, dataList) ;
	}

	@Override
	@Transactional( readOnly = true )
	public CommResponseVo getBaseOutLine( BuldHubReqVo paramVo ) {
		paramVo.setPage();  
		Long t_cnt = mapper.selectBaseOutLineCount(paramVo) ;
		List<CommonMap> dataList = mapper.selectBaseOutLine(paramVo) ;
		Log.info(paramVo);
		return new CommResponseVo(t_cnt, paramVo.getNumrows(), paramVo.getPgno(), null, dataList) ;
	}

	/* ***********************************************************************
	 * 건축물대장 HUB API 표제부데이터 조회 
	 * ***********************************************************************/
	@Override
	@Transactional( rollbackFor = Exception.class )
	public CommResponseVo getLedgrinfo( BuldHubReqVo paramVo ) {
		BuldApiReqVo reqVo = new BuldApiReqVo(env) ; 
        CommonMap pgmap = new CommonMap() ;
        List<CommonMap> dataList = null ; 
        
		reqVo.setArcd(paramVo.getArcd());
		reqVo.setLegcd(paramVo.getLegcd());
		reqVo.setBunjib(paramVo.getBunjib());
		reqVo.setBunjij(paramVo.getBunjij());
		if ( "HDB".equals(paramVo.getSelgb())) {			
			paramVo.setPage();			
			int total_cnt = mapper.selectHBDLedgrCount(paramVo) ;
			dataList = mapper.selectHBDLedgrList(paramVo) ;
			return new CommResponseVo(StringUtil.getLongValue(total_cnt)
	                , paramVo.getNumrows()
	                , paramVo.getPgno()
	                , dataList) ;		
		}
		
		reqVo.setPage(paramVo.getPgno());
		reqVo.setNumrows(paramVo.getNumrows()); 
		String respData = this.get(EnumApiOperation.REQ_API_LEDGR.getOperation(), reqVo) ;
        String respCode = this.getApiRespCode(respData) ;
        pgmap.put("tcnt"   , 0l) ; 
        pgmap.put("numrows", 100) ; 
        pgmap.put("page"   , 1) ; 
    	if ( "00".equals( respCode )) {
    		/* page info */
    		pgmap = this.getApiPageinfo(respData) ;
    		List<Map<Object, Object>> apiList = this.getApiDataList(respData) ;
    		dataList = this.getLedgerData(paramVo, apiList) ; 
    		/* ********************************************************************
    		 * DB에 한번 넣어볼까 ? 
    		 * ********************************************************************/
    		/* 홈즈관리대장_표제부 존재여부 확인 */
    		/*
    		for ( CommonMap cmap : dataList ) {
    			String pk = cmap.getStringValue("buldRegstrPk") ;
        		int has_co = mapper.selectTempLedgrCount(pk) ;
        		if ( has_co == 0 ) {
        			mapper.insertTempLedgr(cmap) ;
        		}
    		}
    		*/
    	}
		return new CommResponseVo(pgmap.getLongValue("tcnt")
                , pgmap.getIntValue("numrows")
                , pgmap.getIntValue("page") 
                , dataList) ;
	}

	public List<CommonMap> getLedgerData(BuldHubReqVo paramVo, List<Map<Object, Object>> apiList) {
		List<CommonMap> dataList = new ArrayList<CommonMap>() ;
		if ( apiList != null && apiList.size() > 0) {
			for ( Map<Object, Object> apimap : apiList ) {

    			String confde = Optional.ofNullable((String)apimap.get("useAprDay")).orElse("") ; /* 승인일자 */ 
    			String cfmvgb = !"".equals(confde) ? "1" : "0" ;
    			
				CommonMap cmap = new CommonMap() ; 
				/* BULD_REGSTR_PK */
				cmap.put("BULD_REGSTR_PK", apimap.get("mgmBldrgstPk")) ; 
				cmap.put("BULDGB", apimap.get("regstrGbCd")) ; 
				cmap.put("ARCD"  , paramVo.getArcd()) ; 
				cmap.put("LEGCD" , paramVo.getLegcd()) ; 
				/*
				cmap.put("BUNJIB", paramVo.getBunjib()) ; 
				cmap.put("BUNJIJ", paramVo.getBunjij()) ;
				*/ 

				cmap.put("BUNJIB", apimap.get("bun")) ; 
				cmap.put("BUNJIJ", apimap.get("ji")) ;
				
				cmap.put("BULDNM", apimap.get("bldNm")) ; 
				cmap.put("DONGNM", apimap.get("dongNm")) ; 
				cmap.put("BDADDR", apimap.get("platPlc")) ; 
				cmap.put("RDADDR", apimap.get("newPlatPlc")) ; 
				cmap.put("PPSCD" , apimap.get("mainPurpsCd")) ; 
				cmap.put("PPSETCNM", apimap.get("etcPurps")) ;
				
				cmap.put("CFMVGB", cfmvgb) ; 
				cmap.put("CONFDE", confde) ; 
				cmap.put("MOVEINDE", "") ; 
				cmap.put("RIDNG_ELVTR_CO", apimap.get("rideUseElvtCnt")) ; 
				cmap.put("EMGR_ELVTR_CO", apimap.get("emgenUseElvtCnt")) ; 
				cmap.put("GRNDCO", apimap.get("grndFlrCnt")) ; 
				cmap.put("UNDERCO", apimap.get("ugrndFlrCnt")) ; 
				cmap.put("HSHLDCO", apimap.get("hhldCnt")) ; 
				cmap.put("FMLYCO", apimap.get("fmlyCnt")) ; 
				cmap.put("HOSILCO", apimap.get("hoCnt")) ; 
				
				String rdcode  = Optional.ofNullable((String)apimap.get("naRoadCd")).orElse("") ; 
				String rdlegcd = Optional.ofNullable((String)apimap.get("naBjdongCd")).orElse("") ; 
				String underAt = Optional.ofNullable((String)apimap.get("naUgrndCd")).orElse("") ; 
				String rdmbun  = Optional.ofNullable((String)apimap.get("naMainBun")).orElse("") ; 
				String rdsbun  = Optional.ofNullable((String)apimap.get("naSubBun")).orElse("") ; 
				
				cmap.put("RDCODE"     , StringUtil.strLpad(rdcode, 12, '0')) ; 
				cmap.put("RDLEGCD"    , StringUtil.strLpad(rdlegcd, 5, '0')) ; 
				cmap.put("UNDER_AT"   , underAt) ; 
				cmap.put("RD_MAIN_BUN", StringUtil.strLpad(rdmbun, 5, '0')) ; 
				cmap.put("RD_SUB_BUN" , StringUtil.strLpad(rdsbun, 5, '0')) ; 
				
				cmap.put("TOTAL_AR" , apimap.get("totArea")) ; 
				cmap.put("SUPPLY_AR", 0l) ; 
				cmap.put("BULD_AR"  , apimap.get("archArea")) ; 
				cmap.put("PLAT_AR"  , apimap.get("platArea")) ; 
				
				cmap.put("BULD_LND_RT"      , apimap.get("bcRat")) ; /* 건폐율 */ 
				cmap.put("BULK_CALC_TOT_AR" , apimap.get("vlRatEstmTotArea")) ; /* 용적률산정 연면적 */ 
				cmap.put("BULK_RT"          , apimap.get("vlRat")) ; /* 용적률 */ 
				
				cmap.put("CRDE", apimap.get("crtnDay")) ; 
				
				/* 주차 대수 = 옥내 기계식 + 옥외 기계식 + 옥내 자동 + 옥외 자동 */
				String[] im = String.valueOf(apimap.get("indrMechUtcnt")).split("[.]") ; 
				String[] om = String.valueOf(apimap.get("oudrMechUtcnt")).split("[.]") ; 
				String[] ia = String.valueOf(apimap.get("indrAutoUtcnt")).split("[.]") ;
				String[] oa = String.valueOf(apimap.get("oudrAutoUtcnt")).split("[.]") ;

				int imcnt = Integer.parseInt(im[0]) ; 
				int omcnt = Integer.parseInt(om[0]) ; 
				int iacnt = Integer.parseInt(ia[0]) ; 
				int oacnt = Integer.parseInt(oa[0]) ;
				cmap.put("PARKNGCO", imcnt + omcnt + iacnt + oacnt ) ;
				
				cmap.put("BULDCO", 0) ;
				
//				Log.info( "********** cmap: {}",  cmap ) ; 
				/* 부속건물은 제외 */ 
				String mainbuld = Optional.ofNullable((String)apimap.get("mainAtchGbCd")).orElse("") ; 
				if ("0".equals( mainbuld )) {
					dataList.add(cmap) ;
				} 
			}
		}
		return dataList ;
	}
	
	public List<CommonMap> getRecapData(BuldHubReqVo paramVo, List<Map<Object, Object>> apiList) {
		List<CommonMap> dataList = new ArrayList<CommonMap>() ;
		if ( apiList != null && apiList.size() > 0) {
			for ( Map<Object, Object> apimap : apiList ) {

				CommonMap cmap = new CommonMap() ; 
				cmap.put("TOTAL_REGSTR_PK", apimap.get("mgmBldrgstPk")) ; 
				cmap.put("BULDGB", apimap.get("regstrGbCd")) ; 
				cmap.put("ARCD"  , paramVo.getArcd()) ; 
				cmap.put("LEGCD" , paramVo.getLegcd()) ; 
				cmap.put("BUNJIB", apimap.get("bun")) ; 
				cmap.put("BUNJIJ", apimap.get("ji")) ;				
				cmap.put("BULDNM", apimap.get("bldNm")) ; 
				
				cmap.put("DONGNM", apimap.get("dongNm")) ; 
				cmap.put("BDADDR", apimap.get("platPlc")) ; 
				cmap.put("RDADDR", apimap.get("newPlatPlc")) ; 
				cmap.put("PPSCD" , apimap.get("mainPurpsCd")) ; 
				cmap.put("PPSETCNM", apimap.get("etcPurps")) ; 
				
				cmap.put("HSHLDCO"  , apimap.get("hhldCnt")) ; 
				cmap.put("FMLYCO"   , apimap.get("fmlyCnt")) ; 
				cmap.put("HOSILCO"  , apimap.get("hoCnt")) ; 
				cmap.put("BULDCO"   , apimap.get("mainBldCnt")) ; /* 주 건축물 수 */ 
				cmap.put("BULDSUBCO", apimap.get("atchBldCnt")) ; /* 부속 건축물 수 */
				cmap.put("PARKNGCO" , apimap.get("totPkngCnt")) ; 
				String rdcode  = Optional.ofNullable((String)apimap.get("naRoadCd")).orElse("") ; 
				String rdlegcd = Optional.ofNullable((String)apimap.get("naBjdongCd")).orElse("") ; 
				String underAt = Optional.ofNullable((String)apimap.get("naUgrndCd")).orElse("") ; 
				String rdmbun  = Optional.ofNullable((String)apimap.get("naMainBun")).orElse("") ; 
				String rdsbun  = Optional.ofNullable((String)apimap.get("naSubBun")).orElse("") ; 
				
				cmap.put("RDCODE"     , StringUtil.strLpad(rdcode, 12, '0')) ; 
				cmap.put("RDLEGCD"    , StringUtil.strLpad(rdlegcd, 5, '0')) ; 
				cmap.put("UNDER_AT"   , underAt) ; 
				cmap.put("RD_MAIN_BUN", StringUtil.strLpad(rdmbun, 5, '0')) ; 
				cmap.put("RD_SUB_BUN" , StringUtil.strLpad(rdsbun, 5, '0')) ; 
				
				cmap.put("TOTAL_AR" , apimap.get("totArea")) ;  
				cmap.put("PLAT_AR"  , apimap.get("platArea")) ; 
				cmap.put("BULD_AR"  , apimap.get("archArea")) ;
				cmap.put("SUPPLY_AR", 0l) ; 
				
				cmap.put("BULD_LND_RT"      , apimap.get("bcRat")) ; /* 건폐율 */ 
				cmap.put("BULK_CALC_TOT_AR" , apimap.get("vlRatEstmTotArea")) ; /* 용적률산정 연면적 */ 
				cmap.put("BULK_RT"          , apimap.get("vlRat")) ; /* 용적률 */ 
				
				cmap.put("CONFDE", apimap.get("useAprDay")) ; /* 승인일자 */
				cmap.put("CRDE"  , apimap.get("crtnDay")) ; 
				
				/* 주차 대수 = 옥내 기계식 + 옥외 기계식 + 옥내 자동 + 옥외 자동 */
				/*
				String[] im = String.valueOf(apimap.get("indrMechUtcnt")).split("[.]") ; 
				String[] om = String.valueOf(apimap.get("oudrMechUtcnt")).split("[.]") ; 
				String[] ia = String.valueOf(apimap.get("indrAutoUtcnt")).split("[.]") ;
				String[] oa = String.valueOf(apimap.get("oudrAutoUtcnt")).split("[.]") ;

				int imcnt = Integer.parseInt(im[0]) ; 
				int omcnt = Integer.parseInt(om[0]) ; 
				int iacnt = Integer.parseInt(ia[0]) ; 
				int oacnt = Integer.parseInt(oa[0]) ;
				cmap.put("PARKNGCO", imcnt + omcnt + iacnt + oacnt ) ;				
				cmap.put("BULDCO", 0) ;
				*/
//				Log.info( "********** cmap: {}",  cmap ) ; 
				dataList.add(cmap) ;
			}
		}
		return dataList ;
	}
	public CommonMap getFlrOutLineData(BuldHubReqVo paramVo, Map<Object, Object> apimap) {
		CommonMap apiData = new CommonMap() ; 
		apiData.put("BULD_REGSTR_PK", apimap.get("mgmBldrgstPk")) ; 
		
		apiData.put("BULDNM" , apimap.get("bldNm")) ; 
		apiData.put("DONGNM" , apimap.get("dongNm")) ;
		apiData.put("FLGBCD" , apimap.get("flrGbCd")) ; 
		apiData.put("FLGBNM" , apimap.get("flrGbCdNm")) ; 
		apiData.put("FLOORNO", apimap.get("flrNo")) ; 
		apiData.put("FLOORNM", apimap.get("flrNoNm")) ;
		
		apiData.put("PPSCD"   , apimap.get("mainPurpsCd")) ; 
		apiData.put("PPSETCNM", apimap.get("etcPurps")) ; 
		
		apiData.put("TOTAL_AR"   , apimap.get("area")) ; 
		apiData.put("AR_EXCLD_AT", apimap.get("areaExctYn")) ; 
		
		apiData.put("MAIN_BULD_AT", apimap.get("mainAtchGbCd")) ; 
		apiData.put("CRDE"  , apimap.get("crtnDay")) ; 
		
		apiData.put("mngrno", paramVo.getMngrno()) ;
		return apiData ; 
	}
	public List<CommonMap> getFlrOutLineDataList(BuldHubReqVo paramVo, List<Map<Object, Object>> apiList) {
		List<CommonMap> dataList = new ArrayList<CommonMap>() ;
		if ( apiList != null && apiList.size() > 0) {
			int flrno = 1 ; 
			for ( Map<Object, Object> apimap : apiList ) {
				
				CommonMap cmap = new CommonMap() ; 
				cmap.put("TOTAL_REGSTR_PK", apimap.get("mgmBldrgstPk")) ; 
				cmap.put("FLRNO", flrno) ; 
				
				cmap.put("BULDNM" , apimap.get("bldNm")) ; 
				cmap.put("DONGNM" , apimap.get("dongNm")) ;
				cmap.put("FLGBCD" , apimap.get("flrGbCd")) ; 
				cmap.put("FLGBNM" , apimap.get("flrGbCdNm")) ; 
				cmap.put("FLOORNO", apimap.get("flrNo")) ; 
				cmap.put("FLOORNM", apimap.get("flrNoNm")) ;
				
				cmap.put("PPSCD"   , apimap.get("mainPurpsCd")) ; 
				cmap.put("PPSETCNM", apimap.get("etcPurps")) ; 
				
				cmap.put("TOTAL_AR"   , apimap.get("area")) ; 
				cmap.put("AR_EXCLD_AT", apimap.get("areaExctYn")) ; 
				
				cmap.put("MAIN_BULD_AT", apimap.get("mainAtchGbCd")) ; 
				cmap.put("CRDE"  , apimap.get("crtnDay")) ; 
				
				dataList.add(cmap) ;
				flrno ++ ; 
			}
		}
		return dataList ;
	}

	public List<CommonMap> getPssionDataList(BuldHubReqVo paramVo, List<Map<Object, Object>> apiList) {
		List<CommonMap> dataList = new ArrayList<CommonMap>() ;
		if ( apiList != null && apiList.size() > 0) {
			for ( Map<Object, Object> apimap : apiList ) {
				
				CommonMap cmap = new CommonMap() ; 
				cmap.put("BULD_REGSTR_PK", apimap.get("mgmBldrgstPk")) ; 

				cmap.put("BULDGB", apimap.get("regstrGbCd")) ; 
				cmap.put("ARCD"  , paramVo.getArcd()) ; 
				cmap.put("LEGCD" , paramVo.getLegcd()) ; 
				cmap.put("BUNJIB", apimap.get("bun")) ; 
				cmap.put("BUNJIJ", apimap.get("ji")) ;				
				cmap.put("BULDNM", apimap.get("bldNm")) ; 
				
				cmap.put("DONGNM", apimap.get("dongNm")) ; 
				
				cmap.put("BDADDR", apimap.get("platPlc")) ; 
				cmap.put("RDADDR", apimap.get("newPlatPlc")) ; 

				cmap.put("FLGBCD" , apimap.get("flrGbCd")) ;
				cmap.put("FLGBNM" , apimap.get("flrGbCdNm")) ; 
				cmap.put("FLNO"   , apimap.get("flrNo")) ; 
//				cmap.put("FLNM"   , apimap.get("flrNoNm")) ; /* 전유부에는 없음 */ 
				cmap.put("HOSILNM", apimap.get("hoNm")) ; 
				
				
				cmap.put("BULDNM" , apimap.get("bldNm")) ; 
				cmap.put("DONGNM" , apimap.get("dongNm")) ;
				cmap.put("FLGBNM" , apimap.get("flrGbCdNm")) ; 
				
				cmap.put("CRDE"  , apimap.get("crtnDay")) ; ;
				
				dataList.add(cmap) ;
			}
		}
		return dataList ;
	}

	public List<CommonMap> getPssionAreaDataList(BuldHubReqVo paramVo, List<Map<Object, Object>> apiList) {
		List<CommonMap> dataList = new ArrayList<CommonMap>() ;
		if ( apiList != null && apiList.size() > 0) {
			for ( Map<Object, Object> apimap : apiList ) {
				
				CommonMap cmap = new CommonMap() ; 
				cmap.put("BULD_REGSTR_PK", apimap.get("mgmBldrgstPk")) ; 

				cmap.put("BULDGB", apimap.get("regstrGbCd")) ; 
				cmap.put("ARCD"  , paramVo.getArcd()) ; 
				cmap.put("LEGCD" , paramVo.getLegcd()) ; 
				cmap.put("BUNJIB", apimap.get("bun")) ; 
				cmap.put("BUNJIJ", apimap.get("ji")) ;				
				cmap.put("BULDNM", apimap.get("bldNm")) ; 
				
				cmap.put("DONGNM", apimap.get("dongNm")) ; 
				
				cmap.put("BDADDR", apimap.get("platPlc")) ; 
				cmap.put("RDADDR", apimap.get("newPlatPlc")) ; 

				cmap.put("FLGBCD" , apimap.get("flrGbCd")) ;
				cmap.put("FLGBNM" , apimap.get("flrGbCdNm")) ; 
				cmap.put("FLNO"   , apimap.get("flrNo")) ; 
				cmap.put("FLNM"   , apimap.get("flrNoNm")) ; /* 전유부에는 없음 */ 
				cmap.put("HOSILNM", apimap.get("hoNm")) ; 
				
				cmap.put("BULDNM" , apimap.get("bldNm")) ; 
				cmap.put("DONGNM" , apimap.get("dongNm")) ;
				cmap.put("FLGBNM" , apimap.get("flrGbCdNm")) ; 
				
				cmap.put("PUBUSENM", apimap.get("exposPubuseGbCdNm")) ; 
				cmap.put("MAINNM"  , apimap.get("mainAtchGbCdNm")) ; 
				
				cmap.put("PPSNM"   , apimap.get("mainPurpsCdNm")) ; 
				cmap.put("PPSETCNM", apimap.get("etcPurps")) ; 
				
				cmap.put("TOTAL_AR", apimap.get("area")) ; 
				
				cmap.put("CRDE"  , apimap.get("crtnDay")) ; 
				
				dataList.add(cmap) ;
			}
		}
		return dataList ;
	}
	@Override
	@Transactional( rollbackFor = Exception.class )
	public BuldHubResVo insertHbdSummary( BuldHubReqVo paramVo ) {
		paramVo.setBatchAt("1");
		int ins_co = mapper.insertHbdSummary(paramVo) ;
		BuldHubResVo resVo = new BuldHubResVo() ; 
		resVo.setInsco(ins_co);
		resVo.setMessage("기본개요가 등록되었습니다.( " + ins_co + "건 )");
		return resVo ;
	}
	
	@Override
	@Transactional( rollbackFor = Exception.class )
	public BuldHubResVo insertHbdLedgr( BuldHubReqVo paramVo ) {
		paramVo.setBatchAt("1");
		int ins_co = mapper.insertHbdLedgr(paramVo) ; 
		BuldHubResVo resVo = new BuldHubResVo() ; 
		resVo.setInsco(ins_co);
		resVo.setMessage("표제부가 등록되었습니다.( " + ins_co + "건 )");
		return resVo ;
	}

	/* ***********************************************************************
	 * 건축물대장 HUB API 총괄표제부데이터 조회 
	 * ***********************************************************************/
	@Override
	@Transactional( rollbackFor = Exception.class )
	public CommResponseVo getRecapTitleinfo( BuldHubReqVo paramVo ) {
		BuldApiReqVo reqVo = new BuldApiReqVo(env) ;

        CommonMap pgmap = new CommonMap() ;
        List<CommonMap> dataList = null ; 
        
		reqVo.setArcd(paramVo.getArcd());
		reqVo.setLegcd(paramVo.getLegcd());
		reqVo.setBunjib(paramVo.getBunjib());
		reqVo.setBunjij(paramVo.getBunjij());
		if ( "HDB".equals(paramVo.getSelgb())) {			
			paramVo.setPage();			
			int total_cnt = mapper.selectHBDLedgrMasterCount(paramVo) ;
			dataList = mapper.selectHBDLedgrMasterList(paramVo) ;
			return new CommResponseVo(StringUtil.getLongValue(total_cnt)
	                , paramVo.getNumrows()
	                , paramVo.getPgno()
	                , dataList) ;		
		}
		
		reqVo.setPage(paramVo.getPgno());
		reqVo.setNumrows(paramVo.getNumrows()); 
		
		reqVo.setArcd(paramVo.getArcd());
		reqVo.setLegcd(paramVo.getLegcd());
		reqVo.setBunjib(paramVo.getBunjib());
		reqVo.setBunjij(paramVo.getBunjij());
//		reqVo.setBuldnm(paramVo.getBuldnm());
		reqVo.setPage(paramVo.getPgno());
		reqVo.setNumrows(100); /* 100건 고정 */ 
		String respData = this.get(EnumApiOperation.REQ_API_RECAP.getOperation(), reqVo) ;
        String respCode = this.getApiRespCode(respData) ;
        pgmap.put("tcnt"   , 0l) ; 
        pgmap.put("numrows", 100) ; 
        pgmap.put("page"   , 1) ; 

    	if ( "00".equals( respCode )) {
    		/* page info */
    		pgmap = this.getApiPageinfo(respData) ;
    		List<Map<Object, Object>> apiList = this.getApiDataList(respData) ;
    		dataList = this.getRecapData(paramVo, apiList) ; 

    		/* ********************************************************************
    		 * DB에 한번 넣어볼까 ? 
    		 * ********************************************************************/
    		/* 홈즈관리대장_총괄표제부 존재여부 확인 */ 
    		/*
    		for ( CommonMap cmap : dataList ) {
    			String pk = cmap.getStringValue("totalRegstrPk") ;
        		int has_co = mapper.selectTempLedgrMstrCount(pk) ;
        		if ( has_co == 0 ) {
        			mapper.insertTempLedgrMstr(cmap) ;
        		}
    		}
    		*/
    	}
		return new CommResponseVo(pgmap.getLongValue("tcnt")
                , pgmap.getIntValue("numrows")
                , pgmap.getIntValue("page") 
                , dataList) ;
	}
	
	/* ***********************************************************************
	 * 건축물대장 HUB API 층별개요 조회 
	 * ***********************************************************************/
	@Override
	public CommResponseVo getFloorOutLine( BuldHubReqVo paramVo ) {
		BuldApiReqVo reqVo = new BuldApiReqVo(env) ;
		
		paramVo.setNumrows(100); /* 100건 고정 */ 
		reqVo.setArcd(paramVo.getArcd());
		reqVo.setLegcd(paramVo.getLegcd());
		reqVo.setBunjib(paramVo.getBunjib());
		reqVo.setBunjij(paramVo.getBunjij());
		reqVo.setPage(paramVo.getPgno());
		reqVo.setNumrows(100); /* 100건 고정 */ 
		String respData = this.get(EnumApiOperation.REQ_API_FLROUTLINE.getOperation(), reqVo) ;
        String respCode = this.getApiRespCode(respData) ;
        CommonMap pgmap = new CommonMap() ;
        List<CommonMap> dataList = null ; 
    	if ( "00".equals( respCode )) {
    		/* page info */
    		pgmap = this.getApiPageinfo(respData) ;
    		List<Map<Object, Object>> apiList = this.getApiDataList(respData) ;
    		dataList = this.getFlrOutLineDataList(paramVo, apiList) ; 
    	}
		return new CommResponseVo(pgmap.getLongValue("tcnt")
                , pgmap.getIntValue("numrows")
                , pgmap.getIntValue("page") 
                , dataList) ;
	}
	
	@Override
	@Transactional( rollbackFor = Exception.class )
	public BuldHubResVo insertHdbLedgrMst( BuldHubReqVo paramVo ) {
		paramVo.setBatchAt("2") ;
		List<CommonMap> sourceList = mapper.selectMasterSource(paramVo) ; 
		int ins_co = 0 ; 
		for ( CommonMap src : sourceList ) {
			String htbdno   = src.getStringValue("htbdno") ;
			String regstrpk = src.getStringValue("buldRegstrPk") ;
			int has_co = mapper.selectHbdLedgrMstrCount(htbdno) ; 
			if ( has_co == 0 ) {
				paramVo.setHtbdno(htbdno);
				paramVo.setBuldRegstrPk(regstrpk);
				ins_co += mapper.insertHbdLedgrMstr(paramVo) ; 
			}
		}
		/* 총괄표제부가 있는 경우 총괄표제부 정보로변경 */
		/* 정보제거 */ 
		paramVo.setHtbdno("");
		paramVo.setBuldRegstrPk("");
		int upd_co = 0 ; 
		List<CommonMap> upList = mapper.selectMasterUpdateList(paramVo) ;
		for ( CommonMap src : upList ) {
			src.put("mngrno", paramVo.getMngrno()) ; 
			upd_co += mapper.updateHbdLedgrMaster(src) ; 
		}
		
		BuldHubResVo resVo = new BuldHubResVo() ;
		resVo.setInsco(ins_co);
		resVo.setUpdco(upd_co);
		resVo.setMessage("홈즈관리대장 마스터가 등록되었습니다.( 등록 " + ins_co + "건, 수정 " + upd_co + " 건 )");
		return resVo ;
		
	}
	
	@Override
	@Transactional( readOnly = true ) 
	public List<CommonMap> selectBuldList ( BuldHubReqVo paramVo ) {
		return mapper.selectBuldList(paramVo) ; 
	}
	@Override
	@Transactional( readOnly = true ) 
	public List<CommonMap> selectDongList ( BuldHubReqVo paramVo ) {
		return mapper.selectDongList(paramVo) ; 
	}
	
	@Override
	@Transactional( rollbackFor = Exception.class )
	public BuldHubResVo insertTempLedgrFloor( BuldHubReqVo paramVo ) {
		BuldApiReqVo reqVo = new BuldApiReqVo(env) ;
		paramVo.setNumrows(100); /* 100건 고정 */ 
		reqVo.setArcd(paramVo.getArcd());
		reqVo.setLegcd(paramVo.getLegcd());
		reqVo.setBunjib(paramVo.getBunjib());
		reqVo.setBunjij(paramVo.getBunjij());
//		reqVo.setBuldnm(paramVo.getBuldnm());
		reqVo.setPage(paramVo.getPgno());
		reqVo.setNumrows(100); /* 100건 고정 */ 

        int ins_co = this.getApiDataList(EnumApiOperation.REQ_API_FLROUTLINE.getOperation(), reqVo, paramVo) ; 

		BuldHubResVo resVo = new BuldHubResVo() ;
		resVo.setInsco(ins_co);
		resVo.setMessage("홈즈관리대장 층별개요(임시)가 등록되었습니다.");
		return resVo ;
		
	}
	
	@Override
	@Transactional( rollbackFor = Exception.class ) 
	public BuldHubResVo insertLedgrFloor( BuldHubReqVo paramVo ) {
		int ins_co = mapper.insertLedgrFloor(paramVo) ; 
		BuldHubResVo resVo = new BuldHubResVo() ;
		resVo.setInsco(ins_co);
		resVo.setMessage("홈즈관리대장 층별개요가 등록되었습니다.");
		return resVo ;
	}
	
	@Override
	public CommResponseVo getBrExposInfo( BuldHubReqVo paramVo ) {
		BuldApiReqVo reqVo = new BuldApiReqVo(env) ;
		
		paramVo.setNumrows(100); /* 100건 고정 */ 
		reqVo.setArcd(paramVo.getArcd());
		reqVo.setLegcd(paramVo.getLegcd());
		reqVo.setBunjib(paramVo.getBunjib());
		reqVo.setBunjij(paramVo.getBunjij());
		reqVo.setPage(paramVo.getPgno());
		reqVo.setNumrows(100); /* 100건 고정 */ 
		String respData = this.get(EnumApiOperation.REQ_API_PSSION.getOperation(), reqVo) ;
        String respCode = this.getApiRespCode(respData) ;
        CommonMap pgmap = new CommonMap() ;
        List<CommonMap> dataList = null ; 
    	if ( "00".equals( respCode )) {
    		/* page info */
    		pgmap = this.getApiPageinfo(respData) ;
    		List<Map<Object, Object>> apiList = this.getApiDataList(respData) ;
    		dataList = this.getPssionDataList(paramVo, apiList) ; 
    	}
		return new CommResponseVo(pgmap.getLongValue("tcnt")
                , pgmap.getIntValue("numrows")
                , pgmap.getIntValue("page") 
                , dataList) ;
		
	}
	@Override
	public CommResponseVo getBrExposPubuseAreaInfo( BuldHubReqVo paramVo ) {
		BuldApiReqVo reqVo = new BuldApiReqVo(env) ;
		
		paramVo.setNumrows(100); /* 100건 고정 */ 
		reqVo.setArcd(paramVo.getArcd());
		reqVo.setLegcd(paramVo.getLegcd());
		reqVo.setBunjib(paramVo.getBunjib());
		reqVo.setBunjij(paramVo.getBunjij());
		reqVo.setPage(paramVo.getPgno());
		reqVo.setNumrows(100); /* 100건 고정 */ 
		String respData = this.get(EnumApiOperation.REQ_API_PSSION_AREA.getOperation(), reqVo) ;
		String respCode = this.getApiRespCode(respData) ;
		CommonMap pgmap = new CommonMap() ;
		List<CommonMap> dataList = null ; 
		if ( "00".equals( respCode )) {
			/* page info */
			pgmap = this.getApiPageinfo(respData) ;
			List<Map<Object, Object>> apiList = this.getApiDataList(respData) ;
			dataList = this.getPssionAreaDataList(paramVo, apiList) ; 
		}
		return new CommResponseVo(pgmap.getLongValue("tcnt")
				, pgmap.getIntValue("numrows")
				, pgmap.getIntValue("page") 
				, dataList) ;
		
	}
	

	@Override
	@Transactional( rollbackFor = Exception.class ) 
	public BuldHubResVo insertLedgrPssion( BuldHubReqVo paramVo ) {
		int ins_co = mapper.insertLedgrPssion(paramVo) ; 
		BuldHubResVo resVo = new BuldHubResVo() ;
		resVo.setInsco(ins_co);
		resVo.setMessage("홈즈관리대장 층별개요가 등록되었습니다.");
		return resVo ;
	}
	
}
