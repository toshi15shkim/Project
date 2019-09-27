const express = require('express');
const app = express();
const http = require('http').Server(app);

//base경로
global.__base = __dirname + '/';

const cm = require(__base+'module/common');

const requireParam = {
	app : app
}

const schedule = require('./scheduler/schedule')(requireParam);
const users = require('./controller/users')(requireParam);
const test = require('./controller/test')(requireParam);

http.listen(8080);
cm.logger.debug("Server Start on port 8080");