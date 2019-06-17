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

    // List user challenges, no time limit.
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

    // List user challenges, time limit only active.
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
                        Ref1, Ref2, Ref3
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
                        var answers = [];
                        results.forEach(element => {
                            answers.push({
                                ID: element.ID,
                                Owner: element.Owner,
                                Description: element.Answer
                            })
                        });

                        callback({
                            Challenge: challengeInfo,
                            Answers: answers
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
    }
}
