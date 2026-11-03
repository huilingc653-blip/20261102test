bt.use(['form', 'corp', 'dateType', 'selectType', 'dicts', 'treeTable','freezeTable'], function () {
    $ = bt.jquery, form = bt.form();

    //设置默认值
    var defaulShowMoney = $('[data-name="showMoney"]').data("defaultvalue");
    var defaulShowItem = $('[data-name="showItem"]').data("defaultvalue");
    if (defaulShowMoney ==''  || (defaulShowMoney && defaulShowMoney.indexOf("all") !== -1)){
        $('[data-name="showMoney"]').data("defaultvalue","all");
    }

    if (defaulShowItem ==''){
        $('[data-name="showItem"]').data("defaultvalue","0");
    }
    var height = $(window).height();
    var corpTableHeight = height - 200;
    bt.treeTable.init({elem: ".treeTable", column: 0});
    var corp = bt.corp.init();
    //树表显示
    $("#corp-table").show();
    var dateType = bt.dateType.init();
    var selectType = bt.selectType.init();
    var dicts = bt.dicts.init();

    if ($("#corp-table").find("tbody tr")[0]){
        //开始表格冻结功能
        bt.freezeTable.init({
            elem:'#corp-table',//表格id
            freezeRowNum:'0',//冻结几行
            freezeColumnNum:'0',//冻结几列
            height:corpTableHeight +'px' //表格高度
        })
    }

    //全选
    form.on('checkbox(checkallcorp)', function (data) {
        var checked = data.elem.checked;
        var checkboxs = $("#corp-table").children("tbody").find("input[type='checkbox']");
        bt.each(checkboxs, function () {
            $(this).prop("checked", checked)
        })
        form.render("checkbox")
    });

    form.on("checkbox(checkAll)", function (data) {
        var checked = data.elem.checked;
        //右边全选时，左边相应的要被全选中或不全不选中
        var pid = $(data.elem).data("all-parent-id");
        getNet(pid, checked, 'all');
        form.render("checkbox");
    });

    form.on("checkbox(checkDirect)", function (data) {
        var checked = data.elem.checked;
        //右边全选时，左边相应的要被全选中或不全不选中
        var pid = $(data.elem).data("direct-parent-id");
        getNet(pid, checked, 'direct');
        form.render("checkbox");
    });

    form.on("checkbox(checkLeaf)", function (data) {
        var checked = data.elem.checked;
        //右边全选时，左边相应的要被全选中或不全不选中
        var pid = $(data.elem).data("leaf-parent-id");
        getNet(pid, checked, 'leaf');
        form.render("checkbox");
    });

    function getNet(pid, checked, type) {
        if (pid) {
            if ("all" == type) {
                $("#corp-table ").find("input[value='" + pid + "']").prop("checked", checked);
            }
            // 勾选下方
            bt.each($("#corp-table ").find("input[data-" + type + "-parent-id ='" + pid + "']"), function () {
                $(this).prop("checked", checked);
            });
            // 勾选左侧
            bt.each($("#corp-table ").find("input[data-parent-id ='" + pid + "']"), function () {
                var pidchild = $("#corp-table ").find("input[data-parent-id ='" + $(this).val() + "']");
                if ("all" == type) {
                    $(this).prop("checked", checked);
                    if (pidchild.length) {
                        getNet($(this).val(), checked, type);
                    }
                } else if ("direct" == type) {
                    $(this).prop("checked", checked);
                } else if ("leaf" == type) {
                    if (pidchild.length) {
                        getNet($(this).val(), checked, type);
                    } else {
                        $(this).prop("checked", checked);
                    }
                }
            });
        }
    };

    /**
     * 查询-生成报表
     * @returns {boolean}
     */
    seach = function () {
        // 获取单位
        var corpIds = "";
        bt.each($("#corp-table ").find("input[name=\"corpIds\"]:checked"), function () {
            if (corpIds == "") {
                corpIds += $(this).val();
            } else {
                corpIds += "," + $(this).val();
            }
        });
        if ("" === corpIds) {
            parent.layer.alert(bt.lang("fbs.budgetReport.message.corp.null"), {icon: 2});
            return false;
        }
        // 获取金额显示方式
        var showMoney = "";
        bt.each(dicts.get("showMoney").val(), function (index, item) {
            if (showMoney == "") {
                showMoney += item;
            } else {
                showMoney += "," + item;
            }
        });
        if ("" === showMoney) {
            parent.layer.alert(bt.lang("fbs.budgetReport.message.showMoney.null"), {icon: 2});
            return false;
        }
        $("#corpIds").val(corpIds);
        var url = basePath + "/budget/fbsbudgetreport/seachMergeFbsBudgetTemp";
        url += "?timeType=" + selectType.get("timeType").val();
        url += "&seachDate=" + dateType.get("beginDate").val() + "&showMoney=" + showMoney;
        url += "&showItem=" + dicts.get("showItem").val();
        if(corpIds.length<50){
            url += "&corpId="+corpIds;
        }
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.search'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url
        });
    }

    /**
     * 查询
     * @returns {boolean}
     */
    query = function () {
        // 网点
        if (corp.get("netId").val()=="") {
            parent.layer.alert(bt.lang("table.fcReportFormat.firstSelectNet"), {icon: 2});
            return false;
        }else{
            var action =basePath + "/budget/fbsbudgetreport/mergeQueryByNetId";
            $("#searchForm").attr("action",action);
            $("#searchForm").submit();
        }
    }
});
