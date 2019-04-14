var AWS = require('aws-sdk');
var http = require('http');
http.createServer(function handler(req, res) {
    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end('Hello World\n');
}).listen(1337, '127.0.0.1');
console.log('Server running at http://127.0.0.1:1337/');

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