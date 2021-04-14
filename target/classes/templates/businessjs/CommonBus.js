(function (win) {
    if (!win.COMMON) {
        win.COMMON = {
            getUserInfo: function(){
                return JSON.parse(localStorage.getItem('log_info'))
            },
            isMobile: function () {
                var mobile = false;
                try {
                    mobile = /Android|webOS|iPhone|iPad|iPod|pocket|psp|kindle|avantgo|blazer|midori|Tablet|Palm|maemo|plucker|phone|BlackBerry|symbian|IEMobile|mobile|ZuneWP7|Windows Phone|Opera Mini/i.test(navigator.userAgent);
                } catch (e) {
                    console.info(e);
                }
                return mobile;
            },
            //字符串时间格式化后Date
            formatDate: function (strDate) {
                return new Date(strDate.replace(/-/g, "/"))
            },
            //计算时间戳之间的差值
            DateDiff: function (startTime, endTime) {
                return (this.formatDate(endTime) - this.formatDate(startTime)) / 1000;
            },
            //判断数据是否为
            CheckIsNullOrEmpty: function (arg1) {
                return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
            },
            //判断数据是否为Null或者undefined或者为空字符串
            CheckIsNullOrEmptyStr: function (value) {
                //正则表达式用于判斷字符串是否全部由空格或换行符组成
                var reg = /^\s*$/
                //返回值为true表示不是空字符串
                return (value != null && value != undefined && !reg.test(value))
            },
            //table公共方法，简单的封装了一下。
            TableTools: function (tableId, url, columns, params) {
                var $table = $('#' + tableId);
                // //加入时间戳
                // if (!this.CheckIsNullOrEmpty(url)) {
                //     if (url.indexOf("?") > 0) {
                //         url = url + "&time=" + new Date().getTime();
                //     } else {
                //         url = url + "?time=" + new Date().getTime();
                //     }
                // }
                var option = {
                    method: "get",
                    url: url,
                    cache: false,
                    showToggle: true,
                    cardView: COMMON.isMobile(),
                    pagination: true, //启动分页
                    pageSize: 10, //每页显示的记录数
                    pageNumber: 1, //当前第几页
                    pageList: [10, 20, 30, 50, 100], //记录数可选列表
                    showColumns: true,
                    striped: true,                      //是否显示行间隔色
                    sortable: true,
                    sortOrder: 'desc',
                    showRefresh: true,
                    clickToSelect: true,                //是否启用点击选中行
                    uniqueId: "id",                     //每一行的唯一标识，一般为主键列
                    buttonsPrefix: 'btn btn-gradient-primary btn-sm',//覆盖原有样式
                    minimumCountColumns: 3,//最少显示列数，默认1列
                    sidePagination: "server", //表示服务端请求
                    paginationHAlign: "left",
                    ajax: function ajaxRequest(request) {
                        $.ajax({
                            type: request.type,
                            url: request.url,
                            timeout: 0,
                            data: request.data,
                            cache: request.cache,
                            success: function (result, status, xhr) {
                                var obj = {};
                                obj.total = result.data.totalCount;
                                obj.rows = result.data.list;
                                request.success(obj);
                            },
                            error: function (xhr, status, err) {
                                request.error({status: status, resp: xhr});
                            }
                        });
                    },
                    columns: columns
                };
                if (!this.CheckIsNullOrEmpty(params)) {
                    $.extend(option, params);
                }
                $table.bootstrapTable('destroy').bootstrapTable(option);
                $table.on("post-body.bs.table", function (e) {
                    $(this).find("td.tipCtnt").each(function (i, td) {
                        var $td = $(td);
                        $td.attr("title", $td.text());
                    });
                });
            }
        }
    }
})(window);