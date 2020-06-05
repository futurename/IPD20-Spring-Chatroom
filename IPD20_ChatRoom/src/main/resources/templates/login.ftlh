<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Login page</title>

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
    <body >

        <#import "layout/defaultLayout.ftlh" as defPage>
        <@defPage.headerDiv/>

        <div class="max-w-2xl mx-auto flex justify-center h-full">
            <div class="my-5">
                <span class="text-3xl font-bold mb-5 inline-block w-full text-center">Login</span><br>

                <#if error >
                    <p class="text-sm text-red-400">Invalid confidential.</p>
                </#if>
                <form method="post" action="/login" class="mb-6">
                    <div>
                        <div>
                            <i class="fas fa-envelope"></i>
                            <label for="email" class="text-lg font-bold py-1">Email</label>
                        </div>
                        <input type="email" id="email" name="email" value="jerry@jerry.com" class="border-solid border-2 w-full mb-3">
                    </div>
                    <div>
                        <div>
                            <i class="fas fa-unlock-alt"></i>
                            <label for="pass" class="text-lg font-bold py-1">Password</label><br>
                        </div>
                        <input type="password" id="pass" name="password" value="jerry" class="border-solid border-2 w-full mb-3">
                    </div>

                    <div class="flex justify-center items-center">
                        <button type="submit" id="btSubmit" class="text-xl text-white w-full font-bold mr-3 py-2 text-center
                        rounded-full" style="background-color: #ff5100">Submit</button>
                        <a href="/register" class="underline">Register</a>
                    </div>
                </form>
            </div>
        </div>
        <@defPage.footerDiv/>
    </body>
</html>