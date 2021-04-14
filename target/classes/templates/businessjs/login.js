$(function () {


});

function login() {
    $.ajax({
        url: "/cnbm/checkLogin",
        data: {name: $("#name").val(), password: $("#password").val()},
        type: "post",
        success: function (result) {
            if (result.isFlag) {
                $(location).attr('href', '/cnbm/');
                var name = $("#name").val();
                localStorage.setItem('log_info', JSON.stringify({'flag': result.isAdmin, 'name': name}))
            }
        },
        error: function () {
        }
    });
}

function logout() {
    $.ajax({
        url: "/cnbm/logout",
        type: "post",
        success: function (result) {
            if (result.isFlag) {
                $(location).attr('href', '/cnbm/');
                var name = $("#name").val();
                localStorage.setItem('log_info', JSON.stringify({'flag': result.isAdmin, 'name': name}))
            }
        },
        error: function () {
        }
    });
}