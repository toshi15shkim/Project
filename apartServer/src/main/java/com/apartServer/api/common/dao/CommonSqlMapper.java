package com.apartServer.api.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface CommonSqlMapper {

    public List<Map<String, Object>> CommonCodeView(Map<String, Object> paramMap);

    public List<Map<String, Object>> AccidentGCateGoryList(Map<String, Object> paramMap);

    public List<Map<String, Object>> AccidentMCateGoryList(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectActionsGCateGoryList(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectActionsMCateGoryList(Map<String, Object> paramMap);

    public boolean CommonCodeMoniTimerModi(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectGCateGoryList(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectMCateGoryList(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectSCateGoryList(Map<String, Object> paramMap);
    
    public List<Map<String, Object>> selectTunnelInfo(Map<String, Object> paramMap);
    
	//알람발생 수 조회
    public int getAlarmTotal(Map paramMap);
    //알람발생 내역 조회
    public List selectAlarmList(Map paramMap);
}