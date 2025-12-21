package homes.api.naver.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import homes.api.naver.vo.NaverClientVo;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverApiServiceImpl implements NaverApiService {
	public Logger Log = LogManager.getLogger(NaverApiServiceImpl.class) ;
	public String getLocalAddress(NaverClientVo paramVo) {
        String query = "";
        try {
        	query = URLEncoder.encode(paramVo.getQuery(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new HomesException("검색어[" + paramVo.getQuery() + "] 인코딩 실패");
        }
        String apiuri = paramVo.getApiuri() + "?query=" + query;    // JSON 결과
        apiuri += "&display=" + paramVo.getDisplay() ; 
        apiuri += "&start=" + paramVo.getStart() ;
        
        Log.info("*** naver addr api uri: {}", apiuri) ; 
        
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id"    , paramVo.getId());
        requestHeaders.put("X-Naver-Client-Secret", paramVo.getSecret());
        String responseBody = get(apiuri,requestHeaders);
        
        return responseBody ;
	}
	
    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = (new URI(apiUrl)).toURL();
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
        	throw new HomesException("API URL이 잘못되었습니다. : " + apiUrl) ; 
//            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new HomesException("연결이 실패했습니다. : " + apiUrl);
        } catch (URISyntaxException e) {
        	throw new HomesException("API URL이 잘못되었습니다. : " + apiUrl) ; 
		}
    }
    

    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new HomesException("API 응답을 읽는 데 실패했습니다.");
        }
    }

    private String get(String apiUrl, Map<String, String> headers){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :headers.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                throw new HomesException(responseCode, "API 응답을 읽는 데 실패했습니다.");
            }
        } catch (IOException e) {
            throw new HomesException("API 요청과 응답 실패");
        } finally {
            con.disconnect();
        }
    }
	
	@Override
	public String getLocalAddr(NaverClientVo paramVo) {
		// 네이버 검색 API 클라이언트 ID
        URI uri = UriComponentsBuilder
        		  .fromUriString("https://openapi.naver.com")
        		  .path("/v1/search/local.json")
        		  .queryParam("query"  , paramVo.getQuery())
        		  .queryParam("display", paramVo.getDisplay())
        		  .queryParam("start"  , paramVo.getStart())
        		  .queryParam("sort"   , paramVo.getSort())
        		  .encode(Charset.forName("UTF-8"))
        		  .build()
                .toUri();        
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", paramVo.getId())
                .header("X-Naver-Client-Secret", paramVo.getSecret())
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(req, String.class);
        
        String responseBody = responseEntity.getBody();
        
        Log.info("API Response: " + responseBody);
//        Log.info("API uri: " + uri);

		return responseBody ; 
	}
	
}
