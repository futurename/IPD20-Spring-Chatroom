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

        <div class=" block flex justify-center">
            <div class="my-5">
                <span class="text-3xl font-bold mb-5 inline-block">Create Free Account</span>
                <form method="post" action="/register" enctype="multipart/form-data">
                    <div>
                        <i class="fas fa-user"></i>
                        <label for="name" class="text-lg font-bold py-1">Name</label>
                        <input type="text" id="name" name="name" value="${(userForm.name)!}" class="border-solid border-2 w-full mb-3">
                        <p class="-mt-3 h-3 text-right text-xs text-red-400">${(bindingResult.getFieldError("name").getDefaultMessage())!}</p>
                    </div>
                    <div>
                        <i class="fas fa-envelope"></i>
                        <label for="email" class="text-lg font-bold py-1">Email</label>
                        <input type="email" id="email" name="email" value="${(userForm.email)!}" class="border-solid border-2 w-full mb-3">
                        <p class="-mt-3 h-3 text-right text-xs text-red-400">${(bindingResult.getFieldError("email").getDefaultMessage())!}</p>
                    </div>
                    <div>
                        <i class="fas fa-unlock-alt"></i>
                        <label for="pass1" class="text-lg font-bold py-1">Password</label>
                        <input type="password" id="pass1" name="pass1" class="border-solid border-2 w-full mb-3">
                        <p class="-mt-3 h-3 text-right text-xs text-red-400">${(bindingResult.getFieldError("pass1").getDefaultMessage())!}</p>
                    </div>
                    <div>
                        <i class="fas fa-unlock-alt"></i>
                        <label for="pass2" class="text-lg font-bold py-1">Password Again</label>
                        <input type="password" id="pass2" name="pass2" class="border-solid border-2 w-full mb-3">
                        <p class="-mt-3 h-3 text-right text-xs text-red-400">${(bindingResult.getGlobalError().getDefaultMessage())!}</p>
                    </div>
                    <div class="flex">
                        <div>
                            <i class="fas fa-icons"></i>
                            <span class="text-lg font-bold py-1">Icon</span>
                        </div>
                        <div class="ml-8">
                            <label id="lblIcon" for="icon" class="flex items-center w-32 h-32 cursor-pointer">
                                <img id="iconImg" class="hidden border-solid border-2 w-full h-32" src="" />
                                <img id="defaultIconImg" class="border-solid border-2 w-full h-32" src="/images/icon.jpg" />
                            </label>
                            <input type="file" id="icon" name="icon" class="hidden" accept="image/png, image/jpeg">
                        </div>
                    </div>
                    <p id="errorIcon" class="h-3 text-right text-xs text-red-400">${(bindingResult.getFieldError("icon").getDefaultMessage())!}</p>
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