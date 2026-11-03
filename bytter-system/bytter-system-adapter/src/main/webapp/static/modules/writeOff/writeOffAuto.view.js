var moneyType;

//读取并解析对象
var temp = localStorage.getItem($('#docId').val());
var dataVo = JSON.parse(temp);
$("#bankAccTypeStr").text('自动匹配规则：'+dataVo.bankAccTypeStr);

//删除缓存数据
localStorage.removeItem($('#docId').val());


bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect', 'externalCorpBankAcc'], function () {
     var $ = bt.jquery, form = bt.form();
     moneyType = bt.moneyType.init();

     var appTableSure = bt.datatable.initMultiTables({
         elem: "#appTableSure",
         menuName: '核销处理',
         checkbox: false,
         mulselect: true,
         selectCss: true,
         jumpPageSelect: false,//跨页选择
         isInitData: true,
         searchForm: {
            formElem: "#sureSearchForm"
         },
         ajax: {
             'url': basePath + "/accountReceivable/writeOff/sureAutoDatatable"
         },
        ignoreButtons:['colvis','output','print'], //隐藏按钮
//        initComplete: function(){ //可以隐藏表单的“保存”按钮
//
//        },
         columns: [
            {data: "docManageVo.code", bSortable: false},
             {
                  data: "docManageVo.writeOffBal", bSortable: true, className: 'textRight',
                  render: function (data, type, full, meta) {
                      return full.docManageVo.enCode + bt.money.format(full.docManageVo.writeOffBal);
                  }
              },
              {
                    data: "docManageVo.executeMoney", bSortable: true, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return full.docManageVo.enCode + bt.money.format(full.docManageVo.executeMoney);
                    }
               },
             {data: "docManageVo.writeOffStatusName", bSortable: false},
             {data: "afterDocWriteOffStatusName", bSortable: false},



             {data: "bisAccDtlVO.bankSerialNo", bSortable: true},
             {
                 data: "bisAccDtlVO.writeOffBal", bSortable: true, className: 'textRight',
                 render: function (data, type, full, meta) {
                     return full.bisAccDtlVO.enCode + bt.money.format(full.bisAccDtlVO.writeOffBal);
                 }
             },
             {
                  data: "bisAccDtlVO.executeMoney", bSortable: true, className: 'textRight',
                  render: function (data, type, full, meta) {
                      return full.bisAccDtlVO.enCode + bt.money.format(full.bisAccDtlVO.executeMoney);
                  }
              },
             {data: "bisAccDtlVO.writeOffStatusName", bSortable: true},
             {data: "afterRelationWriteOffStatusName", bSortable: false},

            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="detail(this)">' + bt.lang("详情") + '</a>';
                return html;
                }
            }
         ],
         btClickRowCallback: function (data1) {

         },
         btDrawCallback: function (setting) { //回调方法
         },
     });


     form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/accountReceivable/writeOff/sureWriteOff";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            if(result.success){
                result.msg = bt.lang("submitted.successfully.pending.approval");
            }
            parent.layer.open({content:result.msg, yes: function(){
                parent.layer?parent.layer.closeAll():layer.closeAll();
                bt.navtab.refreshCurrentTable("appTableDoc");
            }
            });
        },'JSON');
        return false;
     });
});

function cancle(){
    parent.layer?parent.layer.closeAll():layer.closeAll();
}

//跳转自动核销详情页面
function detail(el){
    var autoData = layui.jquery("#appTableSure").DataTable().row(layui.jquery(el).data("row")).data();
    var docId = autoData.docManageVo.id;

    //缓存数据
    localStorage.setItem(docId, JSON.stringify(autoData));

    bt.loading.hide();
    parent.layer.open({
        type: 2,
        area: ['1200px', '650px'],
        maxmin: true,
        title: '自动核销详情',
        content: basePath + "/accountReceivable/writeOff/viewDetailAuto?docId="+docId,
        end:function(){
        }
    });
}

