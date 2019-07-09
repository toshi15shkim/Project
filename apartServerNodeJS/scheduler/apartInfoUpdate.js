const schedule = require('node-schedule');
const sleep = require('system-sleep');
const cm = require(__base+'module/common');

//매일 새벽 1시 20분에 실행
const rule = new schedule.RecurrenceRule();
rule.dayOfWeek = [0];
rule.hour = 1;
rule.minute = 20;
const test = schedule.scheduleJob(rule, function() {
    execute();
});

const execute = () => {
    cm.logger.info("Apart Info Update Scheduling Start");
    allDataSelect().then(function(data) {
        console.log("b");
    });
}

//모든 거래내역 조회
const allDataSelect = () => {
    return new Promise(function(resolve, reject) {
        var defaultParam = {
            TableName : "trade_detail_real"
        }
        cm.db.scan(defaultParam, function(err, data) {
            if(err) {
                cm.logger.error("apartInfoUpdate execute ERR " + err);
                reject();
            } else {
                //console.log(data);
                resolve(data);
            }
        });
    });
}

exports.execute = execute;