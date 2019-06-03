const express = require('express');
const mysql = require('mysql');
const server = express();

const parser = require('body-parser');

server.use(parser.urlencoded({
  extended: true
}));
server.use(parser.json());

// http://35.229.58.165:81/login?user=usuario&password=contraseña
server.get('/login', function (req, res) {
  var usr = req.query.user;
  var pss = req.query.password;

  console.log('Conexión.');

  var con = mysql.createConnection({
    host     : 'localhost',
    user     : 'jess',
    password : '1qaz!QAZ,.,',
    database : 'codebook'
  });

  var sql = `SELECT Password FROM Users WHERE NickName = "${usr}" OR Email = "${usr}";`;
  console.log(sql);
  con.query(sql,
    function (error, results, fields) {
      if (error) {
        console.error(error);
        res.send(`Error al realizar la consulta. ${error}`);
      } else {
        if (results) {
          if (results.length > 0) {
            if (results[0].Password == pss) {
              res.send('0');
            } else {
              res.send('1');
            }
          } else {
            res.send('2');
          }
        } else {
          res.send('2');
        }
      }
  });
  con.end();
});

server.listen(81, function () {
  console.log('Escuchando en el puerto 81.');
});
