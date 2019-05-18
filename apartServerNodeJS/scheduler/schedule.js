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

    app.get('/test', function(req, res) {
        cm.getPortalKey().then(function(portalKey) {
            var dataParam = {
                url : "http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev?serviceKey="+portalKey,
                qs : {
                    "pageNo": 1,
                    "startPage": 1,
                    "numOfRows": 1,
                    "pageSize": 10,
                    "LAWD_CD": 11110,
                    "DEAL_YMD": 201512
                }
            }
            request(dataParam, function(err, response, body) {
                var parser = new xml2js.Parser();
                parser.parseString(body, function(err, result) {
                    // console.log(JSON.stringify(result));
                    var bodyData = result.response.body[0].items[0].item;
                    for(var i = 0; i < bodyData.length; i ++) {
                        console.log(bodyData[i]['거래금액'][0].replace(/,/gi, "").trim());
                        console.log(bodyData[i]['건축년도'][0]);
                        console.log(bodyData[i]['년'][0]);
                        console.log(bodyData[i]['도로명'][0]);
                        console.log(bodyData[i]['도로명건물본번호코드'][0]);
                        console.log(bodyData[i]['도로명건물부번호코드'][0]);
                        console.log(bodyData[i]['도로명시군구코드'][0]);
                        console.log(bodyData[i]['도로명일련번호코드'][0]);
                        console.log(bodyData[i]['도로명지상지하코드'][0]);
                        console.log(bodyData[i]['도로명코드'][0]);
                        console.log(bodyData[i]['법정동'][0].trim());
                        console.log(bodyData[i]['법정동본번코드'][0]);
                        console.log(bodyData[i]['법정동부번코드'][0]);
                        console.log(bodyData[i]['법정동시군구코드'][0]);
                        console.log(bodyData[i]['법정동읍면동코드'][0]);
                        console.log(bodyData[i]['법정동지번코드'][0]);
                        console.log(bodyData[i]['아파트'][0]);
                        console.log(bodyData[i]['월'][0]);
                        console.log(bodyData[i]['일'][0]);
                        console.log(bodyData[i]['일련번호'][0]);
                        console.log(bodyData[i]['전용면적'][0]);
                        console.log(bodyData[i]['지번'][0]);
                        console.log(bodyData[i]['지역코드'][0]);
                        console.log(bodyData[i]['층'][0]);
                    }
                    console.log(JSON.stringify(bodyData));
                });
            });
        });
    });
}

var rule = new schedule.RecurrenceRule();
rule.second = new schedule.Range(0, 59, 5);

var area_update = schedule.scheduleJob(rule, function() {
    // console.log(":aa");
});