<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Header</title>
        <link type="text/css" rel="stylesheet" href="/css/app.css">


    </head>
    <body class="" >
        <div class=" h-screen flex">
            <#macro headerDiv>
                <div class="w-full h-24 flex flex-row">
                    <div class="inline-block my-auto">
                        <img src="/images/Logo_RGB_Orange.png" class="h-16">
                    </div>
                    <span class="text-4xl font-bold my-auto mx-auto pr-64" style="color: #ff5100">HEKO-WEI CHATROOM</span>

                    <#if user?? >
                        <div class="flex">
                            <div class="flex mr-3 text-lg">
                                    <img class="mx-1 w-8 h-8 border-2 border-gray-300 rounded-full" src="${user.base64Icon!'/images/default_icon.png'}" />
                                ${user.name},
                            </div>
                            <a class="underline text-sm" href="/logout">logout</a>
                        </div>
                    <#else>
                        <a class="underline text-sm" href="/login">Login</a>
                    </#if>
                </div>
            </#macro>

            <#macro footerDiv>
                <div class="w-full h-24 bg-gray-300  text-center block">
                    <span class="text-3xl">This is footer</span>
                </div>
            </#macro>

        </div>


    </body>
</html>