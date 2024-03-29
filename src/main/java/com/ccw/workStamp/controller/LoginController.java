package com.ccw.workStamp.controller;

import java.util.HashMap;
import java.util.Map;

import java.util.List;
import java.rmi.server.ExportException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.ccw.workStamp.service.LoginService;
import com.ccw.workStamp.util.BusinessException;


@Controller
@CrossOrigin("*")
@SuppressWarnings("unchecked")
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
    @Autowired
    LoginService loginService;
    
    /**
     * ソーシャルメディアのログインUIDをもとに会員登録されているユーザーなのかを判別する。
     *　(検索検索、登録されている場合はメインページへ移動、登録されていない場合は会員登録ページへ移動する)
     *
     * sns간편로그인 UID 값을 바탕으로 해당 사용자가 회원가입되어 있는지를 조회한다.
     * (조회결과가 있다면 APP에서는 메인페이지로 이동, 없다면 회원가입페이지로 이동)
     * 
     * @author ジョチャンウク／조창욱
     * @version 1.0
     * @param ソーシャルメディア タイプ、UID／sns간편로그인 타입 및 고유ID
     * @return ユーザーシーケンス、ユーザー名、社員番号、会員情報(郵便番号、基本住所、詳細住所、位置座標)
     *
     *         사용자일련번호, 사용자이름, 사용자사원번호, 회사주소정보(우편번호, 기본주소, 상세주소, 위치좌표) 
     * @exception　サーバーで定義したbussinessExceptionが起こる場合はrsltCdは-1、errMsgはbussinessExaptionのメッセージをリターン
     *          　思わなかったExceptionが起こる場合はrsltCdは-1、errMsgはじてい指定されたメッセージをリターン
     *
     *            서버가 정의한 bussinessException이 일어나는 경우 rsltCd는 -1, errMsg는 bussinessExaption의 메시지를 반환
     *            의도치 않은 Exception이 일어나는 경우 rsltCd는 -1, errMsg는 지정된 메시지를 반환 
     * 
     **/
    @RequestMapping(value="/retrieveUserInfoCmd", consumes="application/json", produces = "application/json;charset=UTF-8")
    public @ResponseBody Map<String, Object> retrieveUserInfoCmd(@RequestBody HashMap<String, Object> requestMap) {
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> commData = new HashMap<String, Object>();
        List<Map<String,Object>> loopData = new ArrayList<Map<String,Object>>();
       
        try{
             
            resultMap.put("rsltCd", 0);
            resultMap.put("errMsg", "");
            
            commData = loginService.retrieveUserInfo((Map<String, Object>) requestMap.get("commData"));
            
        }catch(BusinessException err){      
            resultMap.put("rsltCd", -1);
            resultMap.put("errMsg", err.toString());
        }catch(Exception err){
            resultMap.put("rsltCd", -397);
            resultMap.put("errMsg", "회원정보 조회 중 오류가 발생하였습니다.");
            err.printStackTrace();  
        }
        
        resultMap.put("commData", commData);
        resultMap.put("loopData", loopData);
        
       return resultMap;
    }
    
	
}
