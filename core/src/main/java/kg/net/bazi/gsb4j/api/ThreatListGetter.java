/*
 * Copyright 2018 Azilet B.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kg.net.bazi.gsb4j.api;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kg.net.bazi.gsb4j.data.ThreatListDescriptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Retrieves names of the Safe Browsing threat lists.
 *
 * @author azilet
 */
public class ThreatListGetter extends SafeBrowsingApiBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreatListGetter.class);

    /**
     * Retrieves threat lists currently available in Safe Browsing API.
     *
     * @return list of threat list descriptors
     */
    public List<ThreatListDescriptor> getLists() {
        List<ThreatListDescriptor> threatLists;
        HttpUriRequest req = makeRequest(HttpGet.METHOD_NAME, "threatLists", null);
        try (CloseableHttpResponse resp = httpClient.execute(req);
            Reader reader = getResponseReader(resp)) {
            ThreatListsResponse apiResp = gson.fromJson(reader, ThreatListsResponse.class);
            threatLists = apiResp.threatLists;
        } catch (IOException ex) {
            LOGGER.error("Failed to get threat lists", ex);
            return Collections.emptyList();
        }
        Iterator<ThreatListDescriptor> it = threatLists.iterator();
        while (it.hasNext()) {
            ThreatListDescriptor desc = it.next();
            if (desc.getThreatType() == null || desc.getPlatformType() == null || desc.getThreatEntryType() == null) {
                it.remove();
            }
        }

        StringBuilder sb = new StringBuilder();
        threatLists.forEach(d -> sb.append(System.lineSeparator()).append(d));
        LOGGER.info("Fetched {} threat list descriptors:{}", threatLists.size(), sb);

        return threatLists;
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

    private static class ThreatListsResponse {

        private List<ThreatListDescriptor> threatLists;

    }

}
