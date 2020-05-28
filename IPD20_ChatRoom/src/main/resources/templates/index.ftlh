<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <link type="text/css" rel="stylesheet" href="/css/app.css">
        <title>Index</title>
    </head>
    <body>
        <#import "layout/indexLayout.ftlh" as indexHeader>
        <@indexHeader.headerDiv/>


        <#list msgList as msg>
            <p class="text-red-500 text-2xl">Message sent: ${msg.body}</p>
        <#else>
            <p>There is no Message</p>
        </#list>

        <@indexHeader.footerDiv/>

    </body>
</html>