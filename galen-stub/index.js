var express = require('express');
var app = express();

app.get('/healthchecks/:healthcheck/statistics/status_codes', function(req, res) {
  var healthcheck = req.params.healthcheck;

  console.log("GET /healthchecks/" + healthcheck + "/statistics/status_codes");

  res.status(200).json({
    "healthcheck": healthcheck,
    "status_codes": [{
      "status_code": 200,
      "count": 10
    }, {
      "status_code": 400,
      "count": 1
    }, {
      "status_code": 500,
      "count": 2
    }]
  });
});

app.get('/tasks', function(req, res) {
  console.log("GET /tasks");
  res.status(200).json({
    "_links": {
      "search": {
        "href": "http://localhost:8080/tasks/search"
      }
    },
    "_embedded": {
      "tasks": [{
        "lastUpdated": "2015-07-30T08:16:06.930Z",
        "period": 10,
        "name": "healthcheck1",
        "url": "http://example.com/1",
        "method": "GET",
        "headers": [{
          "header": "X-REQBOT-RESPONSE",
          "value": "e1791911-319d-48b2-acb3-d2c51a1d4106"
        }, {
          "header": "h1",
          "value": "v1"
        }],
        "_links": {
          "self": {
            "href": "http://localhost:8080/tasks/1"
          }
        }
      }, {
        "lastUpdated": "2015-07-30T08:16:06.930Z",
        "period": 10,
        "name": "healthcheck2",
        "url": "http://example.com/2",
        "method": "POST",
        "headers": [{
          "header": "X-REQBOT-RESPONSE",
          "value": "e1791911-319d-48b2-acb3-d2c51a1d4106"
        }, {
          "header": "h1",
          "value": "v1"
        }],
        "_links": {
          "self": {
            "href": "http://localhost:8080/tasks/2"
          }
        }
      }, {
        "lastUpdated": "2015-07-30T08:16:06.930Z",
        "period": 10,
        "name": "healthcheck3",
        "url": "http://example.com/3",
        "method": "GET",
        "headers": [{
          "header": "X-REQBOT-RESPONSE",
          "value": "e1791911-319d-48b2-acb3-d2c51a1d4106"
        }, {
          "header": "h1",
          "value": "v1"
        }],
        "_links": {
          "self": {
            "href": "http://localhost:8080/tasks/2"
          }
        }
      }]
    }
  });
});

var server = app.listen(8080, function() {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);
});
