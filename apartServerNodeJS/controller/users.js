var cm = require(__base+'module/common');

module.exports = function(requireParam) {
    var app = requireParam.app;

    app.get('/test', function(req, res) {
        cm.logger.info('test Call');
    });
}