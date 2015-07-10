$(function() {
  console.log("ready!");

  // select response_time,status_code from statistic where time > now() - 2m
  $.get("http://192.168.59.103:8086/query?db=galen&q=select%20response_time%2Cstatus_code%20from%20statistic%20where%20time%20%3E%20now()%20-%202m", function(data) {
    console.dir(data.results[0].series[0].values);

    var x = ['time'];
    var y = ['duration'];
    var statuscode = {};

    _.each(data.results[0].series[0].values, function(value) {
      x.push(new Date(value[0]));
      y.push(value[1]);
      statuscode[value[2]] = 1 + (statuscode[value[2]] || 0);
    });

    console.log(statuscode);

    piechart_data = [];

    _.each(_.keys(statuscode), function(key) {
      piechart_data.push([key, statuscode[key]]);
    });

    console.log(piechart_data);

    var response_time = c3.generate({
      bindto: '#response_time',
      data: {
        x: 'time',
        //x_format: '%Y-%m-%dT%H:%M:%S.%L%Z',
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
});
