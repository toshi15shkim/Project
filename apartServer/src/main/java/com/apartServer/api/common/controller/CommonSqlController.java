package com.apartServer.api.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.apartServer.api.common.service.CommonSqlService;
import com.alibaba.fastjson.JSON;

 
@Controller
@RequestMapping(path = "/common")
public class CommonSqlController {
	
	private final Log logger = LogFactory.getLog(CommonSqlController.class);
	
	@Autowired
	CommonSqlService commonSqlService;
	
	@ResponseBody
	@RequestMapping(path="/getAccidentMCateGoryList", produces = "application/text; charset=utf8")
	public ResponseEntity<String> getAccidentMCateGoryList(@RequestParam Map<String, Object> paramMap)
	{
        try {
    		List<Map<String, Object>> MCateGoryList = commonSqlService.getAccidentMCateGoryList(paramMap);
        	
            return new ResponseEntity<String>( JSON.toJSONString( MCateGoryList ), HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<String>( e.getMessage(), HttpStatus.BAD_REQUEST );
        }
	}
	
	@ResponseBody
	@RequestMapping(path="/getActionsMCateGoryList", produces = "application/text; charset=utf8")
	public ResponseEntity<String> getActionsMCateGoryList(@RequestParam Map<String, Object> paramMap)
	{
        try {
    		List<Map<String, Object>> MCateGoryList = commonSqlService.getActionsMCateGoryList(paramMap);
        	
            return new ResponseEntity<String>( JSON.toJSONString( MCateGoryList ), HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<String>( e.getMessage(), HttpStatus.BAD_REQUEST );
        }
	}
		 
	@ResponseBody
	@RequestMapping(path="/getMCateGoryList", produces = "application/text; charset=utf8")
	public ResponseEntity<String> getMCateGoryList(@RequestParam Map<String, Object> paramMap)
	{
        try {
    		List<Map<String, Object>> MCateGoryList = commonSqlService.getMCateGoryList(paramMap);
        	
            return new ResponseEntity<String>( JSON.toJSONString( MCateGoryList ), HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<String>( e.getMessage(), HttpStatus.BAD_REQUEST );
        }
	}
	
	@ResponseBody
	@RequestMapping(path="/getSCateGoryList", produces = "application/text; charset=utf8")
	public ResponseEntity<String> getSCateGoryList(@RequestParam Map<String, Object> paramMap)
	{
        try {
    		List<Map<String, Object>> SCateGoryList = commonSqlService.getSCateGoryList(paramMap);
        	
            return new ResponseEntity<String>( JSON.toJSONString( SCateGoryList ), HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<String>( e.getMessage(), HttpStatus.BAD_REQUEST );
        }
	}
	
	/**
     * 공통 / 상단 돌발상황 전체 알람 수
     * @param request
     * @param paramMap
     * @return JSON
     */
    @ResponseBody
    @RequestMapping(path = "/alarmView", produces = "application/text; charset=utf8")
    public ResponseEntity<String> alarmView(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
    	Map resultMap = new HashMap();
        try {
        	HttpSession session = request.getSession();
        	//알람 체크박스 선택 여부
        	String sAlarmSound = (String) session.getAttribute("sAlarmSound");
        	if(sAlarmSound == null) {	
        		sAlarmSound = "";
        	}
        	resultMap.put("sAlarmSound", sAlarmSound);
        	
        	//알람발생 수 조회
        	int recordCount = commonSqlService.getAlarmTotal(paramMap);
        	resultMap.put("recordCount", recordCount);
        	
        	//알람발생 내역 조회
        	List alarmList = commonSqlService.selectAlarmList(paramMap);
        	resultMap.put("alarmList", alarmList);
        	return new ResponseEntity<>(JSON.toJSONString(resultMap), HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e);
            resultMap.put("errMsg", "서버에서 처리중 에러가 발생했습니다.");
            return new ResponseEntity<>(JSON.toJSONString(resultMap), HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 공통 / 상단 알람 체크박스 체크 (세션)
     * @param request
     * @param paramMap
     * @return JSON
     */
    @ResponseBody
    @RequestMapping(path = "/putAlarmSession", produces = "application/text; charset=utf8")
    public ResponseEntity<String> putAlarmSession(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
    	Map resultMap = new HashMap();
        try {
        	HttpSession session = request.getSession();
        	String sAlarmSound = (String) paramMap.get("alarmSound");
        	session.setAttribute("sAlarmSound", sAlarmSound);
        	resultMap.put("sAlarmSound", sAlarmSound);
        	return new ResponseEntity<>(JSON.toJSONString(resultMap), HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e);
            resultMap.put("errMsg", "서버에서 처리중 에러가 발생했습니다.");
            return new ResponseEntity<>(JSON.toJSONString(resultMap), HttpStatus.BAD_REQUEST);
        }
    }
}