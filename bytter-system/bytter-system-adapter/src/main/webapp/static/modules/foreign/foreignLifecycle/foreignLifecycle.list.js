var datatable;
bt.use(['form', 'datatable', 'selectType', 'moneyType', 'dicts','dateType','mulselect'], function () {
    var  $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    bt.selectType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();

    datatable = bt.datatable.init({
        elem: "#appTable",
        checkbox: false,
        clickRowSelected:false,
        order: [[1, 'desc']],
        searchForm: {
          formElem: "#searchForm",
          formFilter: "search"
        },
        ajax: {
          'url': basePath + "/foreign/fexTradingLifecycle/datatable"
        },
        buttons: [
          {
            extend: "colvis",
            text: bt.lang('common.button.theadHide'),
            columns: ':not(:first):not(:last)'
          }
       ],
       fixedColumns: {
        rightColumns: 1//代表冻结右边一列
       },
       columns: [
          {data: "billNo", bSortable: true, className: 'center'},
          {data: "opType", bSortable: true, className: 'center',
              render: function (data, type, full, meta) {
                  return bt.dict("fexProduct.lifeCycle", full.opType);
              }
           },
          {data: "relBillNo", bSortable: true},
          {data: "tranCurPairIdName", bSortable: true},
          {data: "quotationMethod", bSortable: true},
          {
            data: "opDate", bSortable: true, className: 'center',
            render: function (data, type, full, meta) {
              if (data) {
                return bt.date.format(data, "yyyy-MM-dd");
              }
              return '';
            }
          },
          {
            data: "settleDate", bSortable: true, className: 'center',
            render: function (data, type, full, meta) {
              if (data) {
                return bt.date.format(data, "yyyy-MM-dd");
              }
              return '';
            }
          },
          {
            data: "opMoney", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
              return bt.money.format(full.opMoney);
            }
          },
          {
            data: "spotRate", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.spotRate,{
                    separator:"",
                    decimal:'6'
                });
            }
          },
          {data: "profitLossCur", bSortable: true},
          {
            data: "profitLossMoney", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
              return bt.money.format(full.profitLossMoney);
            }
          },
          {data: "convertCurType", bSortable: true, className: 'center'},
          {
            data: "convertRate", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
              return bt.money.format(full.convertRate,{
                separator:"",
                decimal:'6'
              });
            }
          },
          {
            data: "convertLossMoney", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
              return bt.money.format(full.convertLossMoney);
            }
          },
          {data: "lifeStatus", bSortable: true, className: 'center',
              render: function (data, type, full, meta) {
                  return bt.dict("choBankFinance.status", String(full.lifeStatus));
              }
          },
          {
            data: null, bSortable: false, className: 'center',
            render: function (data, type, full, meta) {
              var html = '';
              if ((full.lifeStatus == "0" || full.lifeStatus == "-1") && bt.isResource('foreign:fextran:lifecycle:apply')) {
                html += '<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="updateLife(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                html += '&nbsp;<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="deleteLife(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
              }
              html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
              return html;
            }
          }
      ]
  });
});


// 打开子页面
function openChildWeb(webName,url) {
  if (bt.navtab) {
    bt.navtab.addNavtab(webName, url);
    return;
  }

  parent.layer.open({
    type: 2,
    area: ['850px', '530px'],
    maxmin: true,
    content: url
  });
}

// 查看
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexTradingLifecycle/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTradingLifecycle/view/" + data.id
    });
}

  /**
   * 更新
   * @param el
   */
  function updateLife(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
      if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexTradingLifecycle/update/" + data.id);
        return;
      }
      parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTradingLifecycle/update/" + data.id,
        end: function () {
          datatable && datatable.draw();
        }
      });
    }
  }

  /**
   * 删除
   * @param el
   */
  function deleteLife(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
      parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/foreign/fexTradingLifecycle/delLifecycle/" + data.id, function(result){
          bt.datatable.refreshCurrentPage();
          parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
      });
    }
  }


