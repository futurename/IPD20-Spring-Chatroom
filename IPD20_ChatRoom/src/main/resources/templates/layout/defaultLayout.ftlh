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
    <body class="flex flex-col min-h-screen h-screen " >
        <div class="min-h-screen h-full flex">
            <#macro headerDiv>
                <div class="w-full h-24 flex flex-row">
                    <div class="inline-block my-auto">
                        <img src="images/Logo_RGB_Orange.png" class="h-16">
                    </div>
                    <span class="text-4xl font-bold my-auto mx-auto pr-64" style="color: #ff5100">HEKO-WEI CHATROOM</span>
                </div>
            </#macro>

            <#macro footerDiv>
                <div class="w-full h-24 bg-gray-300  text-center ">
                    <span class="text-3xl">This is footer</span>
                </div>
            </#macro>

        </div>


    </body>
</html>