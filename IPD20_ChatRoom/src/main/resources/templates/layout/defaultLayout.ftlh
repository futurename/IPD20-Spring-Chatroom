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
<body>
<#macro headerDiv>
    <div class="max-w-2xl mx-auto my-5 flex items-center justify-between">
        <a class="inline-block" href="/">
            <img src="/images/Logo_RGB_Orange.png" class="h-8 sm:h-16">
        </a>

        <#if user?? >
        <div class="flex items-center mr-3 text-lg">
            <div id="profile" class="flex">
                <img class="mx-1 w-8 h-8 border-2 border-gray-300 rounded-full cursor-pointer"
                     src="${base64Icon!'/images/default_icon.png'}"/>
                <span class="cursor-pointer hover:underline">${user.name},</span>
                <div id="profileContent" class="hidden absolute right-0 sm:right-auto bg-white z-10">
                    <@profileDiv/>
                </div>
            </div>
            <a class="underline ml-2 text-sm" href="/logout">logout</a>
        </div>
        <#elseif !springMacroRequestContext.requestUri?contains("/login")>
        <a class="underline text-sm" href="/login">Login</a>
        </#if>
    </div>
    <script>
        $(document).ready(function () {
            $("#profile").click(function(e){
                $("#profileContent").show();
                e.stopPropagation();
            });
            $("#profileContent").click(function(e){
                e.stopPropagation();
            });
            $(document).click(function(){
                $("#profileContent").hide();
            });
        });
    </script>
</#macro>

<#macro footerDiv>
    <div class="max-w-2xl mx-auto my-5 text-sm text-gray-700 text-right">
        <p>Developed by
            <a href="https://www.linkedin.com/in/wei-wang-2428a976/" class="font-bold hover:underline">Wei Wang</a>,
            <a href="https://www.linkedin.com/in/donghyeokseo/" class="font-bold hover:underline">Donghyeok Seo</a></p>
        <p>Visit our <a href="https://github.com/futurename/IPD20-Spring-Chatroom" class="underline">github page.</a></p>
    </div>
</#macro>

<#macro profileDiv>
<#if user?? >
    <div class="p-4 border-2 border-gray-500 inline-block">
        <div class="flex justify-between items-center h-16">
            <img class="h-16 w-16 rounded-full border-2" src="${base64Icon!'/images/default_icon.png'}"/>
            <p>${user.name}</p>
        </div>

        <p>${user.email}</p>

        <p>Total Message: ${totalMsg}</p>

        <div>
            <p>Your Channels: ${totalOwnChannel!0}</p>
            <ul>
                <#list user.channels as channel >
                    <li class="text-xs text-right">
                        <#assign maxLength = 15 >
                        <a href="/chatroom/${channel.id}">
                            <#if channel.title?length < maxLength>
                                ${channel.title}
                            <#else>
                                ${channel.title?substring(0, maxLength - 3)} ...
                            </#if>
                        </a>
                        <button class="btDeleteChannel border-2">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                        <span class="channelId hidden">${channel.id}</span>
                    </li>
                <#else>
                    <li class="text-xs text-right">You don't have Channel yet.</li>
                </#list>
            </ul>
        </div>
        <div>
            <p>Favorite Channels: ${user.favoriteChannels?size}</p>
            <ul>
                <#list user.favoriteChannels as favChannel >
                    <li class="text-xs text-right">
                        <a href="/chatroom/${favChannel.id}">
                            <#assign maxLength = 20 >
                            <#if favChannel.title?length < maxLength>
                                ${favChannel.title}
                            <#else>
                                ${favChannel.title?substring(0, maxLength - 3)} ...
                            </#if>
                        </a>
                    </li>
                <#else>
                    <li class="text-xs text-right">There is no Favorite Channel</li>
                </#list>
            </ul>
        </div>

    </div>
</#if>
    <script>
        $(document).ready(function () {
            $(".btDeleteChannel").click(function () {
                let id = $(this).next(".channelId").text();
                $.ajax({
                    url: "/chatroom/" + id,
                    type: "DELETE"
                }).done(function (result) {
                    location.reload();
                }).fail(function () {
                    alert("Something is wrong with ajax.");
                });
            });
        });
    </script>
</#macro>
</body>
</html>