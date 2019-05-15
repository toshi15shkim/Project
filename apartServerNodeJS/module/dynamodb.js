var AWS = require('aws-sdk');

AWS.config.update({
    region: "ap-northeast-2"
});

var dynamodb = new AWS.DynamoDB();
var docClient = new AWS.DynamoDB.DocumentClient();

exports.dynamodb = dynamodb;
exports.docClient = docClient;