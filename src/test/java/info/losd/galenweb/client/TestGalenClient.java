package info.losd.galenweb.client;


import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import info.losd.galenweb.GalenWeb;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {GalenWeb.class})
@TestPropertySource("/test.properties")
public class TestGalenClient {
    @Rule
    @SuppressFBWarnings(value = {"URF_UNREAD_FIELD", "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"}, justification = "Wiremock uses it")
    public WireMockRule wireMockRule = new WireMockRule();

    @Autowired
    Client client;

    @Test
    public void test_it_can_get_a_list_of_healthchecks() {
        stubFor(get(urlEqualTo("/tasks"))
                .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("healthcheck_list.json")
                ));

        List<GalenHealthCheck> result = client.getHealthChecks();
        assertThat(result, IsCollectionWithSize.hasSize(3));

        checkHealthCheck(result.get(0), "GET", "http://example.com/1", "healthcheck1");
        checkHealthCheck(result.get(1), "GET", "http://example.com/2", "healthcheck2");
        checkHealthCheck(result.get(2), "GET", "http://example.com/3", "healthcheck3");
    }

    private void checkHealthCheck(GalenHealthCheck check, String expectedMethod, String expectedUrl, String expectedName) {
        assertThat(check.getMethod(), is(equalTo(expectedMethod)));
        assertThat(check.getUrl(), is(equalTo(expectedUrl)));
        assertThat(check.getName(), is(equalTo(expectedName)));
    }
}
