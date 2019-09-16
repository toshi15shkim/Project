const mysql = require('mysql');

const conn = mysql.createConnection({
    host : 'localhost',
    port : '13306',
    user : 'toshi',
    password : '1234',
    database : 'apart'
});

module.exports = conn;