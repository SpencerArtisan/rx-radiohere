'use strict'                                                                                                          

app = angular.module("app", [])

app.controller("RadiohereController", ["$scope", (scope) ->
	scope.artists = []

	scope.loadArtists = =>
		console.log "Loading artists"
		exampleSocket = new WebSocket "ws://localhost:8025/websockets/game", []
		console.log exampleSocket
		exampleSocket.onmessage = (event) ->
		  scope.$apply ->
			  scope.artists.push jQuery.parseJSON(event.data)
])