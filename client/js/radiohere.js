(function() {
  'use strict';

  var app;

  app = angular.module("app", []);

  app.controller("RadiohereController", [
    "$scope", function(scope) {
      var _this = this;
      scope.artists = [];
      return scope.loadArtists = function() {
        var exampleSocket;
        console.log("Loading artists");
        exampleSocket = new WebSocket("ws://localhost:8025/websockets/game", []);
        console.log(exampleSocket);
        return exampleSocket.onmessage = function(event) {
          return scope.$apply(function() {
            return scope.artists.push(jQuery.parseJSON(event.data));
          });
        };
      };
    }
  ]);

}).call(this);
