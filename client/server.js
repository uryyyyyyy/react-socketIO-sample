// Setup basic express server
var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var port = process.env.PORT || 3000;

server.listen(port, function () {
  console.log('Server listening at port %d', port);
});

// Routing
app.use(express.static(__dirname + '/public'));

io.on('connection', function (socket) {

  // when the client emits 'new message', this listens and executes
  socket.on('normal request', function (data) {
    console.log("normal");
    socket.emit('normal response', {
      name: "normal"
    });
  });

  // when the client emits 'add user', this listens and executes
  socket.on('broadcast request', function (username) {
    console.log("broadcast");
    socket.broadcast.emit('broadcast response', {
      name: "broadcast"
    });
  });

});
