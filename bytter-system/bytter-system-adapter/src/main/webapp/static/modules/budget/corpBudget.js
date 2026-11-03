layui.use(['form', 'jquery', 'treeTable', 'checkboxs'], function () {
    $ = layui.jquery;
    form = bt.form();
    bt.treeTable.init({elem: "#corp-table", column: 0});
    /**
     * 全选- 选择要计划上报的单位
     */
    form.on('checkbox(checkAllReportCorp)', function (data) {
        var checked = data.elem.checked;
        $("#corp-table").children("tbody").find("input[data-name='checkReport']").prop("checked", checked);
        $("#corp-table").children("tbody").find("input[data-name='checkReportAll']").prop("checked", checked);
        if (checked) {
            $("#corp-table").children("tbody").find("input[data-name='checkControl']").removeAttr("disabled");
            $("#corp-table").children("tbody").find("input[data-name='checkControlAll']").removeAttr("disabled");
        } else {
            $("#corp-table").children("tbody").find("input[data-name='checkControl']").prop("checked", checked);
            $("#corp-table").children("tbody").find("input[data-name='checkControlAll']").prop("checked", checked);
            $("#corp-table").children("tbody").find("input[data-name='checkControl']").prop("disabled", true);
            $("#corp-table").children("tbody").find("input[data-name='checkControlAll']").prop("disabled", true);
        }
        form.render("checkbox");
    });

    /**
     * 选择计划上报
     */
    form.on("checkbox(checkReportCorp)", function (data) {

        //控件左边的checkbox选中时，全部被选中或不选中
        var cid = $(data.elem).data("report-child-id");
        var checked = data.elem.checked;
        getCorp(cid, checked);
        //右边全选时，左边相应的要被全选中或不全不选中
        var pid = $(data.elem).data("report-parent-id");
        getNet(pid);

        // 根据上报勾选，禁用无上级单位的控制勾选
        if (!cid) {
            var id = $(data.elem).data("report-id");
            if (checked) {
                $("#corp-table").find("input[data-control-id ='" + id + "']").removeAttr("disabled");
            } else {
                $("#corp-table").find("input[data-control-id ='" + id + "']").prop("checked", checked);
                $("#corp-table ").find("input[data-control-id ='" + id + "']").prop("disabled", true);
            }
        }
        form.render("checkbox");
    });

    /**
     * 设置上报网点是否选中
     * @param cid
     */
    function getNet(pid) {
        if (pid) {
            var checkedboxsLen = $("#corp-table").find("input[data-report-parent-id ='" + pid + "']:checked").length;
            var checkboxsLen = $("#corp-table").find("input[data-report-parent-id ='" + pid + "']").length;
            var flag = checkedboxsLen === checkboxsLen;
            var checkboxschild = $("#corp-table ").find("input[data-report-child-id ='" + pid + "']");
            bt.each(checkboxschild, function () {
                $(this).prop("checked", flag);
                var pidchild = $(this).data("report-parent-id");
                if (pidchild) {
                    getNet(pidchild);
                }
            });
        }
    };

    /**
     * 设置上报单位是否选中
     * @param cid
     */
    function getCorp(cid, checked) {
        $("#corp-table").find("input[data-report-parent-id ='" + cid + "']").prop("checked", checked);
        // 根据上报勾选，禁用控制勾选
        if (checked) {
            $("#corp-table").find("input[data-control-parent-id ='" + cid + "']").removeAttr("disabled");
            $("#corp-table ").find("input[data-control-child-id ='" + cid + "']").removeAttr("disabled");
        } else {
            $("#corp-table").find("input[data-control-parent-id ='" + cid + "']").prop("checked", checked);
            $("#corp-table ").find("input[data-control-child-id ='" + cid + "']").prop("checked", checked);
            $("#corp-table").find("input[data-control-parent-id ='" + cid + "']").prop("disabled", true);
            $("#corp-table ").find("input[data-control-child-id ='" + cid + "']").prop("disabled", true);
        }
        // 存在下级
        var nextCidparentElems = $("#corp-table").find("input[data-report-parent-id ='" + cid + "']");

        bt.each(nextCidparentElems,function () {
            var nextCidparent = $(this).data("report-child-id");

            if (nextCidparent && nextCidparent.length) {
                getCorp(nextCidparent, checked);
            }

        })
    }

    /**
     * 全选- 选择要受计划控制的单位
     */
    form.on('checkbox(checkAllControlCorp)', function (data) {
        var checked = data.elem.checked;
        $("#corp-table").children("tbody").find("input[data-name='checkControl']").filter(":not([disabled])").prop("checked", checked);
        $("#corp-table").children("tbody").find("input[data-name='checkControlAll']").filter(":not([disabled])").prop("checked", checked);
        form.render("checkbox");
    });

    /**
     * 选择计划控制
     */
    form.on("checkbox(checkControlCorp)", function (data) {
        //控件左边的checkbox选中时，全部被选中或不选中
        var cid = $(data.elem).data("control-child-id");
        var checked = data.elem.checked;
        getControlCorp(cid, checked);
        //右边全选时，左边相应的要被全选中或不全不选中
        var pid = $(data.elem).data("control-parent-id");
        getControlNet(pid);
        form.render("checkbox")
    });

    /**
     * 设置控制网点是否选中
     * @param cid
     */
    function getControlNet(pid) {
        if (pid) {
            var checkedboxsLen = $("#corp-table").find("input[data-control-parent-id ='" + pid + "']:checked").length;
            var checkboxsLen = $("#corp-table").find("input[data-control-parent-id ='" + pid + "']").length;

            var checkboxschild = $("#corp-table").find("input[data-control-child-id ='" + pid + "']").filter(":not([disabled])");
            bt.each(checkboxschild, function () {
                if (checkedboxsLen === checkboxsLen) {
                    $(this).prop("checked", true);
                } else {
                    $(this).prop("checked", false);
                }
                var pidchild = $(this).data("control-parent-id");
                if (pidchild) {
                    getControlNet(pidchild);
                }
            });
        }
    };

    /**
     * 设置控制单位是否选中
     * @param cid
     */
    function getControlCorp(cid, checked) {
        $("#corp-table").find("input[data-control-parent-id ='" + cid + "']").prop("checked", checked);

        // 存在下级
        var nextCidparentElems = $("#corp-table").find("input[data-control-parent-id ='" + cid + "']");
        bt.each(nextCidparentElems,function () {
            var nextCidparent = $(this).data("control-child-id");
            if (nextCidparent && nextCidparent.length) {
                getControlCorp(nextCidparent, checked);
            }
        })
    }

    /**
     * 初始化
     */
    function init() {
        // 初始化上报勾选
        var checkReportAll = $("#corp-table").children("tbody").find("input[data-name='checkReportAll']");
        bt.each(checkReportAll, function () {
            var cid = $(this).data("report-child-id");
            var childCount = $(this).data("childcount");
            var checkedLen = $("#corp-table").find("input[data-report-parent-id ='" + cid + "']:checked").length;
            if (childCount === checkedLen && checkedLen != 0) {
                $(this).prop("checked", true);
            }
        });

        // 根据上报勾选，禁用控制勾选
        var checkNotParent = $("#corp-table").find("input[data-name='checkControl']").filter("[data-control-id]");
        bt.each(checkNotParent, function () {
            var id = $(this).data("control-id");
            var reportbudget = $(this).data("reportbudget");
            if (reportbudget == 0) {
                $("#corp-table").find("input[data-control-id ='" + id + "']").prop("checked", false);
                $("#corp-table").find("input[data-control-id ='" + id + "']").prop("disabled", true);
            }
        });
        // 初始化控制勾选
        var checkControlAll = $("#corp-table").children("tbody").find("input[data-name='checkControlAll']");
        bt.each(checkControlAll, function () {
            var cid = $(this).data("control-child-id");
            var childCount = $(this).data("childcount");
            var checkedLen = $("#corp-table").find("input[data-control-parent-id ='" + cid + "']:checked").length;
            if (childCount === checkedLen && checkedLen != 0) {
                $(this).prop("checked", true);
            }else {
                var disabledLen = $("#corp-table").find("input[data-control-parent-id ='" + cid + "']").filter("[disabled]").length;
                if(childCount === disabledLen && disabledLen != 0){
                    $(this).prop("checked", false);
                    $(this).prop("disabled", true);
                }
            }
        });

        form.render("checkbox")
    }

    init();

    /**
     * 查询
     */
    form.on('submit(seach)', function (data) {
        var seach = $("#seachShow").val();
        if(seach != ""){
            $("#seach").val(encodeURI(seach));
        } else {
            $("#seach").val("");
        }
        var action = basePath + "/budget/fbscorpbudget/queryList/1";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    });

    /**
     * 重置
     */
    resetCode = function () {
        $("#seachShow").val("");
        $("#seach").val("");
    }

    /**
     * 提交
     */
    form.on('submit(submit)', function (data) {
        var reportCorpIds = [];
        $('form').find('input[data-name="checkReport"]:checked').each(function () {
            reportCorpIds.push($(this).val());
        });
        if (reportCorpIds.length > 0) {
            data.field.reportCorpIds = reportCorpIds;
        } else {
            reportCorpIds.push("");
            data.field.reportCorpIds = reportCorpIds;
        }
        var controlCorpIds = [];
        $('form').find('input[data-name="checkControl"]:checked').each(function () {
            controlCorpIds.push($(this).val());
        });
        if (controlCorpIds.length > 0) {
            data.field.controlCorpIds = controlCorpIds;
        } else {
            controlCorpIds.push("");
            data.field.controlCorpIds = controlCorpIds;
        }
        var action = "/budget/fbscorpbudget/saveCtlBudget";
        $.post(basePath + action, data.field, function (result) {
            if (result.success) {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
            location.reload();
            parent.layer ? parent.layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            }) : layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            });
        }, 'JSON');
        return false;
    });


});