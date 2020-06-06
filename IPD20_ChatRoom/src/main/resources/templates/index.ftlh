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

        <div class="flex flex-col max-w-2xl mx-auto text-center my-5">
            <p class="text-2xl font-bold mb-2">Find Chat Room</p>
            <p class="text-lg font-bold mb-2">Select a room you would like to join from the list below</p>

            <div class="flex justify-evenly items-center p-2 mb-2 bg-gray-200">
                <div class="flex">
                    <input type="text" class="border-solid border-2 p-1" placeholder="Search chatroom">
                    <button><i class="fas fa-search p-2"></i></button>
                </div>
                <a href="/chatroom" class="hidden sm:block bg-red-600 text-white py-1 px-4 rounded-full font-bold">
                    Create ChatRoom
                </a>
                <a href="/chatroom" class="sm:hidden bg-red-600 text-white px-3 py-1 rounded-full font-bold">
                    <i class="fas fa-plus"></i>
                </a>
            </div>

            <#if activeChannelStatus??>
                <p class="font-bold text-lg bg-yellow-300 text-red-500">${errorMsg}</p>
            </#if>

            <div class="p-2 sm:flex sm:flex-wrap border-2 border-solid">
                <#list allChannels as channel>
                    <div class="mb-1 sm:w-1/3 flex items-center sm:block">
                        <div class="flex flex-row items-center border-l-4 border-orange-500 channelWrapper">
                            <a class="p-2 block justify-start items-center font-bold text-lg
                                    hover:underline" href="#" onclick="enterChatroom(${channel.id});" title="${channel.title}">
                                <#assign maxLength = 20 >
                                <#if channel.title?length < maxLength>
                                    ${channel.title}
                                <#else>
                                    ${channel.title?substring(0, maxLength - 3)} ...
                                </#if>
                            </a>

                            <div class="ChannelInfo inline-block ml-2 my-auto">(34)</div>

                            <a class="px-2 py-1 my-auto favStar text-red-700 transform hover:scale-150"
                               id="${channel.id}">
                                <i class="fas fa-star"></i>
                            </a>
                        </div>
                        <div class="hidden ml-2 sm:-mt-2 sm:block chatting text-sm text-blue-700 font-bold"
                             id="channel_${channel.id}">
                            Chatting...
                        </div>
                    </div>
                </#list>
            </div>


        </div>
        <@defPage.footerDiv/>

    </body>

    <script>

        function enterChatroom(curChannelId) {
            window.location = "/chatroom/"+curChannelId + "/" + sessionStorage.getItem("unique_id");
        }

        var stompClient = null;

        function connect() {

            initSessionId();

            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected);
            // event.preventDefault();
        }

        function initSessionId() {
            var curSessionId = sessionStorage.getItem("unique_id");
            curSessionId = (curSessionId === null) ? "undefined" : curSessionId;
            var dataContent = {};
            dataContent['sessionId'] = curSessionId;
          //  alert(curSessionId);

            $.ajax({
                url: "/chatroom/requestSessionId",
                type: "POST",
                data: dataContent,
            }).done(function(payload) {
               // alert("success: " + payload);
                sessionStorage.setItem("unique_id",payload);
            }).fail(function(jqxhr) {
                //alert("error:" + jqxhr.responseText);
            })
        }

        function onConnected() {
            stompClient.subscribe('/status/${userId}', displayInfo);
        }

        function displayInfo(payload) {
            document.location.href = "/";
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
        $('#channel_${activeChannel.channelId}').show();

        </#list>


        $(document).ready(function () {
            $(".channelWrapper").on("mouseover", function () {
                if (!$(this).children(".favStar").hasClass("showFav")) {
                    $(this).children(".favStar").show();
                }
            });

            $(".channelWrapper").on("mouseout", function () {
                if (!$(this).children(".favStar").hasClass("showFav")) {
                    $(this).children(".favStar").hide();
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
                        data: dataContent,
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