package kr.or.nextit.detect;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@WebServlet("/detect")
public class DetectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/detect.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String text = req.getParameter("text");

        URL url = new URL("http://localhost:8000/lang");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        // Post 방식으로 메시지를 전달할 때 보내는 메시지 타입을 설정
        conn.setRequestProperty("Content-type", "application/json;utf-8");
        // 응답받는 데이터 타입 설정
        conn.setRequestProperty("Accept", "application/json");
        // Json 데이터 만들기
//        String jsonData = String.format("{\"text\": \"%s\"}", text);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", text);
        String jsonData = jsonObject.toJSONString();

        System.out.println(jsonData);
        // 데이터 전송하기
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(jsonData.getBytes("utf-8"));
        outputStream.close();
        // 데이터 받기

        // 접속 결과 코드 값
        System.out.println("Response Code: " + conn.getResponseCode());
        /*
        StringReader: 동기화가 필요할 때
        StringBuffer: 동기화가 필요없을 때
         */
        StringBuffer sb = new StringBuffer();
        BufferedReader br;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        conn.disconnect();
//        String msg = sb.toString();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject object = (JSONObject) jsonParser.parse(sb.toString());
            String msg = object.get("msg").toString();
            req.setAttribute("msg", msg);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        req.setAttribute("text", text);
        req.getRequestDispatcher("/detect.jsp").forward(req, resp);
    }
}
