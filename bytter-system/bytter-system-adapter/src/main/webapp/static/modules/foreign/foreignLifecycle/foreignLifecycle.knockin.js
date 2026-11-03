bt.use(['form', 'rateType', 'moneyType','dateType'], function () {
  var $ = bt.jquery, form = bt.form();
  bt.moneyType.init();
  bt.rateType.init();
  bt.dateType.init();

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
})
