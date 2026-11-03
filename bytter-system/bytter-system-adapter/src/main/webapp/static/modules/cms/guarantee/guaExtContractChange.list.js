var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts'], function(){
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
        order: [[2,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/guarantee/guaExtContractChange/datatable"
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"corpName",bSortable:true,className:'center'},
            {data:"guaNo",bSortable:true,className:'center'},
            {data:"guaTypeName",bSortable:true,className:'center'},
            {data:"debtorPersonName",bSortable:true,className:'center'},
            {data:"creditorPerson",bSortable:true,className:'center'},
            {data:"curName",bSortable:true,className:'center'},
            {data:"guaMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.guaMoney);
                }
            },
            {data:"contractCode",bSortable:true,className:'center'},
            {data:"startDate",bSortable:true,className:'center'},
            {data:"endDate",bSortable:true,className:'center'},
            {data:"updateDate",bSortable:true,className:'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
               if(bt.isResource('gua:extContract:change:apply',full.corpId)&& full.status==95){//更新权限
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("table.guaContract.change")+'</a><span class="text-explode"></span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="viewHistory(this)">'+bt.lang("table.guaContract.history")+'</a><span class="text-explode"></span>';
                }
                 return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8', //汇总列序号
                    dataName: 'guaMoney' //汇总列担保额度
                }
            ]
        }
    });
});


//查看历史信息详情
function viewHistory(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.guaContract.history'), basePath + "/cms/guarantee/guaContractChange/queryGuaContractHistory/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/guaContractChange/queryGuaContractHistory/" + data.id,
            end: function () {
            }
        });
    }
}


//变更
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/cms/guarantee/guaExtContractChange/changeContractInfo/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/guaExtContractChange/changeContractInfo/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}


