package info.losd.galenweb.api;

import info.losd.galenweb.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    Client client;

    @RequestMapping(value = "/healthchecks/{healthcheck}", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<String> healthCheck(@PathVariable String healthcheck) {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @RequestMapping(value = "/healthchecks/{healthchecks}/statistics", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<String> statistics(@PathVariable String api,
                                         @RequestParam(value = "period",
                                                 required = false,
                                                 defaultValue = "2m")
                                         String period) {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @RequestMapping(value = "/healthchecks/{healthcheck}/statistics/mean", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<String> mean(@PathVariable String api,
                                   @RequestParam(value = "period",
                                           required = false,
                                           defaultValue = "2m")
                                   String period) {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
}
