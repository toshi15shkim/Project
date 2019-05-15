var schedule = require('node-schedule');
var fs = require('fs');
var cm = require(__base+'module/common');

module.exports = function(requireParam) {
    var app = requireParam.app;

    app.get('/area_new_insert', function(req, res) {
        var fullAreaCode = fs.readFileSync(__base+'scheduler/fullAreaCodeInfo.txt').toString().split("\n");
        for(var i = 1; i < fullAreaCode.length; i ++) {
        // for(var i = 1; i < 1000; i ++) {
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
                        cm.db.docClient.put(insertParam, function(err, data) {
                            if(err) {
                                cm.logger.error("ERR " + err);
                            }
                        });
                    }
                }
            }
        }
    });
}

var rule = new schedule.RecurrenceRule();
rule.second = new schedule.Range(0, 59, 5);

var area_update = schedule.scheduleJob(rule, function() {
    // console.log(":aa");
});