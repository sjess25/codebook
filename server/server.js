const express = require('express');
const server = express();

const requests = require('./requests');

const parser = require('body-parser');

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

  console.log(req.rawHeaders + '\n\n');

  requests.login(usr, pss, function (ans) {
    res.send(ans);
  });
});

server.post('/data', function (req, res) {
  console.log(JSON.stringify(req.body));
  if (Array.isArray(req.body)) {
    req.body = req.body[0];
    console.error('Was array');
  }

  var id = Number(req.body.ID);

  console.log(req.rawHeaders + '\n\n');

  switch (id) {
    case 0: // user data
        var usr = req.body.User;
        var pss = req.body.Password;

        requests.login(usr, pss, function (ans) {
          console.log(JSON.stringify(ans, 2) + '\n\n');
          res.send(ans);
        }, true);
      break;

    case 1: // challenges list
      var from = req.body.Owner;
      requests.listChallenges(from, function (ans) {
        console.log(JSON.stringify(ans, 2) + '\n\n');
        res.send(ans);
      });
      break;

    case 2: // specific challenge
      var what = req.body.Challenge;
      requests.getChallenge(what, function (ans) {
        console.log(JSON.stringify(ans, 2) + '\n\n');
        res.send(ans);
      });
      break;
  
    default:
      res.send({
        status: 'error',
        data: 'Invalid request.'
      });
      break;
  }
});

server.post('/register', function (req, res) {
  var nick = req.body.NickName;
  var mail = req.body.Email;
  var pass = req.body.Password;
  var name = req.body.Name;

  console.log(req.rawHeaders + '\n\n');

  requests.register(nick, mail, pass, name, function (ans) {
    res.send(ans);
  });
});

server.listen(81, function () {
  console.log('Escuchando en el puerto 81.');
});
