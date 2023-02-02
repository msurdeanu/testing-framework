Feature: Test response time and number of errors for multiple services

    @Performance
    Scenario: Check samples per second and errors when 10 clients are requesting same url for 100 times
        Given up and running testing engine
        When I setup a HTTP sampler "sampler" to
            | url    | https://mihaisurdeanu.ro |
            | method | GET                      |
        When I setup a thread group "threadGroup" with "10" threads and "100" iterations for each thread
        When I inject HTTP sampler "sampler" into thread group "threadGroup"
        When I setup a local test plan "testPlan" using thread group "threadGroup" and schedule it immediately
        Then errors count for test plan "testPlan" is equal to "0"