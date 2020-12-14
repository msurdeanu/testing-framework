Feature: Test multiple REST endpoints provided by Wordpress platform for my blog

  Scenario: REST API is enabled for my blog
    Given up and running testing engine
    When I make a GET request "1" to URL "https://mihaisurdeanu.ro/wp-json/wp/v2" with no authentication
    Then status code for request "1" is "200"
    Then response body for request "1" is a string
    Then deserialize response body for request "1" to JSON format

  Scenario: Multiple posts are published on my blog
    Given up and running testing engine
    When I make a GET request "1" to URL "https://mihaisurdeanu.ro/wp-json/wp/v2/posts" with no authentication
    Then status code for request "1" is "200"
    Then response body for request "1" is a string
    Then deserialize response body for request "1" to JSON format
