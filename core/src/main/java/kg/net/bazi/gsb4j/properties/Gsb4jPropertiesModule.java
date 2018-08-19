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

package kg.net.bazi.gsb4j.properties;


import java.util.Properties;

import com.google.inject.AbstractModule;

import kg.net.bazi.gsb4j.Gsb4j;
import kg.net.bazi.gsb4j.Gsb4jBinding;


/**
 * Guice module to initialize bindings related to gsb4j properties.
 * <p>
 * This module is not supposed to be used directly! To bootstrap, consider bootstrap methods in {@link Gsb4j} or methods
 * that return list of all necessary modules.
 *
 * @author <a href="https://github.com/bazi">bazi</a>
 */
public class Gsb4jPropertiesModule extends AbstractModule
{

    private Properties properties;


    /**
     * Sets properties instance to use as a source of values.
     *
     * @param properties properties instance
     * @return this class for chained setup
     */
    public Gsb4jPropertiesModule setPropertiesFile( Properties properties )
    {
        this.properties = properties;
        return this;
    }


    @Override
    protected void configure()
    {
        if ( properties != null )
        {
            bind( Properties.class ).annotatedWith( Gsb4jBinding.class ).toInstance( properties );
            bind( Gsb4jProperties.class ).to( Gsb4jFileProperties.class );
        }
        else
        {
            bind( Gsb4jProperties.class ).to( Gsb4jSystemProperties.class );
        }
    }


}

