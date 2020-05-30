<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Document</title>

        <!-- tailwind css -->
        <link type="text/css" rel="stylesheet" href="/css/app.css">

        <!-- favicon -->
        <link rel="shortcut icon" href="images/favicon.ico"/>

        <!-- Awesome Icons -->
        <link rel="stylesheet" href="/fontawesome/css/all.css">

        <!-- JQuery -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

        <!-- Stomp.js -->
        <script src="https://cdn.bootcdn.net/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

        <!-- SockJs -->
        <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    </head>
    <body>

        <#import "layout/defaultLayout.ftlh" as defPage>
        <@defPage.headerDiv/>

        <div class="flex flex-col w-1/2 mx-auto text-center my-5">
            <span class="text-2xl font-bold mb-2">Find Chat Room</span><br>
            <span class="text-lg font-bold mb-2">Select a room you would like to join from the list below</span><br>
            <div class="block flex justify-between mb-2 bg-gray-200">
                <div class="w-1/2 inline-block my-auto py-2 flex justify-start">
                    <input type="text" class="border-solid border-2 w-2/3 ml-5" placeholder="Search chatroom">
                    <button><i class="fas fa-search p-2"></i></button>
                </div>
                <div class="my-auto mr-5">
                    <button class="border-2 border-solid bg-red-600 text-white py-1 px-4 rounded-full font-bold">Create
                        ChatRoom</button>
                </div>
            </div>
            <div class="border-2 border-solid flex flex-wrap">

                <#list allChannels as channel>
                    <a class="channelWrapper w-1/3 justify-start flex pl-5 my-2">
                        <div class="channelTitle inline-block font-bold px-1">${channel.title}</div>
                        <div class="ChannelInfo inline-block">(34)</div>
                    </a>
                </#list>

            </div>

        </div>
        <@defPage.footerDiv/>

    </body>
</html>