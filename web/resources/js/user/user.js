$(function(){
    $('#doRegist').click(doRegist);
    $('#doReset').click(doReset);
    $('#username').focus(function(){$('#usernameerr').html("&nbsp;");}).blur(isExist);
    $('#password').focus(function(){$('#passworderr').html("&nbsp;");});
    $('#capthca').focus(function(){$('#capthcaerr').html("&nbsp;");});
    $('#captchaimg').click(function () {
        $(this).hide().attr('src', '/user/captcha?bg=1&deep=' + new Date().getTime()).fadeIn();
    });
    function goLogin(){
        window.location='/login/index';
    }
    function doRegist(){
        if(checkForm()){
            var uname = $('#username').val();
            var pass = $('#password').val();
            var captcha = $('#captcha').val();
            $.ajax({
                type: "GET",
                url: "/user/doRegist",
                async:false,
                data:{username:uname,password:pass,captcha:captcha},
                success: function (resp) {
                    resp = eval("(" + resp + ")");
                    if(resp.success){
                        $('#createstatus').html(resp.msg+".<a href='javascript:void(0)' id='goLogin'>Go login?</a>");
                        $('#goLogin').click(goLogin);
                        doReset();
                    }else{
                        if(resp.msg!=undefined && typeof(resp.msg)!=undefined){
                            $('#createstatus').html(resp.msg+".<a href='javascript:void(0)' id='goLogin'>Go login?</a>");
                            $('#goLogin').click(goLogin);
                        }else{
                            $('#captchaerr').html(resp.captcha);
                        }
                    }

                }
            });
        }
    }
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
                    if(resp.isexist){
                        $('#usernameerr').html(resp.msg);
                    }else{
                        $('#usernameerr').html(resp.msg);
                    }

                }
            });
        }
    }
    function checkForm(){
        var uname = $('#username').val();
        var pass = $('#password').val();
        var captcha = $('#captcha').val();
        var checked = true;
        $('#usernameerr').html("&nbsp;");
        $('#passworderr').html("&nbsp;");
        $('#captchaerr').html("&nbsp;");
        $('#createstatus').html("&nbsp;");
        if(trim(uname)==""){$('#usernameerr').html("Username can't be empty.");checked = false;}
        if(trim(pass)==""){$('#passworderr').html("Password can't be empty.");checked = false;}
        if(trim(captcha)==""){$('#captchaerr').html("Captcha can't be empty.");checked = false;}
        return checked;
    }
    function doReset(){
        $('#username').val('');
        $('#password').val('');
        $('#captcha').val('');
    }
})
