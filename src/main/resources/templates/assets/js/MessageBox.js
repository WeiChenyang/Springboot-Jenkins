function MessageBox(option) {
    option = option || {};
    var setting = {
        id: 'messageBox',
        msgType: 'error',
        ok: '确认',
        yes: '确认',
        no: '取消',
        close: '关闭',
        style: {
            'success': 'btn-outline-success',
            'confirm': 'btn-gradient-warning',
            'error': 'btn-outline-info'
        },
        successCall: function () {
        },
        cancelCall: function () {
        }
    };
    this.option = $.extend({}, setting, option);
    return this.show();
}
MessageBox.prototype.show = function () {
    var option = this.option;
    var _this = this;
    var width = this.option.width;
    var height = this.option.height;
    var dlgStyle = "";
    var hasWidth = width != null && width != "";
    var hasHeight = height != null && height != "";
    if(hasWidth || hasHeight){
        dlgStyle += ' style="';
        if(hasWidth){
            dlgStyle += 'width:' + width + ';';
        }
        if(hasHeight){
            dlgStyle += 'height:' + height + ';';
        }
        dlgStyle += '"';
    }
    var html = '';
    html += '<div id=' + option.id + ' class="modal fade" tabindex="-1" role="dialog" aria-labelledby="Label" aria-hidden="true">';
    html +=   '<div class="modal-dialog modal-dialog-centered modal-sm"' + dlgStyle + ' role="document">';
    html +=     '<div class="modal-content">';
    html +=       '<div class="modal-header">';
    html +=         '<h4 class="modal-title">' + option.title + '</h4>';
    html +=         '<button type="button" class="close" data-dismiss="modal" aria-label="' + option.close + '"><span aria-hidden="true"></span></button>';
    html +=       '</div>';
    html +=       '<div class="modal-body" style="padding:10px 10px 10px 10px;">';
    html +=         '<p>' + option.msg + '</p>';
    html +=       '</div>';
    html +=       '<div class="modal-footer" style="align-content: center">';
    if (option.msgType == 'confirm') {
        html +=     '<button id="' + option.id + '_confirm_yes" type="button" class="btn btn-success btn-sm">' + option.yes + '</button>';
        html +=     '<button id="' + option.id + '_confirm_cancel" type="button" class="btn btn-outline-dark btn-sm" data-dismiss="modal">' + option.no + '</button>';
    } else {
        html +=     '<button type="button" class="btn btn-success btn-sm" data-dismiss="modal">' + option.ok + '</button>';
    }
    html +=       '</div>';
    html +=     '</div>';
    html +=   '</div>';
    html += '</div>';
    $('body').append(html);
    $("#" + option.id + "_confirm_yes").click(function (e) {
        option.successCall();
        $('#' + option.id).modal('hide');
    });
    $("#" + option.id + "_confirm_cancel").click(function (e) {
        option.cancelCall();
        $('#' + option.id).modal('hide');
    });
    $('#' + option.id).on('hidden.bs.modal', function (e) {
        //option.cancelCall();
        _this.hide();
    })
    $('#' + option.id).modal('show');
};
MessageBox.prototype.hide = function () {
    if ($("#" + this.option.id)) {
        $("#" + this.option.id).remove();
    }
};


MessageBox.error = function (msg, successCall, width, height) {
    new MessageBox({
    	id: (new Date()).getTime() + "",
        msg: msg,
        msgType: 'error',
        title: '错误提示',
        width: width,
        height: height,
        successCall: (typeof successCall == 'function') ? successCall : undefined
    });
};
MessageBox.success = function (msg, successCall, width, height) {
    new MessageBox({
    	id: (new Date()).getTime() + "",
        msg: msg,
        title: '提示信息',
        msgType: 'success',
        width: width,
        height: height,
        successCall: (typeof successCall == 'function') ? successCall : undefined
    });
};
MessageBox.confirm = function (msg, successCall, cancelCall, width, height) {
    new MessageBox({
    	id: (new Date()).getTime() + "",
        msg: msg,
        msgType: 'confirm',
        title: '确认信息',
        width: width,
        height: height,
        successCall: (typeof successCall == 'function') ? successCall : undefined,
        cancelCall: (typeof cancelCall == 'function') ? cancelCall : undefined
    });
};

MessageBox.msgBox = function (data, successCall, width, height) {
    var msgType = data.code == "0" ? "success" : (data.code == "1" ? "error" : "confirm");
    var title = msgType == 'confirm' ? '确认信息' : (msgType == 'success' ? '提示信息' : '错误提示');
    new MessageBox({
    	id: (new Date()).getTime() + "",
        msg: data.message,
        msgType: msgType,
        title: title,
        width: width,
        height: height,
        successCall: (typeof successCall == 'function') ? successCall : undefined
    });
};
