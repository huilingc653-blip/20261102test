var billTable={};
bt.use(['form','datatable','btdate','corp','select','dateType','bank'], function(){
    $ = bt.jquery,form = bt.form();
    corpCallback=function (ids) {
        corpCallbackFun(ids);
    }
    bt.corp.init();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank'
    });

    //加载凭证种类下拉列表
    bt.select.init({
        elem:'.selectVouTid',
        name:"eqs_vouTid",
        valueField:"id",
        textField:"name",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvvouchertype/queryVoucherTypeValidByInbox"
    });
    init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/blankvou/bvvouinfodtl/queryBvVouInfoDtl"
        },
        buttons: [
            /*{
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text: '<i class="layui-icon">&#xe642;</i> '+bt.lang('common.button.edit'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    layer.msg(JSON.stringify(rowData));
                }
            },*/{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"vouNo",bSortable:true,render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" style="color:blue" data-row="'+meta.row+'" onclick="view(this)">'+data+'</a>';
                    return html
                }},
            {data:"voucherTypeName",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"bankName",bSortable:true},
            {data:"bankAcc",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"storageBoxName",bSortable:true},
            {data:"inputDate",bSortable:true},
            {data:"createName",bSortable:true},
            {data:"vouStatusName",bSortable:true}
        ]
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
});
function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouinfodtl/view/" + data.id
    });
}

//单位回调获取银行账户、银行类别、库箱
function corpCallbackFun(ids){
    var $ = bt.jquery,form = bt.form();
    if(ids){
        //库箱
        bt.select.init({
            elem:'.selectSboxId',
            name:"eqs_sboxId",
            valueField:"id",
            textField:"name",
            placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
            search: true,
            disabled: false,
            url: basePath + "/bill/blankvou/bvstoragebox/jsonListByCorpIds/" + ids
        });
        //银行类别
        bt.select.init({
            elem:'.selectBankId',
            name:"eqs_bankName",
            valueField: "bankName",
            textField: "bankName",
            placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
            search: true,
            disabled: false,
            url: basePath + "/bill/blankvou/bvvouinput/getBankAccListByCorpIds/" + ids
        });
    }
}

//重置
function resetUpdate(){
    init();
}

//所属银行、所属库箱加载
function init(){
    bt.select.init({
        elem:'.selectSboxId',
        name:"eqs_sboxId",
        valueField:"id",
        textField:"name",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvstoragebox/selectData"
    });
    //下拉框--加载所属银行
    bt.select.init({
        elem:'.selectBankId',
        name:"eqs_bankName",
        valueField: "bankName",
        textField: "bankName",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvvouinput/getBankAccList/json"
    });
}
