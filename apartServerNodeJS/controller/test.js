const fs = require('fs');
const cm = require(__base+'module/common');

module.exports = function(requireParam) {
    const app = requireParam.app;

    app.get('/down_cont', function(req, res) {
        cm.logger.info('download cont Call');

        let fileNm = 'paas_container.tar';
        let filePath = '/DATA/';
        let fileStream = fs.createReadStream(filePath+fileNm);
        res.setHeader('Content-disposition', 'attachment; filename='+fileNm);
        res.setHeader('Content-type', 'application/tar');
        fileStream.pipe(res);
    });

    app.get('/down_img', function(req, res) {
        cm.logger.info('download img Call');

        let fileNm = 'paas_image.tar';
        let filePath = '/DATA/';
        let fileStream = fs.createReadStream(filePath+fileNm);
        res.setHeader('Content-disposition', 'attachment; filename='+fileNm);
        res.setHeader('Content-type', 'application/tar');
        fileStream.pipe(res);
    });
}