const express = require('express');
const app = express();
const http = require('http').Server(app);

//base경로
global.__base = __dirname + '/';

const cm = require(__base+'module/common');

// var param = {
// 		TableName : "users"
// }

// cm.db.scan(param, function(err, data) {
// 	if(err) {
// 		console.log("ERR " + err);
// 	} else {
// 		console.log("Data " + JSON.stringify(data));
// 	}
// });

const requireParam = {
	app : app
}

const schedule = require('./scheduler/schedule')(requireParam);
const users = require('./controller/users')(requireParam);

http.listen(8080);
cm.logger.debug("Server Start on port 8080");