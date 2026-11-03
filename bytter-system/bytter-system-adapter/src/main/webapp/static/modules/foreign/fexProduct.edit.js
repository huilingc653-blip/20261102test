bt.use(['form', 'layedit', 'dicts', 'selectType','radioType'], function () {
    var $ = bt.jquery, form = bt.form();
    var selectType = bt.selectType.init();
    bt.radioType.init();
    form.render();
    form.renderIcon();



    //外汇期权品种回调函数
    bt.fexOption1Callback = function (data) {
        //外汇期权品种1 0-普通期权、1-奇异期权
        var fexOption1 = bt.dicts.get("fexOption1").val();
        if (fexOption1 == "1") {
            $("#fexOption2Div").show();
            bt.dicts.get('fexOption2').attr({'data-required': true});
            bt.dicts.get('fexOptionType').val(0);
            form.setReadonlyForRadio('#fexOptionTypeDiv',true);
        } else {
            form.setReadonlyForRadio('#fexOptionTypeDiv',false);
            $("#fexOption2Div").hide();
            bt.dicts.get('fexOption2').attr({'data-required': false});
            bt.dicts.get('fexOption2').val('');

            $("#fexOption3Div").hide();
            bt.dicts.get('fexOption3').attr({'data-required': false});
            bt.dicts.get('fexOption3').val('');

            $("#daysRuleDiv").hide();
            bt.dicts.get('daysRule').attr({'data-required': false});

            $("#validStandardDiv").hide();
            bt.dicts.get('validStandard').attr({'data-required': false});
        }
        $('#refundTimeTr').addClass('layui-hide');
        bt.dicts.get('refundTime').attr({'data-required': false});
        form.renderIcon();
    }
    bt.fexOption2Callback = function (data) {
        //外汇期权品种2 1-亚式期权、2-单障碍期权、3-双障碍期权
        var fexOption2 = bt.dicts.get("fexOption2").val();
        if(fexOption2 == "1") {
            //加权天数规则：1-亚式期权时才显示且必选
            $("#daysRuleDiv").show();
            bt.dicts.get('daysRule').attr({'data-required': true});
        }else{
            $("#daysRuleDiv").hide();
            bt.dicts.get('daysRule').attr({'data-required': false});
        }
        if (fexOption2 == "2" || fexOption2 == "3") {
            $("#fexOption3Div").show();
            bt.dicts.get('fexOption3').attr({'data-required': true});
            if(fexOption2 == "2") {
                bt.dicts.get('fexOption3').attr({'data-displayData': '1,2,3,4'});
            }else if(fexOption2 == "3"){
                bt.dicts.get('fexOption3').attr({'data-displayData': '5,6'});
            }
            //交易时障碍验证标准：2-单障碍期权和3-双障碍期权时才显示且必选
            $("#validStandardDiv").show();
            bt.dicts.get('validStandard').attr({'data-required': true});
        } else {
            $("#fexOption3Div").hide();
            bt.dicts.get('fexOption3').attr({'data-required': false});
            bt.dicts.get('fexOption3').val('');

            $("#validStandardDiv").hide();
            bt.dicts.get('validStandard').attr({'data-required': false});
        }
        $('#refundTimeTr').addClass('layui-hide');
        bt.dicts.get('refundTime').attr({'data-required': false});
        form.setReadonlyForRadio('#fexOptionTypeDiv',true);
        form.renderIcon();
    }
    bt.fexOption3Callback = function (data) {
        //外汇期权品种3 1-上涨生效单障碍期权(Up & In)、2-上涨失效单障碍期权(Up & Out)、3-下跌失效单障碍期权(Down & Out)、4-下跌生效单障碍期权(Down & In)、
        // 5-单触碰生效双障碍期权(Double In)、6-单触碰失效双障碍期权(Double Out)
        var fexOption3 = bt.dicts.get("fexOption3").val();
        if (fexOption3 == "2" || fexOption3 == "3") {
            //退款支付时间：2-上涨失效单障碍期权(Up & Out)、3-下跌失效单障碍期权(Down & Out) 必选
            $('#refundTimeTr').removeClass('layui-hide');
            bt.dicts.get('refundTime').attr({'data-required': true});
        }else{
            $('#refundTimeTr').addClass('layui-hide');
            bt.dicts.get('refundTime').attr({'data-required': false});
        }
        form.setReadonlyForRadio('#fexOptionTypeDiv',true);
        form.renderIcon();
    }
    bt.dicts.init();

    //初始化交割类型
    var fexTranTypeDefaultvalue = $('[data-name="fexTranType"]').data('defaultvalue');
    if(fexTranTypeDefaultvalue == '0'){
        $('[data-name="deliveryType"]').find('.layui-form-radio').eq(1).addClass('layui-hide');
    }
    if($("#fexOption1Def").val()=="1"){
        form.setReadonlyForRadio('#fexOptionTypeDiv',true);
    }else{
        form.setReadonlyForRadio('#fexOptionTypeDiv',false);
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        var tranType = $("#tranType").val();
        if(tranType == "4"){
            var nonWorkdayRule;
            // 复选框选中
            $.each($("input[type='checkbox']:checked"), function (i, item) {
                if ($(item).attr("value") != "all") {
                    if (null == nonWorkdayRule) {
                        nonWorkdayRule = $(item).attr("value");
                    } else {
                        nonWorkdayRule += "," + $(item).attr("value");
                    }
                }
            });
            $("#nonWorkdayRule").val(nonWorkdayRule);
            data.field.nonWorkdayRule = nonWorkdayRule;
        }
        //获取外汇交易类型
        var fexTranType = bt.dicts.get('fexTranType').val();
        if(fexTranType == 0){
            //即期
            //设置产品交易类型-外汇即期交易
            data.field.tranType= '7'
        }else if(fexTranType == 1){
            //远期
            //即期是否自动交割置空
            data.field.isAutoDelivery= ''
            //设置产品交易类型-外汇远期交易
            data.field.tranType= '8'
        }else if(fexTranType == 2){
            //掉期
            //即期是否自动交割置空
            data.field.isAutoDelivery= ''
            //设置产品交易类型-外汇掉期交易
            data.field.tranType= '9'
        }

        if(data.field.fexOption3 != "2" && data.field.fexOption3 != "3"){
            delete  data.field.refundTime
        }
        var action = "/foreign/fexproduct/modify";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

});
/**
 * 交割类型回调
 */
deliveryTypeCallBack = function (data) {
    if (data.value == "1") {
        $("#deliveryCurTR").show();
    } else {
        $("#deliveryCurTR").hide();
    }
}

/**
 * 外汇交易类型回调
 */
fexTranTypeCallBack = function (data) {
    bt.dicts.get('deliveryType').attr({'data-defaultvalue':0});
    if (data.value == "0") {
        //即期
        //展示即期是否自动交割、添加必填校验、添加默认值 1-是
        $("#isAutoDeliveryTr").show();
        bt.dicts.get('isAutoDelivery').attr({'data-required': true,'data-value':'1'});
        //隐藏近端
        $('.jinduan').addClass('layui-hide')
        //移除隐藏
        $('.original').removeClass('layui-hide')
        $("#deliveryCurTR").hide();
        bt.selectType.get('deliveryCurId').val('');
    } else if (data.value == "1") {
        //远期
        //隐藏即期是否自动交割、移除必填校验
        $("#isAutoDeliveryTr").hide();
        bt.dicts.get('isAutoDelivery').attr({'data-required': false});
        //隐藏近端
        $('.jinduan').addClass('layui-hide')
        //移除隐藏
        $('.original').removeClass('layui-hide')
        bt.selectType.get('deliveryCurId').val('');
    } else {
        //掉期
        //隐藏即期是否自动交割、移除必填校验
        $("#isAutoDeliveryTr").hide();
        bt.dicts.get('isAutoDelivery').attr({'data-required': false});
        //隐藏近端
        $('.jinduan').removeClass('layui-hide')
        //移除隐藏
        $('.original').addClass('layui-hide')
    }
    //设置交割类型只展示本金交割
    if (data.value == "0") {
        $('[data-name="deliveryType"]').find('.layui-form-radio').eq(1).addClass('layui-hide')
    }
}

function resetFn() {
    $('#fexOption2Div').hide();
    $('#fexOption3Div').hide();
    bt.dicts.get("fexOption1").val('');
}


function resetFnOption() {
    var fexOption1 = bt.dicts.get('fexOption1').attr('data-defaultvalue') || '';
    var fexOption2 = bt.dicts.get('fexOption2').attr('data-defaultvalue') || '';
    var fexOption3 = bt.dicts.get('fexOption3').attr('data-defaultvalue') || '';

    // 编辑页重置应恢复进入页面时的期权品种，并同步恢复联动区域。
    bt.dicts.get('fexOption1').val(fexOption1);
    bt.dicts.get('fexOption2').val(fexOption2);
    bt.dicts.get('fexOption3').val(fexOption3);
    bt.fexOption1Callback();
    bt.fexOption2Callback();
    bt.fexOption3Callback();
}

function resetFnForeign() {
    //即期产品重置后，交割类型仍只展示本金交割（隐藏无本金交割）
    var fexTranTypeDefaultvalue = $('[data-name="fexTranType"]').data('defaultvalue');
    if (fexTranTypeDefaultvalue == '0') {
        $('[data-name="deliveryType"]').find('.layui-form-radio').eq(1).addClass('layui-hide');
    }
}
