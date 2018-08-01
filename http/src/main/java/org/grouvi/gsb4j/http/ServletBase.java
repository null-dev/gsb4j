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

package org.grouvi.gsb4j.http;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.grouvi.gsb4j.api.SafeBrowsingApi;
import org.grouvi.gsb4j.data.ThreatMatch;
import org.slf4j.Logger;

import org.apache.http.entity.ContentType;

import com.google.gson.Gson;
import com.google.inject.Inject;


abstract class ServletBase extends HttpServlet
{
    public static final String URL_PARAM = "url";

    @Inject
    Gson gson;


    abstract Logger getLogger();


    abstract SafeBrowsingApi getSafeBrowsingApi();


    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
    {
        String url = req.getParameter( LookupApiServlet.URL_PARAM );
        if ( url == null || url.trim().isEmpty() )
        {
            writeResponse( HttpServletResponse.SC_BAD_REQUEST, "No URL to check", resp );
            return;
        }
        getLogger().info( "Checking safe browsing: {}", url );

        SafeBrowsingApi api = getSafeBrowsingApi();
        ThreatMatch threat = api.check( url );

        ResponsePayload payload = threat != null ? new ResponsePayload( threat ) : new ResponsePayload();
        String json = gson.toJson( payload );
        writeResponseJson( HttpServletResponse.SC_OK, json, resp );
    }


    void writeResponse( int status, String data, HttpServletResponse resp ) throws IOException
    {
        try ( PrintWriter writer = resp.getWriter() )
        {
            writer.print( data );
        }
        resp.setStatus( status );
    }


    void writeResponseJson( int status, String data, HttpServletResponse resp ) throws IOException
    {
        resp.setContentType( ContentType.APPLICATION_JSON.getMimeType() );
        writeResponse( status, data, resp );
    }


    static class ResponsePayload
    {
        List<ThreatMatch> matches;


        public ResponsePayload()
        {
            matches = Collections.emptyList();
        }


        public ResponsePayload( ThreatMatch threat )
        {
            // copy only useful fields for users; DO NOT send whole object
            ThreatMatch t = new ThreatMatch();
            t.setThreatType( threat.getThreatType() );
            t.setPlatformType( threat.getPlatformType() );

            matches = Collections.singletonList( t );
        }
    }


}

