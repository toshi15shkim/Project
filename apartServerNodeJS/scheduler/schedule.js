const schedule = require('node-schedule');
const request = require('request');
const xml2js = require('xml2js');
const fs = require('fs');
const apartInfoUpdate = require('./apartInfoUpdate');
const sleep = require('system-sleep');
const cm = require(__base+'module/common');

module.exports = function(requireParam) {
    var app = requireParam.app;

    app.get('/area_new_insert', function(req, res) {
        var fullAreaCode = fs.readFileSync(__base+'scheduler/fullAreaCodeInfo.txt').toString().split("\n");
        for(var i = 1; i < fullAreaCode.length; i ++) {
            var areaInfoArray = fullAreaCode[i].split("\t");    //탭으로 구분
            var exist_yn = areaInfoArray[areaInfoArray.length-1];

            if(exist_yn == "존재\r") {  //존재하는 지역
                if(areaInfoArray[0].substr(5) == "00000") {        //오른쪽 끝자리 00000
                    var areaArray = areaInfoArray[1].split(" ");
                    if(areaArray.length > 1) {        //상위 지역만 가져옴 (시군구 이상)
                        var sido_name = areaArray[0];
                        var sgg_name = areaArray[1];
                        if(areaArray.length > 2) {
                            areaArray.shift();
                            sgg_name = areaArray.join(" ");  //시도를 삭제하고 시군구 이어 붙임
                        }

                        //지역 insert
                        cm.conn.query('insert into area_info values(?, ?, ?)', [areaInfoArray[0].substr(0, 5), sgg_name, sido_name], function(err) {
                            if(err) {
                                cm.logger.error("area_new_insert ERR " + err);
                                break;
                            }
                        });
                        sleep(30);
                    }
                }
            }
        }
    });

    //아파트 거래내역 상세 insert
    app.get('/trade_detail_insert', function(req, res) {
        trade_detail_insert();
    });

    //아파트 그룹화 상세정보 update
    app.get('/apartInfoUpdate', function(req, res) {
        apartInfoUpdate.execute();
    });
}

//매일 새벽 1시 10분에 실행
const rule = new schedule.RecurrenceRule();
//rule.dayOfWeek = [0, new schedule.Range(0, 6)];
rule.dayOfWeek = [1];
rule.hour = 1;
rule.minute = 10;
const area_update = schedule.scheduleJob(rule, function() {
    trade_detail_insert();
});

var trade_detail_insert = () => {
    cm.logger.info("Area_Update Scheduling Start");
    cm.getPortalKey().then(function(portalKey) {    //포탈 키 가져오기
        getBasePeriod().then(function(last_date) {  //조회할 날짜 가져오기
            selectAreaList().then(function(area_code) {  //조회할 지역코드 가져오기
                insertDataFnc(0, area_code, last_date, portalKey);
            }).catch((err) => cm.logger.error("selectAreaList err ", err));
        }).catch((err) => cm.logger.error("getBasePeriodv err ", err));
    }).catch((err) => cm.logger.error("getPortalKey err ", err));
}

//데이터 등록
var insertDataFnc = (a_idx, area_code, last_date, portalKey) => {
    cm.logger.info("insertDataFnc",area_code[a_idx]);
    //해당 월의 모든 지역 데이터를 저장하면 월을 +1 해서 다시 조회한다.
    if(a_idx == (area_code.length)) {
        cm.logger.info("@@@@@@@@@@@finished@@@@@@@@@@@@@"+last_date);
        let updateQuery = "update key_info set key_data = ? where key_type = 'period'";
        cm.conn.query(updateQuery, addDate(last_date), function(err) {
            if(err) {
                cm.logger.error("update period ERR " + err);
            } else {
                //변경된 날짜로 다시 insert 시작
                trade_detail_insert();
            }
        });

        // var updateParam = {
        //     TableName : "key_info",
        //     Key : {
        //         "type" : "period"
        //     },
        //     UpdateExpression: 'set key_data = :key',
        //     ExpressionAttributeValues: {
        //         ':key' : addDate(last_date),
        //     }
        // }
        // cm.db.update(updateParam, function(err, data) {
        //     if(err) {
        //         cm.logger.error("update period ERR " + err);
        //     } else {
        //         //변경된 날짜로 다시 insert 시작
        //         trade_detail_insert();
        //     }
        // });
        return 'success';
    }
    var dataParam = {
        url : "http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev?serviceKey="+portalKey,
        qs : {
            "pageNo": 1,
            "startPage": 1,
            "numOfRows": 10000,
            "pageSize": 10,
            "LAWD_CD": area_code[a_idx],
            "DEAL_YMD": last_date
        }
    }
    request(dataParam, function(err, response, body) {
        if(body != undefined) {
            var parser = new xml2js.Parser();
            parser.parseString(body, function(err, result) {
                if(err) {
                    cm.logger.error('data portal Error ', err); 
                    return 'error';
                }
                if(result == undefined) {
                    return 'success';
                }
                if(result.response.header[0].resultCode[0] == "99") {
                    cm.logger.error("data portal key expired");
                    return 'error';
                } else {
                    var bodyData = result.response.body[0].items[0].item;
                    if(bodyData == undefined) {
                        insertDataFnc(++a_idx, area_code, last_date, portalKey);
                    } else {
                        let cnt = 0;
                        tradeDetailRealInsert(0, bodyData, {"a_idx":++a_idx, "area_code":area_code, "last_date":last_date, "portalKey":portalKey});
                    }
                }
            });
        }
    });
}

//조회할 지역 가져오기
var selectAreaList = () => {
    return new Promise(function(resolve, reject) {
        let selectQuery = "select * from area_info";
        cm.conn.query(selectQuery, function(err, result) {
            if(err) {
                cm.logger.error("selectAreaList ERR " + err);
                reject();
            } else {
                var returnData = result.map(function(obj) {
                    return obj.area_code;
                });
                // resolve(returnData);
                // 테스트용
                resolve(['11110', '11140']);
            }
        });
        // var areaParam = {
        //     TableName : "area_info"
        // }
        // cm.db.scan(areaParam, function(err, data) {
        //     if(err) {
        //         cm.logger.error("selectAreaList ERR " + err);
        //         reject();
        //     } else {
        //         var returnData = data.Items.map(function(obj) {
        //             return obj.area_code;
        //         });
        //         resolve(returnData);
        //         // 테스트용
        //         //resolve(['11110', '11140']);
        //     }
        // }); 
    });
}

//조회할 날짜 가져오기
var getBasePeriod = function() {
    return new Promise(function(resolve, reject) {
        let selectQuery = "select key_data from key_info where key_type = 'period'";
        cm.conn.query(selectQuery, function(err, result) {
            if(err) {
                cm.logger.error("get period ERR " + err);
                reject();
            } else {
                resolve(result[0].key_data);
            }
        });
        // var keyParam = {
        //     TableName : "key_info",
        //     Key : {
        //         "type" : "period"
        //     }
        // }
        // cm.db.get(keyParam, function(err, data) {
        //     if(err) {
        //         cm.logger.error("get period ERR " + err);
        //         reject();
        //     } else {
        //         resolve(data.Item.key_data);
        //     }
        // });
    });
}

//실거래 원천정보 insert
const tradeDetailRealInsert = function(idx, bodyData, p_obj) {
    sleep(30); //ProvisionedThroughputExceededException 에러 떄문에 강제 sleep 할당
    console.log("tradeDetailRealInsert",idx, bodyData[idx]['지역코드'][0]);
    if(idx == (bodyData.length-1)) {
        insertDataFnc(p_obj.a_idx, p_obj.area_code, p_obj.last_date, p_obj.portalKey);
        return 'success';
    }
    try {
        let bodyDataItem = new BodyDataItem(bodyData[idx]);
        let paramItem = bodyDataItem.convert();
        console.log(Object.keys(paramItem));    //insert문으로 변경해야함
        //insert문으로 변경해야함
        //insert문으로 변경해야함

        

        // paramItem['serial'] = idx;

        // let insertQuery = "insert into trade_detail_real";

        // let insertParam = {
        //     TableName : "trade_detail_real",
        //     Item : paramItem,
        // }
        // cm.db.put(insertParam, function(err, data) {
        //     if(err) {
        //         cm.logger.error("insert trade_detail_real ERR " + err);
        //         return 'error';
        //     } else {
        //         cm.logger.info("insert trade_detail_real success");
        //         tradeDetailRealInsert(++idx, bodyData, p_obj);
        //     }
        // });
    } catch(e) {
        cm.logger.error("bodyDataItem Error ", e);
        return 'error';
    }
}

//데이터 변환 추상 클래스
class DataConvert {
    constructor(data) {
        this.data = data;
    }
    getAllData() {
        return this.convert();
    }
    convert() {
        return this.data;
    }
    undefinedChk(str) {
        var r_str = false;
        if(str != undefined && str.length > 0) {
            r_str = true;
        }
        return r_str;
    }
}
//데이터 변환 클래스
class BodyDataItem extends DataConvert {
    convert() {
        var returnData = {
            "area_code" : this.data['지역코드'][0],
            "apart_serial_no" : this.data['일련번호'][0],
            "trade_day" : this.data['일'][0],
            "trade_month" : this.data['월'][0],
            "trade_year" : this.data['년'][0],
            "price" : this.data['거래금액'][0].replace(/,/gi, "").trim(),
            "build_year" : this.data['건축년도'][0],
            "law_name" : this.data['법정동'][0].trim(),
            "law_sgg_cd" : this.data['법정동시군구코드'][0],
            "law_emd_cd" : this.data['법정동읍면동코드'][0],
            "law_jibun_cd" : this.data['법정동지번코드'][0],
            "apart_name" : this.data['아파트'][0],
            "area_size" : this.data['전용면적'][0],
            "jibun" : this.data['지번'][0],
            "floor" : this.data['층'][0],
        };

        if(super.undefinedChk(this.data['도로명'])) returnData['road_name'] = this.data['도로명'][0];
        if(super.undefinedChk(this.data['도로명시군구코드'])) returnData['road_sgg_cd'] = this.data['도로명시군구코드'][0];
        if(super.undefinedChk(this.data['도로명일련번호코드'])) returnData['road_serial_cd'] = this.data['도로명일련번호코드'][0];
        if(super.undefinedChk(this.data['도로명코드'])) returnData['road_name_cd'] = this.data['도로명코드'][0];
        if(super.undefinedChk(this.data['도로명건물본번호코드'])) returnData['road_main_cd'] = this.data['도로명건물본번호코드'][0];
        if(super.undefinedChk(this.data['도로명건물부번호코드'])) returnData['road_sub_cd'] = this.data['도로명건물부번호코드'][0];
        if(super.undefinedChk(this.data['도로명지상지하코드'])) returnData['road_updw_cd'] = this.data['도로명지상지하코드'][0];
        if(super.undefinedChk(this.data['법정동본번코드'])) returnData['law_main_cd'] = this.data['법정동본번코드'][0];
        if(super.undefinedChk(this.data['법정동부번코드'])) returnData['law_sub_cd'] = this.data['법정동부번코드'][0];

        return returnData;
    }
}

//마지막 조회 월에서 +1
const addDate = (last_date) => {
    let year = last_date.substr(0, 4);  //yyyy
    let month = last_date.substr(4);    //mm
    ++month;
    if(month > 12) {
        ++year;
        month = "01";
    } else if(month < 10) {
        month = "0"+month;
    }
    cm.logger.info(year+""+month);
    return year+""+month;
}