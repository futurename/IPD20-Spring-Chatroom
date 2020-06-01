<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Test page</title>

        <!-- tailwind css -->
        <link type="text/css" rel="stylesheet" href="/css/app.css">

        <!-- favicon -->
        <link rel="shortcut icon" href="/images/favicon.ico"/>

        <!-- Awesome Icons -->
        <link rel="stylesheet" href="/fontawesome/css/all.css">

        <!-- JQuery -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

        <!-- Stomp.js -->
        <script src="https://cdn.bootcdn.net/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

        <!-- SockJs -->
        <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    </head>
    <body onload="connect()" onclose="">

        <#import "layout/defaultLayout.ftlh" as defPage>
        <@defPage.headerDiv/>

        <div class="flex block justify-center">
            <div id="left" class="flex flex-col block w-1/6">
                <div class="inline-block border-solid border-2 bg-blue-200 pl-2 justify-between flex">
                    <span class="text-2xl font-bold my-auto">${curChannel.title}</span>
                    <button class="mr-0 p-2 hover:text-orange-500">
                        <i class="fas fa-chevron-circle-down fa-2x my-auto"></i>
                    </button>
                </div>
                <div class="inline-block border-solid border-2 justify-between flex">
                    <input type="text" class=" py-1 px-2" placeholder="Search user here">
                    <button class="mr-0 p-2 hover:text-orange-500">
                        <i class="fas fa-search fa-2x"></i>
                    </button>
                </div>
                <div>
                    <#list userList as user>
                        <div class="flex flex-col py-1 pl-2 ">
                            <a class="inline-block" href="#">
                                <span class="font-bold text-lg">${user.name}</span><br>
                            </a>
                        </div>

                    </#list>
                </div>
            </div>

            <div id="content" class="w-2/5  px-2 flex-col flex">
                <div class="h-full border-2 border-solid mb-4">
                    <ul id="messagesList" class="p-2">
                        <#list messageList as msg>
                            <li>
                                <span class="text-base font-bold">${msg.user.name}</span>
                                <#assign hour = msg.createdTS.hour>
                                <#assign min =msg.createdTS.minute>
                                <#assign sec = msg.createdTS.second>

                                <span class="text-sm">(${(hour < 10)?then('0'+hour, hour + '')}:${(min < 10)?then('0'+min, min
                                    + '')}:${(sec < 10)?then('0'+sec, sec + '')})
                                </span><br>
                                <span class="text-sm">${msg.body}</span>
                            </li>
                        </#list>
                    </ul>

                </div>
                <div>
                    <textarea id="textInput" class="h-20 block w-full border-solid border-2 mb-4"
                              placeholder="Input here..."></textarea>
                </div>
                <div class="inline-block justify-end flex mb-6">
                    <button id="btSubmit" type="Submit" class="border-2 border-solid border-red-500 text-lg font-bold
                    rounded-full py-1 px-8
                    hover:bg-red-500 hover:text-white">
                        Send
                    </button>
                </div>
            </div>
            <div id="right">

            </div>
        </div>


        <script>
            var stompClient = null;

            function connect() {

                var socket = new SockJS('/ws');
                stompClient = Stomp.over(socket);
                stompClient.connect({}, onConnected);
                // event.preventDefault();
            }

            function onConnected() {
                // alert("connecting...");
                stompClient.subscribe('/topic/public', messageReceieved);
                // alert("finish");

            }


            function sendMsg(event) {
                var body = $("#textInput").val();

                if (body && stompClient) {
                    //alert("Sending msg: " + body);
                    var chatMessage = {
                        body: body,
                        channelId: ${curChannel.id},
                        senderId: 2,
                        type: 'CHAT'
                    };
                    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
                    $("#textInput").val("");

                }
                event.preventDefault();
            }

            function messageReceieved(payload) {

                let msg = JSON.parse(payload.body);

                //alert("received msg: " + msg.body);
                var msgBox = document.querySelector('#messagesList');
                var li = document.createElement('li');

                var spanUser = document.createElement('span');
                var userText = document.createTextNode(msg.senderName);
                spanUser.append(userText);
                spanUser.classList.add("font-bold");
                spanUser.classList.add("text-base");

                var spanTime = document.createElement('span');

                var datetime = new Date(msg.createdTS);

                var timeText = document.createTextNode(" (" + formatTime(datetime.getHours()) + ":" + formatTime(datetime.getMinutes())+
                    ":" + formatTime(datetime.getSeconds()) + ")");
                spanTime.append(timeText);
                spanTime.classList.add("text-sm");

                var br = document.createElement('br');

                var spanMsg = document.createElement('span');
                var msgText = document.createTextNode(msg.body)
                spanMsg.append(msgText);
                spanMsg.classList.add("text-sm");

                li.append(spanUser);
                li.append(spanTime);
                li.append(br);
                li.append(spanMsg);

                msgBox.append(li);
            }

            function formatTime(number){
                return number < 10? '0' + number : number;
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
            btSubmit.addEventListener("submit", sendMsg, true);

        </script>


    </body>
</html>