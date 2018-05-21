package com.thunisoft.zipper.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class RequestUtil {

    /**
     * UTF8
     */
    private static final String UTF8 = "UTF-8";

    /**
     * 传输文件的请求
     * @param url
     * @param files
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String executePostWithFile(String url, File... files)
            throws ClientProtocolException, IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for (File file : files) {
                builder.addBinaryBody(file.getName(), file,
                        ContentType.DEFAULT_BINARY, file.getName());
            }
            post.setEntity(builder.build());
            ResopnseHandler handler = new ResopnseHandler();
            String result = client.execute(post, handler);
            return result;
        } finally {
            IOUtils.closeQuietly(client);
        }
    }

    static class ResopnseHandler implements ResponseHandler<String> {

        public String handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= HttpStatus.SC_OK
                    && statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
                HttpEntity entity = response.getEntity();
                String result = null;
                if (entity != null) {
                    result = EntityUtils.toString(entity, UTF8);
                }
                EntityUtils.consume(entity);
                return result;
            } else {
                throw new ClientProtocolException("Unexpected response status:"
                        + statusCode);
            }
        }

    }
}
