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
                        <a id="btReturn"  href="/">Return to index
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
            // alert("finish");
            var userConnInfo = {
                senderName: "Jerry",
                channelId:${curChannel.id},
                senderId: 1
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
            spanMsg.classList.add("whitespace-pre-wrap")
            spanMsg.classList.add("break-words")

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