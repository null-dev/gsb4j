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

package org.grouvi.gsb4j.api;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;


/**
 * Guice module to initialize API specific bindings.
 *
 * @author azilet
 */
public class SafeBrowsingApiModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind( ThreatListUpdateRunner.class ).asEagerSingleton();

        bind( SafeBrowsingApi.class ).annotatedWith( Names.named( SafeBrowsingApi.LOOKUP_API ) ).to( LookupApi.class );
        bind( SafeBrowsingApi.class ).annotatedWith( Names.named( SafeBrowsingApi.UPDATE_API ) ).to( UpdateApi.class );
        bind( SafeBrowsingApi.class ).to( UpdateApi.class );
    }
}

