var express = require('express');
var app = express();
var http = require('http').Server(app);

//base경로
global.__base = __dirname + '/';

var cm = require(__base+'module/common');

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

var schedule = require('./scheduler/schedule')(requireParam);
var users = require('./controller/users')(requireParam);

http.listen(8080);
cm.logger.debug("Server Start on port 8080");

/*var param = {
		TableName : "users",
		KeyConditionExpression : "contains(mobile_key, :t)",
		ExpressionAttributeValues : {
			":t" : "test"
		}
};
var docClient = new AWS.DynamoDB.DocumentClient();
docClient.get(param, function(err, data) {
	if(err) {
		console.log("ERR " + err);
	} else {
		console.log("Data " + JSON.stringify(data));
	}
});*/