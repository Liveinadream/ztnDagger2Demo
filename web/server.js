const socketIo = require('socket.io');
const express = require("express");
const http = require("http");
const app = express();
const server = http.createServer(app);

const io = new socketIo(server);

app.use("/", express.static(__dirname + "/"));

server.listen(8080);
io.on("connection", function (clientSocket) {
    clientSocket.on("sendMsg", function (data) {
        console.log(`接收到了消息 ${data.msg}`);
        clientSocket.broadcast.emit("receiveMsg", data)
    })

});
