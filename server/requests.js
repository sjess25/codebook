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
        var sql = `SELECT Who, Challenge, TakenAt,
                    (DATEDIFF(NOW(), DATE_ADD(TakenAt, INTERVAL c.TimeLimit DAY))) as Diff
                    FROM ChallengeSubscribers cs,
                    (SELECT ID AS cID, Title, Description, Owner, Creation, TimeLimit
                     FROM Challenges) AS c
                    WHERE Who = "${who}"
                    ORDER BY TakenAt DESC;`;
        
        console.log(`Lista de retos suscritos activos solicitada.\n${sql}\n`);
        
        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback([{
                    status: 'error',
                    data: error
                }]);
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
                                });
                            }
                        });

                        callback(result);
                    } else {
                        callback([{
                            status: 'error',
                            data: 'No challenges found.'
                        }]);
                    }
                } else {
                    callback([{
                        status: 'error',
                        data: 'No challenges found.'
                    }]);
                }
            }
        });
    },
    
    // List user subscribed inactive challenges per technology.
    inactiveSubscribed: function (who, tid, callback) {
        var result = [];
        var sql = `SELECT Who, Challenge, TakenAt,
                    (DATEDIFF(NOW(), DATE_ADD(TakenAt, INTERVAL c.TimeLimit DAY))) as Diff
                    FROM ChallengeSubscribers cs,
                    (SELECT ID AS cID, Title, Description, Owner, Creation, TimeLimit
                     FROM Challenges) AS c,
                    (SELECT ID AS tID, Technologie, Challenge AS tChallenge
                     FROM ChallengeTechnologies) AS t
                    WHERE Who = "${who}"
                    AND tChallenge = c.cID AND tID = "${tid}"
                    ORDER BY TakenAt DESC;`;
        
        console.log(`Lista de retos suscritos inactivos solicitada.\n${sql}\n`);
        
        sqlQuery(sql, function (error, results, fields) {
            if (error) {
                console.error(error);
                callback([{
                    status: 'error',
                    data: error
                }]);
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
                        callback([{
                            status: 'error',
                            data: 'No challenges found.'
                        }]);
                    }
                } else {
                    callback([{
                        status: 'error',
                        data: 'No challenges found.'
                    }]);
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
                callback({
                    status: 'error',
                    data: error
                });
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

    // List user owned challenges, time limit only active.
    // --> Wrong use of function. DO NOT USE.
    listActiveChallenges: function (owner, callback) {
        var result = [];
        var sql = `SELECT * FROM ChallengeTechnologies ct,
                        (SELECT ID AS cID, Title, Description, Owner, Creation, TimeLimit,
                        (DATEDIFF(NOW(), DATE_ADD(Creation, INTERVAL TimeLimit DAY))) as Diff
                        FROM Challenges WHERE Owner = "${owner}") AS c
                        WHERE ct.Challenge = c.cID
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
    // --> Wrong use of function. DO NOT USE.
    listTecChallenges: function (tid, callback) {
        var result = [];
        var sql = `SELECT * FROM ChallengeTechnologies ct,
                        (SELECT ID AS cID, Title, Description, Owner, Creation, TimeLimit,
                        (DATEDIFF(NOW(), DATE_ADD(Creation, INTERVAL TimeLimit DAY))) as Diff
                        FROM Challenges) AS c
                        WHERE ct.Challenge = c.cID,
                        AND Tecnologie = "${tid}"
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
                            if (element.Diff > 0) {
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
                        }
                        
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
                            OwnerAnswer: 
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
        var sql = `SELECT ID AS ChallengeID,
                        Title, Description, Creation, Difficulty, TimeLimit,
                        Ref1, Ref2, Ref3
                    FROM Challenges WHERE ID = "${ID}"
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
    }
}
