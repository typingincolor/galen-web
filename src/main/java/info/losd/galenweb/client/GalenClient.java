package info.losd.galenweb.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import net.minidev.json.JSONArray;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
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
@Component
public class GalenClient implements Client {
    private Logger LOG = LoggerFactory.getLogger(GalenClient.class);

    @Value("${galen.url}")
    private String url;

    @Override
    public List<GalenHealthCheck> getHealthChecks() {
        try {
            HttpResponse response = Request.Get(String.format("%s/tasks", url))
                    .execute()
                    .returnResponse();


            ReadContext ctx = JsonPath.parse(response.getEntity().getContent());

            JSONArray tasks = ctx.read("$._embedded.tasks");

            return new ObjectMapper().readValue(tasks.toJSONString(), new TypeReference<List<GalenHealthCheck>>() {
            });
        } catch (IOException e) {
            LOG.error("Problem getting health check list", e);
        } catch (PathNotFoundException e) {
            LOG.debug("There are no healthchecks");
        }

        return Collections.emptyList();
    }
}
