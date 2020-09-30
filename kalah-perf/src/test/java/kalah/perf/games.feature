Feature: games crud

  Background:
    * url 'http://localhost:8080/'

  Scenario: create a game, make a move and then get it by id
    Given path 'games'
    And request ''
    And header content-type = 'application/json'
    When method post
    Then status 201
    And match header Location != ''

    * def gameId = response.id
    * def pitId = 1
    Given path 'games', gameId, 'pits', pitId
    And request ''
    When method put
    Then status 200

    Given path 'games', gameId
    When method get
    Then status 200
