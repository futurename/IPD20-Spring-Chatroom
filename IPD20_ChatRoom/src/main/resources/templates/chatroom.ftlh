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

        <!-- Emojis -->
        <script src="https://cdn.jsdelivr.net/npm/emoji-button@latest/dist/index.min.js"></script>
        <script src="/js/emoji.js" type="text/javascript"></script>

        <!-- CKEditor -->
        <script src="https://cdn.ckeditor.com/ckeditor5/19.1.1/classic/ckeditor.js"></script>


    </head>
    <body onload="connect()">

        <#import "layout/defaultLayout.ftlh" as defPage>
        <@defPage.headerDiv/>
        <p id="message" class="hidden text-center text-xl">
            <span class="text-blue-500">Channel will delete in <span id="msgSecond" class="text-red-400 font-bold">3</span> seconds.</span>
        </p>

        <div class="max-w-2xl mx-auto flex flex-col sm:flex-row">
            <div id="left" class="flex flex-col p-2 sm:w-1/3">
                <div class="relative flex bg-blue-200 border-2 items-center justify-between">
                    <span class="text-2xl px-1 font-bold my-auto">${curChannel.title}</span>
                    <button class="absolute text-xl block right-0 hover:text-orange-500">
                        <i class="fas fa-chevron-circle-down"></i>
                    </button>
                </div>
                <div class="relative flex border-2 items-center justify-between">
                    <input type="text" class="py-1 px-2 w-full" placeholder="Search user here">
                    <button class="absolute text-lg block right-0 hover:text-orange-500">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
                <div class="hidden sm:block border-2">
                    <#list userList as user>
                        <div class="flex flex-col py-1 pl-2 ">
                            <a class="inline-block" href="#">
                                <span class="font-bold text-lg">${user.name}</span><br>
                            </a>
                        </div>
                    </#list>
                </div>
            </div>

            <div id="content" class="px-2 flex flex-col sm:w-2/3">
                <div class="h-full border-2 border-solid mb-4">
                    <div id="messagesList" class="p-2">
                        <#list messageList as msg>
                            <div>
                                <span class="text-base font-bold">${msg.user.name}</span>
                                <#assign hour = msg.createdTS.hour>
                                <#assign min =msg.createdTS.minute>
                                <#assign sec = msg.createdTS.second>

                                <span class="text-sm">(${(hour < 10)?then('0'+hour, hour + '')}:${(min < 10)?then('0'+min, min
                                    + '')}:${(sec < 10)?then('0'+sec, sec + '')})
                                </span><br>
                                <div class="whitespace-pre-wrap text-sm break-words"><#noautoesc>${msg.body}</#noautoesc></div>
                            </div>
                        </#list>
                    </div>

                </div>
                <div class="mb-4">
                    <div id="textInput" class="block w-full" contenteditable="true"></div>
                </div>
                <div class="inline-block flex mb-6 justify-between">
                    <div class="border-2 border-solid border-red-500 text-lg font-bold
                    rounded-full py-1 px-6 hover:bg-red-500 hover:text-white">
                        <a id="btReturn" onclick="leaveChatroom();">
                            <span class="hidden sm:inline">Return to index</span>
                            <span class="sm:hidden"><i class="fas fa-arrow-left"></i></span>
                        </a>
                    </div>
                    <div>
                        <button id="btEmoji" class="px-3 py-1 mr-2">&#128515;</button>
                        <button id="btSubmit" type="Submit" class="border-2 border-solid border-red-500 text-lg font-bold
                    rounded-full py-1 px-8 hover:bg-red-500 hover:text-white">
                            Send
                        </button>
                    </div>
                </div>
            </div>

            <div id="right">
                <div id="editor">

                </div>
            </div>
        </div>
    </body>

    <script>

        function leaveChatroom(){
            window.location = "/chatroom/leave/" + ${curChannel.id} + "/" + sessionStorage.getItem("unique_id");
        }

        var stompClient = null;

        function connect() {
            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected);
            // event.preventDefault();
        }

        function onConnected() {
            // alert("connecting...");
            stompClient.subscribe('/chatroom/' + ${curChannel.id}, messageReceieved);
            stompClient.subscribe('/notification/' + ${user.id}, notification);
            stompClient.subscribe('/chatroomNotification/' + ${curChannel.id}, chatroomNotification);

            // alert("finish");
            var userConnInfo = {
                channelId:${curChannel.id}
            }
            stompClient.send("/app/chat.addUser", {}, JSON.stringify(userConnInfo));
        }

        function sendMsg(event) {
            var body = $(".ck-content").html();

            if (body && stompClient) {
                //alert("Sending msg: " + body);
                var chatMessage = {
                    body: body,
                    channelId: ${curChannel.id},
                    senderId: 1,
                    type: 'CHAT'
                };
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
                document.querySelector(".ck-content").firstElementChild.innerHTML = "";
            }
            event.preventDefault();
        }

        function chatroomNotification(payload) {
            if(payload.body == "delete"){
                $("#message").show();
                setTimeout(function () {
                    $("#msgSecond").text("3");
                }, 1000);
                setTimeout(function () {
                    $("#msgSecond").text("2");
                }, 2000);
                setTimeout(function () {
                    $("#msgSecond").text("1");
                }, 3000);
                setTimeout(function () {
                    window.location.replace("/");
                }, 4000);
            }
        }

        function notification(payload) {
            alert(payload);
        }

        function messageReceieved(payload) {

            let msg = JSON.parse(payload.body);

            //alert("received msg: " + msg.body);
            var msgBox = document.querySelector('#messagesList');
            var li = document.createElement('div');

            var spanUser = document.createElement('span');
            var userText = document.createTextNode(msg.senderName);
            spanUser.append(userText);
            spanUser.classList.add("font-bold");
            spanUser.classList.add("text-base");

            var spanTime = document.createElement('span');

            var datetime = new Date(msg.createdTS);

            var timeText = document.createTextNode(" (" + formatTime(datetime.getHours()) + ":" + formatTime(datetime.getMinutes()) +
                ":" + formatTime(datetime.getSeconds()) + ")");
            spanTime.append(timeText);
            spanTime.classList.add("text-sm");

            var br = document.createElement('br');

            var spanMsg = document.createElement('div');

            spanMsg.innerHTML = msg.body;

            spanMsg.classList.add("text-sm");
            spanMsg.classList.add("whitespace-pre-wrap");
            spanMsg.classList.add("break-words");

            li.append(spanUser);
            li.append(spanTime);
            li.append(br);
            li.append(spanMsg);

            msgBox.append(li);
        }

        function formatTime(number) {
            return number < 10 ? '0' + number : number;
        }


        $(document).ready(function () {

            $("#btSubmit").on("click", function (e) {
                sendMsg(e);
            });


        })


        var btSubmit = document.querySelector('#btSubmit');
        btSubmit.addEventListener("submit", sendMsg, true);


    </script>


    <script>
        ClassicEditor
            .create(document.querySelector('#textInput'))
            .catch(error => {
                console.error(error);
            });
    </script>

</html>