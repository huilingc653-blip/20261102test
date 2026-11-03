var selectType, page, dateType, corp;
bt.use(['printType'], function () {
    bt.printType.init();
})

/**
 * 打印函数 - 横向打印
 */
function print() {
    var printObj = bt.lodop.init();
    setTimeout(function () {
        LODOP = printObj.lodop;//获取lodop打印对象
        if (!LODOP) {
            return;
        }
        var style = '<link rel="stylesheet" href="' + basePath + '/static/plugins/layui/css/layui.css" media="all" />';
        style += '<link rel="stylesheet" href="' + basePath + '/static/css/common.css" />';
        style += '<style>'
            + ' table.layui-table{border:1px solid #000!important;}'
            + ' table th {border:1px solid #000!important; height:20px;font-size: 12px!important;font-weight: 400!important;}'
            + ' table td {border:1px solid #000!important; font-size: 12px!important; line-height: 15px!important;min-height: 20px!important;}'
            + ' .center {text-align:center} .textLeft{text-align:left;} .left{text-align:left;}  .textRight{text-align:right;} .right{text-align:right;}table.layui-table *{font-family: "宋体"}'
            + ' </style>';
        var size = $("#size").val();
        for(var i=1;i<=size;i++) {
            LODOP.NewPageA();
            var allhtml = $("#print_"+i).html();
            var html = '<head>' + style + '</head><body>' + allhtml + '</body>';
            // 表格数据
            LODOP.ADD_PRINT_TABLE('30mm', '0mm', 'RightMargin:0mm', "BottomMargin:9mm", html);

            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:90%");
            // 表头 取网点名称
            LODOP.ADD_PRINT_TEXT('2mm', '50%', '794', 100, $('#netName_'+i).val());
            LODOP.SET_PRINT_STYLEA(0,"LinkedItem",9*(i-1)+1);
            LODOP.SET_PRINT_STYLEA(9*(i-1)+2, "FontSize", 20);
            LODOP.SET_PRINT_STYLEA(9*(i-1)+2, "FontName", "宋体");
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // LODOP.ADD_PRINT_HTM('8mm', '50%', "<font style='font-size:15px'><span>" + bt.lang("table.accountStatement.tableTitle") + "</span></font>");
            LODOP.ADD_PRINT_TEXT('15mm', '50%', '200', 100, bt.lang("table.accountStatement.tableTitle"));
            LODOP.SET_PRINT_STYLEA(0,"LinkedItem",9*(i-1)+1);
            LODOP.SET_PRINT_STYLEA(9*(i-1)+3, "FontSize", 16);
            LODOP.SET_PRINT_STYLEA(9*(i-1)+3, "FontName", "宋体");
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 表头信息
            LODOP.ADD_PRINT_TEXT('25mm', '20mm', '25%', 100, $("#accountShowContainerElement_"+i).html());   // 账号
            LODOP.SET_PRINT_STYLEA(0,"LinkedItem",9*(i-1)+1);
            LODOP.SET_PRINT_STYLEA(9*(i-1)+4, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('25mm', '90mm', '25%', 100, $("#accNameShowContainerElement_"+i).html());   // 户名
            LODOP.SET_PRINT_STYLEA(0,"LinkedItem",9*(i-1)+1);
            LODOP.SET_PRINT_STYLEA(9*(i-1)+5, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('25mm', '160mm', '25%', 100, $("#yesterBalanceShowContainerElement_"+i).html());  // 期初余额
            LODOP.SET_PRINT_STYLEA(0,"LinkedItem",9*(i-1)+1);
            LODOP.SET_PRINT_STYLEA(9*(i-1)+6, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('25mm', '240mm', '25%', 100, $("#curNameShowContainerElement_"+i).html());      // 币别
            LODOP.SET_PRINT_STYLEA(0,"LinkedItem",9*(i-1)+1);
            LODOP.SET_PRINT_STYLEA(9*(i-1)+7, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 制表日期
            LODOP.ADD_PRINT_HTM('97%', '3%', 300, 120, "<font style='font-size:12px' format='0'><span>" + bt.lang('com.table.makeDate') + ':' + $("#curDate").val() + "</span></font>");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            // 分页显示
            LODOP.ADD_PRINT_HTM('97%', '88%', 300, 100, "<font style='font-size:12px' format='0'><span tdata='pageNO'>第##页</span>/<span tdata='pageCount'>共##页</span></font>");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
        }
        LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT","100%");
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