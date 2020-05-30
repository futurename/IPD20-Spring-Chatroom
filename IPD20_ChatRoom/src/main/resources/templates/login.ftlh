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
        <link rel="shortcut icon" href="images/favicon.ico"/>

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
        <div class=" block flex justify-center">
            <div class="my-5 w-1/6">
                <span class="text-3xl font-bold mb-5 inline-block w-full text-center">Login</span><br>
                <form method="post" action="/messages">
                    <i class="fas fa-envelope"></i>
                    <label for="email" class="text-lg font-bold py-1">Email</label><br>
                    <input type="email" id="email" class="border-solid border-2 w-full mb-3"><br>
                    <i class="fas fa-unlock-alt"></i>
                    <label for="pass" class="text-lg font-bold py-1">Password</label><br>

                    <input type="password" id="pass" class="border-solid border-2 w-full mb-3"><br>

                    <div class="flex justify-center">
                        <input type="submit" id="btSubmit" class="text-xl w-full font-bold py-2 text-center mt-2 mb-6
                        rounded-full" style="background-color: #ff5100;color: white"    value="Submit">
                    </div>
                </form>
            </div>
        </div>

        <@defPage.footerDiv/>

        <script>
            $(document).ready(function () {

            })
        </script>

    </body>
</html>