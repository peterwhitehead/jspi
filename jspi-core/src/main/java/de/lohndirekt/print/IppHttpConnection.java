/**
 * Copyright (C) 2003 <a href="http://www.lohndirekt.de/">lohndirekt.de</a>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *  
 */
package de.lohndirekt.print;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import de.lohndirekt.print.exception.AuthenticationException;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * simple facade / abstraction layer to "commons httpclient"
 * 
 * @author sefftinge
 *  
 */
class IppHttpConnection implements IppConnection {
    private final Credentials credentials;
    private int statusCode;

    private final HttpPost method;

    /**
     * @param uri
     * @param user
     * @param passwd
     */
    public IppHttpConnection(final URI uri, final String user, final String passwd) {
        this.credentials = new UsernamePasswordCredentials(user, passwd);
        method = new HttpPost(toHttpURI(uri).toString());
        method.addHeader("Content-type", "application/ipp");
        method.addHeader("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
    }

    /**
     * @return content of the response
     * @throws IOException
     */
    public InputStream getIppResponse() throws IOException {
        return method.getEntity().getContent();
    }

    /**
     * @return the statuscode of last request
     * @throws IOException
     */
    public int getStatusCode() {
        return statusCode;
    }

    private static URI toHttpURI(final URI uri) {
        if (uri.getScheme().equals("ipp")) {
            final String uriString = uri.toString().replaceFirst("ipp", "http");
            try {
               return new URI(uriString);
            } catch (URISyntaxException e) {
                throw new RuntimeException("toHttpURI buggy? : uri was " + uri);
            }
        }
        return uri;
    }

    /**
     * @param stream
     */
    public void setIppRequest(final InputStream stream) {
        final HttpEntity inputStreamRequestEntity = new InputStreamEntity(stream);
        method.setEntity(inputStreamRequestEntity);
    }
    
    public boolean execute() {
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        try (CloseableHttpClient httpConn = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build()) {
            try (CloseableHttpResponse response = httpConn.execute(method)){
                this.statusCode = response.getStatusLine().getStatusCode();
                if (this.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    throw new AuthenticationException(response.getStatusLine().getReasonPhrase());
                }
            }
            return true;
        } catch (IOException e){
            return false;
        }
    }
}