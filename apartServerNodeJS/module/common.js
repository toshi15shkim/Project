var logger = require('./logger');
var db = require('./dynamodb');

//공공데이터 키 가져오기
var getPortalKey = function() {
    return new Promise(function(resolve, reject) {
        var keyParam = {
            TableName : "key_info",
            Key : {
                "type" : "data_portal"
            }
        }
        db.get(keyParam, function(err, data) {
            if(err) {
                logger.error("get key ERR " + err);
                reject();
            } else {
                resolve(data.Item.key);
            }
        });
    });
}

var nullChk = function(val) {
    if(val == undefined || val == null) {
        val = "";
    }
    return val;
}

exports.getPortalKey = getPortalKey;
exports.nullChk = nullChk;
exports.logger = logger;
exports.db = db;