layui.use(['form','jquery','treeTable','checkboxs'], function(){
    $ = layui.jquery;form = bt.form();
    bt.treeTable.init({elem:"#corp-table",column:0});
    /**
     * 全选- 选择要受计划控制的单位
     */
    form.on('checkbox(checkAllCollectCorp)', function (data) {
        var checked = data.elem.checked;
        $("#corp-table").children("tbody").find("input[data-name='checkCollect']").filter(":not([disabled])").prop("checked", checked);
        $("#corp-table").children("tbody").find("input[data-name='checkCollectAll']").filter(":not([disabled])").prop("checked", checked);
        form.render("checkbox");
    });

    /**
     * 选择计划控制
     */
    form.on("checkbox(checkCollectCorp)", function (data) {
        //控件左边的checkbox选中时，全部被选中或不选中
        var cid = $(data.elem).data("collect-child-id");
        var checked = data.elem.checked;
        getControlCorp(cid, checked);
        //右边全选时，左边相应的要被全选中或不全不选中
        var pid = $(data.elem).data("collect-parent-id");
        getControlNet(pid);
        form.render("checkbox")
    });

    /**
     * 设置控制网点是否选中
     * @param cid
     */
    function getControlNet(pid) {
        if (pid) {
            var checkedboxsLen = $("#corp-table").find("input[data-collect-parent-id ='" + pid + "']:checked").length;
            var checkboxsLen = $("#corp-table").find("input[data-collect-parent-id ='" + pid + "']").length;

            var checkboxschild = $("#corp-table ").find("input[data-collect-child-id ='" + pid + "']");
            bt.each(checkboxschild, function () {
                if(checkedboxsLen === checkboxsLen){
                    $(this).prop("checked", true);
                }else {
                    $(this).prop("checked", false);
                }
                var pidchild = $(this).data("collect-parent-id");
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
        $("#corp-table").find("input[data-collect-parent-id ='" + cid + "']").filter(":not([disabled])").prop("checked", checked);
        var nextCidparentElems = $("#corp-table").find("input[data-collect-parent-id ='" + cid + "']");
        bt.each(nextCidparentElems,function () {
            var cidparent = $(this).data("collect-child-id");
            if (cidparent && cidparent.length) {
                getControlCorp(cidparent, checked)
            }
        })
    }

    function init(){
        // 根据上报勾选，禁用无parentID的控制勾选
        var checkNotParent = $("#corp-table").find("input[data-name='checkCollect']").filter("[data-collect-id]");
        bt.each(checkNotParent, function () {
            var id = $(this).data("collect-id");
            var controlBudget = $(this).data("controlbudget");
            if (controlBudget == 0) {
                $("#corp-table").find("input[data-collect-id ='" + id + "']").prop("checked", false);
                $("#corp-table").find("input[data-collect-id ='" + id + "']").prop("disabled", true);
            }
        });

        var checkReportAll = $("#corp-table").children("tbody").find("input[data-name='checkCollectAll']");
        bt.each(checkReportAll, function () {
            var cid = $(this).data("collect-child-id");
            var childCount = $(this).data("childcount");
            var checkedLen = $("#corp-table").find("input[data-collect-parent-id ='" + cid + "']:checked").length;
            if(childCount === checkedLen && checkedLen != 0){
                $(this).prop("checked", true);
            }else {
                var disabledLen = $("#corp-table").find("input[data-collect-parent-id ='" + cid + "']").filter("[disabled]").length;
                if(childCount === disabledLen && disabledLen != 0){
                    $(this).prop("checked", false);
                    $(this).prop("disabled", true);
                }
            }
        });
        form.render("checkbox");
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
        var action = basePath + "/budget/fbscorpbudget/queryList/2";
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

    //监听提交
    form.on('submit(submit)', function(data) {
        var corpIds = [];
        $('form').find('input[data-name="checkCollect"]:checked').each(function () {
            corpIds.push($(this).val());
        });
        if(corpIds.length > 0){
            data.field.corpIds = corpIds;
        }else{
             corpIds.push("");
            data.field.corpIds = corpIds;
        }
        var action = "/budget/fbscorpbudget/saveCollectFlag";
        $.post(basePath + action, data.field, function(result) {
            if (result.success) {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
            location.reload();
            parent.layer ? parent.layer.alert(result.msg, {
                icon : result.success ? 1 : 2
            }) : layer.alert(result.msg, {
                icon : result.success ? 1 : 2
            });
        }, 'JSON');
        return false;
    });

    /**
     * 初始化零预算
     * @param corpId
     */
    initZeroBudget = function (corpId){
        $.post(basePath + "/budget/fbscorpbudget/initZeroBudget/" + corpId, function(result){
            location.reload(true);
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    }
});