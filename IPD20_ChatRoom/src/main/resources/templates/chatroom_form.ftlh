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
    <body>

        <#import "layout/defaultLayout.ftlh" as defPage>
        <@defPage.headerDiv/>

        <div class="max-w-md mx-auto my-8 p-4 border-2">
            <p class="text-3xl font-bold mb-5 text-center inline-block">Create Chatroom</p>
            <form method="post" action="/chatroom" enctype="multipart/form-data">
                <div>
                    <label for="title" class="text-lg font-bold py-1">Title</label>
                    <input type="text" id="title" name="title" value="${(chatroomForm.title)!"title"}" class="border-solid border-2 w-full mb-3">
                    <p class="-mt-3 p-2 h-3 text-right text-xs text-red-400">${(bindingResult.getFieldError("title").getDefaultMessage())!}</p>
                </div>
                <div>
                    <label for="description" class="text-lg font-bold py-1">Description</label>
                    <textarea id="description" name="description" class="border-solid border-2 w-full mb-3">${(chatroomForm.description)!"description"}</textarea>
                    <p class="-mt-3 p-2 h-3 text-right text-xs text-red-400">${(bindingResult.getFieldError("description").getDefaultMessage())!}</p>
                </div>
                <div class="flex">
                    <span class="text-lg font-bold py-1">Icon</span>
                    <div class="ml-8">
                        <label id="lblIcon" for="icon" class="flex items-center w-32 h-32 cursor-pointer">
                            <img id="iconImg" class="hidden border-solid border-2 w-full h-32" src="" />
                            <img id="defaultIconImg" class="border-solid border-2 w-full h-32" src="/images/icon.jpg" />
                        </label>
                        <input type="file" id="icon" name="icon" class="hidden" accept="image/png, image/jpeg">
                    </div>
                </div>
                <p id="errorIcon" class="h-3 text-right text-xs text-red-400">${(bindingResult.getFieldError("icon").getDefaultMessage())!}</p>
                <div class="flex justify-center items-center">
                    <input type="submit" id="btSubmit" class="text-xl font-bold mr-3 py-2 px-4 rounded-full" style="background-color: #ff5100;color: white"    value="Submit">
                    <a href="/" class="underline">Go back home</a>
                </div>
            </form>
        </div>

        <@defPage.footerDiv/>

        <script>
            $(document).ready(function () {
                $("input[name=icon]").change(function() {
                    let imgFile = this.files[0];
                    if(!imgFile){
                        $("#iconImg").hide();
                        $("#defaultIconImg").show();
                        return;
                    }

                    if(imgFile.size > 512000){
                        $("#errorIcon").text("Must be less than 500kb");
                        this.files = undefined;
                        $("#iconImg").hide();
                        $("#defaultIconImg").show();
                        return;
                    } else {
                        $("#errorIcon").text("");
                        $("#iconImg").attr("src",URL.createObjectURL(imgFile));
                        $("#iconImg").show();
                        $("#defaultIconImg").hide();
                    }
                });
            });
        </script>
    </body>
</html>