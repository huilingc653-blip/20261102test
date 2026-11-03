bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','element', 'selectTable','dateType','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var selectType = bt.selectType.init();
    bt.rateType.init();
    bt.dicts.init();
    bt.corp.init();

    //结算金额回调函数
    bt.settleMoneyCallback = function (data) {
        renderBudget();
    }
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //交易地点
    if($("#tranAddr")) {
        var tranAddrInit = bt.selectTable.init({
            elem: "#tranAddrTable",
            url: basePath + "/basic/business/syscountrycust/listAllSysCountryArea",
            searchInput: "#tranAddr",
            scrollX: "900px",
            scrollY: "300px",
            columns: [
                {data: "code",},
                {data: "nameZh",},
                {data: "nameEn",}
            ],
            clickCallback: function (data, row, inputElem) {
                inputElem.val(data.nameZh);
                $('#tranAddrId').val(data.id);
            },
        });
    }

    //结算方式为0-现金，行权时，挂接资金计划
    if($("#opType").val() == "3" && $("#tranSettleMethod").val()=="0"){
        renderBudget();
    }
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        data.field.status = $(this).attr("value");
        var action = "/foreign/fextranreq/saveOptionDeal";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParentTable();
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });

    //币别回调函数
    bt.curCallback = function (data) {
        //加载资金计划
        renderBudget();
    };

    /**
     * 计划明细控制项
     */
    var dtlColSels = [];
    if ($("#dtlColSel").val()) {
        bt.each($("#dtlColSel").val().split(","), function (i, col) {
            dtlColSels[col] = col;
        });
    }

    /**
     * 生成公共的属性
     * @return
     */
     function $genOtherHtml() {
        var url = "";
        var param = {};
        if($("#id").val()) {
            url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
            param = {
                corpId: $('input[name="corpId"]').val(),
                billId: $("#id").val(),
                businessName: $("#businessName").val(),
                processSysname: $("#processSysname").val(),
                processDate: $("#opDate").val(),
                type: 'select',
                detailControl: true,
                curId: $("#curId").val(),
                date: (new Date()).getTime()
            };
        }else{
            url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
            param = {
                corpId: $('input[name="corpId"]').val(),
                itemCode: $('input[name="payItem"]').val() ? $('input[name="payItem"]').val() : "400",
                processDate: dateType.get("opDate").val(),
                type: 'select',
                detailControl: true,
                curId: selectType.get("curId").val(),
                wishPayday: dateType.get("opDate").val(),
                date: (new Date()).getTime()
            };
        }
        $.ajax({
            url: url,
            method: 'POST',
            data: param,
            async: true,
            success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
            }
        });
    };

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        var settleMoney = moneyType.get('settleMoney').val();
        if (budgetSel == "1"  && settleMoney && settleMoney < 0) {
            $genOtherHtml();
        } else {
            $("#tagItemDetail").html("");
        }
    }
});

/**
 * 计划执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("settleMoney").val("-"+data);
}