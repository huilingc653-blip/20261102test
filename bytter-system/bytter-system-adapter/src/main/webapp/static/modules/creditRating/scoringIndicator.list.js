var dict;
bt.use(['jquery', 'treeTable', 'form', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    dict = bt.dicts.init();
    bt.treeTable.init({elem: "#scoringIndicatorTable", column: 1});

    $getId = function (currnet) {
        var id = currnet.parents('tr').data('tt-id');
        return id ? id : "";
    }

    $getIsEffect = function (currnet) {
        return currnet.siblings('input').val() || "";
    }

    $("#exportExecl").click(function () {
        var scoringIndicatorCode = $("#scoringIndicatorCode").val();
        var scoringIndicatorName = $("#scoringIndicatorName").val();
        var status = dict.get("status").val();

        var url = basePath + "/creditRating/crScoringIndicator/exportData";
        DownLoadFile({url: url, data: {scoringIndicatorCode:scoringIndicatorCode,scoringIndicatorName:scoringIndicatorName,status:status}, method: 'post'});
        return false;
    })

    var DownLoadFile = function (options) {
        var config = $.extend(true, {method: 'post'}, options);
       // var $iframe = $('<iframe id="down-file-iframe" />');
        var $form = $('<form  target="_self" method="' + config.method + '" />');
        $form.attr("action", config.url);
        for (var key in config.data) {
            $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
        }
        //$iframe.append($form);

        $form.appendTo('body');
        $form.submit();

        $form.remove();
    }
    /**
     * 复制
     */
    $("[name='copy']").click(function () {
        var id = $getId($(this));
        //删除该指标类别会删除其下所有子指标类别，是否删除？
        //是否删除该指标
        bt.loading.show()
        var url = basePath + "/creditRating/crScoringIndicator/copy/" + id;
        $.post(url, function (result) {
            if (result.success) {
                bt.loading.hide()
                location.reload();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
    /**
     * 新增/新增下一级
     */
    $("[name='add']").click(function () {
        var parentId = $getId($(this));
        var isEffect = $getIsEffect($(this));
        if (isEffect === 0) {
            parent.layer.alert("该指标类别为无效，不能新增下一级！", {icon: 2});
            return;
        }
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('credit.scoring.add'), basePath + "/creditRating/crScoringIndicator/add?parentId=" + parentId);
            return;
        }
        parent.layer.open({
            title: "#springMessage('credit.scoring.add')",
            type: 2,
            area: ['850px', '630px'],
            maxmin: true,
            content: basePath + "/creditRating/crScoringIndicator/add?parentId=" + parentId,
            end: function () {
            }
        });
    });

    /**
     * 编辑
     */
    $("[name='edit']").click(function () {
        var id = $getId($(this));
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('credit.scoring.update'), basePath + '/creditRating/crScoringIndicator/edit?id=' + id);
            return;
        }
        parent.layer.open({
            title: "#springMessage('credit.scoring.update')",
            type: 2,
            area: ['850px', '630px'],
            maxmin: true,
            content: basePath + '/creditRating/crScoringIndicator/edit?id=' + id,
            end: function () {
            }
        });
    });

    /**
     * 查看
     */
    $("[name='view']").click(function () {
        var id = $getId($(this));
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('credit.scoring.view'), basePath + "/creditRating/crScoringIndicator/view/" + id);
            return;
        }
        parent.layer.open({
            title: "#springMessage('credit.scoring.view')",
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/creditRating/crScoringIndicator/view/" + id,
        });
    });
    /**
     * 删除
     */
    $("[name='del']").click(function () {
        var id = $getId($(this));
        //是否删除该指标?
        var msg = bt.lang("credit.indicator.delete.yOrn");
        //删除该指标类别会删除其下所有子指标类别，是否删除？
        $.post(basePath + "/creditRating/crScoringIndicator/adjustScoringId/" + id, function (result) {
            if (result.success) {
                if (result.attributes.isNotUsedModel) {
                    //该指标已挂接其它模型是否删除?
                    msg = bt.lang("credit.indicator.delete.hitch");
                }
                if (result.attributes.isNotParent) {
                    //删除该指标类别会删除其下所有子指标类别,是否删除?
                    msg = bt.lang("credit.indicator.delete.all");
                }
            }
            //是否删除该指标
            parent.layer.confirm(msg, function () {
                var url = basePath + "/creditRating/crScoringIndicator/delete/" + id;
                $.post(url, function (result) {
                    if (result.success) {
                        location.reload();
                    }
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            });
        }, 'JSON');
    });


    $("[name='importInit']").click(function () {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('finacial.paramConfig.fcSubject.title.importInit'), basePath + "/creditRating/scoringIndicator/importSubject");
            return;
        }
        parent.layer.open({
            title: "#springMessage('finacial.paramConfig.fcSubject.title.importInit')",
            type: 2,
            area: ['1000px', '630px'],
            maxmin: true,
            content: basePath + "/creditRating/scoringIndicator/importSubject",
            end: function () {
            }
        });
    });
    // 打印按钮点击事件
    $("#printBt").click(function () {
        print();
    });

    resetCallback = function () {
        $("#scoringIndicatorCode").val("");
        $("#scoringIndicatorName").val("");
        dict.get("status").val("")
    }
});

/**
 * 打印函数 - 横向打印
 */
function print() {
    setTimeout(function () {
        var LODOP = getLodop();//获取lodop打印对象
        if (!LODOP) {
            return;
        }

        var style = '<link rel="stylesheet" href="' + basePath + '/static/plugins/layui/css/layui.css" media="all" />';
        style += '<link rel="stylesheet" href="' + basePath + '/static/css/common.css" />';
        style += '<style>'
            + ' table.layui-table{border:1px solid #000!important;width: 80%}'
            + ' table th {border:1px solid #000!important; height:20px;font-size: 12px!important;font-weight: 400!important;}'
            + ' table td {border:1px solid #000!important; font-size: 12px!important; line-height: 15px!important;min-height: 20px!important;}'
            + ' .center {text-align:center} .textLeft{text-align:left;} .left{text-align:left;}  .textRight{text-align:right;} .right{text-align:right;}table.layui-table *{font-family: "宋体"}'
            + ' </style>';
		// 获取包含table的div元素
		var div = document.getElementById('print');

		// 获取修改后的HTML内容
		var allhtml = div.innerHTML;
        var html = '<head>' + style + '</head><body>' + allhtml + '</body>';
        // 表头 取网点名称
        LODOP.ADD_PRINT_TEXT('2mm', '50%', '794', 100, $('#netName').val());
        LODOP.SET_PRINT_STYLEA(0, "FontSize", 20);
        LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
        // 文本框里 内容对于文本框居中
        LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
        // 打印项在纸张中水平居中
        LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
        // 每一页都打印
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

        // 文本框里 内容对于文本框居中
        LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
        // 打印项在纸张中水平居中
        LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
        // 每一页都打印
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);


        // 表格数据
        LODOP.ADD_PRINT_TABLE('30mm', '0mm', 'RightMargin:0mm', "BottomMargin:9mm", html);
        LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");

        // 分页显示
        LODOP.ADD_PRINT_HTM('97%', '88%', 300, 100, "<font style='font-size:12px' format='0'><span tdata='pageNO'>第##页</span>/<span tdata='pageCount'>共##页</span></font>");
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

        LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Auto-Width");
        // 2---横向打印，固定纸张；
        LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
        // 横向时的正向显示
        LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED", 1);
        // 设置可以重新选择纸张大小
        LODOP.SET_PRINT_MODE("RESELECT_PAGESIZE", 1);
        // 设置可以重新打印方向
        LODOP.SET_PRINT_MODE("RESELECT_ORIENT", 1);

        LODOP.PREVIEW();
    }, 800);
}