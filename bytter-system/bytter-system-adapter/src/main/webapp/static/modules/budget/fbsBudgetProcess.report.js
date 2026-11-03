var dicts,tree;
bt.use(['form', 'treeTable', 'printType','dicts','toLigerTree'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.printType.init();
    dicts = bt.dicts.init();
    tree = bt.toLigerTree.init2({
        elem: '#appTable',
        newElem: '#maingrid1',
        rownumbers: true,//序号列数，默为无， 1代表左边第一列为序号列
        isDoubleHeader: true,//是否有双表头
        allowHideColumn: false,
        resize:false,
        height: "100%",
        fixedLeftColumns: 3,
        columnName:'itemCode',
        onAfterShowData:function(currentData){
            var level = dicts.get('level').val();
            for(var i=0;i<currentData.Rows.length;i++){
                var row=currentData.Rows[i];
                if(row.children && row.children.length>0){
                    if(level == 0 || level == ""){

                    }else if(level == 1){
                        this.collapse(row);
                        collapseChild(row.children,this);
                    }else if(level == 2 || level == 3){
                        var row2 = row.children;
                        for(var j = 0,len2 = row2.length;j< len2;j++){
                            if(row2[j].children && row2[j].children.length){
                                if(level == 2){
                                    this.collapse(row2[j]);
                                    collapseChild(row2[j].children,this);
                                }else if(level == 3){
                                    var row3 = row2[j].children;
                                    for(var k = 0,len3 = row3.length;k< len3;k++){
                                        if(row3[k].children && row3[k].children.length){
                                            this.collapse(row3[k]);
                                            collapseChild(row3[k].children,this);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    });
    function collapseChild(node,treeObj){
        for(var j = 0,len2 = node.length;j< len2;j++){
            if(node[j].children && node[j].children.length){
                treeObj.collapse(node[j]);
                var child = node[j].children;
                collapseChild(node[j].children,treeObj);
            }
        }
    }

    /**
     * 导出功能
     */
    exports = function () {
        var url = basePath + "/budget/fbsBudgetProcessReport/exportsBudgetProcess";
        window.location.href = url;
    };

    /**
     * 跳转到计划追加明细报表
     * @param el
     * @param status
     */
    openSuperadd = function (itemCode, status) {
        var statusFlag = "95" == status;
        var url = basePath + "/budget/fbsBudgetProcessReport/openSuperaddInfo";
        url += "?itemCode=" + itemCode + "&corpId=" + $("#eqs_corpId").val() + "&statusFlag=" + statusFlag;
        url += "&beginDate=" + $("#ged_beginDate").val() + "&endDate=" + $("#led_endDate").val() + "&timeType=" + $("#eqs_timeType").val();
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.search'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url,
            end: function () {
            }
        });
    }

    /**
     * 跳转到计划执行明细报表
     * @param el
     * @param status
     * @param innerSign
     */
    openProcess = function (itemCode, status, innerSign) {
        var statusFlag = "95" == status;
        var innerSignFlag = "1" == innerSign;
        var curId = $("#curId").val();
        var url = basePath + "/budget/fbsBudgetProcessReport/openProcessInfo";
        url += "?itemCode=" + itemCode + "&corpId=" + $("#eqs_corpId").val();
        url += "&statusFlag=" + statusFlag + "&innerSignFlag=" + innerSignFlag;
        url += "&beginDate=" + $("#ged_beginDate").val() + "&endDate=" + $("#led_endDate").val()+"&curId="+curId;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.search'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url,
            end: function () {
            }
        });
    }


    /**
     * 查看执行明细
     * @param budgetId
     */
    viewDtlProcess = function(budgetId) {
        if(budgetId===null||budgetId===""||budgetId===undefined){
            budgetId = "-1";
        }
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/budget/fbsBudgetProcessReport/viewDtlProcess/" + budgetId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['950px', '530px'],
            maxmin: true,
            content: basePath + "/budget/fbsBudgetProcessReport/viewDtlProcess/" + budgetId
        });
    }
});

var data;
chooseLevel = function () {
    var level = dicts.get('level').val();
    if(data == null) {
        data = tree.get('chooseLevel').getData();//获取数据的方法；
    }
    tree.get('chooseLevel').loadData();
};
