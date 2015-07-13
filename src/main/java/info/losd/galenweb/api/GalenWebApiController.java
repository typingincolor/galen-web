package info.losd.galenweb.api;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 Andrew Braithwaite
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
@RestController
public class GalenWebApiController {
    InfluxDB influxDB = InfluxDBFactory.connect("http://docker.local:8086", "root", "root");

    @RequestMapping(value = "/apis", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<List<Api>> getApiList() {
        Query query = new Query("SHOW TAG VALUES FROM statistic WITH KEY = api", "galen");
        QueryResult apiList = influxDB.query(query);

        List<Api> apis = new LinkedList<>();

        apiList.getResults().get(0).getSeries().get(0).getValues().forEach(value -> {
            String name = value.get(0).toString();

            Api api = new Api(name);
            api.add(linkTo(methodOn(GalenWebApiController.class).api(name)).withSelfRel());
            api.add(linkTo(methodOn(GalenWebApiController.class).statistics(name, "10m")).withRel("statistics"));
            api.add(linkTo(methodOn(GalenWebApiController.class).mean(name, "10m")).withRel("mean"));
            apis.add(api);
        });

        return new ResponseEntity<>(apis, HttpStatus.OK);
    }

    @RequestMapping(value = "/apis/{api}", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<String> api(@PathVariable String api) {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @RequestMapping(value = "/apis/{api}/statistics", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<String> statistics(@PathVariable String api,
                                         @RequestParam(value = "period",
                                                       required = false,
                                                       defaultValue = "2m")
                                         String period)
    {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @RequestMapping(value = "/apis/{api}/statistics/mean", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<String> mean(@PathVariable String api,
                                   @RequestParam(value = "period",
                                                 required = false,
                                                 defaultValue = "2m")
                                   String period)
    {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
}
