$(function(){
    $("#regist").click(toRegistPage);
    $('#username').focus(function(){$('#unamestatus').html("&nbsp;");});
    $('#password').focus(function(){$('#passstatus').html("&nbsp;");});
    $('#captcha').focus(function(){$('#captchastatus').html("&nbsp;");});
    //$('#doLogin').click(doLogin);
    $("#forgetpass").click(forgetPass);
    $('#captchaimg').click(function () {
        $(this).hide().attr('src', '/user/captcha?bg=1&deep=' + new Date().getTime()).fadeIn();
    });
    function toRegistPage(){
        window.location='/user/regist';
    }
    //function doLogin(){
    //    var uname = trim($('#username').val());
    //    var pass = trim($('#password').val());
    //    var captcha = trim($('#captcha').val());
    //    if(checkLogin()){
    //        $.ajax({
    //            type: "GET",
    //            url: "/user/login",
    //            //async:false,
    //            data:{username:uname,password:pass,captcha:captcha},
    //            success: function (resp) {
    //                console.info(resp);
    //                resp = eval("(" + resp + ")");
    //                if(resp.loginAlert!='ok'){
    //                    $('#loginAlert').html(resp.loginAlert);
    //                }
    //            }
    //        });
    //    }
    //}
    function isExist(){
        var uname = trim($('#username').val());
        if(uname!=''){
            $.ajax({
                type: "GET",
                url: "/user/exist",
                async:true,
                data:{username:uname},
                success: function (resp) {
                    resp = eval("(" + resp + ")");
                    if(resp.msg!=undefined&&typeof(resp.msg)!=undefined){
                        $('#unamestatus').html(resp.msg);
                    }else{
                        $('#unamestatus').html("&nbsp;");
                    }
                }
            });
        }
    }
    function forgetPass(){window.location='/user/forgetPass';}
});