package homes.api.kakao.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import homes.api.kakao.vo.KakaoApiReqVo;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoApiServiceImpl implements KakaoApiService {
	public Logger Log = LogManager.getLogger(KakaoApiServiceImpl.class) ;
	public String getKakaoTranscoord(KakaoApiReqVo paramVo) {
        String apiuri = paramVo.KAKAO_REST_API_TRANS_COORDS_URL + "?input_coord=WTM&output_coord=WGS84"; 
        
        apiuri += "&x=" + paramVo.getX() ;  
        apiuri += "&y=" + paramVo.getY() ;        
        
        Log.info("*** kakao trans coords api uri: {}", apiuri) ; 
        
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", "KakaoAK " + paramVo.getKakaoApiKey());
        String responseBody = get(apiuri,requestHeaders);
        Log.info("*** kakao trans coords : {}", responseBody) ; 
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
            	Log.error("Request Header[{}]: {}", header.getKey(), header.getValue());
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
            	Log.error("responseCode: {}", responseCode);
                throw new HomesException(responseCode, "API 응답을 읽는 데 실패했습니다.");
            }
        } catch (IOException e) {
            throw new HomesException("API 요청과 응답 실패");
        } finally {
            con.disconnect();
        }
    }
}
