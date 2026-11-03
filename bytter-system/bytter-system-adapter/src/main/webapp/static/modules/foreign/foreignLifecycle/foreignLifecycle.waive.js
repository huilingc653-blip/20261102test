var moneyType,moneyFun;
bt.use(['form', 'rateType', 'moneyType','dateType','selectTable'], function () {
  var $ = bt.jquery, form = bt.form();

  bt.rateType.init();
  bt.dateType.init();
  // 第一行操作金额回调
  bt.firstMoneyCallback = function(data) {
    var firstMoney = data;
    var busDirection = $("#busDirection").val();//买卖方向
    var updownDirection = $("#updownDirection").val();//涨跌方向
    var contractRate = $("#contractRate").val();//执行汇率
    var secondMoney = 0;
    //如果货币对相等
    if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
      secondMoney = moneyFun.mul(firstMoney,contractRate);
    } else {
      secondMoney = moneyFun.div(firstMoney,contractRate,2);
    }
    $("#opMoney").val(firstMoney);
    if (("1" == busDirection && "0" == updownDirection) || ("2" == busDirection && "1" == updownDirection)) {
      moneyType.get("sellMoney").val(secondMoney);
    } else {
      moneyType.get("buyMoney").val(secondMoney);
    }

    calculateBal(firstMoney);
  }
  // 第二行操作金额回调
  bt.secondMoneyCallback = function(data) {
    var secondMoney = data;
    var busDirection = $("#busDirection").val();
    var updownDirection = $("#updownDirection").val();//涨跌方向
    var contractRate = $("#contractRate").val();
    var firstMoney = 0;
    if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
      firstMoney = moneyFun.div(secondMoney,contractRate,6);
    } else {
      firstMoney = moneyFun.mul(secondMoney,contractRate);
    }
    $("#opMoney").val(firstMoney);
    if (("1" == busDirection && "0" == updownDirection) || ("2" == busDirection && "1" == updownDirection)) {
      moneyType.get("buyMoney").val(firstMoney);
    } else {
      moneyType.get("sellMoney").val(firstMoney);
    }
    calculateBal(firstMoney);
  }

  moneyType = bt.moneyType.init();
  moneyFun = bt.moneyType.getFun();

  //监听提交
  form.on('submit(submit)', function (data) {
    bt.loading.show();
    //$("#status").val($(this).attr("value"));
    data.field.lifeStatus = $(this).attr("value");



    var submitData = data.field;
    var action = "/foreign/fexTradingLifecycle/updateLifecycle";
    $.ajax({
      "url": basePath + action,
      data: JSON.stringify(submitData),
      type:"post",
      dataType:"json",
      contentType: "application/json",
      async: false,
      success:function (result) {
        bt.loading.hide();
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
      }
    });
    return false;
  });

})

function calculateBal(firstMoney){
  var positionMoney = $("#positionMoney").val();
  var busDirection = $("#busDirection").val();
  var updownDirection = $("#updownDirection").val();//涨跌方向
  var firstBal = moneyFun.sub(positionMoney,firstMoney);
  var contractRate = $("#contractRate").val();
  var secondBal = 0;
  if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
    secondBal = moneyFun.mul(firstBal,contractRate);
  } else {
    secondBal = moneyFun.div(firstBal,contractRate,6);
  }
  $("#firstBal").val(firstBal);
  $("#secondBal").val(secondBal);
  if (("1" == busDirection && "0" == updownDirection) || ("2" == busDirection && "1" == updownDirection)) {
    moneyType.get("buyBal").val(firstBal);
    moneyType.get("sellBal").val(secondBal);
  } else {
    moneyType.get("buyBal").val(secondBal);
    moneyType.get("sellBal").val(firstBal);
  }

}

/**
 * 查看交易详情
 * @param id
 */
function viewFexTranReq(id) {
  if (bt.navtab) {
    bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/view/" + id);
    return;
  }
  layer.open({
    type: 2,
    area: ['850px', '530px'],
    maxmin: true,
    content: basePath + "/foreign/fextranreq/view/" + id
  });
}