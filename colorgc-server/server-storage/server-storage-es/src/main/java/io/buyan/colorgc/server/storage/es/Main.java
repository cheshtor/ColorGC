package io.buyan.colorgc.server.storage.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/27
 */
public class Main {

    public static void main(String[] args) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("home.extends.top", 9200, "http"))
        );
        CreateIndexRequest request = new CreateIndexRequest("colorgc-instance");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1));


    }

}
