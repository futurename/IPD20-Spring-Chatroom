<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Register page</title>

        <!-- tailwind css -->
        <link type="text/css" rel="stylesheet" href="/css/app.css">

        <!-- favicon -->
        <link rel="shortcut icon" href="images/live-chat-orange.jpg"/>

        <!-- Awesome Icons -->
        <link rel="stylesheet" href="/css/all.min.css">

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
        <div class=" block flex justify-center">
            <div class="my-5">
                <span class="text-2xl font-bold mb-4 inline-block">Create your own free account</span><br>
                <form method="post" action="/messages">
                    <label for="name" class="text-lg font-bold py-1">Name</label><br>
                    <input type="text" id="name" class="border-solid border-2 w-full mb-3"><br>
                    <label for="email" class="text-lg font-bold py-1">Email</label><br>
                    <input type="email" id="email" class="border-solid border-2 w-full mb-3"><br>
                    <label for="pass1" class="text-lg font-bold py-1">Password</label><br>
                    <input type="password" id="pass1" class="border-solid border-2 w-full mb-3"><br>
                    <label for="pass2" class="text-lg font-bold py-1">Password Again</label><br>
                    <input type="password" id="pass2" class="border-solid border-2 w-full mb-3"><br>
                    <div class="flex justify-center">
                        <input type="submit" id="btSubmit" class="text-lg font-bold bg-blue-400 py-2 px-4 mt-2 mb-6 rounded-full "
                               value="Submit">
                    </div>

                </form>
            </div>
        </div>

        <@defPage.footerDiv/>

        <script>
            $(document).ready(function () {
               /* $("#btSubmit").on("click", function () {
                    $.ajax({
                        type: "POST",
                        url: "/messages",
                        dataType: "json",
                        data: JSON.stringify({}),
                        success: function (data, textStatus, jQxhr) {

                        },
                        error: function (jqxhr, status, errorThrown) {

                        }
                    })
                })*/
            })
        </script>

    </body>
</html>