var datatable;
bt.use(['form','datatable','select','corp','money','dicts','dateType','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();

    // 生成列表
    datatable = bt.datatable.init({  
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[11,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/guarantee/guaContractUse/datatables"
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
                  {data:"corpName",className:'center',bSortable:true},
                  {data:"guaNo",className:'center',bSortable:true},
                  {data:"guaTypeName",className:'center',bSortable:true},
                  {data:"guaPerson",className:'center',bSortable:true},
                  {data:"debtorPerson",className:'center',bSortable:true},
                  {data:"creditorPerson",className:'center',bSortable:true},
                  {data:"curName",className:'center',bSortable:true},
                  {data:"guaMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                  		return bt.money.format(full.guaMoney);
                      }
                  },
                  {data:"contractCode",className:'center',bSortable:true},
                  {data:"startDate",className:'center',bSortable:true},
                  {data:"endDate",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('gua:contract:use:query', full.corpId)) {//查看权限
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="useList(this)">'+bt.lang('table.guaContractUse.list')+'</a><span class="text-explode"></span>';
                    }
                    if (bt.isResource('gua:contract:use:apply', full.corpId)) {//操作权限
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="addUse(this)">'+bt.lang('table.guaContractUse.register')+'</a><span class="text-explode"></span>';
                    }
                    if (bt.isResource('gua:contract:use:query', full.corpId)) {//查看权限
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>';
                    }
                    return html;
            }}
        ]
    });
});

//使用列表
function useList(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.guaContractUse.list'), basePath + "/cms/guarantee/guaContractUse/useList/" + data.id);
            return;
        }
    }
}

//使用登记
function addUse(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('table.guaContractUse.register'),basePath + "/cms/guarantee/guaContractUse/addUse?contractId="+data.id);
            return;
        }
    }
}

//查看
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '500px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaContractInfo/viewInfo/" +data.id
    });
}




