/*
 * Licensed to David Pilato (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.pilato.elasticsearch.crawler.fs.test.integration;

import fr.pilato.elasticsearch.crawler.fs.settings.Fs;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.Test;

import static fr.pilato.elasticsearch.crawler.fs.framework.FsCrawlerUtil.INDEX_SUFFIX_FOLDER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test index_folders crawler setting
 */
public class FsCrawlerTestIgnoreFoldersIT extends AbstractFsCrawlerITCase {

    /**
     * Test case for #155: https://github.com/dadoonet/fscrawler/issues/155 : New option: do not index folders
     */
    @Test
    public void test_ignore_folders() throws Exception {
        Fs fs = startCrawlerDefinition()
                .setIndexFolders(false)
                .build();
        startCrawler(getCrawlerName(), fs, endCrawlerDefinition(getCrawlerName()), null);

        // We expect to have two files
        countTestHelper(new SearchRequest(getCrawlerName()), 2L, null);

        // We expect having no folders
        SearchResponse response = elasticsearchClient.search(new SearchRequest(getCrawlerName() + INDEX_SUFFIX_FOLDER), RequestOptions.DEFAULT);
        staticLogger.trace("result {}", response.toString());
        assertThat(response.getHits().getTotalHits(), is(0L));
    }
}
