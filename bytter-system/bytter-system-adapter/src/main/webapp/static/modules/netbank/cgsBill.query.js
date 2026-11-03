bt.use(['form','datatable','dateType','corp','selectTree','moneyType','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/netbank/cgsbill/datatable"
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
            {data:"consignDate",bSortable:true},
            {data:"consignCode",bSortable:true},
            {data:"payerCorpName",bSortable:true},
            {data:"payeeCorpName",bSortable:true},
            {data:"payeeAcc",bSortable:true},
            {data:"money",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.money);
                }
            },
            {data:"pactCode",bSortable:true},
            {data:"abs",bSortable:true},
            {data:"status",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("cgsBill.status",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                return html;
            }}
        ]
    });
	  //初始化付款单位下拉树
		bt.selectTree.init({
			url : basePath + "/sys/corp/tree",
			elem : ".payercorpTree",
			name : 'ins_payerCorpId',
			selected : "",
			checkType : "check",
			placeholder : bt.lang('common.placeholder.select'),
		});
	  //初始化收款单位下拉树
		bt.selectTree.init({
			url : basePath + "/sys/corp/tree",
			elem : ".payeecorpTree",
			name : 'ins_payeeCorpId',
			selected : "",
			checkType : "check",
			placeholder : bt.lang('common.placeholder.select'),
		});
});

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/cgsbill/view/" + data.id
    });
}
