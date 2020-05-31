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
    <body>

        <#import "layout/defaultLayout.ftlh" as defPage>
        <@defPage.headerDiv/>

        <div class="flex flex-row">
            <div id="left" class="flex flex-col">
                <div class="inline-block border-solid border-2 bg-blue-200">
                    <#-- <span class="text-2xl font-bold">${curChannel.title}</span>-->
                    <button class="p-2 hover:text-orange-500"><i class="fas fa-chevron-circle-down fa-2x my-auto "></i></button>
                </div>
                <div>
                    <#list userList as user>
                        <span>${user.name}</span><br>
                    </#list>
                </div>
            </div>
            <div id="content">
                <#list messageList as msg>
                    <span class="text-lg font-bold">${msg.body} by ${msg.user.name}</span><br>
                </#list>
            </div>
            <div id="right">

            </div>
        </div>



        <@defPage.footerDiv/>


    </body>
</html>