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

package kg.net.bazi.gsb4j.http;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import kg.net.bazi.gsb4j.api.SafeBrowsingApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet for Safe Browsing Lookup API.
 *
 * @author azilet
 */
class LookupApiServlet extends ServletBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupApiServlet.class);

    final Provider<SafeBrowsingApi> lookupApiProvider;

    @Inject
    public LookupApiServlet(Gson gson, @Named(SafeBrowsingApi.Type.LOOKUP_API) Provider<SafeBrowsingApi> lookupApiProvider) {
        super(gson);
        this.lookupApiProvider = lookupApiProvider;
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

    @Override
    SafeBrowsingApi getSafeBrowsingApi() {
        return lookupApiProvider.get();
    }

}
