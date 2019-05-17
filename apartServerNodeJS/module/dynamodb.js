var AWS = require('aws-sdk');

AWS.config.update({
    region: "ap-northeast-2"
});

var docClient = new AWS.DynamoDB.DocumentClient();

module.exports = docClient;