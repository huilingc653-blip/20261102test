var selectType,moneyType;
bt.use(['form', 'rateType', 'moneyType','dateType','selectType', 'selectTable','laydate','date'], function () {
  var $ = bt.jquery, form = bt.form();

  //开始时间
  var $start = $('#opDate');
  var start = bt.laydate.render({
    elem:$start[0],
    type:'date',
    value:$start.val(),
    min:$start.data('min') || '1900-01-01 00:00:00',
    max:$start.data('max') || '2099-12-31 23:59:59',
    trigger:'click',
    btns: [ 'clear','confirm'],
    lang:window.top.webLocale?(window.top.webLocale == 'en_US'?'en':'cn'):'cn',
    ready:function(date){
    },
    done:function(value, date,endDate){
      if(!date.year){
        var min = $start.data('min') || '1900-01-01 00:00:00';
        end.config.min = makeDateObj(min);
      }else {
          var endValue = $end.val();

          end.config.min = makeDateObj(value);
          // 大于零代表  操作日期 > 结算日期
          if(endValue && bt.date.compare(value,endValue) > 0){
              $end.val('')
            end.config.value = ''
          }

      }
    }
  })

  //结束时间
  var $end = $('#settleDate')
  var defEndVal = $end.val() || new Date().format('yyyy-MM-dd');
  $end.val(defEndVal);
  var end = bt.laydate.render({
    elem:$end[0],
    type:'date',
    value:defEndVal,
    min:$start.val()||'1900-01-01 00:00:00',
    max:'2099-12-31 23:59:59',
    trigger:'click',
    lang:window.top.webLocale?(window.top.webLocale == 'en_US'?'en':'cn'):'cn',
    btns: [ 'clear','confirm'],
    ready:function(date){

    },
    done:function(value, date, endDate){
    }
  })
  //制作一个时间对象
  function makeDateObj(d){
    if(d){
      var ymd = [], hms = [];
      ymd = (d.match(/\d+-\d+-\d+/) || [''])[0].split('-');
      hms = (d.match(/\d+:\d+:\d+/) || [''])[0].split(':');
      var month = ymd[1] ? (ymd[1] | 0): new Date().getMonth()
      return  {
        year: ymd[0] | 0 || new Date().getFullYear()
        ,month:month-1
        ,date: ymd[2] | 0 || new Date().getDate()
        ,hours: hms[0] | 0
        ,minutes: hms[1] | 0
        ,seconds: hms[2] | 0
      };
    }
  }

  // 操作日期
  var opDate = $('[name="opDate"]').val() || $('[name="opDate"]').data('min');
  // 结算日期 不能小于操作日期
  if($('[name="settleDate"]')[0]){
    $('[name="settleDate"]').data('min',opDate);
  }

  bt.rateType.init();
  bt.dateType.init();
  selectType = bt.selectType.init();

  var curId = $("#settleCurId").val() || $("#buyCurId").val();
  var bankAccTable = bt.selectTable.init({
    elem: "#bankAccTable",
    url: basePath+"/foreign/fextranreq/getBoodBankAcc",
    urldata: {curId: curId, corpId: $("input[name='corpId']").val(),bankTypeId: $('#corpBank').val()},
    scrollX: true,
    scrollY:"200px",
    searchInput:"#bankAcc",
    paging:true,
    columns: [
      {data:"bankAcc",},
      {data:"accName",},
      {data:"bankName",},
      {data:"curName",}
    ],
    clickCallback:function(data, row, inputElem){
      if(data){
        inputElem.val(data.bankAcc);
        $('#bankAcc').val(data.bankAcc);
      }else{
        $('input.text').val('');
      }
    }
  });

  //监听提交
  form.on('submit(submit)', function (data) {
    bt.loading.show();
    $("#status").val($(this).attr("value"));
    data.field.lifeStatus = $(this).attr("value");
    var submitData = data.field;
    var action = "/foreign/fexTradingLifecycle/updateLifecycle";
    $.ajax({
      "url": basePath + action,
      data: JSON.stringify(submitData),
      type: "post",
      dataType: "json",
      contentType: "application/json",
      async: false,
      success: function (result) {
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
  // 结算金额回调
  bt.settleMoneyCallback = function () {
    calculateFexLifeCycleProfitLoss();
  }

  // 操作日记账汇率回调
  bt.recordRateCallback = function () {
    calculateFexLifeCycleProfitLoss();
  }

  // 损益金额回调
  bt.profitLossMoneyCallback = function () {
    calculateFexLifeCycleConvertLossMoney();
  }

  // 结算币种回调
  bt.settleCurCallback = function (data) {
    var profitLossCur = selectType.get("profitLossCur").val();
    bankAccTable.get("bankAcc").reload({curId: data.id, corpId: $('input[name="corpId"]').val(),bankTypeName:data.cotpname,bankTypeId: $('#corpBank').val()});
    if (profitLossCur == data.id) {
      $(".recordRateTd").hide();
    } else {
      $(".recordRateTd").show();
    }
    calculateSettleMoney();
  }
  moneyType = bt.moneyType.init();
})


// 计算生命周期损益
function calculateFexLifeCycleProfitLoss(){
  var profitLossCur = selectType.get("profitLossCur").val();
  var settleMoney = moneyType.get("money").val();
  var recordRate = moneyType.get("recordRate").val();
  var quotationCurId = $("#quotationCurId").val();
  var settleCur = selectType.get("curId").val();

  if(!settleCur || !recordRate || !settleMoney){
    return;
  }


  var calculateForm = {
    profitLossCur: profitLossCur,
    settleMoney: settleMoney,
    recordRate: recordRate,
    quotationCurId: quotationCurId,
    settleCur: settleCur,
    corpId: $("#corpId").val()
  }
  if ("Y" == $("#isShowConvert").val()) {
    calculateForm.convertRate = moneyType.get('convertRate').val()
  }
  $.ajax({
    "url": basePath + "/foreign/fexProfit/lossManage/calculateFexLifeCycleProfitLoss",
    data: JSON.stringify(calculateForm),
    type:"post",
    dataType:"json",
    contentType: "application/json",
    async: false,
    success:function (result) {
      if (result.success) {
        moneyType.get('profitLossMoney').val(result.obj.profitLossMoney);
        if ("Y" == $("#isShowConvert").val()) {
          moneyType.get('convertLossMoney').val(result.obj.convertLossMoney);
        }

      }
    }
  });
}


// 计算结算金额
function calculateSettleMoney(){
  var spotRate = moneyType.get("spotRate").val();
  var settleCurId = selectType.get("curId").val();
  if (!spotRate || !settleCurId) {
    return
  }
  if ("1" == $("#isRefund").val()) {
    // 计算损益
    calculateFexLifeCycleProfitLoss();
    return;
  }
  $.ajax({
    "url": basePath + "/foreign/fexTradingLifecycle/calculateSettleMoney",
    data:{
      tranCurPairIdName: $("#tranCurPairIdName").val(),
      tranCurPairBaseId: $("#tranCurPairBaseId").val(),
      quotationMethod:$("#quotationMethod").val(),
      buyMoney: $("#buyMoney").val(),
      sellMoney: $("#sellMoney").val(),
      buyCurId: $("#buyCurId").val(),
      sellCurId: $("#sellCurId").val(),
      contractRate: $("#contractRate").val(),
      rate: spotRate,
      settleCurId: settleCurId,
      busDirection:$("#busDirection").val(),
      tranType: "2",
      updownDirection: $("#updownDirection").val()
    },
    type:"post",
    dataType:"json",
    async: false,
    success:function (result) {
      if (result.success) {
        moneyType.get("money").val(result.obj);
        // 计算损益
        calculateFexLifeCycleProfitLoss();
      }
    }
  });
}


// 计算折本位币生命周期损益
function calculateFexLifeCycleConvertLossMoney(){
  if ("Y" != $("#isShowConvert").val()) {
    return;
  }
  var calculateForm = {
    profitLossCur: selectType.get("profitLossCur").val(),
    convertCurType: $("#convertCurType").val(),
    profitLossMoney: moneyType.get('profitLossMoney').val(),
    convertRate: moneyType.get('convertRate').val(),
    corpId: $("#corpId").val()
  }

  $.ajax({
    "url": basePath + "/foreign/fexProfit/lossManage/calculateFexLifeCycleConvertLossMoney",
    data: JSON.stringify(calculateForm),
    type:"post",
    dataType:"json",
    contentType: "application/json",
    async: false,
    success:function (result) {
      if (result.success) {
        moneyType.get('convertLossMoney').val(result.obj);
      }
    }
  });
}
