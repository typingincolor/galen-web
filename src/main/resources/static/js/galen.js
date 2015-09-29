$(function() {
  var healthcheck = $('#status').attr("data-healthcheck");

  var graphs = function() {
    // select response_time,status_code from statistic where time > now() - 2m
    $.get("/healthchecks/" + healthcheck +  "/statistics?period=2m", function(data) {

      var x = ['time'];
      var y = ['duration'];
      var statuscode = {};

      _.each(data, function(value) {
        x.push(new Date(value.timestamp));
        y.push(value.response_time);
        statuscode[value.status_code] = 1 + (statuscode[value.status_code] || 0);
      });

      piechart_data = [];

      _.each(_.keys(statuscode), function(key) {
        piechart_data.push([key, statuscode[key]]);
      });

      var response_time = c3.generate({
        bindto: '#response_time',
        data: {
          x: 'time',
          columns: [
            x,
            y
          ]
        },
        axis: {
          x: {
            type: 'timeseries',
            tick: {
              format: '%H:%M:%S.%L'
            }
          }
        }
      });

      var status_code = c3.generate({
        bindto: '#status_code',
        data: {
          columns: piechart_data,
          type: 'donut'
        }
      });
    });
  };

  var mean_response_time = function() {
    // select mean(response_time) from statistic where time > now() - 2m
    $.get("/healthchecks/" + healthcheck + "/statistics/mean?period=2m", function(data) {
      $('#mean_response_time').text(Math.round((data.mean * 100)) / 100);
    });
  };

  graphs();
  mean_response_time();

  setInterval(function() {
    graphs();
  }, 1000 * 10);

  setInterval(function() {
    mean_response_time();
  }, 1000 * 10);
});
