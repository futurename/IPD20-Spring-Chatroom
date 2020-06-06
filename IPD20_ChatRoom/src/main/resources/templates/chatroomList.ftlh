<div class="p-2 sm:flex sm:flex-wrap border-2 border-solid">



    <#list allChannels as channel>
    <div class="mb-1 sm:w-1/3 flex items-center sm:block">
        <div class="flex flex-row items-center border-l-4 border-orange-500 channelWrapper">
            <a class="p-2 block justify-start items-center font-bold text-lg cursor-pointer
                    hover:underline"
               onclick="enterChatroom(${channel.id})"
               title="${channel.title}">
                <#assign maxLength = 20 >
                <#if channel.title?length < maxLength>
                    ${channel.title}
                <#else>
                    ${channel.title?substring(0, maxLength - 3)} ...
                </#if>
            </a>

            <div class="ChannelInfo inline-block ml-2 my-auto">
                <#list activeUserCount as k,v>
                    <#if k == channel.id>
                        (${v})
                    </#if>
                </#list>
            </div>

            <a class="<#if !channel.users?seq_contains(user) > hidden <#else> showFav </#if>
                    px-2 py-1 my-auto favStar text-red-700 transform hover:scale-150"
               id="${channel.id}">
                <i class="fas fa-star"></i>
            </a>
        </div>
        <div class="sm:-mt-3 ml-4 text-left text-xs text-gray-500">
            by ${channel.owner.name}
        </div>
        <div class="<#if !activeChannels?seq_contains(channel.id) >hidden</#if> ml-2 sm:-mt-2 chatting text-sm text-blue-700
        font-bold"
             id="channel_${channel.id}">
            Chatting...
        </div>
    </div>
<#else>
    <div>There is no channel.</div>
</#list>
</div>
<div>
    <#if currentPage != 1>
        <span id="prev" class="text-sm cursor-pointer hover:underline">prev</span>
    </#if>
    <#list 1..totalPage as x>
        <#if x == currentPage>
            <span class="text-2xl">#{x}</span>
        <#else>
            <span class="pages text-sm cursor-pointer hover:underline">#{x}</span>
        </#if>
    </#list>
    <#if currentPage != totalPage>
        <span id="next" class="text-sm cursor-pointer hover:underline">next</span>
    </#if>
</div>
<script>

    function enterChatroom(curChannelId) {
        window.location = "/chatroom/"+curChannelId + "/" + sessionStorage.getItem("unique_id");
    }
</script>
