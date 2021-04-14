$(function () {
    COMMON.TableTools("interfaceLogTable", '/cnbm/intf/list', [
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
                return '<a href="#" onclick="viewLog(' + row.id + ')">' + value + '</a>';
            }
        },
        {
            field: "code",
            title: '编码',
            titleTooltip: '编码',
            align: 'center',
            sortable: true,
            width: '8%',
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
            sortable: true,
            width: '20%',
            order: 'desc'
        },
        {
            field: "outputName",
            title: '消费者名称',
            titleTooltip: '消费者系统名称',
            align: 'center',
            sortable: true,
            width: '12%',
            order: 'desc'
        },
        {
            field: "status",
            title: '状态',
            titleTooltip: '状态',
            align: 'center',
            sortable: true,
            width: '8%',
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
            width: '8%',
            align: 'center',
            formatter: function (value, row, index) {
                return '<button type="button" class="btn btn-gradient-primary btn-table-in btn-sm"   onclick="viewLog(' + row.id + ',\'' + row.name + '\')">日志查看</button>';
            }
        }],{
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
function queryInfo() {
    $('#interfaceLogTable').bootstrapTable("refresh");
}

function clearInfo() {
    $("#collapseExample input,select").val('');
}
function queryLogInfo() {
    $('#interfaceLogListTable').bootstrapTable("refresh");
}

function clearLogInfo() {
    $("#collapseLogInfo input,select").val('');
}
function viewLog(id, name) {
    $("#interfaceLogListModalLabel").text(name);
    $('#interfaceLogListView').off('show.bs.modal').on("show.bs.modal", function (e) {
        //查询接口日志信息
        COMMON.TableTools("interfaceLogListTable", '/cnbm/intflog/list?intfId=' + id, [
            {
                field: "status",
                titleTooltip: '整体状态',
                title: '状态',
                align: 'center',
                sortable: true,
                width: '8%',
                order: 'desc',
                formatter: function (value, row, index) {
                    var result = '<label class="badge badge-gradient-success">成功</label>';
                    if (value == '1') {
                        result = '<label class="badge badge-gradient-danger">失败</label>';
                    }
                    return result;
                }
            },
            {
                field: "inputStatus",
                titleTooltip: '生产者调用状态',
                title: '生产状态',
                align: 'center',
                sortable: true,
                width: '8%',
                order: 'desc',
                formatter: function (value, row, index) {
                    var result = '<label class="badge badge-gradient-success">成功</label>';
                    if (value == '1') {
                        result = '<label class="badge badge-gradient-danger">失败</label>';
                    }
                    return result;
                }
            },
            {
                field: "outputStatus",
                titleTooltip: '消费者调用状态',
                title: '消费状态',
                width: '8%',
                align: 'center',
                sortable: true,
                order: 'desc',
                formatter: function (value, row, index) {
                    var result = '<label class="badge badge-gradient-success">成功</label>';
                    if (value == '1') {
                        result = '<label class="badge badge-gradient-danger">失败</label>';
                    }
                    return result;
                }
            },
            {
                field: "createdDate",
                titleTooltip: '创建时间',
                title: '创建时间',
                align: 'center',
                width: '10%'
            },
            {
                field: "responseTimeExt",
                titleTooltip: '请求响应时间，\n从生产者请求开始计算，到消费者响应成功截至\n更新时间-创建时间,时间格式yyyy-MM-dd HH:mm:ss',
                title: '响应数',
                align: 'center',
                width: '6%',
                formatter: function (value, row, index) {
                    if (value) {
                        return value;
                    }
                    return "-";
                }
            },
            {
                field: "inputParams",
                title: '生产者调用参数',
                titleTooltip: '生产者调用参数',
                align: 'center',
                width: '14%',
                class: "longTextCell tipCtnt"
            },
            {
                field: "inputReturnVal",
                title: '生产者响应结果',
                titleTooltip: '生产者响应结果',
                align: 'center',
                width: '14%',
                class: "longTextCell tipCtnt"
            },
            {
                field: "outputParams",
                title: '消费者调用参数',
                titleTooltip: '消费者调用参数',
                align: 'center',
                width: '14%',
                class: "longTextCell tipCtnt"
            },
            {
                field: "failedReason",
                title: '失败原因',
                titleTooltip: '失败原因',
                width: '14%',
                align: 'center',
                class: "longTextCell tipCtnt"
            },
            {
                title: '操作',
                titleTooltip: '操作',
                width: '6%',
                align: 'center',
                formatter: function (value, row, index) {
                    return '<button type="button" class="btn btn-gradient-success btn-table-in btn-sm" onclick="viewSingleDetailLog(' + row.id + ',\'' + id + '\',\'' + name + '\')">明细</button>';
                }
            }],{
                queryParams: function (params) {
                    var dynParams = {
                        'inputParams': $('#query_inputParams').val(),
                        'inputReturnVal': $('#query_inputReturnVal').val(),
                        'outputParams': $('#query_outputParams').val(),
                        'status': $('#query_statusLog').val(),
                        'createdDate': $('#query_createdDate').val()
                    };
                    $.extend(params, dynParams);
                    return params;
                }
            })
    });
    $('#interfaceLogListView').modal('show');
    //关闭对话框后清除modal中数据
    $("#interfaceLogListView").off('show.bs.modal').off("hidden.bs.modal").on("hidden.bs.modal", function () {
        $("#interfaceLogListTable").bootstrapTable('removeAll');
        $(this).removeData("bs.modal");
    });
}

//查看明细
function viewSingleDetailLog(id, intfId, name) {
    logListHide();
    //打开明细前需要先隐藏日志列表
    $('#interfaceLogInfoView').off('show.bs.modal').on("show.bs.modal", function (e) {
        $.ajax({
            url: "/cnbm/intflog/get?id=" + id,
            type: "get",
            success: function (result) {
                $('#interfaceLogInfoView').modal('show')
                if (result) {
                    if (result.code === 0) {
                        var tempDoc = $;
                        var createdDate = '', updatedDate = '';
                        $.each(result.data, function (key, value) {
                            tempDoc("#cnbmIntfLog_" + key).val(value);
                            if (key == 'createdDate') {
                                createdDate = value;
                            }
                            if (key == 'updatedDate') {
                                updatedDate = value;
                            }
                        });
                    } else {
                        console.error("查询结果失败")
                    }
                } else {
                }
            },
            error: function () {
            }
        });
    })
    $('#interfaceLogInfoView').modal('show');
    //关闭对话框后清除modal中数据
    $("#interfaceLogInfoView").off('show.bs.modal').off("hidden.bs.modal").on("hidden.bs.modal", function () {
        $("#interfaceLogInfoView input,textarea,select").val('');
        $(this).removeData("bs.modal");
        //回显logList模态框
        viewLog(intfId, name)
    });
}

function logListHide() {
    //关闭对话框后清除modal中数据
    $("#interfaceManageNew").off('show.bs.modal').off("hidden.bs.modal").on("hidden.bs.modal", function () {
        $(this).removeData("bs.modal");
    });
    $('#interfaceManageNew').modal('hide');
}