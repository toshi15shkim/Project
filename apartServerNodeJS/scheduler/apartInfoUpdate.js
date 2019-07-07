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
    console.log("aaaa");
}

exports.execute = execute;