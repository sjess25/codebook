const express = require('express');
const mysql = require('mysql');
const server = express();

const parser = require('body-parser');

// misc functions

function sqlQuery(sql, callback) {
  var con = mysql.createConnection({
    host: 'localhost',
    user: 'jess',
    password: '1qaz!QAZ,.,',
    database: 'codebook'
  });

  con.query(sql, callback);

  con.end();
}

server.use(parser.urlencoded({
  extended: true
}));
server.use(parser.json());
server.use(function(req, res, next) {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
  next();
});

// http://URL:81/login?user=usuario&password=contraseña
server.get('/login', function (req, res) {
  var usr = req.query.user;
  var pss = req.query.password;

  var sql = `SELECT Password FROM Users WHERE NickName = "${usr}" OR Email = "${usr}";`;

  console.log(`Petición de login.\n${sql}`);

  sqlQuery(sql,
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
});

server.post('/register', function (req, res) {
  var nick = req.body.NickName;
  var mail = req.body.Email;
  var pass = req.body.Password;
  var name = req.body.Name;

  var sql = `INSERT INTO Users (NickName, Email, Password, Name)
              VALUES ("${nick}", "${mail}", "${pass}", "${name}");`;

  console.log(`Registrando usuario.\n${sql}`);

  sqlQuery(sql, function (error, results, fields) {
    if (error) {
      console.error(error);
      res.send({
        result: "error",
        data: error.message
      });
    } else {
      if (results.insertId) {
        res.send({
          result: "success",
          data: results.insertId
        });
      } else {
        res.send({
          result: "error",
          data: "Failed at insertion."
        });
      }
    }
  });
});

server.listen(81, function () {
  console.log('Escuchando en el puerto 81.');
});