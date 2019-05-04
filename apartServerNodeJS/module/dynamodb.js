var AWS = require('aws-sdk');

AWS.config.update({
    region: "ap-northeast-2"
});

var dynamodb = new AWS.DynamoDB();

module.exports = dynamodb;