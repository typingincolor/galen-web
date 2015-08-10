package info.losd.galenweb.web;

import info.losd.galenweb.client.Client;
import info.losd.galenweb.client.GalenHealthCheck;
import info.losd.galenweb.client.GalenHealthCheckStatusCodes;
import info.losd.galenweb.client.GalenStatusCodeCount;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
public class TestWebControllerIndex {
    private MockMvc mockMvc;

    @Mock
    private Client client;

    @InjectMocks
    private GalenWebController webController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(webController).build();
    }

    @Test
    public void it_renders_the_index_page() throws Exception {
        List<GalenHealthCheck> healthchecks = new LinkedList<>();

        GalenHealthCheck healthCheck1 = new GalenHealthCheck("healthcheck1", "GET", "http://example.com/hc1");
        GalenHealthCheck healthCheck2 = new GalenHealthCheck("healthcheck2", "POST", "http://example.com/hc2");

        healthchecks.add(healthCheck1);
        healthchecks.add(healthCheck2);

        List<GalenStatusCodeCount> codes1 = new LinkedList<>();
        codes1.add(new GalenStatusCodeCount(200, 10));
        codes1.add(new GalenStatusCodeCount(202, 1));
        codes1.add(new GalenStatusCodeCount(400, 1));
        codes1.add(new GalenStatusCodeCount(404, 2));
        codes1.add(new GalenStatusCodeCount(500, 6));
        codes1.add(new GalenStatusCodeCount(503, 6));

        List<GalenStatusCodeCount> codes2 = new LinkedList<>();
        codes2.add(new GalenStatusCodeCount(200, 1));
        codes2.add(new GalenStatusCodeCount(202, 1));
        codes2.add(new GalenStatusCodeCount(400, 10));
        codes2.add(new GalenStatusCodeCount(404, 2));
        codes2.add(new GalenStatusCodeCount(500, 6));
        codes2.add(new GalenStatusCodeCount(503, 9));

        when(client.getHealthChecks()).thenReturn(healthchecks);
        when(client.getStatusCodeCounts("healthcheck1"))
                .thenReturn(new GalenHealthCheckStatusCodes("healthcheck1", codes1));
        when(client.getStatusCodeCounts("healthcheck2")).thenReturn(
                new GalenHealthCheckStatusCodes("healthcheck2", codes2));


        MvcResult mvcResult = mockMvc.perform(get("/")).andExpect(status().is2xxSuccessful())
                                     .andExpect(view().name(is("index")))
                                     .andReturn();

        List<Healthcheck> result = (List<Healthcheck>) mvcResult.getModelAndView().getModel().get("healthchecks");

        checkHealthCheck(result.get(0), "GET", "healthcheck1", "http://example.com/hc1", 11, 3, 12);
        checkHealthCheck(result.get(1), "POST", "healthcheck2", "http://example.com/hc2", 2, 12, 15);

        verify(client, times(1)).getHealthChecks();
        verify(client, times(1)).getStatusCodeCounts("healthcheck1");
        verify(client, times(1)).getStatusCodeCounts("healthcheck2");
    }

    private void checkHealthCheck(Healthcheck check, String expectedMethod, String expectedName, String expectedUrl,
                                  long expectedOkCount, long expectedClientErrorCount, long expectedServerErrorCount) {
        assertThat(check.getMethod(), is(equalTo(expectedMethod)));
        assertThat(check.getName(), is(equalTo(expectedName)));
        assertThat(check.getUrl(), is(equalTo(expectedUrl)));
        assertThat(check.getOkCount(), is(equalTo(expectedOkCount)));
        assertThat(check.getClientErrorCount(), is(equalTo(expectedClientErrorCount)));
        assertThat(check.getServerErrorCount(), is(equalTo(expectedServerErrorCount)));
    }
}
