$(function () {
    COMMON.TableTools("interfaceTable", '/cnbm/intf/list', [
        {
            field: "checkbox",
            checkbox: true,
            title: '选择',
            titleTooltip: '选择',
            width: '3%',
            align: 'center'
        },
        {
            field: "name",
            title: '名称',
            titleTooltip: '名称',
            align: 'center',
            sortable: true,
            width: '12%',
            order: 'desc',
            formatter: function (value, row, index) {
                return '<a href="#" onclick="editView(' + row.id + ')">' + value + '</a>';
            }
        },
        {
            field: "code",
            title: '编码',
            titleTooltip: '编码',
            align: 'center',
            width: '8%',
            sortable: true,
            order: 'desc'
        },
        {
            field: "inputName",
            title: '生产者名称',
            titleTooltip: '生产者系统名称',
            align: 'center',
            sortable: true,
            width: '12%',
            order: 'desc'
        },
        {
            field: "inputUrl",
            title: '生产者地址',
            titleTooltip: '生产者系统地址',
            align: 'center',
            width: '25%',
            sortable: true,
            order: 'desc'
        },
        {
            field: "outputName",
            title: '消费者名称',
            titleTooltip: '消费者系统名称',
            align: 'center',
            width: '12%',
            sortable: true,
            order: 'desc'
        },
        {
            field: "isRetry",
            title: '是否重试',
            titleTooltip: '是否重试',
            align: 'center',
            width: '8%',
            sortable: true,
            order: 'desc',
            formatter: function (value, row, index) {
                var result = '<label class="badge badge-gradient-success">是</label>';
                if (value == '1') {
                    result = '<label class="badge badge-gradient-info">否</label>';
                }
                return result;
            }
        },
        {
            field: "status",
            title: '状态',
            titleTooltip: '状态',
            align: 'center',
            width: '8%',
            sortable: true,
            order: 'desc',
            formatter: function (value, row, index) {
                var result = '<label class="badge badge-gradient-success">启用</label>';
                if (value == '1') {
                    result = '<label class="badge badge-gradient-danger">禁用</label>';
                }
                return result;
            }
        },
        {
            title: '操作',
            titleTooltip: '操作',
            width: '10%',
            align: 'center',
            formatter: function (value, row, index) {
                return '<button type="button" class="btn btn-gradient-success btn-sm"  style="margin-left: 5px" onclick="editView(' + row.id + ')">修改</button>' +
                    '<button type="button" class="btn btn-gradient-danger btn-sm" style="margin-left: 5px" onclick="del(' + row.id + ')">删除</button>';
            }
        }
    ], {
        toolbar: '#interfaceTableToolbar',
        queryParams: function (params) {
            var dynParams = {
                'code': $('#query_code').val(),
                'name': $('#query_name').val(),
                'inputName': $('#query_inputName').val(),
                'status': $('#query_status').val(),
                'outputName': $('#query_outputName').val()
            };
            $.extend(params, dynParams);
            return params;
        }
    })
});

//新建
function interfaceSave() {
    //参数校验
    var code = $("#cnbmIntf_code").val();
    var msg = "";
    if (!COMMON.CheckIsNullOrEmptyStr(code)) {
        msg += "接口编码不能为空\n";
    }
    var cnbmIntf_name = $("#cnbmIntf_name").val();
    if (!COMMON.CheckIsNullOrEmptyStr(cnbmIntf_name)) {
        msg += "接口名称不能为空\n";
    }
    var cnbmIntf_inputName = $("#cnbmIntf_inputName").val();
    if (!COMMON.CheckIsNullOrEmptyStr(cnbmIntf_inputName)) {
        msg += "生产者名称不能为空\n";
    }
    var cnbmIntf_inputUrl = $("#cnbmIntf_inputUrl").val();
    if (!COMMON.CheckIsNullOrEmptyStr(cnbmIntf_inputUrl)) {
        msg += "生产者地址不能为空\n";
    }
    var cnbmIntf_inputMethodName = $("#cnbmIntf_inputMethodName").val();
    if (!COMMON.CheckIsNullOrEmptyStr(cnbmIntf_inputMethodName)) {
        msg += "生产者方法不能为空\n";
    }
    var cnbmIntf_outputName = $("#cnbmIntf_outputName").val();
    if (!COMMON.CheckIsNullOrEmptyStr(cnbmIntf_outputName)) {
        msg += "消费者名称不能为空\n";
    }
    var cnbmIntf_inputParamFormat = $("#cnbmIntf_inputParamFormat").val();
    if (!COMMON.CheckIsNullOrEmptyStr(cnbmIntf_inputParamFormat)) {
        msg += "生产者参数格式不能为空\n";
    }
    var cnbmIntf_outputFormat = $("#cnbmIntf_outputFormat").val();
    if (!COMMON.CheckIsNullOrEmptyStr(cnbmIntf_outputFormat)) {
        msg += "消费者名称不能为空\n";
    }
    if(COMMON.CheckIsNullOrEmptyStr(msg)){
        MessageBox.error(msg);
        return;
    }
    $.ajax({
        url: "/cnbm/intf/saveOrUpdate",
        data: $("#interfaceForm").serialize(),
        type: "post",
        success: function (result) {
            $('#interfaceManageNew').modal('hide')
            if (result) {
                if (result.code === 0) {
                    $('#interfaceTable').bootstrapTable("refresh");
                } else {
                    MessageBox.error(result.msg);
                }
            } else {
            }
        },
        error: function () {
            $('#interfaceManageNew').modal('hide')
        }
    });
}

function newView() {
    $('#interfaceManageNew').modal('show');
    //关闭对话框后清除modal中数据
    $("#interfaceManageNew").off("hidden.bs.modal").on("hidden.bs.modal", function () {
        $('#interfaceForm')[0].reset();
    });
}

function editView(id) {
    if (id) {
        $('#interfaceManageNew').off('show.bs.modal').on("show.bs.modal", function (e) {
            $.ajax({
                url: "/cnbm/intf/get?id=" + id,
                type: "get",
                success: function (result) {
                    if (result) {
                        if (result.code === 0) {
                            var tempDoc = $;
                            $.each(result.data, function (key, value) {
                                tempDoc("#cnbmIntf_" + key).val(value);
                            });
                        } else {
                        }
                    } else {
                    }
                },
                error: function () {
                }
            });
        })
    }
    $('#interfaceManageNew').modal('show');
    //关闭对话框后清除modal中数据
    $("#interfaceManageNew").off('show.bs.modal').off("hidden.bs.modal").on("hidden.bs.modal", function () {
        $('#interfaceForm')[0].reset();
    });
}

function del(id) {
    var ids = [];
    var selected = $('#interfaceTable').bootstrapTable("getAllSelections");
    if (selected instanceof Array) {
        for (var tempId in selected) {
            ids.push(selected[tempId]['id']);
        }
    }
    if (id) {
        ids.push(id);
    }
    MessageBox.confirm("确认是否删除该记录？", function () {
        $.ajax({
            url: "/cnbm/intf/delete",
            type: "post",
            data: {ids: ids},
            success: function (result) {
                if (result) {
                    if (result.code === 0) {
                        $('#interfaceTable').bootstrapTable("refresh");
                    } else {
                    }
                } else {
                }
            },
            error: function () {
            }
        });
    })
}

function queryInfo() {
    $('#interfaceTable').bootstrapTable("refresh");
}

function clearInfo() {
    $("#collapseExample input,select").val('');
}