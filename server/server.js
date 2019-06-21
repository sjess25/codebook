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
      
    case 3: // post a challenge
      var tec = req.body.Technologie;
      var tit = req.body.Title;
      var des = req.body.Description;
      var dif = req.body.Difficulty;
      var til = req.body.TimeLimit;
      var own = req.body.Owner;
      var ans = req.body.Answer;
      var rf1 = req.body.ref1;
      var rf2 = req.body.ref2;
      var rf3 = req.body.ref3;

      requests.postChallenge(tec, tit, des, dif, til, own, ans, rf1, rf2, rf3, function (ans) {
        console.log(JSON.stringify(ans, 2) + '\n\n');
        res.send(ans);
      });
      break;
    
    case 4: // specific basic info challenge
      var x = req.body.Challenge;
      requests.getBasicChallenge(x, function (ans) {
        console.log(JSON.stringify(ans, 2) + '\n\n');
        res.send(ans);
      });
      break;
    
    case 5: // active challenges per user
      var usr = req.body.Who;
      requests.activeSubscribed(usr, function (ans) {
        console.log(JSON.stringify(ans, 2) + '\n\n');
        res.send(ans);
      });
      break;

    case 6: // finished challenges per technology
      var tec = req.body.Technologie;
      var usr = req.body.Who;
      requests.inactiveSubscribed(usr, tec, function (ans) {
        console.log(JSON.stringify(ans, 2) + '\n\n');
        res.send(ans);
      });
      break;
      
    case 7:
      var tec = req.body.Technologie;
      var qry = req.body.Query;
      
      requests.search(tec, qry, function (ans) {
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
