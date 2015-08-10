package info.losd.galenweb.web;

import info.losd.galenweb.client.GalenHealthCheck;

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
public class Healthcheck {
    private String name;
    private String method;
    private String url;
    private long okCount;
    private long clientErrorCount;
    private long serverErrorCount;

    Healthcheck(String name, String method, String url) {
        this.name = name;
        this.method = method;
        this.url = url;
    }

    Healthcheck(GalenHealthCheck galen, long okCount, long clientErrorCount, long serverErrorCount) {
        this(galen.getName(), galen.getMethod(), galen.getUrl());
        this.okCount = okCount;
        this.clientErrorCount = clientErrorCount;
        this.serverErrorCount = serverErrorCount;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }


    public String getMethod() {
        return method;
    }

    public long getOkCount() {
        return okCount;
    }

    public long getClientErrorCount() {
        return clientErrorCount;
    }

    public long getServerErrorCount() {
        return serverErrorCount;
    }
}
