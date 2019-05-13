var schedule = require('node-schedule');
var fs = require('fs');
var cm = require(__base+'module/common');

module.exports = function(requireParam) {
    var app = requireParam.app;

    app.get('/area_new_insert', function(req, res) {
        var fullAreaCode = fs.readFileSync('./fullAreaCodeInfo.txt').toString().split("\n");
        cm.logger.debug(fullAreaCode.length);
    });
}

var rule = new schedule.RecurrenceRule();
rule.second = new schedule.Range(0, 59, 5);

var area_update = schedule.scheduleJob(rule, function() {
    console.log(":aa");
});