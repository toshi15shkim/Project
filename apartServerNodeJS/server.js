var express = require('express');
var AWS = require('aws-sdk');
var app = express();
var http = require('http').Server(app);

//base경로
global.__base = __dirname + '/';

var cm = require(__base+'module/common');

//http.createServer(function handler(req, res) {
//    res.writeHead(200, {'Content-Type': 'text/plain'});
//    res.end('Hello World\n');
//}).listen(1337, '127.0.0.1');
//console.log('Server running at http://127.0.0.1:1337/');

AWS.config.update({
	  region: "ap-northeast-2"
});

var dynamodb = new AWS.DynamoDB();
var param = {
		TableName : "users"
}
dynamodb.describeTable(param, function(err, data) {
	if(err) {
		console.log("ERR " + err);
	} else {
		console.log("Data " + JSON.stringify(data));
	}
});

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