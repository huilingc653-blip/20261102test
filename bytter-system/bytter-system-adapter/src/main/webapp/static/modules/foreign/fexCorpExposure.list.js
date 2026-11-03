layui.use(['form', 'jquery', 'treeTable', 'checkboxs', 'dicts'], function () {
    $ = layui.jquery;
    form = bt.form();
    bt.treeTable.init({elem: "#corp-table", column: 0});
    var dicts = bt.dicts.init();
    /**
     * 全选- 选择要敞口汇总的单位
     */
    form.on('checkbox(checkAllCollect)', function (data) {
        var checked = data.elem.checked;
        $("#corp-table").children("tbody").find("input[data-name='checkCollect']").prop("checked", checked);
        $("#corp-table").children("tbody").find("input[data-name='checkCollectAll']").prop("checked", checked);
        form.render("checkbox");
    });

    /**
     * 选择敞口汇总
     */
    form.on("checkbox(checkIsCollect)", function (data) {
        //控件左边的checkbox选中时，全部被选中或不选中
        var cid = $(data.elem).data("collect-child-id");
        var checked = data.elem.checked;
        getCollectCorp(cid, checked);
        //右边全选时，左边相应的要被全选中或不全不选中
        var pid = $(data.elem).data("collect-parent-id");
        getCollectNet(pid);
        form.render("checkbox");
    });

    /**
     * 设置上报网点是否选中
     * @param cid
     */
    function getCollectNet(pid) {
        if (pid) {
            var checkedboxsLen = $("#corp-table").find("input[data-collect-parent-id ='" + pid + "']:checked").length;
            var checkboxsLen = $("#corp-table").find("input[data-collect-parent-id ='" + pid + "']").length;
            var flag = checkedboxsLen === checkboxsLen;
            var checkboxschild = $("#corp-table ").find("input[data-collect-child-id ='" + pid + "']");
            bt.each(checkboxschild, function () {
                $(this).prop("checked", flag);
                var pidchild = $(this).data("collect-parent-id");
                if (pidchild) {
                    getCollectNet(pidchild);
                }
            });
        }
    };

    /**
     * 设置上报单位是否选中
     * @param cid
     */
    function getCollectCorp(cid, checked) {
        $("#corp-table").find("input[data-collect-parent-id ='" + cid + "']").prop("checked", checked);
        // 存在下级
        var nextCidparentElems = $("#corp-table").find("input[data-collect-parent-id ='" + cid + "']");

        bt.each(nextCidparentElems,function () {
            var nextCidparent = $(this).data("collect-child-id");

            if (nextCidparent && nextCidparent.length) {
                getCollectCorp(nextCidparent, checked);
            }

        })
    }

    /**
     * 全选- 层级内是否自然对冲
     */
    form.on('checkbox(checkAllNaturalHedge)', function (data) {
        var checked = data.elem.checked;
        $("#corp-table").children("tbody").find("input[data-name='checkNaturalHedge']").filter(":not([disabled])").prop("checked", checked);
        $("#corp-table").children("tbody").find("input[data-name='checkNaturalHedgeAll']").filter(":not([disabled])").prop("checked", checked);
        form.render("checkbox");
    });

    /**
     * 选择层级内是否自然对冲
     */
    form.on("checkbox(checkIsNaturalHedge)", function (data) {
        //控件左边的checkbox选中时，全部被选中或不选中
        var cid = $(data.elem).data("naturalhedge-child-id");
        var checked = data.elem.checked;
        getNaturalHedgeCorp(cid, checked);
        //右边全选时，左边相应的要被全选中或不全不选中
        var pid = $(data.elem).data("naturalhedge-parent-id");
        getNaturalHedgeNet(pid);
        form.render("checkbox")
    });

    /**
     * 设置控制网点是否选中
     * @param cid
     */
    function getNaturalHedgeNet(pid) {
        if (pid) {
            var checkedboxsLen = $("#corp-table").find("input[data-naturalhedge-parent-id ='" + pid + "']:checked").length;
            var checkboxsLen = $("#corp-table").find("input[data-naturalhedge-parent-id ='" + pid + "']").length;
            var flag = checkedboxsLen === checkboxsLen;
            var checkboxschild = $("#corp-table ").find("input[data-naturalhedge-child-id ='" + pid + "']");
            bt.each(checkboxschild, function () {
                $(this).prop("checked", flag);
                var pidchild = $(this).data("naturalhedge-parent-id");
                if (pidchild) {
                    getNaturalHedgeNet(pidchild);
                }
            });
        }
    };

    /**
     * 设置控制单位是否选中
     * @param cid
     */
    function getNaturalHedgeCorp(cid, checked) {
        $("#corp-table").find("input[data-naturalhedge-parent-id ='" + cid + "']").filter(":not([disabled])").prop("checked", checked);
        // 存在下级
        var nextCidparentElems = $("#corp-table").find("input[data-naturalhedge-parent-id ='" + cid + "']");

        bt.each(nextCidparentElems,function () {
            var nextCidparent = $(this).data("naturalhedge-child-id");

            if (nextCidparent && nextCidparent.length) {
                getNaturalHedgeCorp(nextCidparent, checked);
            }

        })
    }

    /**
     * 初始化
     */
    function init() {
        // 初始化汇总单位勾选
        var checkCollectAll = $("#corp-table").children("tbody").find("input[data-name='checkCollectAll']");
        bt.each(checkCollectAll, function () {
            var cid = $(this).data("collect-child-id");
            var childCount = $(this).data("childcount");
            var checkedLen = $("#corp-table").find("input[data-collect-parent-id ='" + cid + "']:checked").length;
            if (childCount === checkedLen && checkedLen != 0) {
                $(this).prop("checked", true);
            }
        });

        // 初始化自然对冲单位勾选
        var checkNaturalHedgeAll = $("#corp-table").children("tbody").find("input[data-name='checkNaturalHedgeAll']");
        bt.each(checkNaturalHedgeAll, function () {
            var cid = $(this).data("naturalhedge-child-id");
            var childCount = $(this).data("childcount");
            var checkedLen = $("#corp-table").find("input[data-naturalhedge-parent-id ='" + cid + "']:checked").length;
            if (childCount === checkedLen && checkedLen != 0) {
                $(this).prop("checked", true);
            }else {
                var disabledLen = $("#corp-table").find("input[data-naturalhedge-parent-id ='" + cid + "']").filter("[disabled]").length;
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
    searchSubmit = function () {
        var search = $("#searchShow").val();
        if(search != ""){
            $("#search").val(encodeURI(search));
        } else {
            $("#search").val("");
        }
        var action = basePath + "/foreign/fexCorpExposure/queryList";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    }

    /**
     * 重置
     */
    resetCode = function () {
        $("#searchShow").val("");
        $("#search").val("");
    }

    /**
     * 提交
     */
    form.on('submit(submit)', function (data) {
        var collectCorpIds = [];
        $('form').find('input[data-name="checkCollect"]:checked').each(function () {
            collectCorpIds.push($(this).val());
        });
        if (collectCorpIds.length > 0) {
            data.field.collectCorpIds = collectCorpIds;
        } else {
            collectCorpIds.push("");
            data.field.collectCorpIds = collectCorpIds;
        }
        var naturalHedgeCorpIds = [];
        $('form').find('input[data-name="checkNaturalHedge"]:checked').each(function () {
            naturalHedgeCorpIds.push($(this).val());
        });
        if (naturalHedgeCorpIds.length > 0) {
            data.field.naturalHedgeCorpIds = naturalHedgeCorpIds;
        } else {
            naturalHedgeCorpIds.push("");
            data.field.naturalHedgeCorpIds = naturalHedgeCorpIds;
        }
        data.field.orgType = "0";
        var action = "/foreign/fexCorpExposure/saveCorpExposure";
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