var datatable;
bt.use(['form','datatable','corp','dateType','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.selectType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        checkbox:false,
        order: [[2,'desc']],
        ajax: {
            'url': basePath+"/budget/fbsbudgetreport/dataTableDetailReport"
        },
        buttons: []
        ,columns: [
            {data:"corpName",bSortable:true},
            {data:"deptName",bSortable:true},
            {data:"itemCode",bSortable:true},
            {data:"itemName",bSortable:true},
            {data:"bMoney",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.bMoney);
                }
            },
            {data:"curName",bSortable:true,className:'center'},
            {data:"vestingDate",bSortable:true,className:'center'},
            {data:"settlTypeName",bSortable:true,className:'center'},
            {data:"purpose",bSortable:true},
            {data:"wishPayday",bSortable:true,className:'center'},
            {data:"payerAcc",bSortable:true,className:'center'},
            {data:"payeeAccName",bSortable:true,className:'center'},
            {data:"payeeAcc",bSortable:true,className:'center'},
            {data:"payeeBank",bSortable:true,className:'center'},
            {data:"contract",bSortable:true,className:'center'}
        ]
    });
});
