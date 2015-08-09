package info.losd.galenweb.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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
//@JsonIgnoreProperties(ignoreUnknown = true)
public class GalenHealthCheckStatusCodes {
    @JsonProperty("healthcheck")
    private String heatlhcheck;

    @JsonProperty("status_codes")
    private List<GalenStatusCodeCount> statusCodes;

    @JsonCreator
    public GalenHealthCheckStatusCodes(@JsonProperty("healthcheck") String heatlhcheck,
                                       @JsonProperty("status_codes") List<GalenStatusCodeCount> statusCodes) {
        this.heatlhcheck = heatlhcheck;
        this.statusCodes = statusCodes;
    }

    public String getHeatlhcheck() {
        return heatlhcheck;
    }

    public List<GalenStatusCodeCount> getStatusCodes() {
        return statusCodes;
    }
}
