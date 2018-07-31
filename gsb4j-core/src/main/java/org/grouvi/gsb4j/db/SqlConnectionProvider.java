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

package org.grouvi.gsb4j.db;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.grouvi.gsb4j.Gsb4jConst;
import org.grouvi.gsb4j.properties.Gsb4jProperties;
import org.sqlite.JDBC;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


/**
 * Data source provider for SQL local database.
 *
 * @author azilet
 */
class SqlConnectionProvider implements Provider<DataSource>
{
    private DataSource dataSource;


    @Inject
    SqlConnectionProvider( Gsb4jProperties properties )
    {
        Path dataDir = properties.getDataDirectory();
        if ( !Files.exists( dataDir ) )
        {
            try
            {
                Files.createDirectories( dataDir );
            }
            catch ( IOException ex )
            {
                throw new ProvisionException( "Failed to create data directory", ex );
            }
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl( JDBC.PREFIX + dataDir.resolve( "local.db" ) );
        config.setPoolName( Gsb4jConst.GSB4J );
        config.setAutoCommit( false );
        config.setConnectionTimeout( TimeUnit.SECONDS.toMillis( 20 ) );
        config.setIdleTimeout( TimeUnit.MINUTES.toMillis( 10 ) );
        config.setMaxLifetime( TimeUnit.MINUTES.toMillis( 30 ) );
        config.setMinimumIdle( 4 );
        config.setMaximumPoolSize( 10 );

        this.dataSource = new HikariDataSource( config );
    }


    @Override
    public DataSource get()
    {
        return dataSource;
    }
}

