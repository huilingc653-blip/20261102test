bt.use(['form','datatable', 'dateType','moneyType','btdate','corp','dicts','moneyType', 'btImport'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    bt.corp.init();
    bt.dicts.init();
    moneyType=bt.moneyType.init();
    bt.dateType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [1,'desc'],
        ajax: {
            'url': basePath+"/cms/lease/UpdateObjectInfo/datatable?leaseId="+$("#leaseId").val()
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"name",className:'center',bSortable:true},
            {data:"leaseItemCode",className:'center',bSortable:true},
            {data:"leaseItemName",className:'center',bSortable:true},
            {data:"brand",className:'center',bSortable:true,},
            {data:"speModel",className:'center',bSortable:true,},
            {data:"numb",className:'center',bSortable:true},
            {data:"curName",className:'center',bSortable:true,},
            {data:"oldMoney",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.oldMoney);
                }
            },
            {data:"status",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("update.lease.status",full.status);
                }
            },{data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '';
                    if(bt.isResource('cms:LeaseObjectInfo:view',full.corpId)){
                        html += ' <a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    }
                    return html;
                }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: false,//已选汇总功能开启
            outerData: true,
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '9', //汇总列序号
                    dataName: 'oriMoney' //汇总列数据
                }
            ]
        }
    });
});

//查看
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/lease/UpdateObjectInfo/view/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/lease/UpdateObjectInfo/view/" + data.id
        });
    }
}

//打印
function printContract(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('cms.loanContract.print'),basePath + "/cms/loan/cmscontract/print?liCode=" + data.billCode);
            return;
        }
    }
}

