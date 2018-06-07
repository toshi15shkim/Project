package com.apartServer.api.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apartServer.api.common.dao.CommonSqlMapper;
 
@Service
public class CommonSqlService {
	
	@Autowired
	CommonSqlMapper commonSqlMapper;

	 
	public List<Map<String, Object>> getCommonCodeView(Map<String, Object> paramMap) {
		return commonSqlMapper.CommonCodeView(paramMap);
	}
	
	public List<Map<String, Object>> getAccidentGCateGoryList(Map<String, Object> paramMap) {
		return commonSqlMapper.AccidentGCateGoryList(paramMap);
	}
	
	public List<Map<String, Object>> getAccidentMCateGoryList(Map<String, Object> paramMap) {
		return commonSqlMapper.AccidentMCateGoryList(paramMap);
	}
	
	public List<Map<String, Object>> getActionsGCateGoryList(Map<String, Object> paramMap) {
		return commonSqlMapper.selectActionsGCateGoryList(paramMap);
	}
	
	public List<Map<String, Object>> getActionsMCateGoryList(Map<String, Object> paramMap) {
		return commonSqlMapper.selectActionsMCateGoryList(paramMap);
	}
	
	public boolean modifyCommonCodeMoniTimerModi(List<String> idList, List<String> valList){
		boolean successYn = true;
		
		Map<String,Object> resendMap = null;
		
		int i = 0;
		for(String m : idList) {           
	        resendMap = new HashMap<String, Object>();
	            
	        resendMap.put("DTL_CD_ID", m);
	        resendMap.put("MEMO", valList.get(i));
	        
			successYn = successYn && commonSqlMapper.CommonCodeMoniTimerModi(resendMap);
	        i++;
	    }

        return successYn;
	}
	
	public List<Map<String, Object>> getGCateGoryList(Map<String, Object> paramMap) {
		return commonSqlMapper.selectGCateGoryList(paramMap);
	}
	
	public List<Map<String, Object>> getMCateGoryList(Map<String, Object> paramMap) {
		return commonSqlMapper.selectMCateGoryList(paramMap);
	}
	
	public List<Map<String, Object>> getSCateGoryList(Map<String, Object> paramMap) {
		return commonSqlMapper.selectSCateGoryList(paramMap);
	}

	public List<Map<String, Object>> getTunnelInfo(Map<String, Object> paramMap) {
		return commonSqlMapper.selectTunnelInfo(paramMap);
	}
	
	/**
	 * 알람발생 수 조회
	 * @param mapParameter
	 * @exception Exception
	 */
	public int getAlarmTotal(Map paramMap) {
		return commonSqlMapper.getAlarmTotal(paramMap);
	}
	
	/**
	 * 알람발생 내역 조회
	 * @param mapParameter
	 * @exception Exception
	 */
	public List selectAlarmList(Map paramMap) {
		return commonSqlMapper.selectAlarmList(paramMap);
	}
}