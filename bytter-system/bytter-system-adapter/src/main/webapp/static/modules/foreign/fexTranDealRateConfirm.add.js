bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','element', 'selectTable','dateType'], function(){
    var $ = bt.jquery,form = bt.form();

    //汇率确认日期回调函数
    deliveryDateCallback = function (data, $elem){
        renderBudget();
    };

    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var selectType = bt.selectType.init();
    bt.rateType.init();
    bt.dicts.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        data.field.status = $(this).attr("value");

        //判断是否挂接资金计划
        var settleMoney = moneyFun.toNumber(moneyType.get("settleMoney").val());
        if ($("input[name='sumMoney']").length) {
            var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
            // 校验金额与资金计划总金额一致
            if (sumMoney != settleMoney) {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.budgetMoney"), {icon: 2});
                return false;
            }
        }

        var action = "/foreign/fextranreq/saveDeal";
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
                billId: $("#tranReqId").val(),
                businessName: $("#businessName").val(),
                processSysname: $("#processSysname").val(),
                processDate: $("#deliveryDate").val(),
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
                processDate: dateType.get("deliveryDate").val(),
                type: 'select',
                detailControl: true,
                curId: $("#curId").val(),
                wishPayday: dateType.get("deliveryDate").val(),
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

    renderBudget();

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
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
    if(!$("#id").val()) {
        bt.moneyType.get("settleMoney").val(data);
    }
}