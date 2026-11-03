var datatable,moneyFun;
bt.use(['form', 'bttable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType','element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    bt.dateType.init();
    bt.selectType.init();
    bt.dicts.init();

    var rowTr = 0;
    var docId = $("#docId").val();

    var columnsTab = function() {
        var columns = [
            {
                data: 'allocationDate',
                render: function (data, row) {
                    return '<input type="hidden" name="notices[' + row + '].id" value="'+data.id+'"/>' +
                           '<input type="text" lay-verify="required" name="notices[' + row + '].allocationDate" value="'+data.allocationDate+'" class="layui-input bt-input-inline bt-biz-dateType layui-input-list" readOnly />';
                }
            },
            {
                data: 'allocationBy',
                render: function (data, row) {
                    return '<input type="text" name="notices[' + row + '].allocationBy" value="'+data.allocationBy+'" autocomplete="off" class="layui-input layui-input-list" readOnly />';
                }
            },
            {
                data: 'allocationResult',
                render: function (data, row) {
                    //return '<input type="text" name="notices[' + row + '].allocationResult" value="'+data.allocationResult+'" autocomplete="off" class="layui-input layui-input-list" readOnly />';
                    return '<textarea name="notices[' + row + '].allocationResult" autocomplete="off" class="layui-textarea layui-input-list auto-wrap" readOnly >'+data.allocationResult+'</textarea>';
                }
            },
            {
                data: 'overdueReason',
                render: function (data, row) {
                    //return '<textarea name="notices[' + row + '].overdueReason" value="'+data.overdueReason+'" autocomplete="off" class="layui-input layui-input-list auto-wrap" rows="2" cols="15" readOnly />';
                    return '<textarea name="notices[' + row + '].overdueReason" autocomplete="off" class="layui-textarea layui-input-list auto-wrap" readOnly >' +data.overdueReason+'</textarea>';
                }
            }

        ];
        return columns;
    }

    $(document).on('click', ".delete-notic", function () {
        var tr = $(this).parents('tr');
        var index = tr.attr("row");
        //$('input[name="externalAcc[' + index + '].externalAcc"]').attr("value","");
        tr.remove();
    });

    //初始化表格
    var datatable = bt.bttable.init({
          scrollX: true,
          //scrollY: "400px",
          elem: "#appTable",
          url: basePath + "/accountReceivable/query/docOverdueNotice/datatableNotice?docId=" + docId,
          buttons: [
          ],
          columns: columnsTab(),
          delRowCallback: function (data, $tr) {
              $tr.remove();
          },
          tableInitCallback: function (data) {
              rowTr = data.length;
              form.render();
          }
      });

});

function closecurrent(){
    parent.layer?parent.layer.closeAll():layer.closeAll();
    return false;
}