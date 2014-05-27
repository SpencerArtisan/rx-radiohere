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
			  
	scope.play = (streamUrl, track) ->
		console.log streamUrl
		$("#jquery_jplayer_1").jPlayer("setMedia",
		      title: track
		      mp3: streamUrl
		      )
		$("#jquery_jplayer_1").jPlayer("play")
])