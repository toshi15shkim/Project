var schedule = require('node-schedule');
var request = require('request');
var xml2js = require('xml2js');
var fs = require('fs');
var cm = require(__base+'module/common');

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
                        var insertParam = {
                            TableName : "area_info",
                            Item: {
                                "area_code" : areaInfoArray[0].substr(0, 5),
                                "sgg_name" : sgg_name,
                                "sido_name" : sido_name
                            }
                        }
                        cm.db.put(insertParam, function(err, data) {
                            if(err) {
                                cm.logger.error("area_new_insert ERR " + err);
                            }
                        });
                    }
                }
            }
        }
    });

    app.get('/trade_detail_insert', function(req, res) {
        // trade_detail_insert();
        nextAreaCodeFnc();
    });
}

var rule = new schedule.RecurrenceRule();
rule.second = new schedule.Range(0, 59, 5);

var area_update = schedule.scheduleJob(rule, function() {
    // console.log(":aa");
});

var trade_detail_insert = (area_cd) => {
    cm.getPortalKey().then(function(portalKey) {    //포탈 키 가져오기
        getBasePeriod().then(function(last_date) {  //조회할 날짜 가져오기
            selectAreaList(area_cd).then(function(area_code) {  //조회할 지역코드 가져오기
                return Promise.all(area_code.map(function (code) {
                    insertDataFnc(code, last_date, portalKey).then((msg) => {
                        cm.logger.info(msg + "/" + code + "/" + last_date);
                    });
                }));
            }).catch((err) => cm.logger.error("selectAreaList err ", err));
        }).catch((err) => cm.logger.error("getBasePeriodv err ", err));
    }).catch((err) => cm.logger.error("getPortalKey err ", err));
}

let nextAreaCode = () => new Promise((resolve) => {
    var keyParam = {
        TableName : "key_info",
        Key : {
            "type" : "area_code"
        }
    }
    cm.db.get(keyParam, function(err, data) {
        if(err) {
            logger.error("get key_info area_code ERR " + err);
            reject();
        } else {
            resolve(data.Item.key);
        }
    });
});

async function nextAreaCodeFnc() {
    let area_cd = await nextAreaCode();
    trade_detail_insert(area_cd);
}

var insertDataFnc = (area_code, last_date, portalKey) => {
    return new Promise(function(resolve, reject) {
        var dataParam = {
            url : "http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev?serviceKey="+portalKey,
            qs : {
                "pageNo": 1,
                "startPage": 1,
                "numOfRows": 10000,
                "pageSize": 10,
                "LAWD_CD": area_code,
                "DEAL_YMD": last_date
            }
        }
        request(dataParam, function(err, response, body) {
            var parser = new xml2js.Parser();
            parser.parseString(body, function(err, result) {
                if(err) {
                    console.log(JSON.stringify(result));
                    cm.logger.error('data portal Error ', err); 
                }
                if(result == undefined) {
                    return resolve('success');
                }
                if(result.response.header[0].resultCode[0] == "99") {
                    cm.logger.error("data portal key expired");
                    return reject();
                } else {
                    // cm.logger.info("data portal search success")
                    var bodyData = result.response.body[0].items[0].item;
                    if(bodyData == undefined) {
                        return resolve('success');
                    }
                    let cnt = 0;
                    for(var i = 0; i < bodyData.length; i ++) {
                        tradeDetailRealInsert(bodyData[i], i).then(() => {
                            ++cnt;
                            if(bodyData.length == cnt) {
                                return resolve('success');
                            }
                        });
                    }
                }
            });
        });
    });
}

//조회할 지역 가져오기
var selectAreaList = (area_cd) => {
    return new Promise(function(resolve, reject) {
        var areaParam = {
            TableName : "area_info",
            FilterExpression: "area_code = :area_code",
            ExpressionAttributeValues: {
                ":area_code": area_cd,
            }
        }
        cm.db.scan(areaParam, function(err, data) {
            if(err) {
                cm.logger.error("selectAreaList ERR " + err);
                reject();
            } else {
                var returnData = data.Items.map(function(obj) {
                    return obj.area_code;
                }); 
                resolve(returnData);
                // 테스트용
                // resolve(['47190']);
            }
        }); 
    });
}

//조회할 날짜 가져오기
var getBasePeriod = function() {
    return new Promise(function(resolve, reject) {
        var keyParam = {
            TableName : "key_info",
            Key : {
                "type" : "period"
            }
        }
        cm.db.get(keyParam, function(err, data) {
            if(err) {
                cm.logger.error("get period ERR " + err);
                reject();
            } else {
                resolve(data.Item.key);
            }
        });
    });
}

//실거래 원천정보 insert
var tradeDetailRealInsert = function(bodyData, idx) {
    return new Promise(function(resolve, reject) {
        try {
            var bodyDataItem = new BodyDataItem(bodyData);
            var paramItem = bodyDataItem.convert();
            paramItem['serial'] = idx;
            var insertParam = {
                TableName : "trade_detail_real",
                Item : paramItem,
            }
            cm.db.put(insertParam, function(err, data) {
                if(err) {
                    cm.logger.error("insert trade_detail_real ERR " + err);
                    return reject();
                } else {
                    cm.logger.info("insert trade_detail_real success");
                    return resolve();
                }
            });
        } catch(e) {
            cm.logger.error("bodyDataItem Error ", e);
            return reject();
        }
    });
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
            "day" : this.data['일'][0],
            "month" : this.data['월'][0],
            "year" : this.data['년'][0],
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