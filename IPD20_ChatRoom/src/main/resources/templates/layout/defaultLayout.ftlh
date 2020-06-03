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
        <div class="flex">
            <#macro headerDiv>
                <div class="w-full h-24 flex flex-row">
                    <div class="inline-block my-auto">
                        <img src="/images/Logo_RGB_Orange.png" class="h-16">
                    </div>
                    <span class="text-4xl font-bold my-auto mx-auto pr-64" style="color: #ff5100">HEKO-WEI CHATROOM</span>

                    <#if user?? >
                        <div class="flex">
                            <div class="flex mr-3 text-lg">
                                    <img class="mx-1 w-8 h-8 border-2 border-gray-300 rounded-full" src="${base64Icon!'/images/default_icon.png'}" />
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

            <#macro profileDiv>
                <#if user?? >
                    <div class="p-4 border-2 border-gray-500 inline-block">
                        <div class="flex justify-between items-center h-16">
                            <img class="h-16 w-16 rounded-full border-2" src="${base64Icon!'/images/default_icon.png'}" />
                            <p>${user.name}</p>
                        </div>

                        <p>${user.email}</p>

                        <p>Total Message: ${totalMsg}</p>

                        <div>
                            <p>Your Channels: ${totalOwnChannel!0}</p>
                            <ul>
                                <#list user.channels as channel >
                                    <li class="text-xs text-right">
                                        <#assign maxLength = 10 >
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
                            <p>Favorite Channels: ${totalFavChannels!0}</p>
                            <ul>
                            <#list user.favoriteChannels as favChannel >
                                <li class="text-xs text-right">
                                    <a href="/chatroom/${favChannel.id}">
                                        <#assign maxLength = 10 >
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
                                alert(result);
                                location.reload();
                            }).fail(function () {
                                alert("Something is wrong with ajax.");
                            });
                        });
                    });
                </script>
            </#macro>
        </div>


    </body>
</html>