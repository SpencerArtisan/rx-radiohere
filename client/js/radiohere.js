(function() {
  'use strict';

  var app;

  app = angular.module("app", []);

  app.controller("RadiohereController", [
    "$scope", function(scope) {
      var _this = this;
      scope.gigs = [];
      scope.loadArtists = function() {
        var exampleSocket;
        console.log("Loading gigs");
        exampleSocket = new WebSocket("ws://radiohere.herokuapp.com/game", []);
//        exampleSocket = new WebSocket("ws://localhost:8025/game", []);
        console.log(exampleSocket);
        exampleSocket.onopen = function() {
        	exampleSocket.send("51.5544,-0.0907,4.0");
        	//exampleSocket.send("51.5403,-0.0884,5.0");
        };
        return exampleSocket.onmessage = function(event) {
          return scope.$apply(function() {
            return scope.gigs.push(jQuery.parseJSON(event.data));
          });
        };
      };
      return scope.play = function(gig) {
        console.log(gig.tracks[0]);
        $("#jquery_jplayer_1").jPlayer("setMedia", {
          title: gig.tracks[0].name,
          mp3: gig.tracks[0].streamUrl
        });
//        return $("#jquery_jplayer_1").jPlayer("play");
      };
    }
  ]);

}).call(this);
