package info.losd.galenweb.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import net.minidev.json.JSONArray;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
@Component
public class GalenClient implements Client {
    private Logger LOG = LoggerFactory.getLogger(GalenClient.class);
    private Configuration conf = Configuration.defaultConfiguration();
    ;

    {
        conf = conf.addOptions(Option.SUPPRESS_EXCEPTIONS);
    }

    @Value("${galen.url}")
    private String url;

    @Override
    public List<GalenHealthCheck> getHealthChecks() {
        try {
            ResponseHandler<List<GalenHealthCheck>> handler = response -> {
                StatusLine status = response.getStatusLine();

                if (status.getStatusCode() == 200) {
                    Optional<JSONArray> tasks = Optional.ofNullable(
                            JsonPath.using(conf).parse(response.getEntity().getContent()).read("$._embedded.tasks"));

                    if (tasks.isPresent()) {
                        return new ObjectMapper()
                                .readValue(tasks.get().toJSONString(), new TypeReference<List<GalenHealthCheck>>() {
                                });
                    }
                }

                LOG.error("Problem getting healthchecks - status code: {}, reason: {}", status.getStatusCode(),
                          status.getReasonPhrase());
                return Collections.emptyList();
            };

            return Request.Get(String.format("%s/tasks", url))
                          .execute()
                          .handleResponse(handler);
        } catch (IOException e) {
            LOG.error("Problem getting healthchecks", e);
            return Collections.emptyList();
        }
    }

    @Override
    public GalenHealthCheckStatusCodes getStatusCodeCounts(String healthcheck) {
        try {
            ResponseHandler<GalenHealthCheckStatusCodes> handler = response -> {
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() == 200) {
                    return new ObjectMapper()
                            .readValue(response.getEntity().getContent(),
                                       GalenHealthCheckStatusCodes.class);
                }

                LOG.error("Problem getting status codes - status code: {}, reason: {}",
                          status.getStatusCode(),
                          status.getReasonPhrase());
                return new GalenHealthCheckStatusCodes(healthcheck, Collections.emptyList());
            };

            return
                    Request.Get(String.format("%s/healthchecks/%s/statistics/status_codes", url, healthcheck))
                           .execute()
                           .handleResponse(handler);
        } catch (IOException e) {
            LOG.error("Problem getting healthcheck status codes", e);
            return new GalenHealthCheckStatusCodes(healthcheck, Collections.emptyList());
        }
    }
}
