var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank',
    });
    
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[4,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bond/fcbondInfo/datatable" 
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
            {data:"corpName",bSortable:true,className:'textCenter'},
            {data:"itemNo",bSortable:true,className:'textCenter'},
            {data:"itemType",bSortable:true,className:'center',
            	render:function(data,type,full,meta){
            		return bt.dict("fcBond.itemType",full.itemType);
            	}
            },
            {data:"startDate",bSortable:true,className:'textCenter'},
            {data:"endDate",bSortable:true,className:'textCenter'},
            {data:"bankAcc",bSortable:true,className:'textCenter'},
            {data:"accName",bSortable:true,className:'textCenter'},
            {data:"bankName",bSortable:true,className:'textCenter'},
            {data:"curName",bSortable:true,className:'textCenter'},
            {data:"balMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
            		return bt.money.format(full.balMoney);
                }
            },
            {data:"applyMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
            		return bt.money.format(full.applyMoney);
                }
            },
            {data:"createDate",bSortable:true,className:'textCenter',
                render:function( data, type, full, meta){
                    return bt.date.format(full.createDate, 'yyyy-MM-dd');
                },},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
               // if(bt.isResource('netbank:expense:nisExpenseDetail:touchApply',full.corpId)){
               // }
                   html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                  return html;
            }}
        ]
    });
});

//查看
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
      	if(bt.navtab){
      	 	bt.navtab.addNavtab(bt.lang('common.button.view'), basePath + "/bond/fcbondInfo/view/"+data.id);
      		return;
      	}
}

