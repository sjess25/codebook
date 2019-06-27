const mysql = require('mysql');

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

module.exports = {

    login: function (usr, pss, callback, info) {
        var sql = `SELECT ${info ? "*" : "Password"} FROM Users WHERE NickName = "${usr}" OR Email = "${usr}";`;
        var ires = {
            ID: 0,
            Name: "",
            NickName: "",
            Email: "",
            Teacher: false,
            Profile: ""
        }

        console.log(`Petición de login.\n${sql}\n`);

        sqlQuery(sql,
            function (error, results, fields) {
                if (error) {
                    console.error(error);
                    callback(`Error al realizar la consulta. ${error}`);
                } else {
                    if (results) {
                        if (results.length > 0) {
                            if (results[0].Password == pss) {
                                if (!info) {
                                    callback('0');
                                } else {
                                    ires.ID = results[0].ID;
                                    ires.Email = results[0].Email;
                                    ires.Name = results[0].Name;
                                    ires.NickName = results[0].NickName;
                                    ires.Profile = results[0].Profile;
                                    ires.Teacher = results[0].Teacher;

                                    callback(ires);
                                }
                            } else {
                                if (!info) {
                                    callback('1');
                                } else {
                                    callback(ires);
                                }
                            }
                        } else {
                            if (!info) {
                                callback('2');
                            } else {
                                callback(ires);
                            }
                        }
                    } else {
                        if (!info) {
                            callback('2');
                        } else {
                            callback(ires);
                        }
                    }
                }
            });
    },

    register: function (nick, mail, pass, name, callback) {
        var sql = `INSERT INTO Users (NickName, Email, Password, Name)
              VALUES ("${nick}", "${mail}", "${pass}", "${name}");`;
        // var sql2 = ''; // Add SQL query to include default registration challenge.

        console.log(`Registrando usuario.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback({
                    result: "error",
                    data: error.message
                });
            } else {
                if (results.insertId) {
                    callback({
                        result: "success",
                        data: results.insertId
                    });
                } else {
                    callback({
                        result: "error",
                        data: "Failed at insertion."
                    });
                }
            }
        });
    },

    // List user subscribed active challenges.
    activeSubscribed: function (who, callback) {
        var result = [];
        var sql = `SELECT *, (DATEDIFF(NOW(), DATE_ADD(TakenAt, INTERVAL c.TimeLimit DAY))) as Diff
                     FROM ChallengeSubscribers cs,
                     (SELECT ID AS cID, Title, Description, Owner, Creation, TimeLimit, Owner as cOwn
                      FROM Challenges) AS c,
                     (SELECT ID AS tID, Technologie, Challenge AS tChallenge
                      FROM ChallengeTechnologies) AS t
                     WHERE cs.Who = "${who}"
                     AND t.tChallenge = c.cID
                     AND cs.Challenge = c.cID
                     ORDER BY TakenAt DESC;`;
        var sqlA = ``;
        
        console.log(`Lista de retos suscritos activos solicitada.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback([]);
            } else {
                if (results) {
                    if (results.length > 0) {
                        results.forEach(element => {
                            if (element.Diff <= 0) {
                                result.push({
                                    ID: element.cID,
                                    Title: element.Title,
                                    Description: element.Description,
                                    Technologie: element.Technologie,
                                    Owner: element.cOwn
                                });
                            }
                        });

                        callback(result);
                    } else {
                        callback([]);
                    }
                } else {
                    callback([]);
                }
            }
        });
    },

    // List user subscribed inactive challenges per technology.
    inactiveSubscribed: function (who, tid, callback) {
        var result = [];
        var sql = `SELECT *, (DATEDIFF(NOW(), DATE_ADD(TakenAt, INTERVAL c.TimeLimit DAY))) as Diff
                    FROM ChallengeSubscribers cs,
                    (SELECT ID AS cID, Title, Description, Owner, Creation, TimeLimit
                     FROM Challenges) AS c,
                    (SELECT ID AS tID, Technologie, Challenge AS tChallenge
                     FROM ChallengeTechnologies) AS t
                    WHERE cs.Who = "${who}"
                    AND t.tChallenge = c.cID AND t.Technologie = "${tid}"
                    AND cs.Challenge = c.cID
                    ORDER BY TakenAt DESC;`;

        console.log(`Lista de retos suscritos inactivos solicitada.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback([]);
            } else {
                if (results) {
                    if (results.length > 0) {
                        results.forEach(element => {
                            if (element.Diff > 0) {
                                result.push({
                                    ID: element.cID,
                                    Title: element.Title,
                                    Description: element.Description,
                                    Technologie: element.Technologie
                                });
                            }
                        });

                        callback(result);
                    } else {
                        callback([]);
                    }
                } else {
                    callback([]);
                }
            }
        });
    },

    // List user owned challenges.
    listChallenges: function (owner, callback) {
        var result = [];
        var sql = `SELECT * FROM ChallengeTechnologies ct,
                  (SELECT ID AS cID, Title, Description, Owner, Creation
                    FROM Challenges WHERE Owner = "${owner}") AS c
                  WHERE ct.Challenge = c.cID
                  ORDER BY c.Creation DESC;`;

        console.log(`Lista de retos solicitada.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback([]);
            } else {
                if (results) {
                    if (results.length > 0) {
                        results.forEach(element => {
                            result.push({
                                ID: element.cID,
                                Title: element.Title,
                                Description: element.Description,
                                Technologie: element.Technologie
                            })
                        });

                        callback(result);
                    } else {
                        callback([]);
                    }
                } else {
                    callback([]);
                }
            }
        });
    },

    // List user owned challenges, time limit only active.
    // --> Wrong use of function. DO NOT USE.
    listActiveChallenges: function (owner, callback) {
        var result = [];
        var sql = `SELECT * FROM ChallengeTechnologies ct,
                        (SELECT ID AS cID, Title, Description, Owner, Creation, TimeLimit,
                        (DATEDIFF(NOW(), DATE_ADD(Creation, INTERVAL TimeLimit DAY))) as Diff
                        FROM Challenges WHERE Owner = "${owner}") AS c,
                        (SELECT Challenge AS cLK, (SUM(Positive)) AS Likes, (SUM(Negative)) AS Dislikes
                        FROM ChallengeLikes) AS lk
                        WHERE ct.Challenge = c.cID AND lk.cLK = c.cID
                        ORDER BY c.Creation DESC;`;

        console.log(`Lista de retos solicitada.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback({
                    status: 'error',
                    data: error
                });
            } else {
                if (results) {
                    if (results.length > 0) {
                        results.forEach(element => {
                            if (element.Diff <= 0) {
                                result.push({
                                    ID: element.cID,
                                    Title: element.Title,
                                    Description: element.Description,
                                    Technologie: element.Technologie
                                })
                            }
                        });

                        callback(result);
                    } else {
                        callback({
                            status: 'error',
                            data: 'No challenges found.'
                        });
                    }
                } else {
                    callback({
                        status: 'error',
                        data: 'No challenges found.'
                    });
                }
            }
        });
    },

    // List technology specific challenges.
    listTecChallenges: function (tid, callback) {
        var result = [];
        var sql = `SELECT * FROM ChallengeTechnologies ct,
                        (SELECT ID AS cID, Title, Description, Owner, Creation, TimeLimit
                        FROM Challenges) AS c
                        WHERE ct.Challenge = c.cID
                        AND Technologie = "${tid}"
                        ORDER BY c.Creation DESC
                        LIMIT 100;`;

        console.log(`Lista de retos solicitada.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback([]);
            } else {
                if (results) {
                    if (results.length > 0) {
                        results.forEach(element => {
                            result.push({
                                ID: element.cID,
                                Title: element.Title,
                                Description: element.Description,
                                Technologie: element.Technologie
                            });
                        });

                        callback(result);
                    } else {
                        callback([]);
                    }
                } else {
                    callback([]);
                }
            }
        });
    },

    // full challenge information
    getChallenge: function (ID, callback) {
        var sql = `SELECT * FROM ChallengeAnswers ca,
                        (SELECT ID AS ChallengeID,
                        Title, Description, Creation, Difficulty, TimeLimit,
                        Ref1, Ref2, Ref3, Owner as ChallengeOwner
                        FROM Challenges WHERE ID = "${ID}")
                    AS c WHERE ca.Challenge = c.ChallengeID
                    ORDER BY c.Creation DESC;`;

        console.log(`Información de reto solicitada.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback({
                    status: 'error',
                    data: error
                });
            } else {
                if (results) {
                    if (results.length > 0) {
                        var challengeInfo = {
                            ID: results[0].ChallengeID,
                            Title: results[0].Title,
                            Description: results[0].Description,
                            Creation: results[0].Creation,
                            Difficulty: results[0].Difficulty,
                            TimeLimit: results[0].TimeLimit,
                            Ref1: results[0].Ref1,
                            Ref2: results[0].Ref2,
                            Ref3: results[0].Ref3
                        };

                        // --->> Added challenge owner answer distinction.
                        var answers = [];
                        var oanswer = {};
                        results.forEach(element => {
                            if (element.Owner == element.ChallengeOwner) {
                                oanswer = {
                                    ID: element.ID,
                                    Owner: element.Owner,
                                    Description: element.Answer
                                };
                            } else {
                                answers.push({
                                    ID: element.ID,
                                    Owner: element.Owner,
                                    Description: element.Answer
                                });
                            }
                        });

                        callback({
                            Challenge: challengeInfo,
                            Answers: answers,
                            OwnerAnswer: oanswer
                        });
                    } else {
                        callback({
                            status: 'error',
                            data: 'No challenges found.'
                        });
                    }
                } else {
                    callback({
                        status: 'error',
                        data: 'No challenges found.'
                    });
                }
            }
        });
    },

    // Basic challenge information
    getBasicChallenge: function (ID, callback) {
        var sql = `SELECT *
                    FROM Challenges cs,
                    (SELECT Challenge, (SUM(Positive)) AS Likes, (SUM(Negative)) AS Dislikes
                    FROM ChallengeLikes WHERE Challenge = "${ID}") lk,
                    (SELECT ID as uID, NickName FROM Users) as usr
                    WHERE ID = "${ID}" AND usr.uID = cs.Owner
                    ORDER BY Creation DESC;`;

        console.log(`Información básica de reto solicitada.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback({
                    status: 'error',
                    data: error
                });
            } else {
                if (results) {
                    if (results.length > 0) {
                        var challengeInfo = {
                            ID: results[0].ID,
                            Title: results[0].Title,
                            Description: results[0].Description,
                            Creation: results[0].Creation,
                            Difficulty: results[0].Difficulty,
                            TimeLimit: results[0].TimeLimit,
                            Ref1: results[0].Ref1,
                            Ref2: results[0].Ref2,
                            Ref3: results[0].Ref3,
                            Owner: results[0].Owner,
                            OwnerNickname: results[0].NickName,
                            Likes: results[0].Likes,
                            Dislikes: results[0].Dislikes
                        };

                        callback(challengeInfo);
                    } else {
                        callback({
                            status: 'error',
                            data: 'No challenges found.'
                        });
                    }
                } else {
                    callback({
                        status: 'error',
                        data: 'No challenges found.'
                    });
                }
            }
        });
    },

    // post a challenge
    postChallenge: function (tec, tit, des, dif, til, own, ans, rf1, rf2, rf3, callback) {
        var sql = `INSERT INTO Challenges (Title, Description, Difficulty, TimeLimit, Owner, Ref1, Ref2, Ref3)
                    VALUES ("${tit}", "${des}", "${dif}", "${til}", "${own}", "${rf1}", "${rf2}", "${rf3}");`;

        console.log(`Publicación de reto solicitada.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback({
                    cID: -1,
                    aID: -1
                });
            } else {
                if (results) {
                    if (results.insertId > 0) {
                        var sql2 = `INSERT INTO ChallengeTechnologies (Technologie, Challenge)
                        VALUES ("${tec}", "${results.insertId}");`;
                        var sql3 = `INSERT INTO ChallengeAnswers (Owner, Answer, Challenge)
                        VALUES ("${own}", "${ans}", "${results.insertId}");`;
                        console.log(sql2 + '\n\n');
                        console.log(sql3 + '\n\n');
                        sqlQuery(sql2, function (errort, resultst, fieldst) {
                            if (errort) {
                                console.error(errort);
                                callback({
                                    cID: -1,
                                    aID: -1
                                });
                            } else {
                                if (resultst) {
                                    if (resultst.insertId > 0) {
                                        sqlQuery(sql3, function (errora, resultsa, fieldsa) {
                                            if (errora) {
                                                console.error(errora);
                                                callback({
                                                    cID: -1,
                                                    aID: -1
                                                });
                                            } else {
                                                if (resultsa) {
                                                    if (resultsa.insertId > 0) {
                                                        callback({
                                                            cID: results.insertId,
                                                            aID: resultst.insertId
                                                        });
                                                    } else {
                                                        callback({
                                                            cID: -1,
                                                            aID: -1
                                                        });
                                                    }
                                                } else {
                                                    callback({
                                                        cID: -1,
                                                        aID: -1
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        callback({
                                            cID: -1,
                                            aID: -1
                                        });
                                    }
                                } else {
                                    callback({
                                        cID: -1,
                                        aID: -1
                                    });
                                }
                            }
                        });
                    } else {
                        callback({
                            cID: -1,
                            aID: -1
                        });
                    }
                } else {
                    callback({
                        cID: -1,
                        aID: -1
                    });
                }
            }
        });
    },

    // search for user
    searchUser: function (usr, callback) {
      var sql = `SELECT ID FROM Users WHERE NickName = "${usr}"`;

      console.log(`Búsqueda de usuario.\n${sql}\n`);

      sqlQuery(sql, function (error, results, fields) {
          if (error) {
              console.error(error);
              callback(-1);
          } else {
              if (results) {
                  if (results.length > 0) {
                      callback(results[0].ID);
                  } else {
                      callback(-1);
                  }
              } else {
                  callback(-1);
              }
          }
      });
    },

    // Search challenges
    search: function (tec, query, callback) {
        switch (query) {
            case "news":
                this.listTecChallenges(tec, function (ans) {
                  callback(ans);
                });
                break;
            default:
                var me = this;
                this.searchUser(query, function (usr) {
                  if (usr > -1) {
                    me.listChallenges(usr, function (ans) {
                      callback(ans);
                    });
                  } else {
                    var sql = `SELECT * FROM ChallengeTechnologies ct,
                                    (SELECT ID AS cID, Title, Description, Creation
                                    FROM Challenges) AS c
                                    WHERE ct.Challenge = c.cID
                                    AND c.Title LIKE "%${query}%"
                                    ORDER BY c.Creation DESC
                                    LIMIT 100;`;

                    console.log(`Búsqueda de retos.\n${sql}\n`);

                    sqlQuery(sql, function (error, results, fields) {
                        if (error) {
                            console.error(error);
                            callback([]);
                        } else {
                            if (results) {
                                if (results.length > 0) {
                                    var res = [];
                                    for (var i = 0; i < results.length; i++) {
                                      var challengeInfo = {
                                          ID: results[i].cID,
                                          Title: results[i].Title,
                                          Description: results[i].Description,
                                          Technologie: results[i].Technologie
                                      };

                                      res.push(challengeInfo);
                                    }

                                    callback(res);
                                } else {
                                    callback([]);
                                }
                            } else {
                                callback([]);
                            }
                        }
                    });
                  }
                });
                break;
        }
    },

    // subscribe to challenge
    challengeSubscribe: function (who, challenge, callback) {
        var sql = `INSERT INTO ChallengeSubscribers (Who, Challenge)
                    VALUES ("${who}", "${challenge}");`;

        console.log(`Subscripción de reto solicitada.\n${sql}\n`);

        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback({
                    aID: -1
                });
            } else {
                if (results) {
                    if (results.insertId > 0) {
                        callback({
                            aID: results.insertId
                        });
                    } else {
                        callback({
                            aID: -1
                        });
                    }
                } else {
                    callback({
                        aID: -1
                    });
                }
            }
        });
    },

    // Getting answers
    readAnswers: function (challenge, callback) {
      var sql = `SELECT * FROM ChallengeAnswers ca,
                (SELECT ID AS uID, NickName FROM Users) AS usr
                 WHERE Challenge = "${challenge}"
                 AND usr.uID = ca.Owner;`;

      console.log(`Obtener respuestas de reto solicitada.\n${sql}\n`);

      sqlQuery(sql, function (error, results, fields) {
        if (error) {
            console.error(error);
            callback([]);
        } else {
            if (results) {
                if (results.length > 0) {
                    var res = [];
                    for (var i = 0; i < results.length; i++) {
                      var challengeInfo = {
                          Owner: results[i].NickName,
                          Answer: results[i].Answer
                      };

                      res.push(challengeInfo);
                    }

                    callback(res);
                } else {
                    callback([]);
                }
            } else {
                callback([]);
            }
        }
      });
    },
    
    // Liking challenges
    
    // Answer a challenge
    doAnswer: function (who, answer, challenge, callback) {
      var sql = `INSERT INTO ChallengeAnswers (Owner, Challenge, Answer)
                  VALUES ("${who}", "${challenge}", "${answer}");`;

      console.log(`Responder a reto solicitada.\n${sql}\n`);

      sqlQuery(sql, function (error, results, fields) {
          if (error) {
              console.error(error);
              callback({
                  aID: -1
              });
          } else {
              if (results) {
                  if (results.insertId > 0) {
                      callback({
                          aID: results.insertId
                      });
                  } else {
                      callback({
                          aID: -1
                      });
                  }
              } else {
                  callback({
                      aID: -1
                  });
              }
          }
      });
    }
    
    // User profile modification
    // --> accepted changes: email, name, password
}
