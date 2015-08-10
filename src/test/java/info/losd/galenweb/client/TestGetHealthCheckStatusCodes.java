package info.losd.galenweb.client;

import info.losd.galenweb.GalenWeb;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class TestGetHealthCheckStatusCodes extends GalenClientTest {
    @Test
    public void test_it_can_get_a_count_of_the_different_https_status_type() {
        stubFor(get(urlEqualTo("/healthchecks/healthcheck1/statistics/status_codes?period=60m")).willReturn(
                aResponse().withHeader("Content-Type", "application/json").withBodyFile("status_codes.json")));

        GalenHealthCheckStatusCodes result = client.getStatusCodeCounts("healthcheck1", "60m");
        assertThat(result.getStatusCodes(), IsCollectionWithSize.hasSize(3));
        assertThat(result.getHeatlhcheck(), is(equalTo("healthcheck1")));
        checkStatusCode(result.getStatusCodes().get(0), 200, 10);
        checkStatusCode(result.getStatusCodes().get(1), 400, 1);
        checkStatusCode(result.getStatusCodes().get(2), 500, 2);
    }

    @Test
    public void test_it_can_handle_a_404_error(){
        stubFor(get(urlEqualTo("/healthchecks/healthcheck1/statistics/status_codes?period=60m"))
                        .willReturn(aResponse().withStatus(404)));

        GalenHealthCheckStatusCodes result = client.getStatusCodeCounts("healthcheck1", "60m");
        assertThat(result.getStatusCodes(), IsCollectionWithSize.hasSize(0));
        assertThat(result.getHeatlhcheck(), is(equalTo("healthcheck1")));
    }

    @Test
    public void test_it_can_handle_a_500_error(){
        stubFor(get(urlEqualTo("/healthchecks/healthcheck1/statistics/status_codes?period=60m")).willReturn(aResponse().withStatus(500)));

        GalenHealthCheckStatusCodes result = client.getStatusCodeCounts("healthcheck1", "60m");
        assertThat(result.getStatusCodes(), IsCollectionWithSize.hasSize(0));
        assertThat(result.getHeatlhcheck(), is(equalTo("healthcheck1")));
    }

    private void checkStatusCode(GalenStatusCodeCount sc, int expectedStatusCode, int expectedCount) {
        assertThat("status_code count", sc.getCount(), is(equalTo(expectedCount)));
        assertThat("status_code", sc.getStatusCode(), is(equalTo(expectedStatusCode)));
    }
}
