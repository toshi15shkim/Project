const logger = require('./logger');
const db = require('./dynamodb');
const conn = require('./mysqldb');

//공공데이터 키 가져오기
// const getPortalKey = function() {
//     return new Promise(function(resolve, reject) {
//         var keyParam = {
//             TableName : "key_info",
//             Key : {
//                 "type" : "data_portal"
//             }
//         }
//         db.get(keyParam, function(err, data) {
//             if(err) {
//                 logger.error("get key ERR " + err);
//                 reject();
//             } else {
//                 resolve(data.Item.key_data);
//             }
//         });
//     });
// }

//공공데이터 키 가져오기
const getPortalKey = function() {
    return new Promise(function(resolve, reject) {
        const query = 'select key_data from key_info where key_type = "data_portal"';

        conn.query(query, function(err, result, fields) {
            if(err) {
                logger.error("get key ERR " + err);
                reject();
            } else {
                resolve(result[0].key_data);
            }
        });
    });
}

exports.getPortalKey = getPortalKey;
exports.logger = logger;
exports.db = db;