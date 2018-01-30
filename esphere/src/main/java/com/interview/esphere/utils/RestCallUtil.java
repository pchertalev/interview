package com.interview.esphere.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

@Component
public class RestCallUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestCallUtil.class);
    private static final int MAX_REDIRECT_COUNT = 3;

    public String restCall(String requestUrl) throws RestCallException {

        int redirectCount = 0;

        try {

            HttpURLConnection conn;
            HttpStatus httpStatus;

            for (;;) {
                LOGGER.info("Requesting {}", requestUrl);

                URL resourceUrl = new URL(requestUrl);
                conn = (HttpURLConnection) resourceUrl.openConnection();

                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.setInstanceFollowRedirects(false);   // Make the logic below easier to detect redirections
                conn.setRequestProperty("User-Agent", "Mozilla/5.0...");

                httpStatus = HttpStatus.valueOf(conn.getResponseCode());
                LOGGER.info("Returned response: '{}'", httpStatus.getReasonPhrase());

                if (httpStatus.is3xxRedirection()) {

                    String location = conn.getHeaderField("Location");
                    location = URLDecoder.decode(location, "UTF-8");
                    URL base = new URL(requestUrl);
                    URL next = new URL(base, location);  // Deal with relative URLs
                    requestUrl = next.toExternalForm();
                    LOGGER.info("Redirecting to {}...", requestUrl);

                    if (redirectCount ++  >= MAX_REDIRECT_COUNT) {
                        throw new RestCallException("redirect limit reached");
                    }
                    continue;
                }
                break;
            }

            if (httpStatus.is2xxSuccessful()) {
                return IOUtils.toString(conn.getInputStream());
            } else if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
                throw new RestCallException(IOUtils.toString(conn.getErrorStream()));
            } else {
                throw new RestCallException("weather server returned not supported response");
            }

        } catch (IOException e) {
            throw new RestCallException(e.getMessage());
        }

    }
}
