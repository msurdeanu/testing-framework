Feature: Test multiple REST endpoints provided by Wordpress platform

    Scenario: REST API is enabled for a given website
        Given up and running testing engine
        When I make HTTP request "1" with no authentication to
            | url | https://aventurata.ro/wp-json/wp/v2 |
        Then status code for request "1" is "200"
        And convert response body for request "1" to JSON format
        And response body for request "1" is in JSON format and meets imposed constraints
            | expression  | operator | expected |
            | $.namespace | =        | wp/v2    |

    Scenario: Multiple posts are published for a given website
        Given up and running testing engine
        When I make HTTP request "1" with no authentication to
            | url | https://aventurata.ro/wp-json/wp/v2/posts |
        Then status code for request "1" is "200"
        Then response body for request "1" is a string
        Then convert response body for request "1" to JSON format
