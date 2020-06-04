<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Index</title>

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
    <body onload="connect();">

        <#import "layout/defaultLayout.ftlh" as defPage>
        <@defPage.headerDiv/>

        <div class="flex flex-col w-1/2 mx-auto text-center my-5">
            <span class="text-2xl font-bold mb-2">Find Chat Room</span><br>
            <span class="text-lg font-bold mb-2">Select a room you would like to join from the list below</span><br>
            <div class="block flex justify-between mb-2 bg-gray-200">
                <div class="w-1/2 inline-block my-auto py-2 flex justify-start">
                    <input type="text" class="border-solid border-2 w-2/3 ml-4" placeholder="Search chatroom">
                    <button><i class="fas fa-search p-2"></i></button>
                </div>
                <div class="my-auto mr-5">
                    <a href="/chatroom" class="border-2 border-solid bg-red-600 text-white py-1 px-4 rounded-full font-bold">Create
                        ChatRoom
                    </a>
                </div>
            </div>
            <div>

                <#if activeChannelStatus??>
                    <p class="font-bold text-lg bg-yellow-300 text-red-500">${errorMsg}</p>
                </#if>
            </div>
            <div class="border-2 border-solid flex flex-wrap">

                <#list allChannels as channel>
                    <div class="w-1/3 inline-block flex flex-row my-2 channelWrapper">
                        <a class=" justify-start flex pl-2 my-2" href="/chatroom/${channel.id}">
                            <div class="channelTitle inline-block font-bold px-1 text-lg">${channel.title}</div>
                            <div class="ChannelInfo inline-block ml-2 my-auto">(34)</div>
                            <div class="chatting text-sm text-blue-700 my-auto inline-block font-bold pl-2"
                                 id="channel_${channel.id}">
                                Chatting...
                            </div>
                        </a>

                        <a class="px-2 py-1 my-auto favStar text-red-700 transform hover:scale-150"
                           id="${channel.id}">
                            <i class="fas fa-star"></i>
                        </a>
                    </div>
                </#list>

            </div>

        </div>
        <@defPage.footerDiv/>

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
            stompClient.subscribe('/status/${userId}', displayInfo);

        }

        function displayInfo(payload){
            document.location.href="/";
        }


        $(".favStar").hide();
        $(".chatting").hide();

        <#list allFavChannels as favChannel>
        <#assign curFavId = favChannel.id>
        $("#${curFavId}").addClass("showFav");
        $("#${curFavId}").show();
        </#list>

        <#list activeChannels as activeChannel>
        <#assign activeId = activeChannel.id>
        $('#channel_${activeChannel.id}').show();

        </#list>


        $(document).ready(function () {

            $(".channelWrapper").on("mouseover", function () {
                if (!$(this).children("a:nth-child(2)").hasClass("showFav")) {
                    $(this).children("a:nth-child(2)").show();
                }
            });

            $(".channelWrapper").on("mouseout", function () {
                if (!$(this).children("a:nth-child(2)").hasClass("showFav")) {
                    $(this).children("a:nth-child(2)").hide();
                }
            })

            $(".favStar").on("click", function () {
                if ($(this).hasClass("showFav")) {
                    $(this).removeClass("showFav");
                    $(this).hide();
                    var dataContent = {};
                    dataContent['userId'] = 1;
                    dataContent['channelId'] = $(this).attr("id");
                    $.ajax({
                        url: "/chatroom/delFavChannel",
                        type: "DELETE",
                        contentType: "application/json",
                        dataType: "json",
                        data: JSON.stringify(dataContent),
                        error: function (jqxhr, status, errorThrown) {

                        }
                    });
                } else {
                    $(this).addClass("showFav");
                    $(this).show();

                    var dataContent = {};
                    dataContent['userId'] = ${userId};
                    dataContent['channelId'] = $(this).attr("id");
                    $.ajax({
                        url: "/chatroom/addFavChannel",
                        type: "POST",
                        contentType: "application/json",
                        dataType: "json",
                        data: JSON.stringify(dataContent),
                        error: function (jqxhr, status, errorThrown) {

                        }
                    });
                }
            })

        })
    </script>
</html>