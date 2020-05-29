<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <link type="text/css" rel="stylesheet" href="/css/app.css">
        <title>Index</title>

        <!-- favicon -->
        <link rel="shortcut icon" href="../images/live-chat-icon.png"/>

        <!-- Awesome Icons -->
        <link rel="stylesheet" href="/css/all.min.css">

        <!-- JQuery -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

        <!-- Stomp.js -->
        <script src="https://cdn.bootcdn.net/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

        <!-- SockJs -->
        <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>

    </head>
    <body>
        <#import "layout/indexLayout.ftlh" as indexHeader>
        <@indexHeader.headerDiv/>


        <#list msgList as msg>
            <p class="text-red-500 text-2xl">Message sent: ${msg.body}</p>
        <#else>
            <p>There is no Message</p>
        </#list>
        <h1>asalskdjflkas</h1>

        <ul id="messageBox" class="font-bold">
            <li>first li row</li>
        </ul>

        <button id="btConn" type="submit" class="bg-blue-200 border-solid font-bold p-2">Connect</button>
        <br>
        <textarea id="taMessage" class="w-full h-16 border-2 border-solid"></textarea>
        <button id="btSubmit" type="submit" class="bg-blue-200 border-solid font-bold p-2">Submit</button>
        <br>

        <@indexHeader.footerDiv/>

    </body>

    <script>

        var stompClient = null;

        function connect(event) {

            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected);
            event.preventDefault();
        }

        function onConnected() {
            alert("connecting...");
            stompClient.subscribe('/topic/public', messageReceieved);
            alert("finish");

        }


        function sendMsg(event) {
            var body = $("#taMessage").val();

            if (body && stompClient) {
                alert("Seding msg: " + body);
                var chatMessage = {
                    sender: 'tester',
                    body: body,
                    type: 'CHAT'
                };
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
                $("#taMessage").val("");

            }
            event.preventDefault();
        }

        function messageReceieved(payload) {

            let msg = payload;

            alert("received msg: " + msg);
            var msgBox = document.querySelector('#messageBox');
            msgBox.append('<li>'+msg + '</li>');
            alert('<li>'+msg + '</li>');
        }


        $(document).ready(function () {
            $("#btConn").on("click", function (e) {
                connect(e);
            })

            $("#btSubmit").on("click", function (e) {
                sendMsg(e);
            })
        })

        var btSubmit = document.querySelector('#btSubmit');
        var btConn = document.querySelector("#btConn");
        btSubmit.addEventListener("submit", sendMsg, true);
        btConn.addEventListener("submit", connect, true)
    </script>
</html>