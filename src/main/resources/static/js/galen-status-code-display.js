$(function() {
  _.each($("span[id^=healthcheck-data-]"), function(healthcheck) {
    console.log(healthcheck);

    var chart = c3.generate({
      bindto: healthcheck,
      size: {
        height: 20
      },
      padding: {
        top: 0,
        bottom: 0
      },
      data: {
        columns: [
          ['2xx', $(healthcheck).attr("data-2xx")],
          ['4xx', $(healthcheck).attr("data-4xx")],
          ['5xx', $(healthcheck).attr("data-5xx")]
        ],
        type: 'bar',
        colors: {
          '2xx': '#ff0000',
          '4xx': '#00ff00',
          '5xx': '#0000ff'
        },
        groups: [
          ['2xx', '4xx', '5xx']
        ],
        order: null
      },
      bar: {
        width: 10
      },
      axis: {
        rotated: true,
        x: {
          show: false
        },
        y: {
          show: false
        },
        legend: {
          show: false
        }
      },
      legend: {
        show: false
      }
    });
  });
});
