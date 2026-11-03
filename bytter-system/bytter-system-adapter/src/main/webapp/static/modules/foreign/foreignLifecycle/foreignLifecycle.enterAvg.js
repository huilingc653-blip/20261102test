bt.use(['form', 'rateType', 'moneyType','dateType'], function () {
  var $ = bt.jquery, form = bt.form();
  bt.moneyType.init();
  bt.rateType.init();
  bt.dateType.init();

  //监听提交
  form.on('submit(submit)', function (data) {
    bt.loading.show();
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