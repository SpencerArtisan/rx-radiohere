console.log "hello"

exampleSocket = new WebSocket "ws://localhost:8025/websockets/game", []
console.log exampleSocket
exampleSocket.onmessage = (event) ->
  console.log event.data