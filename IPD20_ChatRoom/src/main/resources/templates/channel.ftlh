<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <link type="text/css" rel="stylesheet" href="/css/app.css">
        <title>channel</title>
    </head>
    <body>
        <#import "layout/indexLayout.ftlh" as indexHeader>
        <@indexHeader.headerDiv/>


        <#list searchList as search >
            <p class="text-red-500 text-2xl">Channel title: ${search.title}</p>
            <p class="text-red-500 text-2xl">Channel description: ${search.description}</p>
        <#else>
            <p>There is no Message</p>
        </#list>
        <h1>asalskdjflkas</h1>

        <@indexHeader.footerDiv/>

    </body>
</html>