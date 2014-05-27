(function() {
  var exampleSocket;

  console.log("hello");

  exampleSocket = new WebSocket("ws://localhost:8025/websockets/game", []);

  console.log(exampleSocket);

  exampleSocket.onmessage = function(event) {
    return console.log(event.data);
  };

}).call(this);
